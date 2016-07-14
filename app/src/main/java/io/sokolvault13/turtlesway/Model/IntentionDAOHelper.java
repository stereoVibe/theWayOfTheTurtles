package io.sokolvault13.turtlesway.model;

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

   public static BigGoal getBigGoal (Dao<BigGoal, Integer> dao, int bigGoalId) throws SQLException {
       return dao.queryForId(bigGoalId);
   }

    public static <T extends Goal> List<T> getAllSubIntentionsList (Dao<T, Integer> dao,
                                                   Intention goal,
                                                   String idField) throws  SQLException {
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

}