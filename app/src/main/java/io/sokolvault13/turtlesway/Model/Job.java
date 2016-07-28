package io.sokolvault13.turtlesway.model;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "jobs")
public class Job extends Intention implements Goal, Comparable<Goal> {

    @DatabaseField(generatedId = true, canBeNull = false, index = true)
    private int id;
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
    @DatabaseField (canBeNull = false, columnName = "completed_quantity")
    private int mCompletedQuantity;
    @DatabaseField (canBeNull = false, columnName = "goals_quantity")
    private int mGoalQuantity = 0;
    @DatabaseField(canBeNull = false, columnName = "priority", defaultValue = "1")
    private int mPriority;
    @DatabaseField(canBeNull = false, dataType = DataType.DOUBLE, columnName = "progress", defaultValue = "0")
    private double mProgress;
    @DatabaseField (foreign = true, index = true, foreignAutoRefresh = true, canBeNull = false, columnName = BIGGOAL_FIELD)
    private BigGoal mBigGoal;
    @DatabaseField (canBeNull = false, columnName = BIGGOAL_ID_FIELD)
    private int mBigGoalId;

    public Job() {
    }

    protected Job(String title, String description, Date endDate, int goalQuantity){
        this.title = title;
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = false;
        this.mCompletedQuantity = 0;

        if (goalQuantity > 0) {
            this.mGoalQuantity = goalQuantity;
        }

        if (description != null){
            this.description = description;
        }
        if (endDate != null) {
            this.endDate = endDate;
        }

    }

    public int getCompletedQuantity() {
        return mCompletedQuantity;
    }
    public void setCompletedQuantity(int completedQuantity) {
        this.mCompletedQuantity = completedQuantity;
    }

    public int getGoalQuantity() {
        return mGoalQuantity;
    }
    public void setGoalQuantity(int goalQuantity) {
        this.mGoalQuantity = goalQuantity;
    }

    public double getProgress() {
        return mProgress;
    }

    public void addProgress(double progress) {
        mProgress += progress;
    }

    public int getPriority() {
        return mPriority;
    }
    public void setPriority(int priority) {
        mPriority = priority;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return title;
    }
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
        return startDate;
    }
    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }
    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int getOutOfDate() {
        return isOutOfDate;
    }
    @Override
    public void setOutOfDate(int isOutOfDate) {
        this.isOutOfDate = isOutOfDate;
    }

    @Override
    public boolean getCompleteStatus() {
        return isComplete;
    }

    public void setCompleteStatus(boolean isComplete) {
        this.isComplete = isComplete;
    }

    @Override
    public BigGoal getBigGoal() {
        return this.mBigGoal;
    }

    @Override
    public void setBigGoal(BigGoal bigGoal) {
        this.mBigGoal = bigGoal;
    }

    @Override
    public int getBigGoalId() {
        return mBigGoalId;
    }

    @Override
    public void setBigGoalId(BigGoal bigGoal) {
        this.mBigGoalId = bigGoal.getId();
    }

    @Override
    public Date getDateAsSortingParameter() {
        return this.startDate;
    }

    @Override
    public int compareTo(@NonNull Goal goal) {
//        int date = Integer.parseInt(String.valueOf(this.startDate));
        return startDate.compareTo(goal.getDateAsSortingParameter());
    }

}