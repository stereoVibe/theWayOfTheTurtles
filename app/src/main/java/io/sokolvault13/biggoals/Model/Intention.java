package io.sokolvault13.biggoals.Model;

import java.util.Date;

public abstract class Intention {

    public static final String BIGGOAL_FIELD = "big_goal";
    public static final String BIGGOAL_ID_FIELD = "big_goal_id";

    private int id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int isOutOfDate;
    private int isComplete;

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
    public abstract int getCompleteStatus();
    public abstract void setCompleteStatus(int isComplete);

    public int getId(){
        return id;
    }

    private static <T extends Performable> T createEmptyIntention(ObjectiveType objectiveType){
        Object intention = null;
        switch (objectiveType){
            case SIMPLE:
                intention = new Task();
                break;
            case CONTINUOUS:
                intention = new Job();
                break;
        }
        return (T) intention;
    }

    private static <T> boolean isIntentionCorrect (T intention, ObjectiveType objectiveType) {

        return intention instanceof Job && objectiveType.equals(ObjectiveType.CONTINUOUS)
                || intention instanceof Task && objectiveType.equals(ObjectiveType.SIMPLE);
    }

    public static <T extends Performable> T createIntention(T intention, ObjectiveType objectiveType) {
        if (isIntentionCorrect(intention, objectiveType)){
            return createEmptyIntention(objectiveType);
        }
        return null;
    }
}
