package io.sokolvault13.biggoals;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class SubGoalsListActivity extends SingleFragmentActivity {
    private static final String EXTRA_BIG_GOAL_ID = "io.sokolvault.turtlesway.big_goal_id";

    public static Intent newIntent(Context context, int bigGoalId){
        Intent intent = new Intent(context, SubGoalsListActivity.class);
        intent.putExtra(EXTRA_BIG_GOAL_ID, bigGoalId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int bigGoalId = getIntent().getIntExtra(EXTRA_BIG_GOAL_ID, 0);
        return SubGoalsListFragment.newInstance(bigGoalId);
    }

}
