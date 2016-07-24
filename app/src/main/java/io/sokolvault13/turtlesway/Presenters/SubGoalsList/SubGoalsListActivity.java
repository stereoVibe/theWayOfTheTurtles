package io.sokolvault13.turtlesway.presenters.SubGoalsList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.j256.ormlite.dao.Dao;

import java.text.DateFormat;
import java.util.Locale;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
import io.sokolvault13.turtlesway.model.BigGoal;
import io.sokolvault13.turtlesway.model.IntentionDAOHelper;
import io.sokolvault13.turtlesway.model.Job;
import io.sokolvault13.turtlesway.model.Task;
import io.sokolvault13.turtlesway.presenters.SingleFragmentActivity;
import io.sokolvault13.turtlesway.presenters.SubGoalCreation.SubGoalCreationActivity;
import io.sokolvault13.turtlesway.utils.Constants;

public class SubGoalsListActivity extends SingleFragmentActivity {
    public static final String SUB_GOALS_LIST_FRAGMENT_TAG = "sub_goals_list";


    private DatabaseHelper dbHelper;
    private Dao<BigGoal, Integer> mBigGoalsDAO;
    private Dao<Job, Integer> mJobsDAO;
    private Dao<Task, Integer> mTasksDAO;
    private int mBigGoalId;
    private BigGoal mBigGoal;

    private TextView mDescription;
    private TextView mEndDate;
    private NumberProgressBar mProgressBar;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;


    public static Intent newIntent(Context context, int bigGoalId){
        Intent intent = new Intent(context, SubGoalsListActivity.class);
        intent.putExtra(Constants.EXTRA_BIG_GOAL_ID, bigGoalId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        mBigGoalId = getIntent().getIntExtra(Constants.EXTRA_BIG_GOAL_ID, -1);
        return SubGoalsListFragment.newInstance(mBigGoalId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int layoutId = R.layout.activity_sub_goals_list;
        int toolbarResourceId = R.id.sub_goals_list_toolbar_uncollapse;
        int toolbarLayoutId = R.layout.action_bar_list_sub_goals;
        int toolbarMenuId = R.menu.subgoals_list_menu_toolbar;
        assignResources(layoutId,
                toolbarLayoutId,
                toolbarResourceId,
                toolbarMenuId,
                SUB_GOALS_LIST_FRAGMENT_TAG,
                true);

        HelperFactory.setHelper(getApplication());

        super.onCreate(savedInstanceState);

        mDescription = (TextView) findViewById(R.id.big_goal_inner_description);
        mEndDate = (TextView) findViewById(R.id.big_goal_inner_end_date);
        mProgressBar = (NumberProgressBar) findViewById(R.id.big_goal_inner_progressBar);
        mToolbar = (Toolbar) findViewById(R.id.sub_goals_list_toolbar_collapsed);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.sub_goals_collapsingToolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.sub_goals_list_container);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dbHelper = HelperFactory.getHelper();
        try {
            mBigGoalsDAO = dbHelper.getBigGoalDAO();
            mJobsDAO = dbHelper.getJobDAO();
            mTasksDAO = dbHelper.getTaskDAO();
            mBigGoal = IntentionDAOHelper.getBigGoal(mBigGoalsDAO, mBigGoalId);

            // Needs to be deleted
//            HashMap<String, Object> hashMap = Intention.prepareSubGoal("Создать и выложить 30 приложений в GoogleStore",
//                    null, null, 30);
////            Intention.createSubGoal(ObjectiveType.SIMPLE, mBigGoal, hashMap, mTasksDAO);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_sub_goal_btn);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SubGoalCreationActivity.newIntent(getApplicationContext(), mBigGoalId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete_big_goal:
                showDeleteBigGoalConfirmationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null && mBigGoal.getTitle() != null){
//            actionBar.setTitle(mBigGoal.getTitle());
//        }
        mCollapsingToolbarLayout.setTitle(mBigGoal.getTitle());

        super.onPostCreate(savedInstanceState);

//        mBigGoal.setProgress(34);
        mDescription.setText(mBigGoal.getDescription());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        mEndDate.setText(String.format("%s", dateFormat.format(mBigGoal.getEndDate())));
        mProgressBar.setProgress((int) mBigGoal.getProgress());
    }

    @Override
    protected void onResume() {
        if (dbHelper == null) {
            HelperFactory.setHelper(getApplication());
            dbHelper = HelperFactory.getHelper();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (dbHelper != null){
            HelperFactory.releaseHelper();
        }
        super.onPause();
    }

    private void showDeleteBigGoalConfirmationDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DeleteBigGoalConfirmationDialog deleteBigGoalConfirmationDialog = DeleteBigGoalConfirmationDialog.newInstance(mBigGoal.getTitle(), mBigGoalId);
        deleteBigGoalConfirmationDialog.show(fragmentManager, "Alert Dialog");
    }
}
