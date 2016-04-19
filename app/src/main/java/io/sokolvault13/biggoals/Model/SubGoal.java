package io.sokolvault13.biggoals.Model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@DatabaseTable(tableName = "sub_goals")
public class SubGoal extends Intention {

    @DatabaseField(generatedId = true, index = true)
    private int id;
    @DatabaseField (canBeNull = false)
    private String title;
    @DatabaseField
    private String description;
    @DatabaseField (canBeNull = false, index = true, dataType = DataType.ENUM_STRING, columnName = "objective_type")
    private ObjectiveType mObjectiveType;
    @DatabaseField (canBeNull = false, dataType = DataType.DATE_STRING, columnName = "start_date")
    private Date startDate;
    @DatabaseField (dataType = DataType.DATE_STRING, columnName = "end_date")
    private Date endDate;
    @DatabaseField (canBeNull = false, columnName = "is_due")
    private int isOutOfDate;
    @DatabaseField (canBeNull = false, columnName = "is_complete")
    private int isComplete;
    @DatabaseField (canBeNull = false, columnName = "priority")
    private int mPriority;
    @ForeignCollectionField(columnName = "tasks_collects", eager = true)
    private ForeignCollection<Task> tasks;
    @DatabaseField (foreign = true, index = true, foreignAutoRefresh = true, canBeNull = false, columnName = "big_goal_id")
    private BigGoal mBigGoal;

    protected SubGoal() {
    }

    protected SubGoal(String title, ObjectiveType objectiveType) {
        this.title = title;
        this.mObjectiveType = objectiveType;
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = 0;
        this.mPriority = 1;
    }

    protected SubGoal(String title, String description, ObjectiveType objectiveType) {
        this.title = title;
        this.description = description;
        this.mObjectiveType = objectiveType;
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = 0;
        this.mPriority = 1;
    }

    public int getPriority() {
        return mPriority;
    }

    public ObjectiveType getObjectiveType() {
        return mObjectiveType;
    }

    public void setObjectiveType(ObjectiveType objectiveType) {
        mObjectiveType = objectiveType;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public void setBigGoal(BigGoal bigGoal) {
        mBigGoal = bigGoal;
    }

    public ForeignCollection<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return "SubGoal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", mObjectiveType=" + mObjectiveType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isOutOfDate=" + isOutOfDate +
                ", isComplete=" + isComplete +
                ", mPriority=" + mPriority +
                ", tasks=" + tasks +
                ", mBigGoal=" + mBigGoal +
                '}';
    }
}