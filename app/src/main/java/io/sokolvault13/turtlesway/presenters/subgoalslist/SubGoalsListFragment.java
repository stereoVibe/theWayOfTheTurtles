package io.sokolvault13.turtlesway.presenters.subgoalslist;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
import io.sokolvault13.turtlesway.model.BigGoal;
import io.sokolvault13.turtlesway.model.Intention;
import io.sokolvault13.turtlesway.model.IntentionDAOHelper;
import io.sokolvault13.turtlesway.model.Job;
import io.sokolvault13.turtlesway.model.ObjectiveType;
import io.sokolvault13.turtlesway.model.SubGoal;
import io.sokolvault13.turtlesway.model.Task;
import io.sokolvault13.turtlesway.utils.Constants;

public class SubGoalsListFragment extends Fragment implements RecyclerViewClickListener, SubGoalDetailsDialog.NoticeDialogListener {

    private SubGoalsAdapter mSubGoalsAdapter;
    private RecyclerView mSubGoalsRecyclerView;
    private DatabaseHelper dbHelper;
    private int mBigGoalId;
    private BigGoal mBigGoal;
    private ArrayList<SubGoal> mSubGoalsList;
    private Dao mBigGoalsDAO;
    private Dao mJobsDAO;
    private Dao mTasksDAO;
    private Context mContext;
    private RecyclerViewClickListener mClickListener;

    public static SubGoalsListFragment newInstance(int bigGoalId){
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_BIG_GOAL_ID, bigGoalId);
        SubGoalsListFragment fragment = new SubGoalsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void recyclerViewListClicked(View v, int position, ObjectiveType goalType) {
        int goalID = mSubGoalsList.get(position).getId();
        showSubGoalDetailsDialog(goalID, goalType);
//        Toast.makeText(getContext(), "Это подцель c ID: " + mSubGoal.getId(), Toast.LENGTH_SHORT).show();
    }

    private void showSubGoalDetailsDialog(int goalID, ObjectiveType goalType) {
        FragmentManager fm = getFragmentManager();
        SubGoalDetailsDialog dialog = SubGoalDetailsDialog.newInstance(goalID, goalType);
//        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SubGoalsDialog);
        dialog.show(fm, "subGoalDetailsDialog");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mBigGoalId = getArguments().getInt(Constants.ARG_BIG_GOAL_ID);
        HelperFactory.setHelper(getActivity());
        super.onCreate(savedInstanceState);
        dbHelper = HelperFactory.getHelper();
        try {
            HashMap allDAO = dbHelper.getAllDAO();
            mBigGoalsDAO = (Dao) allDAO.get(Constants.BIG_GOALS_DAO);
            mJobsDAO = (Dao) allDAO.get(Constants.JOBS_DAO);
            mTasksDAO = (Dao) allDAO.get(Constants.TASKS_DAO);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_goals_list, container, false);
        mSubGoalsRecyclerView = (RecyclerView) view.findViewById(R.id.sub_goals_recycler_view);
        mSubGoalsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            mBigGoal = IntentionDAOHelper.getBigGoal(mBigGoalsDAO, mBigGoalId);
            updateUI();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            updateUI();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void updateUI() throws SQLException {
        List<SubGoal> jobsList = IntentionDAOHelper.getAllSubIntentionsList(mJobsDAO, mBigGoal, Intention.BIGGOAL_ID_FIELD);
        List<SubGoal> tasksList = IntentionDAOHelper.getAllSubIntentionsList(mTasksDAO, mBigGoal, Intention.BIGGOAL_ID_FIELD);

        mSubGoalsList = new ArrayList<>();
        Set<SubGoal> sortedSet = new TreeSet<>();
        sortedSet.addAll(jobsList);
        sortedSet.addAll(tasksList);

        for (SubGoal aSortedSet : sortedSet) {
            mSubGoalsList.add(aSortedSet);
        }

        if (mSubGoalsAdapter == null) {
            mSubGoalsAdapter = new SubGoalsAdapter(mSubGoalsList, mContext, this);
            mSubGoalsRecyclerView.setAdapter(mSubGoalsAdapter);
        } else {
            mSubGoalsAdapter.clearItems();
            mSubGoalsAdapter.setItems(mSubGoalsList, mContext, this);
            mSubGoalsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDialogClick(View.OnClickListener dialogFragment) {
        try {
            updateUI();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class JobHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        PerformJobComplete mJobComplete;
        private TextView title,
                         goal,
                         completed;
        private CheckBox completedCheckBox;
        private ProgressBar progressBar;
        private View mView;

        public JobHolder(final View itemView) {
            super(itemView);
            this.mView = itemView;
            title = (TextView) itemView.findViewById(R.id.item_job_title);
            goal = (TextView) itemView.findViewById(R.id.item_goal);
            completed = (TextView) itemView.findViewById(R.id.item_complete);
            completedCheckBox = (CheckBox) itemView.findViewById(R.id.complete_job_checkBox);
            progressBar = (ProgressBar) itemView.findViewById(R.id.job_progress_bar);

            mJobComplete = new PerformJobComplete();
            mJobComplete.setView(itemView);

            completedCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mJobComplete.getStatus() == AsyncTask.Status.FINISHED){
                        mJobComplete = new PerformJobComplete();
                        mJobComplete.setView(itemView);
                        mJobComplete.setPosition(getLayoutPosition());
                        mJobComplete.execute();
                    } else if (mJobComplete.getStatus() != AsyncTask.Status.RUNNING){
                        mJobComplete.setPosition(getLayoutPosition());
                        mJobComplete.execute();
                    }
                }
            });

            itemView.setOnClickListener(this);
        }

        public TextView getTitle() {
            return title;
        }
        public void setTitle(TextView title) {
            this.title = title;
        }

        public TextView getGoal() {
            return goal;
        }
        public void setGoal(TextView goal) {
            this.goal = goal;
        }

        public TextView getCompleted() {
            return completed;
        }

        public View getView() {
            return mView;
        }

        public CheckBox getCompletedCheckBox() {
            return completedCheckBox;
        }

        public PerformJobComplete getJobComplete() {
            return mJobComplete;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        @Override
        public void onClick(View view) {
            mClickListener.recyclerViewListClicked(view, this.getLayoutPosition(), ObjectiveType.CONTINUOUS);
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private CheckBox completedCheckBox;
        private View mView;
        private PerformTaskComplete mPerformTaskComplete;

        public TaskHolder(final View itemView) {
            super(itemView);
            this.mView = itemView;
            title = (TextView) itemView.findViewById(R.id.item_task_title);
            completedCheckBox = (CheckBox) itemView.findViewById(R.id.complete_task_checkBox);

            mPerformTaskComplete = new PerformTaskComplete();
            mPerformTaskComplete.setView(mView);

            completedCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPerformTaskComplete.getStatus() == AsyncTask.Status.FINISHED){
                        mPerformTaskComplete = new PerformTaskComplete();
                        mPerformTaskComplete.setView(itemView);
                        mPerformTaskComplete.setPosition(getLayoutPosition());
                        mPerformTaskComplete.execute();
                    } else if (mPerformTaskComplete.getStatus() != AsyncTask.Status.RUNNING){
                        mPerformTaskComplete.setPosition(getLayoutPosition());
                        mPerformTaskComplete.execute();
                    }
                }
            });

            itemView.setOnClickListener(this);
        }

        public TextView getTitle() {
            return title;
        }
        public void setTitle(TextView title) {
            this.title = title;
        }
        public View getView() {
            return mView;
        }
        public CheckBox getCompletedCheckBox() {
            return completedCheckBox;
        }

        public PerformTaskComplete getPerformTaskComplete() {
            return mPerformTaskComplete;
        }

        @Override
        public void onClick(View view) {
            mClickListener.recyclerViewListClicked(view, this.getLayoutPosition(), ObjectiveType.SIMPLE);
        }
    }

    private class SubGoalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public final static int JOB = 0,
                                TASK = 1;
        ArrayList<SubGoal> items = (ArrayList<SubGoal>) mSubGoalsList;

        public SubGoalsAdapter(ArrayList<SubGoal> subGoalsList, Context context, RecyclerViewClickListener itemListener) {
            this.items = subGoalsList;
            mContext = context;
            mClickListener = itemListener;
        }

        public void setItems(ArrayList<SubGoal> items, Context context, RecyclerViewClickListener itemListener){
            this.items = items;
            mContext = context;
            mClickListener = itemListener;
        }
        public void clearItems(){
            this.items.clear();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType){
                case JOB:
                    View jobView = inflater.inflate(R.layout.item_sub_goal_job, parent, false);
                    viewHolder = new JobHolder(jobView);
                    break;
                case TASK:
                    View taskView = inflater.inflate(R.layout.item_sub_goal_task, parent, false);
                    viewHolder = new TaskHolder(taskView);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case JOB:
                    JobHolder jobHolder = (JobHolder) holder;
                    configureJobViewHolder(jobHolder, position);
                    break;
                case TASK:
                    TaskHolder taskHolder = (TaskHolder) holder;
                    configureTaskViewHolder(taskHolder, position);
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (items.get(position) instanceof Job) {
                return JOB;
            } else if (items.get(position) instanceof Task) {
                return TASK;
            }
            return -1;
        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }

        private void configureJobViewHolder(JobHolder jobHolder, int position) {
            Job job = (Job) items.get(position);
            jobHolder.getTitle().setText(job.getTitle());
            jobHolder.getGoal().setText(String.valueOf(job.getGoalQuantity()));
            jobHolder.getCompleted().setText(String.valueOf(job.getCompletedQuantity()));
            jobHolder.getProgressBar().setProgress((int) job.getProgress());

            if (job.getCompleteStatus()) {
                jobHolder.getTitle().setPaintFlags(jobHolder.getTitle().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                jobHolder.getView().setBackgroundColor(getResources().getColor(R.color.complete_row));
                jobHolder.getCompletedCheckBox().setChecked(true);
                jobHolder.getCompletedCheckBox().setEnabled(false);
                jobHolder.getProgressBar().setProgress(100);
            }
        }

        private void configureTaskViewHolder(TaskHolder taskHolder, int position) {
            Task task = (Task) items.get(position);
            taskHolder.getTitle().setText(task.getTitle());

            if (task.getCompleteStatus()){
                taskHolder.getTitle().setPaintFlags(taskHolder.getTitle().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                taskHolder.getView().setBackgroundColor(getResources().getColor(R.color.complete_row));
                taskHolder.getCompletedCheckBox().setChecked(true);
                taskHolder.getCompletedCheckBox().setEnabled(false);
            } else {
                taskHolder.getTitle().setPaintFlags(taskHolder.getTitle().getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                taskHolder.getView().setBackgroundColor(0);
                taskHolder.getCompletedCheckBox().setChecked(false);
                taskHolder.getCompletedCheckBox().setEnabled(true);
            }
        }
    }

    private class PerformJobComplete extends AsyncTask<Void, Void, Void>{
        View mView;
        Job mJob;
        int completedQuantity;
        double mSingleTaskProgressAmount;

        @Override
        protected Void doInBackground(Void... voids) {
            completedQuantity = mJob.getCompletedQuantity();
            mSingleTaskProgressAmount = (100 / mJob.getGoalQuantity());

            if (completedQuantity < mJob.getGoalQuantity()) {
                try {
                    IntentionDAOHelper.updateJobCompletedQuantity(mJobsDAO, mJob, completedQuantity + 1);
                    IntentionDAOHelper.updateJobProgress(mJobsDAO, mJob, mSingleTaskProgressAmount);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (completedQuantity == mJob.getGoalQuantity() - 1) {
                try {
                    IntentionDAOHelper.setComplete(mJobsDAO, mJob);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            try {
                mBigGoal = IntentionDAOHelper.updateBigGoalProgress(mBigGoalId, mBigGoalsDAO, mJobsDAO, mTasksDAO);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            title.startAnimation(mFadeOutAnimation);
//            (TextView) mView.findViewById(R.id.item_complete).se;
            CheckBox checkBox = (CheckBox) mView.findViewById(R.id.complete_job_checkBox);
            ProgressBar progressBar = (ProgressBar) mView.findViewById(R.id.job_progress_bar);
            NumberProgressBar BigGoalProgressBar = (NumberProgressBar) getActivity().findViewById(R.id.big_goal_inner_progressBar);

            if (completedQuantity == mJob.getGoalQuantity() - 1) {
                Toast.makeText(getContext(), "Выполнено +1 задача к Вашей цели!", Toast.LENGTH_SHORT).show();

                TextView textViewComplete = (TextView) mView.findViewById(R.id.item_complete);
                TextView textView = (TextView) mView.findViewById(R.id.item_job_title);

                textViewComplete.setText(String.valueOf(mJob.getCompletedQuantity()));
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mView.setBackgroundColor(getResources().getColor(R.color.complete_row));
                progressBar.setProgress(100);

                BigGoalProgressBar.setProgress((int) mBigGoal.getProgress());

                checkBox.setChecked(true);
                checkBox.setEnabled(false);
            } else if (completedQuantity < mJob.getGoalQuantity()){

                BigGoalProgressBar.setProgress((int) mBigGoal.getProgress());

                Toast.makeText(getContext(), "Выполнено +1 задача к Вашей цели!", Toast.LENGTH_SHORT).show();
                progressBar.setProgress((int) mJob.getProgress());
                checkBox.setChecked(false);
                TextView textView = (TextView) mView.findViewById(R.id.item_complete);
                textView.setText(String.valueOf(mJob.getCompletedQuantity()));
            }
        }

        private void setView(View view) {
            mView = view;
        }

        private void setPosition(int layoutPosition) {
            int goalID = mSubGoalsList.get(layoutPosition).getId();

            try {
                mJob = (Job) IntentionDAOHelper.getSubGoal(mJobsDAO, goalID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private class PerformTaskComplete extends AsyncTask<Void, Void, Void> {
        View mView;
        Task mTask;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                IntentionDAOHelper.setComplete(mTasksDAO, mTask);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                mBigGoal = IntentionDAOHelper.updateBigGoalProgress(mBigGoalId, mBigGoalsDAO, mJobsDAO, mTasksDAO);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            CheckBox checkBox = (CheckBox) mView.findViewById(R.id.complete_task_checkBox);
            NumberProgressBar BigGoalProgressBar = (NumberProgressBar) getActivity().findViewById(R.id.big_goal_inner_progressBar);

                Toast.makeText(getContext(), "Выполнено +1 задача к Вашей цели!", Toast.LENGTH_SHORT).show();

                TextView textView = (TextView) mView.findViewById(R.id.item_task_title);
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mView.setBackgroundColor(getResources().getColor(R.color.complete_row));

                BigGoalProgressBar.setProgress((int) mBigGoal.getProgress());

                checkBox.setChecked(true);
                checkBox.setEnabled(false);
        }

        private void setView(View view) {
            mView = view;
        }

        private void setPosition(int layoutPosition) {
            int goalID = mSubGoalsList.get(layoutPosition).getId();
            try {
                mTask = (Task) IntentionDAOHelper.getSubGoal(mTasksDAO, goalID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
