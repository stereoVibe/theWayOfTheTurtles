package io.sokolvault13.biggoals;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.db.DatabaseHelper;
import io.sokolvault13.biggoals.db.HelperFactory;

import static io.sokolvault13.biggoals.IntentionDAOHelper.createBigGoalRecord;

public class BigGoalsListFragment extends Fragment {

    private RecyclerView mBigGoalsRecyclerView;
    private BigGoalsAdapter mBigGoalsAdapter;
    private DatabaseHelper dbHelper;
    private Dao<BigGoal, Integer> bigGoalsDAO;

//    public BigGoalsListFragment() {
//        // Required empty public constructor
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        HelperFactory.setHelper(getContext());
        super.onCreate(savedInstanceState);
        dbHelper = HelperFactory.getHelper();
        try {
            bigGoalsDAO = dbHelper.getBigGoalDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        try {
//            BigGoal bigGoal = createBigGoalRecord(new BigGoal("Первая Цель", "Описание один для первой цели. Оно не должно быть очень длинным"),
//                    bigGoalsDAO);
//            BigGoal bigGoal1 = createBigGoalRecord(new BigGoal("Вторая Цель", "Описание два для второй цели. По идее оно короче."),
//                    bigGoalsDAO);
//            BigGoal bigGoal2 = createBigGoalRecord(new BigGoal("Третья Цель", "Самое короткое описание из всех. Описание три."),
//                    bigGoalsDAO);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_big_goals_list, container, false);
        mBigGoalsRecyclerView = (RecyclerView) view.findViewById(R.id.big_goals_recycler_view);
        mBigGoalsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            updateUI();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return view;
    }

//        TODO: Make this method AsyncTask because DAO do not have time to initialize, in some cases (randomly)
    private void updateUI() throws SQLException {
//        TODO: Instance of this DAO can be removed after method will be AsyncTask
//        Dao<BigGoal, Integer> bigGoalsDAO = dbHelper.getBigGoalDAO();
        List<BigGoal> bigGoals = IntentionDAOHelper.getIntentionList(bigGoalsDAO);
        mBigGoalsAdapter = new BigGoalsAdapter(bigGoals);
        mBigGoalsRecyclerView.setAdapter(mBigGoalsAdapter);
    }

    private class BigGoalsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private BigGoal mBigGoal;
        public TextView mBigGoalTitle;
        public TextView mBigGoalDescription;
        public NumberProgressBar mBigGoalProgress;

        public BigGoalsHolder(View itemView) {
            super(itemView);
            mBigGoalTitle = (TextView) itemView.findViewById(R.id.big_goal_title);
            mBigGoalDescription = (TextView) itemView.findViewById(R.id.big_goal_description);
            mBigGoalProgress = (NumberProgressBar) itemView.findViewById(R.id.progressBar);

        }

        public void bindBigGoal(BigGoal bigGoal){
            mBigGoal = bigGoal;
            mBigGoalTitle.setText(mBigGoal.getTitle());
//            This is a patch for description field happens were RecyclerView is recycling cards
            if (mBigGoal.getDescription() != null){
                mBigGoalDescription.setText(mBigGoal.getDescription());
            } else if(mBigGoal.getDescription() == null){
                Log.d("Big Goal Description", mBigGoalDescription.getText().toString());
                mBigGoalDescription.setText("");
            }
            mBigGoalProgress.setProgress(mBigGoal.getProgress());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mBigGoal.getTitle() + " clicked!", Toast.LENGTH_LONG).show();
        }
    }

    private class BigGoalsAdapter extends RecyclerView.Adapter<BigGoalsHolder>{
        private List<BigGoal> mBigGoals;

        public BigGoalsAdapter(List<BigGoal> bigGoals) {
            mBigGoals = bigGoals;
        }

        @Override
        public BigGoalsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.big_goal_card, parent, false);
            return new BigGoalsHolder(view);
        }

        @Override
        public void onBindViewHolder(BigGoalsHolder holder, int position) {
            BigGoal bigGoal = mBigGoals.get(position);
            holder.bindBigGoal(bigGoal);
//            holder.mBigGoalTitle.setText(bigGoal.getTitle());
        }

        @Override
        public int getItemCount() {
            return mBigGoals.size();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        HelperFactory.releaseHelper();
    }
}
