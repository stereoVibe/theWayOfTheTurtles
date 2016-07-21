package io.sokolvault13.turtlesway.model;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "tasks")
public class Task extends Intention implements Goal, Comparable<Goal> {

    @DatabaseField(generatedId = true, canBeNull = false, index = true)
    protected int id;
    @DatabaseField (canBeNull = true)
    private String title;
    @DatabaseField
    private String description;
    @DatabaseField (canBeNull = false, dataType = DataType.DATE_STRING, columnName = "start_date")
    private Date startDate;
    @DatabaseField (dataType = DataType.DATE_STRING, columnName = "end_date")
    private Date endDate;
    @DatabaseField (canBeNull = false, columnName = "is_due")
    private int isOutOfDate;
    @DatabaseField (canBeNull = false, columnName = "is_complete")
    private boolean isComplete;
    @DatabaseField (foreign = true, index = true, foreignAutoRefresh = true, canBeNull = false, columnName = BIGGOAL_FIELD)
    private BigGoal mBigGoal;
    @DatabaseField (canBeNull = false, columnName = BIGGOAL_ID_FIELD)
    private int mBigGoalId;

    public Task() {
    }

    public Task (String title, String description, Date endDate) {
        this.title = title;
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = false;

        if (description != null){
            this.description = description;
        }
        if (endDate != null) {
            this.endDate = endDate;
        }
    }

    @Override
    public BigGoal getBigGoal() {
        return this.mBigGoal;
    }

    @Override
    public void setBigGoal(BigGoal bigGoal) {
        mBigGoal = bigGoal;
    }

    @Override
    public int getBigGoalId() {
        return this.mBigGoalId;
    }

    @Override
    public void setBigGoalId (BigGoal bigGoal) { this.mBigGoalId = bigGoal.getId(); }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Date getStartDate() {
        return null;
    }

    @Override
    public void setStartDate(Date startDate) {

    }

    @Override
    public Date getEndDate() {
        return this.endDate;
    }

    @Override
    public void setEndDate(Date endDate) {

    }

    @Override
    public int getOutOfDate() {
        return this.isOutOfDate;
    }

    @Override
    public void setOutOfDate(int isOutOfDate) {
    }

    @Override
    public boolean getCompleteStatus() {
        return this.isComplete;
    }

    @Override
    public void setCompleteStatus(boolean isComplete) {

    }
    @Override
    public Date getDateAsSortingParameter() {
        return this.startDate;
    }

    @Override
    public int compareTo(@NonNull Goal goal) {
        return startDate.compareTo(goal.getDateAsSortingParameter());
    }
}