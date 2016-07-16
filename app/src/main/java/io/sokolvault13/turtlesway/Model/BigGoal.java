package io.sokolvault13.turtlesway.model;

import android.util.Log;

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
    public static final String TASKS_COLLECTS_FIELD = "tasks_collects";

    @DatabaseField (generatedId = true, canBeNull = false)
    private int id;
    @DatabaseField (canBeNull = false)
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
    private ForeignCollection <Job> mJobs;
    @ForeignCollectionField (columnName = TASKS_COLLECTS_FIELD, eager = true)
    private ForeignCollection <Task> tasks;

    public BigGoal() {
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = 0;
    }
//
    public BigGoal(String title) {
        this.title = title;
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = 0;
    }
//
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


    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return endDate;
    }

    @Override
    public void setEndDate(Date endDate) {

    }

    @Override
    public int getOutOfDate() {
        return 0;
    }

    @Override
    public void setOutOfDate(int isOutOfDate) {

    }

    @Override
    public int getCompleteStatus() {
        return 0;
    }

    @Override
    public void setCompleteStatus(int isComplete) {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }

    public Collection<Job> getJobs() {
        return this.mJobs;
    }

    public <T extends Goal> void assignSubIntention(T subIntention) {
//        B subIntentionTemp = (B) subIntention;
//        if (subIntentionTemp.getTitle() != null){
            subIntention.setBigGoal(this);
            subIntention.setBigGoalId(this);
            Log.d("Assigning BigGoal", subIntention.getBigGoal().toString());
//        }
    }

}
