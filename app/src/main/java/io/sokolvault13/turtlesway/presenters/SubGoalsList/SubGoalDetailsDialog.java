package io.sokolvault13.turtlesway.presenters.SubGoalsList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
import io.sokolvault13.turtlesway.model.Goal;
import io.sokolvault13.turtlesway.model.IntentionDAOHelper;
import io.sokolvault13.turtlesway.model.Job;
import io.sokolvault13.turtlesway.model.ObjectiveType;
import io.sokolvault13.turtlesway.model.Task;

public class SubGoalDetailsDialog extends DialogFragment {
    int mSubGoalID;
    Dao<? extends Goal, Integer> mSubGoalsDAO;
    DatabaseHelper dbHelper;
    Goal goal = null;
    String title;
    String description;
    int goalsQuantity;
    int goalsComplete;

    public SubGoalDetailsDialog() {
    }

    public static SubGoalDetailsDialog newInstance(int goalID, ObjectiveType goalType) {
        SubGoalDetailsDialog detailsDialog = new SubGoalDetailsDialog();
        Bundle args = new Bundle();
        args.putInt("subGoalID", goalID);
        args.putString("ObjectiveType", String.valueOf(goalType));
        detailsDialog.setArguments(args);
        return detailsDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.sub_goal_details, (ViewGroup) getActivity().findViewById(R.id.subGoal_details_layout));

        TextView subGoalDescription = (TextView) layout.findViewById(R.id.subGoal_description_text);

        mSubGoalID = getArguments().getInt("subGoalID");
        ObjectiveType goalType = ObjectiveType.valueOf(getArguments().getString("ObjectiveType"));

        try {
            mSubGoalsDAO = dbHelper.getSubGoalDAO(createGoalType(goalType));
            goal = IntentionDAOHelper.getSubGoal(mSubGoalsDAO, mSubGoalID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        title = goal instanceof Job ? ((Job) goal).getTitle() : ((Task) goal).getTitle();
        description = goal instanceof Job ? ((Job) goal).getDescription() : ((Task) goal).getDescription();
        Log.d("SubGoal Display", title);
        if (description != null) Log.d("SubGoal Display", description);

//        if (goal instanceof Job){
//            title = ((Job) goal).getTitle();
//            Log.d("SubGoal Display", title);
//            if (((Job) goal).getDescription() != null)
//                description = ((Job) goal).getDescription();
//                Log.d("SubGoal Display", description);
//            goalsQuantity = ((Job) goal).getGoalQuantity();
//            goalsComplete = ((Job) goal).getCompleteStatus();
//
//        } else if (goal instanceof Task){
//            title = ((Task) goal).getTitle();
//            if (((Task) goal).getDescription() != null)
//                description = ((Task) goal).getDescription();
//            else description = getResources().getString(R.string.no_description_text);
//        }

        builder.setView(layout)
                .setTitle(title)
                .setPositiveButton("Править", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Будет изменение полей", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        subGoalDescription.setText(description);

        return builder.create();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = getView();
//        assert view != null;
//        TextView subGoalDescription = (TextView) view.findViewById(R.id.subGoal_description_text);
//        subGoalDescription.setText(description);
//
//        return view;
////        return super.onCreateView(inflater, container, savedInstanceState);
//    }

    @Override
    public void onAttach(Context context) {
        HelperFactory.setHelper(getContext());
        super.onAttach(context);
        dbHelper = HelperFactory.getHelper();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private Goal createGoalType(ObjectiveType goalType) {
        switch (goalType) {
            case SIMPLE:
                return new Task();
            case CONTINUOUS:
                return new Job();
        }
        return null;
    }
}
