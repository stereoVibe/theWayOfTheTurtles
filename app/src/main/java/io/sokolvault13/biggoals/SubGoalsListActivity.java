package io.sokolvault13.biggoals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

public class SubGoalsListActivity extends SingleFragmentActivity {
    public static final String SUB_GOALS_LIST_FRAGMENT_TAG = "sub_goals_list";
    private static final String EXTRA_BIG_GOAL_ID = "io.sokolvault.turtlesway.big_goal_id";

    public static Intent newIntent(Context context, int bigGoalId){
        Intent intent = new Intent(context, SubGoalsListActivity.class);
        intent.putExtra(EXTRA_BIG_GOAL_ID, bigGoalId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int bigGoalId = getIntent().getIntExtra(EXTRA_BIG_GOAL_ID, -1);
        return SubGoalsListFragment.newInstance(bigGoalId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int layoutId = R.layout.activity_sub_goals_list;
        int toolbarResourceId = R.id.sub_goals_list_toolbar;
        int toolbarLayoutId = R.layout.action_bar_list_sub_goals;
        int toolbarMenuId = R.menu.subgoals_list_menu_toolbar;
        assignResources(layoutId, toolbarLayoutId, toolbarResourceId, toolbarMenuId,SUB_GOALS_LIST_FRAGMENT_TAG, true);
        super.onCreate(savedInstanceState);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        assert fab != null;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SubGoalsListActivity.this, SubGoalCreationActivity.class);
//                startActivity(intent);
//            }
//        });
    }

}
