package io.sokolvault13.biggoals.Model;

import java.util.Date;

public abstract class Intention {
    private int id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int isOutOfDate;
    private int isComplete;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public final boolean isOutOfDate() {
        return 0 != this.isOutOfDate;
    }

    public void setIsOutOfDate(int isOutOfDate) {
        this.isOutOfDate = isOutOfDate;
    }

    public final boolean isComplete() {
        return 0 != this.isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public int getIsOutOfDate() {
        return isOutOfDate;
    }

    public int getIsComplete() {
        return isComplete;
    }

//    public final boolean hasId(){
//        return 0 != this.id;
//    }

}
