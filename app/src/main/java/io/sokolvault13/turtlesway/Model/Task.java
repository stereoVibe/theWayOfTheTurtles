package io.sokolvault13.turtlesway.Model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "tasks")
public class Task extends Intention implements Goal {

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
    private int isComplete;
    @DatabaseField (foreign = true, index = true, foreignAutoRefresh = true, canBeNull = false, columnName = BIGGOAL_FIELD)
    private BigGoal mBigGoal;
    @DatabaseField (canBeNull = false, columnName = BIGGOAL_ID_FIELD)
    private int mBigGoalId;

    public Task() {

    }

//    protected Task(String title) {
//        this.title = title;
//        this.startDate = new Date();
//        this.isOutOfDate = 0;
//        this.isComplete = 0;
//    }
//
//    protected Task(String title, String description) {
//        this(title);
//        this.description = description;
//    }
//
//    protected Task(String title, Date endDate) {
//        this(title);
//        this.endDate = endDate;
//    }
//
//    protected Task(String title, String description, Date endDate){
//        this(title, description);
//        this.endDate = endDate;
//    }

    protected Task (String title, String description, Date endDate) {
        this.title = title;
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = 0;

        if (description != null){
            this.description = description;
        }
        if (endDate != null) {
            this.description = description;
        }
    }

    @Override
    public void setBigGoal(BigGoal bigGoal) {
        mBigGoal = bigGoal;
    }
    @Override
    public BigGoal getBigGoal() {
        return this.mBigGoal;
    }
    @Override
    public void setBigGoalId (BigGoal bigGoal) { this.mBigGoalId = bigGoal.getId(); }
    @Override
    public int getBigGoalId() {
        return this.mBigGoalId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {

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
        return null;
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
}