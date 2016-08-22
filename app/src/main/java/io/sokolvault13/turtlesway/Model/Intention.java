package io.sokolvault13.turtlesway.model;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public abstract class Intention {

    public static final String BIGGOAL_FIELD = "big_goal";
    public static final String BIGGOAL_ID_FIELD = "big_goal_id";
    public static final String FIELD_INTENTION_TITLE = "title";
    public static final String FIELD_INTENTION_DESCRIPTION = "description";
    public static final String FIELD_INTENTION_END_DATE = "endDate";
    public static final String FIELD_INTENTION_GOAL_QUANTITY = "goalQuantity";

    private int id;

    public static HashMap<String, Object> prepareSubGoal(String title, String description, Date endDate, int goalQuantity) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(FIELD_INTENTION_TITLE, title);
        hashMap.put(FIELD_INTENTION_DESCRIPTION, description);
        if (endDate != null) {
            hashMap.put(FIELD_INTENTION_END_DATE, endDate);
        }
        if (goalQuantity > 0)
            hashMap.put(FIELD_INTENTION_GOAL_QUANTITY, goalQuantity);
        return hashMap;
    }

    public int getId(){
        return id;
    }

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract Date getStartDate();

    public abstract void setStartDate(Date startDate);

    public abstract Date getEndDate();

    public abstract void setEndDate(Date endDate);

    public abstract int getOutOfDate();

    public abstract void setOutOfDate(int isOutOfDate);

    public abstract boolean getCompleteStatus();

    public abstract void setCompleteStatus(boolean isComplete);

    private SubGoal getGoalType(ObjectiveType objectiveType, HashMap<String, Object> goalDetails) {

        SubGoal subGoal;
        switch (objectiveType){
            case SIMPLE:
                subGoal = createLocalTaskInstance(goalDetails);
                return subGoal;

            case CONTINUOUS:
                subGoal = createLocalJobInstance(goalDetails);
                return subGoal;
        }
        return null;
    }

    private SubGoal createLocalJobInstance(HashMap<String, Object> goalDetails) {
        SubGoal subGoal;

        subGoal = new Job(String.valueOf(goalDetails.get(FIELD_INTENTION_TITLE)),
                String.valueOf(goalDetails.get(FIELD_INTENTION_DESCRIPTION)),
                (Date) goalDetails.get(FIELD_INTENTION_END_DATE),
                (int) goalDetails.get(FIELD_INTENTION_GOAL_QUANTITY));
        return subGoal;
    }

    private SubGoal createLocalTaskInstance(HashMap<String, Object> goalDetails) {
        SubGoal subGoal;

        subGoal = new Task(String.valueOf(goalDetails.get(FIELD_INTENTION_TITLE)),
                        String.valueOf(goalDetails.get(FIELD_INTENTION_DESCRIPTION)),
                        (Date) goalDetails.get(FIELD_INTENTION_END_DATE));
        return subGoal;
    }

    public SubGoal createSubGoal(ObjectiveType objectiveType,
                                 BigGoal bigGoal,
                                 HashMap<String, Object> goalDetails,
                                 Dao subGoalDAO) throws SQLException {

        switch (objectiveType){
            case SIMPLE:
                GoalMaker taskGoalMaker = new TaskGoalMaker();
                SubGoal taskSubGoal = taskGoalMaker.createGoal(bigGoal, goalDetails);
                IntentionDAOHelper.createTaskRecord((Task) taskSubGoal, subGoalDAO);
                return taskSubGoal;
            case CONTINUOUS:
                GoalMaker jobGoalMaker = new JobGoalMaker();
                SubGoal jobSubGoal = jobGoalMaker.createGoal(bigGoal, goalDetails);
                IntentionDAOHelper.createJobRecord((Job) jobSubGoal, subGoalDAO);
                return jobSubGoal;
        }

        return null;
    }

    private interface GoalMaker {
        SubGoal createGoal(BigGoal bigGoal, HashMap<String, Object> goalDetails);
    }

    private class TaskGoalMaker implements GoalMaker {

        @Override
        public SubGoal createGoal(BigGoal bigGoal, HashMap<String, Object> goalDetails) {
            SubGoal subGoal = getGoalType(ObjectiveType.SIMPLE, goalDetails);
            bigGoal.assignSubIntention(subGoal);
            return subGoal;
        }
    }

    private class JobGoalMaker implements GoalMaker {

        @Override
        public SubGoal createGoal(BigGoal bigGoal, HashMap<String, Object> goalDetails) {
            SubGoal subGoal = getGoalType(ObjectiveType.CONTINUOUS, goalDetails);
            bigGoal.assignSubIntention(subGoal);
            return subGoal;
        }
    }

//    public int getDateAsSortingParameter() {
//        return Integer.parseInt(String.valueOf(getStartDate()));
//    }
}
