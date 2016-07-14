package io.sokolvault13.turtlesway.model;

import java.util.Date;

public interface Goal {

    void setBigGoal(BigGoal bigGoal);
    BigGoal getBigGoal();
    void setBigGoalId (BigGoal bigGoal);
    int getBigGoalId ();
    Date getDateAsSortingParameter();
}
