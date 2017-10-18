package io.sokolvault13.turtlesway.presenters.bigGoalsList;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.presenters.SingleFragmentActivity;
import io.sokolvault13.turtlesway.presenters.bigGoalCreation.BigGoalCreationDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BigGoalsListActivity extends SingleFragmentActivity {
    public static final String BIG_GOALS_LIST_FRAGMENT_TAG = "big_goals_list";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected Fragment createFragment() {
        return new BigGoalsListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int layoutId = R.layout.activity_big_goals;
        int toolbarResourceId = R.id.create_big_goals_toolbar;
        int toolbarLayoutId = R.layout.action_bar_list_big_goals;
        int toolbarMenuId = R.menu.biggoals_list_menu_toolbar;
        assignResources(layoutId, toolbarLayoutId, toolbarResourceId, toolbarMenuId, BIG_GOALS_LIST_FRAGMENT_TAG, false);

        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(BigGoalsListActivity.this, BigGoalCreationActivity.class);
//                startActivity(intent);
                showBigGoalCreationDialog();
            }
        });
    }

    private void showBigGoalCreationDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        BigGoalCreationDialog dialog = new BigGoalCreationDialog();
        dialog.show(fragmentManager, "SubGoalCreationDialog");
    }
}
