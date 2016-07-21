package io.sokolvault13.turtlesway.model;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public abstract class Intention {

    public static final String BIGGOAL_FIELD = "big_goal";
    public static final String BIGGOAL_ID_FIELD = "big_goal_id";

    private int id;
//    private String title;
//    private String description;
//    private Date startDate;
//    private Date endDate;
//    private int isOutOfDate;
//    private int isComplete;

    public static HashMap<String, Object> prepareSubGoal(String title, String description, Date endDate, int goalQuantity) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title", title);
        hashMap.put("description", description);
        hashMap.put("endDate", endDate);
        if (goalQuantity > 0)
            hashMap.put("goalQuantity", goalQuantity);
        return hashMap;
    }

//    public abstract int getId();
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

    public int getId(){
        return id;
    }

    private Goal getGoalType(ObjectiveType objectiveType, HashMap<String, Object> goalDetails) {

        Goal goal;
        switch (objectiveType){
            case SIMPLE:
                goal = createLocalTaskInstance(goalDetails);
                return goal;

            case CONTINUOUS:
                goal = createLocalJobInstance(goalDetails);
                return goal;
        }
//        GoalMaker maker = new TaskGoalMaker();
//        Goal goal = maker.createGoal(ObjectiveType.SIMPLE,null,"title", "description", 2);
        return null;
    }

    private Goal createLocalJobInstance(HashMap<String, Object> goalDetails) {
        Goal goal;

        goal = new Job(String.valueOf(goalDetails.get("title")),
                String.valueOf(goalDetails.get("description")),
                (Date) goalDetails.get("endDate"),
                (int) goalDetails.get("goalQuantity"));
        return goal;
    }

    private Goal createLocalTaskInstance(HashMap<String, Object> goalDetails){
        Goal goal;

        goal = new Task(String.valueOf(goalDetails.get("title")),
                        String.valueOf(goalDetails.get("description")),
                        (Date) goalDetails.get("endDate"));
        return goal;
    }

    public Goal createSubGoal(ObjectiveType objectiveType,
                              BigGoal bigGoal,
                              HashMap<String, Object> goalDetails,
                              Dao subGoalDAO) throws SQLException {

        switch (objectiveType){
            case SIMPLE:
                GoalMaker taskGoalMaker = new TaskGoalMaker();
                Goal taskGoal = taskGoalMaker.createGoal(bigGoal, goalDetails);
                IntentionDAOHelper.createTaskRecord((Task) taskGoal, subGoalDAO);
                return taskGoal;
            case CONTINUOUS:
                GoalMaker jobGoalMaker = new JobGoalMaker();
                Goal jobGoal = jobGoalMaker.createGoal(bigGoal, goalDetails);
                IntentionDAOHelper.createJobRecord((Job) jobGoal, subGoalDAO);
                return jobGoal;
        }

        return null;
    }

    private interface GoalMaker {
        Goal createGoal(BigGoal bigGoal, HashMap<String, Object> goalDetails);
    }

    private class TaskGoalMaker implements GoalMaker {

        @Override
        public Goal createGoal(BigGoal bigGoal, HashMap<String, Object> goalDetails) {
            Goal goal = getGoalType(ObjectiveType.SIMPLE, goalDetails);
            bigGoal.assignSubIntention(goal);
            return goal;
        }
    }

    private class JobGoalMaker implements GoalMaker {

        @Override
        public Goal createGoal(BigGoal bigGoal, HashMap<String, Object> goalDetails) {
            Goal goal = getGoalType(ObjectiveType.CONTINUOUS, goalDetails);
            bigGoal.assignSubIntention(goal);
            return goal;
        }
    }

//    public int getDateAsSortingParameter() {
//        return Integer.parseInt(String.valueOf(getStartDate()));
//    }
}
