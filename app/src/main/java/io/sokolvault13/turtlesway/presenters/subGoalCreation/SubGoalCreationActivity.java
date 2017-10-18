package io.sokolvault13.turtlesway.presenters.subGoalCreation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.presenters.SingleFragmentActivity;
import io.sokolvault13.turtlesway.utils.Constants;

public class SubGoalCreationActivity extends SingleFragmentActivity {
    public static final String CREATE_SUB_GOAL_FRAGMENT_TAG = "create_sub_goal_tag";
    SubGoalCreationFragment mFragment;
    private int mBigGoalId;

    public static Intent newIntent(Context context, int bigGoalID){
        Intent intent = new Intent(context, SubGoalCreationActivity.class);
        intent.putExtra(Constants.EXTRA_BIG_GOAL_ID, bigGoalID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        mBigGoalId = getIntent().getIntExtra(Constants.EXTRA_BIG_GOAL_ID, -1);
        return SubGoalCreationFragment.newInstance(mBigGoalId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int layoutId = R.layout.activity_create_sub_goal;
        int toolbarResourceId = R.id.create_sub_goals_toolbar;
        int toolbarLayoutId = R.layout.action_bar_create_sub_goal;
        int toolbarMenuId = R.menu.create_goals_menu_toolbar;
        assignResources(layoutId,
                toolbarLayoutId,
                toolbarResourceId,
                toolbarMenuId,
                CREATE_SUB_GOAL_FRAGMENT_TAG,
                true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.continue_creation:
                mFragment = (SubGoalCreationFragment) getSupportFragmentManager().findFragmentByTag(CREATE_SUB_GOAL_FRAGMENT_TAG);
                try {
                    if (mFragment.createSubGoal()) {
                        onBackPressed();
                        finish();
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
