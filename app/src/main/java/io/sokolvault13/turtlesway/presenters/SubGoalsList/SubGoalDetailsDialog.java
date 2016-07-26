package io.sokolvault13.turtlesway.presenters.SubGoalsList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.j256.ormlite.dao.Dao;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
import io.sokolvault13.turtlesway.model.Goal;
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
//        detailsDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SubGoalsDialog);
        return detailsDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sub_goal_details, container, false);
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
//        View layout = layoutInflater.inflate(R.layout.sub_goal_details, (ViewGroup) getActivity().findViewById(R.id.subGoal_details_layout));
//
//        TextView subGoalDescription = (TextView) layout.findViewById(R.id.subGoal_description_text);
//
//        mSubGoalID = getArguments().getInt("subGoalID");
//        ObjectiveType goalType = ObjectiveType.valueOf(getArguments().getString("ObjectiveType"));
//
//        try {
//            mSubGoalsDAO = dbHelper.getSubGoalDAO(createGoalType(goalType));
//            goal = IntentionDAOHelper.getSubGoal(mSubGoalsDAO, mSubGoalID);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        title = goal instanceof Job ? ((Job) goal).getTitle() : ((Task) goal).getTitle();
//        description = goal instanceof Job ? ((Job) goal).getDescription() : ((Task) goal).getDescription();
//        Log.d("SubGoal Display", title);
//        if (description != null) Log.d("SubGoal Display", description);
//
//        builder.setView(layout)
////                .setTitle(title)
//                .setPositiveButton("Править", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getActivity(), "Будет изменение полей", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//
//        subGoalDescription.setText(description);


//        AlertDialog dialog = builder.create();
//        Dialog dialog = new Dialog(this.getContext(), R.style.SubGoalsDialog);
        Dialog dialog = new Dialog(this.getContext(), R.style.SubGoalsDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        View v = dialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        wmlp.gravity = Gravity.TOP;
        wmlp.height = dialog.getWindow().getAttributes().height = (int) (getDeviceMetrics(getContext()).heightPixels * 0.4);
        wmlp.width = dialog.getWindow().getAttributes().width = (int) (getDeviceMetrics(getContext()).widthPixels * 0.95);
        dialog.getWindow().setAttributes(wmlp);

        return dialog;
    }

    private DisplayMetrics getDeviceMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

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
