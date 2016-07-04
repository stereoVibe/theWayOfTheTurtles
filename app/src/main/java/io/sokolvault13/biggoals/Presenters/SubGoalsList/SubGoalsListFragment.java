package io.sokolvault13.biggoals.Presenters.SubGoalsList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.Model.IntentionDAOHelper;
import io.sokolvault13.biggoals.Model.Job;
import io.sokolvault13.biggoals.Model.Task;
import io.sokolvault13.biggoals.R;
import io.sokolvault13.biggoals.db.DatabaseHelper;

public class SubGoalsListFragment extends Fragment {
    public static final String ARG_BIG_GOAL_ID = "big_goal_id";

    private RecyclerView mSubGoalsRecyclerView;
    private DatabaseHelper dbHelper;
    private int mBigGoalId;
    private BigGoal bigGoal;
    private ArrayList subGoalsList;
    private Dao mBigGoalsDAO;
    private Dao mJobsDAO;
    private Dao mTasksDAO;

    public static SubGoalsListFragment newInstance(int bigGoalId){
        Bundle args = new Bundle();
        args.putInt(ARG_BIG_GOAL_ID, bigGoalId);
        SubGoalsListFragment fragment = new SubGoalsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mBigGoalId = getArguments().getInt(ARG_BIG_GOAL_ID);
        super.onCreate(savedInstanceState);
        try {
            HashMap allDAO = ((SubGoalsListActivity)getActivity()).getAllDAO();
            mBigGoalsDAO = (Dao) allDAO.get("BigGoalsDAO");
            mJobsDAO = (Dao) allDAO.get("JobsDAO");
            mTasksDAO = (Dao) allDAO.get("TasksDAO");
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
            bigGoal = IntentionDAOHelper.getBigGoal(mBigGoalsDAO, mBigGoalId);
            updateUI();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return view;
    }

    private void updateUI() throws SQLException {
        List jobsList = IntentionDAOHelper.getAllSubIntentionsList(mJobsDAO, bigGoal, Job.BIGGOAL_ID_FIELD);
        List tasksList = IntentionDAOHelper.getAllSubIntentionsList(mTasksDAO, bigGoal, Task.BIGGOAL_ID_FIELD);
        subGoalsList = new ArrayList();
        subGoalsList.addAll(jobsList);
        subGoalsList.addAll(tasksList);
        mSubGoalsRecyclerView.setAdapter(new SubGoalsAdapter(subGoalsList));
    }

    private class JobHolder extends RecyclerView.ViewHolder {

        private TextView title,
                         goal,
                         completed;

        public JobHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_job_title);
            goal = (TextView) itemView.findViewById(R.id.item_goal);
            completed = (TextView) itemView.findViewById(R.id.item_complete);
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
        public void setCompleted(TextView completed) {
            this.completed = completed;
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public TaskHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_task_title);
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }
    }

    private class SubGoalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public final static int JOB = 0,
                                TASK = 1;

        ArrayList items = subGoalsList;

        public SubGoalsAdapter(ArrayList subGoalsList) {
            this.items = subGoalsList;
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
        }

        private void configureTaskViewHolder(TaskHolder taskHolder, int position) {
            Task task = (Task) items.get(position);
            taskHolder.getTitle().setText(task.getTitle());
        }
    }
}
