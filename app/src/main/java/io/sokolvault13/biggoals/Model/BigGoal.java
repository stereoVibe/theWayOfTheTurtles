package io.sokolvault13.biggoals.Model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.Date;

@DatabaseTable(tableName = "big_goals")
public class BigGoal extends Intention {
    public static final String SUBGOALS_COLLECTS_FIELD = "subgoals_collects";

    @DatabaseField (generatedId = true, canBeNull = false)
    protected int id;
    @DatabaseField (canBeNull = false, useGetSet = true)
    private String title;
    @DatabaseField (canBeNull = true)
    private String description;
    @DatabaseField (canBeNull = false, dataType = DataType.DATE_STRING, columnName = "start_date")
    private Date startDate;
    @DatabaseField (dataType = DataType.DATE_STRING, columnName = "end_date", canBeNull = true)
    private Date endDate;
    @DatabaseField (canBeNull = false, columnName = "is_due")
    private int isOutOfDate;
    @DatabaseField (canBeNull = false, columnName = "is_complete")
    private int isComplete;
    @DatabaseField (canBeNull = false, columnName = "progress")
    private int mProgress;
    @ForeignCollectionField (columnName = SUBGOALS_COLLECTS_FIELD, eager = true)
    private ForeignCollection <SubGoal> subGoals;

    public BigGoal() {
    }

    public BigGoal(String title) {
        setTitle(title);
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = 0;
        this.mProgress = 0;
    }

    public BigGoal(String title, String description) {
        this(title);
        this.description = description;
    }

    public BigGoal(String title, Date endDate){
        this(title);
        this.endDate = endDate;
    }

    public BigGoal(String title, String description, Date endDate){
        this(title, description);
        this.endDate = endDate;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }

    public Collection<SubGoal> getSubGoals() {
        return this.subGoals;
    }

    @Override
    public String toString() {
        return "BigGoal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", isOutOfDate=" + isOutOfDate +
                ", isComplete=" + isComplete +
                ", subGoals=" + subGoals +
                '}';
    }

    public SubGoal createSubGoal(SubGoal subGoal){
        subGoal.setBigGoal(this);
        subGoal.setBigGoalId(this);
        return subGoal;
    }
}
