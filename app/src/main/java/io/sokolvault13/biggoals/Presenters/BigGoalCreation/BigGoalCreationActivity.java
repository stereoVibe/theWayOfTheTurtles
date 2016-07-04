package io.sokolvault13.biggoals.Presenters.BigGoalCreation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.sokolvault13.biggoals.Presenters.SingleFragmentActivity;
import io.sokolvault13.biggoals.R;

public class BigGoalCreationActivity extends SingleFragmentActivity {
    private BigGoalCreationFragment mFragment;
    public static final String CREATE_BIG_GOAL_FRAGMENT_TAG = "create_big_goal_tag";

    @Override
    protected Fragment createFragment() {
        return new BigGoalCreationFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int layoutId = R.layout.activity_create_big_goal;
        int toolbarResourceId = R.id.big_goals_list_toolbar;
        int toolbarLayoutId = R.layout.action_bar_create_big_goal;
        int toolbarMenuId = R.menu.create_big_goal_menu_toolbar;
        assignResources(layoutId,
                toolbarLayoutId,
                toolbarResourceId,
                toolbarMenuId,
                CREATE_BIG_GOAL_FRAGMENT_TAG,
                true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.continue_creation:
                mFragment = (BigGoalCreationFragment) getSupportFragmentManager().findFragmentByTag(CREATE_BIG_GOAL_FRAGMENT_TAG);
                mFragment.createBigGoal();
                Toast.makeText(BigGoalCreationActivity.this, "Button Pressed!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
//        HelperFactory.releaseHelper();
    }




}
