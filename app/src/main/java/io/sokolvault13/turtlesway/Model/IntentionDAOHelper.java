package io.sokolvault13.turtlesway.model;

import android.util.Log;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IntentionDAOHelper {

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

//    public static List<? extends Intention> getSubIntention (Dao<? extends Intention, Integer> dao,
//                                               Intention goal,
//                                               String idField) throws SQLException {
//        return dao.queryBuilder().where()
//                .eq(idField, goal.getId())
//                .query();
//    }

    public static BigGoal getBigGoal(Dao<BigGoal, Integer> dao, int bigGoalId) throws SQLException {
        return dao.queryForId(bigGoalId);
    }

    public static <T extends Goal> T getSubGoal(Dao<T, Integer> dao, int subGoalID) throws SQLException {
        return dao.queryForId(subGoalID);
    }


    public static <T extends Goal> List<T> getAllSubIntentionsList (Dao<T, Integer> dao,
                                                                    Intention goal,
                                                                    String idField) throws SQLException {
        ArrayList<T> subIntentionsList = new ArrayList<>();
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
        ArrayList<T> list = new ArrayList<>();
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

    public static void updateJobCompletedQuantity(Dao<Job, Integer> dao, Job job, int completedQuantity) throws SQLException {
//        dao.queryForId(job.getId()).setCompletedQuantity(completedQuantity);
        job.setCompletedQuantity(completedQuantity);
        dao.update(job);
    }

    public static void addJobProgress(Dao<Job, Integer> dao, Job job, double progress) throws SQLException {
        job.addProgress(progress);

        dao.update(job);
    }

    public static <T extends Intention> void setComplete(Dao<T, Integer> dao, T intention) throws SQLException {
        T goal = dao.queryForId(intention.getId());
        goal.setCompleteStatus(true);
        dao.update(goal);
    }

    public static <T extends Intention> BigGoal updateBigGoalProgress(int bigGoalId, Dao<BigGoal, Integer> dao, Dao<T, Integer>... daos) throws SQLException {
        BigGoal bigGoal = getBigGoal(dao, bigGoalId);
        bigGoal.setProgress(getProgressResult(bigGoalId, dao, daos));
        Log.d("Progress from DAOHelper", String.valueOf(getProgressResult(bigGoalId, dao, daos)));
        dao.update(bigGoal);
        Log.d("Progress after update", String.valueOf(bigGoal.getProgress()));
        return bigGoal;
    }

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