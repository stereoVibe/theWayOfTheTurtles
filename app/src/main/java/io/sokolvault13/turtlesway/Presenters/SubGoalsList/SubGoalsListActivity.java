package io.sokolvault13.turtlesway.presenters.SubGoalsList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
import io.sokolvault13.turtlesway.model.BigGoal;
import io.sokolvault13.turtlesway.model.Intention;
import io.sokolvault13.turtlesway.model.IntentionDAOHelper;
import io.sokolvault13.turtlesway.model.Job;
import io.sokolvault13.turtlesway.model.Task;
import io.sokolvault13.turtlesway.presenters.SingleFragmentActivity;
import io.sokolvault13.turtlesway.presenters.SubGoalCreation.SubGoalCreationDialog;
import io.sokolvault13.turtlesway.utils.Constants;

public class SubGoalsListActivity extends SingleFragmentActivity implements SubGoalDetailsDialog.NoticeDialogListener {
    public static final String SUB_GOALS_LIST_FRAGMENT_TAG = "sub_goals_list";


    private DatabaseHelper dbHelper;
    private Dao<BigGoal, Integer> mBigGoalsDAO;
    private Dao<Job, Integer> mJobsDAO;
    private Dao<Task, Integer> mTasksDAO;
    private int mBigGoalId;
    private BigGoal mBigGoal;
    private Menu mMenu;

    private EditText mExpandedTitle, mDescription;
    private TextView mEndDate;
    private NumberProgressBar mProgressBar;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private boolean isToolbarCollapsed = false;
    private boolean editMode = false;

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

        mExpandedTitle = (EditText) findViewById(R.id.big_goal_inner_expanded_title);
        mExpandedTitle.setEnabled(false);
        mExpandedTitle.setCursorVisible(false);

        mDescription = (EditText) findViewById(R.id.big_goal_inner_description);
        mDescription.setEnabled(false);
        mDescription.setCursorVisible(false);

        mEndDate = (TextView) findViewById(R.id.big_goal_inner_end_date);
        mProgressBar = (NumberProgressBar) findViewById(R.id.big_goal_inner_progressBar);
        mToolbar = (Toolbar) findViewById(R.id.sub_goals_list_toolbar_collapsed);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_sub_goal_btn);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = SubGoalCreationActivity.newIntent(getApplicationContext(), mBigGoalId);
//                startActivity(intent);
                FragmentManager fragmentManager = getSupportFragmentManager();
                SubGoalCreationDialog dialog = SubGoalCreationDialog.newInstance(mBigGoalId);
                dialog.show(fragmentManager, "SubGoalCreationDialog");
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete_big_goal:
                showDeleteBigGoalConfirmationDialog();
                return true;
            case R.id.edit_big_goal:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                enableEditBigGoalMode();
                return true;
            case R.id.save_big_goal:
                updateBigGoal();
                enableEditBigGoalMode();
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
//        mCollapsingToolbarLayout.setTitleEnabled(false);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                verticalOffset = Math.abs(verticalOffset);
                int difference = appBarLayout.getHeight() - mToolbar.getHeight();

//                Log.d("onOffsetChanged", "VO:" + String.valueOf(verticalOffset + "difference: " + String.valueOf(difference)));
                if (verticalOffset >= difference && !isToolbarCollapsed) {
                    TranslateAnimation animation = new TranslateAnimation(0, 0, Animation.RELATIVE_TO_SELF, mToolbar.getHeight() - mExpandedTitle.getHeight() * 1.05f);
                    animation.setDuration(400);
                    animation.setFillAfter(true);
                    isToolbarCollapsed = true;
                    mExpandedTitle.startAnimation(animation);
//                    Log.d("onOffsetChanged", "verticalOffset is larger or equal");
                }
                if (isToolbarCollapsed && verticalOffset == 0) {
                    TranslateAnimation animation = new TranslateAnimation(0, 0, mToolbar.getHeight() - mExpandedTitle.getHeight() * 1.05f, Animation.RELATIVE_TO_SELF);
                    animation.setDuration(400);
                    animation.setFillAfter(true);
                    isToolbarCollapsed = false;
                    mExpandedTitle.startAnimation(animation);
                }
//                } else if (isToolbarCollapsed) {
//                    TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_SELF, 0.2f, Animation.RELATIVE_TO_SELF, 0.2f);
//                    animation.setDuration(500);
//                    animation.setFillAfter(true);
//                    isToolbarCollapsed = false;
//                    mExpandedTitle.startAnimation(animation);
//                    Log.d("onOffsetChanged", "verticalOffset is smaller");
//                }
            }
        });

        super.onPostCreate(savedInstanceState);

        mExpandedTitle.setText(mBigGoal.getTitle());
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

    private void enableEditBigGoalMode() {
        if (!editMode) {
            setEditableMode(true);
        } else {
            setEditableMode(false);
        }
    }

    private void setEditableMode(boolean mode) {
        MenuItem editItem = mMenu.findItem(R.id.edit_big_goal);
        MenuItem saveItem = mMenu.findItem(R.id.save_big_goal);
        mExpandedTitle.setEnabled(mode);
        mExpandedTitle.setCursorVisible(mode);
        mDescription.setEnabled(mode);
        mDescription.setCursorVisible(mode);
        editMode = mode;
        editItem.setEnabled(!mode);
        editItem.setVisible(!mode);
        saveItem.setEnabled(mode);
        saveItem.setVisible(mode);
    }

    private void updateBigGoal() {
        HashMap<String, Object> intentionFields = new HashMap<>();
        intentionFields.put(Intention.FIELD_INTENTION_TITLE, String.valueOf(mExpandedTitle.getText()));
        intentionFields.put(Intention.FIELD_INTENTION_DESCRIPTION, String.valueOf(mDescription.getText()));
        try {
            IntentionDAOHelper.updateIntention(mBigGoalsDAO, mBigGoal, intentionFields);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), getResources().getText(R.string.save_successful), Toast.LENGTH_SHORT).show();
    }


    private void updateBigGoalProgressBar() {
        try {
            mBigGoal = IntentionDAOHelper.getBigGoal(mBigGoalsDAO, mBigGoalId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mProgressBar.setProgress((int) mBigGoal.getProgress());
    }

    @Override
    public void onDialogClick(View.OnClickListener dialogFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SubGoalsListFragment fragment = (SubGoalsListFragment) fragmentManager.findFragmentByTag(SUB_GOALS_LIST_FRAGMENT_TAG);
        try {
            fragment.updateUI();
            updateBigGoalProgressBar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
