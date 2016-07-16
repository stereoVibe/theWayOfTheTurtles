package io.sokolvault13.turtlesway.presenters.SubGoalsList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;

import io.sokolvault13.turtlesway.model.BigGoal;
import io.sokolvault13.turtlesway.model.Intention;
import io.sokolvault13.turtlesway.model.IntentionDAOHelper;
import io.sokolvault13.turtlesway.model.Job;
import io.sokolvault13.turtlesway.model.ObjectiveType;
import io.sokolvault13.turtlesway.model.Task;
import io.sokolvault13.turtlesway.presenters.BigGoalsList.BigGoalsListActivity;
import io.sokolvault13.turtlesway.presenters.SingleFragmentActivity;
import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
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
//            Job job = (Job) new Job().createSubGoal(ObjectiveType.CONTINUOUS, mBigGoal, hashMap, mJobsDAO);

//            Job job = (Job) Intention.createIntention(ObjectiveType.CONTINUOUS);
//            Task task = (Task) Intention.createIntention(ObjectiveType.SIMPLE);
//            Job job1 = (Job) Intention.createIntention(ObjectiveType.SIMPLE);
//            createJob(job);
//            createTask(task);
//            createJob(job1);
//            mBigGoal.assignSubIntention(job);
//            mBigGoal.assignSubIntention(task);
//            mBigGoal.assignSubIntention(job1);
//            IntentionDAOHelper.createJobRecord(job,mJobsDAO);
//            IntentionDAOHelper.createTaskRecord(task, mTasksDAO);
//            IntentionDAOHelper.createJobRecord(job1, mJobsDAO);

        } catch (Exception e) {
            e.printStackTrace();
        }


//        mCollapsingToolbarLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.getParent().requestDisallowInterceptTouchEvent(false);
//                return false;
//            }
//        });

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

    private void showDeleteBigGoalConfirmationDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DeleteConfirmationDialog deleteConfirmationDialog = DeleteConfirmationDialog.newInstance(mBigGoal.getTitle(), mBigGoalId);
        deleteConfirmationDialog.show(fragmentManager, "Alert Dialog");
    }

    public static class DeleteConfirmationDialog extends DialogFragment {
        DatabaseHelper helper;
        BigGoal bigGoal;
        int bigGoalID;
        Dao<BigGoal, Integer> bigGoalDAO;

        public DeleteConfirmationDialog() {
        }

        public static DeleteConfirmationDialog newInstance(String title, int bigGoalID){
            DeleteConfirmationDialog dialog = new DeleteConfirmationDialog();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putInt(Constants.EXTRA_BIG_GOAL_ID, bigGoalID);
            dialog.setArguments(args);

            return dialog;
        }

        @Override
        public void onAttach(Context context) {
            helper = HelperFactory.getHelper();
            super.onAttach(context);
        }

        @Override
        public void onDestroyView() {
            HelperFactory.releaseHelper();
            super.onDestroyView();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title"),
                    confirmation = getResources().getString(R.string.delete_big_goal_dialog),
                    ok = getResources().getString(R.string.confirm_delete_big_goal_dialog),
                    cancel = getResources().getString(R.string.cancel_delete_big_goal_dialog);
            bigGoalID = getArguments().getInt(Constants.EXTRA_BIG_GOAL_ID);

            new initializeBigGoal().execute(helper);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle(title);
            alertDialog.setMessage(confirmation);

            alertDialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        IntentionDAOHelper.deleteIntention(bigGoal, bigGoalDAO);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getContext(), BigGoalsListActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });

            alertDialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            return alertDialog.create();



        }

        private class initializeBigGoal extends AsyncTask<DatabaseHelper, Integer, DatabaseHelper>{

            @Override
            protected void onPreExecute() {
                HelperFactory.setHelper(getContext());
                super.onPreExecute();
            }

            @Override
            protected DatabaseHelper doInBackground(DatabaseHelper... databaseHelpers) {
                helper = HelperFactory.getHelper();
                return helper;
            }

            @Override
            protected void onPostExecute(DatabaseHelper databaseHelper) {
                try {
                    bigGoalDAO = databaseHelper.getBigGoalDAO();
                    bigGoal = IntentionDAOHelper.getBigGoal(bigGoalDAO, bigGoalID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
