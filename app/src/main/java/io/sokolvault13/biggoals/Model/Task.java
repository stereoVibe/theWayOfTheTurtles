package io.sokolvault13.biggoals.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tasks")
public class Task extends Intention {
    public static final String SUBGOAL_ID_FIELD = "sub_goal_id";

    @DatabaseField(generatedId = true, canBeNull = false, index = true)
    protected int id;
    @DatabaseField (canBeNull = false)
    private String title;
    @DatabaseField
    private String description;
    @DatabaseField (canBeNull = false, columnName = "is_due")
    private int isOutOfDate;
    @DatabaseField (canBeNull = false, columnName = "is_complete")
    private int isComplete;
    @DatabaseField (foreign = true, index = true, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false, columnName = "sub_goal")
    private SubGoal mSubGoal;
    @DatabaseField (canBeNull = false, columnName = SUBGOAL_ID_FIELD)
    private int mSubGoalId;

    protected Task() {
    }

    protected Task(String title) {
        this.title = title;
        this.isOutOfDate = 0;
        this.isComplete = 0;
    }

    protected Task(String title, String description) {
        this(title);
        this.description = description;
    }

    @Override
    public int getId() {
        return id;
    }

    protected void setSubGoal(SubGoal subGoal){
        this.mSubGoal = subGoal;
    }

    protected void setSubGoalId (SubGoal subGoalId) { this.mSubGoalId = subGoalId.getId();}

}