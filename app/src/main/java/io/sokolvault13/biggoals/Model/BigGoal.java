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
//    private Collection<SubGoal> mSubGoals;
    @ForeignCollectionField (columnName = "subgoals_collects", eager = true)
    private ForeignCollection <SubGoal> subGoals;

    public BigGoal() {
    }

    public BigGoal(String title) {
        setTitle(title);
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = 0;
    }

    public BigGoal(String title, String description) {
        this.title = title;
        this.description = description;
        this.startDate = new Date();
        this.isOutOfDate = 0;
        this.isComplete = 0;
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

    public Collection<SubGoal> getSubGoals() {
        return this.subGoals;
    }

    private void addSubGoal(SubGoal subGoal) { this.subGoals.add(subGoal);
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

    public SubGoal createSubGoal(String title, ObjectiveType type){
        SubGoal subGoal = new SubGoal(title, type);
        subGoal.setBigGoal(this);
        addSubGoal(subGoal);
        return subGoal;
    }

    public SubGoal createSubGoal(String title, String description, ObjectiveType type){
        SubGoal subGoal = new SubGoal(title, description, type);
        subGoal.setBigGoal(this);
        addSubGoal(subGoal);
        return subGoal;
    }
}
