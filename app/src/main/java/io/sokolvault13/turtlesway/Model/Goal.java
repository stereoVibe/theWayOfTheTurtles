package io.sokolvault13.turtlesway.model;

import java.util.Date;

public interface Goal {

    BigGoal getBigGoal();

    void setBigGoal(BigGoal bigGoal);

    int getBigGoalId ();

    void setBigGoalId(BigGoal bigGoal);

    int getId();
    Date getDateAsSortingParameter();
}
