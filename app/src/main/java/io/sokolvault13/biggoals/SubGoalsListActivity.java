package io.sokolvault13.biggoals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.Model.Intention;
import io.sokolvault13.biggoals.Model.IntentionDAOHelper;
import io.sokolvault13.biggoals.Model.Job;
import io.sokolvault13.biggoals.Model.ObjectiveType;
import io.sokolvault13.biggoals.Model.Task;
import io.sokolvault13.biggoals.db.DatabaseHelper;
import io.sokolvault13.biggoals.db.HelperFactory;

public class SubGoalsListActivity extends SingleFragmentActivity {
    public static final String SUB_GOALS_LIST_FRAGMENT_TAG = "sub_goals_list";
    private static final String EXTRA_BIG_GOAL_ID = "io.sokolvault.turtlesway.big_goal_id";

    private DatabaseHelper dbHelper;
    private Dao<BigGoal, Integer> mBigGoalsDAO;
    private Dao<Job, Integer> mJobsDAO;
    private Dao<Task, Integer> mTasksDAO;
    private int mBigGoalId;
    BigGoal mBigGoal;

    private TextView mDescription;
    private TextView mEndDate;
    private NumberProgressBar mProgressBar;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;


    public static Intent newIntent(Context context, int bigGoalId){
        Intent intent = new Intent(context, SubGoalsListActivity.class);
        intent.putExtra(EXTRA_BIG_GOAL_ID, bigGoalId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        mBigGoalId = getIntent().getIntExtra(EXTRA_BIG_GOAL_ID, -1);
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
//            Job job = Intention.createIntention(new Job(), ObjectiveType.CONTINUOUS);
//            Task task = Intention.createIntention(new Task(), ObjectiveType.SIMPLE);
//            createJob(job);
//            createTask(task);
//            mBigGoal.assignSubIntention(job);
//            mBigGoal.assignSubIntention(task);
//            IntentionDAOHelper.createJobRecord(job,mJobsDAO);
//            IntentionDAOHelper.createTaskRecord(task, mTasksDAO);

        } catch (SQLException e){
            e.printStackTrace();
        }


//        mCollapsingToolbarLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.getParent().requestDisallowInterceptTouchEvent(false);
//                return false;
//            }
//        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete_big_goal:
                try {
                    IntentionDAOHelper.deleteIntention(mBigGoal, mBigGoalsDAO);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

        mBigGoal.setProgress(34);

        mDescription.setText(mBigGoal.getDescription());

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        mEndDate.setText(String.format("%s", dateFormat.format(mBigGoal.getEndDate())));

        mProgressBar.setProgress(mBigGoal.getProgress());
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

    public HashMap<String, Dao<? extends Intention, Integer>> getAllDAO() throws SQLException {
        HashMap<String, Dao<? extends Intention, Integer>> daosMap = new HashMap<>();

        daosMap.put("BigGoalsDAO", dbHelper.getBigGoalDAO());
        daosMap.put("JobsDAO", dbHelper.getJobDAO());
        daosMap.put("TasksDAO", dbHelper.getTaskDAO());
//        Map syncDaosMap = Collections.synchronizedMap(daosMap);
        return daosMap;
    }

    private void createJob(Job job){
        if (job != null) {
            job.setTitle("Создать и выложить 30 приложений в GoogleStore");
            job.setGoalQuantity(30);
        }
    }

    private void createTask(Task task) {
        if (task != null){
            task.setTitle("Записать сюда требования для Toptal");
        }
    }
}
