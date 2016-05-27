package io.sokolvault13.biggoals;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SubGoalsListFragment extends Fragment {
    public static final String ARG_BIG_GOAL_ID = "big_goal_id";

    public static SubGoalsListFragment newInstance(int bigGoalId){
        Bundle args = new Bundle();
        args.putInt(ARG_BIG_GOAL_ID, bigGoalId);

        SubGoalsListFragment fragment = new SubGoalsListFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
