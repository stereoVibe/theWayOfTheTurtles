package io.sokolvault13.biggoals;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.Model.ObjectiveType;
import io.sokolvault13.biggoals.Model.SubGoal;
import io.sokolvault13.biggoals.Model.Task;

public class IntentionDAOHelper {

    public static BigGoal createBigGoalRecord(BigGoal bigGoal,
                                              Dao<BigGoal, Integer> dao) throws SQLException {
        dao.create(bigGoal);
        assignBigGoalEmptyCollection(bigGoal, dao);
        return bigGoal;
    }

    public static SubGoal createSubGoalRecord(SubGoal subGoal,
                                              Dao<SubGoal, Integer> dao,
                                              BigGoal bigGoal) throws SQLException {
        dao.create(subGoal);
        assignSubGoalEmptyCollection(subGoal, dao);
//        bigGoal.getSubGoals().add(subGoal);
//        dao.refresh(subGoal);
        return subGoal;
    }

    public static Task createTaskRecord(Task task,
                                        Dao<Task, Integer> dao,
                                        SubGoal subGoal) throws SQLException{
        dao.create(task);
        subGoal.getTasks().add(task);
        return task;
    }

//    TODO: Create other CRUD methods

    public static <T> List<T> getIntentionList (Dao<T, Integer> dao) throws SQLException {
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

    private static void assignBigGoalEmptyCollection(BigGoal bigGoal,
                                                     Dao<BigGoal,
                                                             Integer> dao) throws SQLException {
        if (bigGoal.getSubGoals() == null) {
            dao.assignEmptyForeignCollection(bigGoal, "subgoals_collects");
        }
    }

    private static void assignSubGoalEmptyCollection(SubGoal subGoal,
                                                     Dao<SubGoal,
                                                             Integer> dao) throws SQLException {
        if (subGoal.getTasks() == null) {
            dao.assignEmptyForeignCollection(subGoal, "tasks_collects");
        }
    }



}