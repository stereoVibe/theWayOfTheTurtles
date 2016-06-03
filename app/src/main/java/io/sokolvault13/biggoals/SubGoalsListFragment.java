package io.sokolvault13.biggoals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.Model.Intention;
import io.sokolvault13.biggoals.Model.IntentionDAOHelper;
import io.sokolvault13.biggoals.Model.Job;
import io.sokolvault13.biggoals.db.DatabaseHelper;
import io.sokolvault13.biggoals.db.HelperFactory;

public class SubGoalsListFragment extends Fragment {
    public static final String ARG_BIG_GOAL_ID = "big_goal_id";
    private RecyclerView mSubGoalsRecyclerView;
    private DatabaseHelper dbHelper;
    private Dao<BigGoal, Integer> mBigGoalsDAO;
    private int bigGoalId;
    private BigGoal bigGoal;
    private Dao<Job, Integer> mJobsDAO;

    public static SubGoalsListFragment newInstance(int bigGoalId){
        Bundle args = new Bundle();
        args.putInt(ARG_BIG_GOAL_ID, bigGoalId);
        SubGoalsListFragment fragment = new SubGoalsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bigGoalId = getArguments().getInt(ARG_BIG_GOAL_ID);
        HelperFactory.setHelper(getActivity());
        super.onCreate(savedInstanceState);
        dbHelper = HelperFactory.getHelper();
        try {
            mBigGoalsDAO = dbHelper.getBigGoalDAO();
            mJobsDAO = dbHelper.getJobDAO();
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
//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.sub_goals_list_container);

        try {
            bigGoal = IntentionDAOHelper.getBigGoal(mBigGoalsDAO, bigGoalId);
            updateUI();
        } catch (SQLException e){
            e.printStackTrace();
        }

//        collapsingToolbarLayout.setTitle(bigGoal.getTitle());
        Log.d("Big Goal", bigGoal.getTitle());
        return view;
    }

    private void updateUI() throws SQLException {
        List<Job> subGoalsList = IntentionDAOHelper.getAllSubIntentionsList(
                mJobsDAO,
                bigGoal, Intention.BIGGOAL_ID_FIELD);
    }
}
