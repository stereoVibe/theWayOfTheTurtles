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
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
import io.sokolvault13.turtlesway.model.Goal;
import io.sokolvault13.turtlesway.model.Intention;
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
    Button doneButton;
    int goalsQuantity;
    int goalsComplete;
    NoticeDialogListener mNoticeDialogListener;

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
        mSubGoalID = getArguments().getInt("subGoalID");
        ObjectiveType goalType = ObjectiveType.valueOf(getArguments().getString("ObjectiveType"));

        View layout = null;
        switch (goalType) {
            case SIMPLE:
                layout = inflater.inflate(R.layout.dialog_task_details, container, false);
                doneButton = (Button) layout.findViewById(R.id.dialog_task_done_btn);
                break;
            case CONTINUOUS:
                layout = inflater.inflate(R.layout.dialog_job_details, container, false);
                doneButton = (Button) layout.findViewById(R.id.dialog_job_done_btn);
                break;
        }


        final EditText subGoalTitle = (EditText) layout.findViewById(R.id.textGetTitle);
        final EditText subGoalDescription = (EditText) layout.findViewById(R.id.subGoal_description_text);
        EditText repeatsQuantity = (EditText) layout.findViewById(R.id.repeatsQuantity);

        try {
            mSubGoalsDAO = dbHelper.getSubGoalDAO(createGoalType(goalType));
            goal = IntentionDAOHelper.getSubGoal(mSubGoalsDAO, mSubGoalID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        title = goal instanceof Job ? ((Job) goal).getTitle() : ((Task) goal).getTitle();
        description = goal instanceof Job ? ((Job) goal).getDescription() : ((Task) goal).getDescription();

        subGoalTitle.setText(title);
        if (description != null) {
            subGoalDescription.setText(description);
        }
        if (goal instanceof Job) {
            goalsQuantity = ((Job) goal).getGoalQuantity();
            repeatsQuantity.setText(String.valueOf(goalsQuantity));
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> intentionFields = new HashMap<>();
                intentionFields.put(Intention.FIELD_INTENTION_TITLE, String.valueOf(subGoalTitle.getText()));
                intentionFields.put(Intention.FIELD_INTENTION_DESCRIPTION, String.valueOf(subGoalDescription.getText()));
                try {
                    IntentionDAOHelper.updateIntention(mSubGoalsDAO, (Intention) goal, intentionFields);
                    getDialog().dismiss();
                    mNoticeDialogListener.onDialogClick(this);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        return layout;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_job_details, (ViewGroup) getActivity().findViewById(R.id.subGoal_details_layout));
//

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

//        AlertDialog dialog = builder.create();
//        Dialog dialog = new Dialog(this.getContext(), R.style.SubGoalsDialog);
        Dialog dialog = new Dialog(this.getContext(), R.style.SubGoalsDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        View v = dialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        wmlp.gravity = Gravity.TOP;
        wmlp.height = dialog.getWindow().getAttributes().height = (int) (getDeviceMetrics(getContext()).heightPixels * 0.34);
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

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mNoticeDialogListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        FragmentManager fragmentManager = getFragmentManager();
//        SubGoalsListFragment fragment = (SubGoalsListFragment) fragmentManager.findFragmentById(R.id.sub_goals_recycler_view);
//        fragment.refreshRecyclerView();
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

    public interface NoticeDialogListener {
        void onDialogClick(View.OnClickListener dialogFragment);
    }
}
