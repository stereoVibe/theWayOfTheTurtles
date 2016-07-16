package io.sokolvault13.turtlesway.presenters.SubGoalsList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
import io.sokolvault13.turtlesway.model.BigGoal;
import io.sokolvault13.turtlesway.model.Goal;
import io.sokolvault13.turtlesway.model.IntentionDAOHelper;
import io.sokolvault13.turtlesway.model.Job;
import io.sokolvault13.turtlesway.model.Task;
import io.sokolvault13.turtlesway.utils.Constants;

public class SubGoalsListFragment extends Fragment {

    SubGoalsAdapter mSubGoalsAdapter;
    private RecyclerView mSubGoalsRecyclerView;
    private DatabaseHelper dbHelper;
    private int mBigGoalId;
    private BigGoal bigGoal;
    private ArrayList<Goal> subGoalsList;
    private Dao mBigGoalsDAO;
    private Dao mJobsDAO;
    private Dao mTasksDAO;

    public static SubGoalsListFragment newInstance(int bigGoalId){
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_BIG_GOAL_ID, bigGoalId);
        SubGoalsListFragment fragment = new SubGoalsListFragment();
        fragment.setArguments(args);
        return fragment;
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
            bigGoal = IntentionDAOHelper.getBigGoal(mBigGoalsDAO, mBigGoalId);
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

    private void updateUI() throws SQLException {
        List<Goal> jobsList = IntentionDAOHelper.getAllSubIntentionsList(mJobsDAO, bigGoal, Job.BIGGOAL_ID_FIELD);
        List<Goal> tasksList = IntentionDAOHelper.getAllSubIntentionsList(mTasksDAO, bigGoal, Task.BIGGOAL_ID_FIELD);

        subGoalsList = new ArrayList<>();
        Set<Goal> sortedSet = new TreeSet<>();
        sortedSet.addAll(jobsList);
        sortedSet.addAll(tasksList);
        Log.d("sortedSet", String.valueOf(sortedSet.size()));
        Iterator<Goal> goalIterator = sortedSet.iterator();
        while (goalIterator.hasNext()){
            subGoalsList.add(goalIterator.next());
//            Log.d("goalIterator", goalIterator.next().getDateAsSortingParameter().toString());
        }
//        subGoalsList.addAll(jobsList);
//        subGoalsList.addAll(tasksList);

        if (mSubGoalsAdapter == null) {
            mSubGoalsAdapter = new SubGoalsAdapter(subGoalsList);
            mSubGoalsRecyclerView.setAdapter(mSubGoalsAdapter);
        } else {
            mSubGoalsAdapter.clearItems();
            mSubGoalsAdapter.setItems(subGoalsList);
            mSubGoalsAdapter.notifyDataSetChanged();
        }
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

        ArrayList<Goal> items = (ArrayList<Goal>) subGoalsList;

        public SubGoalsAdapter(ArrayList<Goal> subGoalsList) {
            this.items = subGoalsList;
        }

        public void setItems(ArrayList<Goal> items){
            this.items = items;
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
        }

        private void configureTaskViewHolder(TaskHolder taskHolder, int position) {
            Task task = (Task) items.get(position);
            taskHolder.getTitle().setText(task.getTitle());
        }

        private class initilizeDAO extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }
        }
    }
}
