package io.sokolvault13.turtlesway.Presenters.BigGoalsList;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import io.sokolvault13.turtlesway.Presenters.BigGoalCreation.BigGoalCreationActivity;
import io.sokolvault13.turtlesway.Presenters.SingleFragmentActivity;
import io.sokolvault13.turtlesway.R;

public class BigGoalsListActivity extends SingleFragmentActivity {
    public static final String BIG_GOALS_LIST_FRAGMENT_TAG = "big_goals_list";

    @Override
    protected Fragment createFragment() {
        return new BigGoalsListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int layoutId = R.layout.activity_big_goals;
        int toolbarResourceId = R.id.big_goals_list_toolbar;
        int toolbarLayoutId = R.layout.action_bar_list_big_goals;
        int toolbarMenuId = R.menu.biggoals_list_menu_toolbar;
        assignResources(layoutId, toolbarLayoutId, toolbarResourceId, toolbarMenuId, BIG_GOALS_LIST_FRAGMENT_TAG, false);

        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BigGoalsListActivity.this, BigGoalCreationActivity.class);
                startActivity(intent);
            }
        });
    }
}
