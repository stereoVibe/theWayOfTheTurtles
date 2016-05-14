package io.sokolvault13.biggoals.Model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "sub_goals")
public class SubGoal extends SubIntention {

    @DatabaseField(generatedId = true, canBeNull = false, index = true)
    private int id;
    @DatabaseField (canBeNull = false)
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
    @DatabaseField (canBeNull = false, columnName = "priority")
    private int mPriority;
    @DatabaseField (foreign = true, index = true, foreignAutoRefresh = true, canBeNull = false, columnName = BIGGOAL_FIELD)
    private BigGoal mBigGoal;
    @DatabaseField (canBeNull = false, columnName = BIGGOAL_ID_FIELD)
    private int mBigGoalId;

    public SubGoal() {
    }

    public SubGoal(String title) {
        this.title = title;
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = 0;
        this.mPriority = 1;
    }

    public SubGoal (String title, int priority){
        this(title);
        this.mPriority = priority;
    }

    public SubGoal(String title, String description) {
        this(title);
        this.description = description;
    }

    public SubGoal (String title, String description, int priority){
        this(title, description);
        this.mPriority = priority;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public int getBigGoalId() {
        return mBigGoalId;
    }

    @Override
    public void setBigGoal(BigGoal bigGoal) {
        this.mBigGoal = bigGoal;
    }

    @Override
    public void setBigGoalId (BigGoal bigGoal) { this.mBigGoalId = bigGoal.getId(); }


}