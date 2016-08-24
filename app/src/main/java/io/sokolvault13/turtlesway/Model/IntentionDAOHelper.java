package io.sokolvault13.turtlesway.model;

import android.util.Log;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/* TO-DO:  Make this class non-static as it
*  not follow OOP */

/*
 * This class is created to help handle
 * general CRUD operations and any CRUD
 * related actions.
 */
public class IntentionDAOHelper {

//    Preparations for converting this class to non-static
//    Singleton creating
/*    private IntentionDAOHelper(){}

    private static class SingletonHelper {
        private static final IntentionDAOHelper INSTANCE = new IntentionDAOHelper();
    }

    public static IntentionDAOHelper getDAOHelper(){
        return SingletonHelper.INSTANCE;
    }*/

    public static BigGoal createBigGoalRecord(BigGoal bigGoal,
                                              Dao<BigGoal, Integer> dao) throws SQLException {
        dao.create(bigGoal);
        if (bigGoal.getJobs() == null) {
            dao.assignEmptyForeignCollection(bigGoal, BigGoal.SUBGOALS_COLLECTS_FIELD);
        }
        return bigGoal;
    }

    public static Job createJobRecord(Job job,
                                      Dao<Job, Integer> dao) throws SQLException {
        dao.create(job);
        return job;
    }

    public static Task createTaskRecord(Task task,
                                        Dao<Task, Integer> dao) throws SQLException{
        dao.create(task);
        return task;
    }

    public static <T extends Intention> void deleteIntention(T intention,
                                                             Dao<T, Integer> dao) throws SQLException{
        dao.deleteById(intention.getId());
    }

    public static BigGoal getBigGoal(Dao<BigGoal, Integer> dao, int bigGoalId) throws SQLException {
        return dao.queryForId(bigGoalId);
    }

    public static <T extends SubGoal> T getSubGoal(Dao<T, Integer> dao, int subGoalID) throws SQLException {
        return dao.queryForId(subGoalID);
    }


    public static <T extends SubGoal> List<T> getAllSubIntentionsList(Dao<T, Integer> dao,
                                                                      Intention goal,
                                                                      String idField) throws SQLException {
        List<T> subIntentionsList = new ArrayList<>();
        CloseableIterator<T> iterator = dao.queryBuilder().where()
                .eq(idField, goal.getId())
                .iterator();
        try {
            while (iterator.hasNext()){
                T intention = iterator.next();
                subIntentionsList.add(intention);
            }
        }finally {
            iterator.close();
        }

        return subIntentionsList;
    }

    public static <T extends Intention> List<T> getIntentionList (Dao<T, Integer> dao) throws SQLException {
        List<T> list = new ArrayList<>();
        CloseableIterator<T> iterator = dao.closeableIterator();
        try {
            while (iterator.hasNext()){
                T intention = iterator.next();
                list.add(intention);
            }
        }finally {
            iterator.close();
        }
        return list;
    }

    /* UPDATE mechanism for any Intention */
    public static <T> void updateIntention(Dao<T, Integer> dao, Intention intention, HashMap<String, Object> intentionFields) throws SQLException {
        int goalQuantity = 0;
        int changesCounter = 0;
        String description = (String) intentionFields.get(Intention.FIELD_INTENTION_DESCRIPTION),
                title = (String) intentionFields.get(Intention.FIELD_INTENTION_TITLE);
        Date endDate = (Date) intentionFields.get(Intention.FIELD_INTENTION_END_DATE);

        if (intentionFields.get(Intention.FIELD_INTENTION_GOAL_QUANTITY) != null) {
            goalQuantity = (int) intentionFields.get(Intention.FIELD_INTENTION_GOAL_QUANTITY);
        }

        if (!intention.getTitle().equals(title)) {
            intention.setTitle(title);
            changesCounter++;
        }
        if (intention.getDescription() != null && !intention.getDescription().equals(description)) {
            intention.setDescription(description);
            changesCounter++;
        }
        if (intention.getEndDate() != null && !intention.getEndDate().equals(endDate)) {
            intention.setEndDate(endDate);
            changesCounter++;
        }

        if (intention instanceof Job && (((Job) intention).getGoalQuantity() != goalQuantity) && goalQuantity != 0) {
            ((Job) intention).setGoalQuantity(goalQuantity);
            changesCounter++;
        }

        if (intention instanceof Task && (intention.getCompleteStatus() != (boolean) intentionFields.get(SubGoal.COMPLETE_STATUS))) {
            intention.setCompleteStatus((boolean) intentionFields.get(SubGoal.COMPLETE_STATUS));
            changesCounter++;
        }

        if (changesCounter > 0) {
            dao.update((T) intention);
        }
    }

    public static void updateJobCompletedQuantity(Dao<Job, Integer> dao, Job job, int completedQuantity) throws SQLException {
//        dao.queryForId(job.getId()).setCompletedQuantity(completedQuantity);
        job.setCompletedQuantity(completedQuantity);
        dao.update(job);
    }

    public static void updateJobProgress(Dao<Job, Integer> dao, Job job, double progress) throws SQLException {
        job.addProgress(progress);
        dao.update(job);
    }

    public static <T extends Intention> void setComplete(Dao<T, Integer> dao, T intention) throws SQLException {
        T goal = dao.queryForId(intention.getId());
        goal.setCompleteStatus(true);
        dao.update(goal);
    }

    /*
     * Direct calling of the method will update progress of the BigGoal.
     */
    public static <T extends Intention> BigGoal updateBigGoalProgress(int bigGoalId, Dao<BigGoal, Integer> dao, Dao<T, Integer>... daos) throws SQLException {
        BigGoal bigGoal = getBigGoal(dao, bigGoalId);
        bigGoal.setProgress(getProgressResult(bigGoalId, dao, daos));
        Log.d("Progress from DAOHelper", String.valueOf(getProgressResult(bigGoalId, dao, daos)));
        dao.update(bigGoal);
        Log.d("Progress after update", String.valueOf(bigGoal.getProgress()));
        return bigGoal;
    }

    /*
     *   getProgressResult method is private method which calculate
     *   overall progress (BigGoal progress) of the goal and return
     *   progress value in percent. Can operate any subGoals which
     *   implemented SubGoal interface.
     *   Calling from updateBigGoalProgress method.
     */

    private static <T extends Intention> double getProgressResult(int bigGoalId, Dao<BigGoal, Integer> bigGoalDAO, Dao<T, Integer>... daos) throws SQLException {
        double goalsCompleted = 0, goalsCapacity = 0;
        BigGoal bigGoal = bigGoalDAO.queryForId(bigGoalId);
        List<Intention> goals = new ArrayList<>();

        for (Dao dao : daos) {
            CloseableIterator iterator = dao.queryBuilder().where()
                    .eq(Intention.BIGGOAL_ID_FIELD, bigGoal.getId())
                    .iterator();
            try {
                while (iterator.hasNext()) {
                    goals.add((Intention) iterator.next());
                }
            } finally {
                iterator.close();
            }
        }

        for (Intention goal : goals) {
            if (goal instanceof Job) {
                goalsCapacity += ((Job) goal).getGoalQuantity();
                goalsCompleted += ((Job) goal).getCompletedQuantity();
            }
            if (goal instanceof Task) {
                goalsCapacity++;
                if (goal.getCompleteStatus()) {
                    goalsCompleted++;
                }
            }
        }
        return goalsCompleted * 100 / goalsCapacity;
    }


}