package io.sokolvault13.turtlesway.presenters.subGoalsList;

import android.view.View;

import io.sokolvault13.turtlesway.model.ObjectiveType;

interface RecyclerViewClickListener {
    void recyclerViewListClicked(View v, int position, ObjectiveType objectiveType);
}
