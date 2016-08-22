package io.sokolvault13.turtlesway.model;

import java.util.Date;

public interface SubGoal {
    String COMPLETE_STATUS = "isComplete";

    BigGoal getBigGoal();

    void setBigGoal(BigGoal bigGoal);

    void setBigGoalId(BigGoal bigGoal);

    int getId();

    Date getDateAsSortingParameter();
}
