package io.sokolvault13.biggoals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.Model.Intention;
import io.sokolvault13.biggoals.Model.IntentionDAOHelper;
import io.sokolvault13.biggoals.Model.Job;
import io.sokolvault13.biggoals.Model.Task;
import io.sokolvault13.biggoals.db.DatabaseHelper;
import io.sokolvault13.biggoals.db.HelperFactory;

public class SubGoalsListFragment extends Fragment {
    public static final String ARG_BIG_GOAL_ID = "big_goal_id";

    private RecyclerView mSubGoalsRecyclerView;
    private DatabaseHelper dbHelper;
    private int mBigGoalId;
    private BigGoal bigGoal;
    private ArrayList subGoalsList;
    private Dao<BigGoal, Integer> mBigGoalsDAO;
    private Dao<Job, Integer> mJobsDAO;
    private Dao<Task, Integer> mTasksDAO;

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
        List<? extends Intention> jobsList = IntentionDAOHelper.getAllSubIntentionsList(mJobsDAO, bigGoal, Job.BIGGOAL_ID_FIELD);
        List<? extends Intention> tasksList = IntentionDAOHelper.getAllSubIntentionsList(mTasksDAO, bigGoal, Task.BIGGOAL_ID_FIELD);
        subGoalsList = new ArrayList();
        subGoalsList.addAll(jobsList);
        subGoalsList.addAll(tasksList);
        mSubGoalsRecyclerView.setAdapter(new SubGoalsAdapter(subGoalsList));
    }

    private class JobHolder extends RecyclerView.ViewHolder {

        public JobHolder(View itemView) {
            super(itemView);
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder {

        public TaskHolder(View itemView) {
            super(itemView);
        }
    }

    private class SubGoalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public final static int JOB = 0,
                                TASK = 1;

        ArrayList<? extends Intention> items = subGoalsList;

        public SubGoalsAdapter(ArrayList subGoalsList) {
            this.items = subGoalsList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
    }
}
