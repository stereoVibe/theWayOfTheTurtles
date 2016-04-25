package io.sokolvault13.biggoals;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.Model.Intention;
import io.sokolvault13.biggoals.Model.SubGoal;
import io.sokolvault13.biggoals.Model.Task;

public class IntentionDAOHelper {

    public static BigGoal createBigGoalRecord(BigGoal bigGoal,
                                              Dao<BigGoal, Integer> dao) throws SQLException {
        dao.create(bigGoal);
        if (bigGoal.getSubGoals() == null) {
            dao.assignEmptyForeignCollection(bigGoal, BigGoal.SUBGOALS_COLLECTS_FIELD);
        }
        return bigGoal;
    }

    public static SubGoal createSubGoalRecord(SubGoal subGoal,
                                              Dao<SubGoal, Integer> dao) throws SQLException {
        dao.create(subGoal);
        if (subGoal.getTasks() == null) {
            dao.assignEmptyForeignCollection(subGoal, SubGoal.TASKS_COLLECTS_FIELD);
        }
        return subGoal;
    }

    public static Task createTaskRecord(Task task,
                                        Dao<Task, Integer> dao) throws SQLException{
        dao.create(task);
        return task;
    }

    public static List<? extends Intention> getSubIntention (Dao<? extends Intention, Integer> dao,
                                               Intention goal,
                                               String idField) throws SQLException {
        return dao.queryBuilder().where()
                .eq(idField, goal.getId())
                .query();
    }

    public static <T extends Intention> List<T> getAllSubIntentionsList (Dao<T, Integer> dao,
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