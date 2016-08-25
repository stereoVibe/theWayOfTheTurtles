package io.sokolvault13.turtlesway.presenters.SubGoalsList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
import io.sokolvault13.turtlesway.model.Intention;
import io.sokolvault13.turtlesway.model.IntentionDAOHelper;
import io.sokolvault13.turtlesway.model.Job;
import io.sokolvault13.turtlesway.model.ObjectiveType;
import io.sokolvault13.turtlesway.model.SubGoal;
import io.sokolvault13.turtlesway.model.Task;
import io.sokolvault13.turtlesway.utils.Constants;

public class SubGoalDetailsDialog extends DialogFragment {
    int mSubGoalID;
    Dao<? extends SubGoal, Integer> mSubGoalsDAO;
    DatabaseHelper dbHelper;
    SubGoal mSubGoal = null;
    String title, description;
    TextView taskCompleteStatusText;
    Button doneButton, cancelLastJobButton;
    Switch switchCompleteStatus = null;
    NoticeDialogListener mNoticeDialogListener;
    //    int goalsQuantity = 0;
//    int goalsComplete;
    private Dao mBigGoalsDAO, mJobsDAO, mTasksDAO;

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
                switchCompleteStatus = (Switch) layout.findViewById(R.id.complete_status_switch);
                taskCompleteStatusText = (TextView) layout.findViewById(R.id.complete_status_text);
                break;
            case CONTINUOUS:
                layout = inflater.inflate(R.layout.dialog_job_details, container, false);
                doneButton = (Button) layout.findViewById(R.id.dialog_job_done_btn);
                cancelLastJobButton = (Button) layout.findViewById(R.id.dialog_cancel_last_job_btn);
                break;
        }


        final EditText subGoalTitle = (EditText) layout.findViewById(R.id.textGetTitle);
        final EditText subGoalDescription = (EditText) layout.findViewById(R.id.subGoal_description_text);
//        final EditText repeatsQuantity = (EditText) layout.findViewById(R.id.repeatsQuantity);

        try {
            mSubGoalsDAO = dbHelper.getSubGoalDAO(createGoalType(goalType));
            mSubGoal = IntentionDAOHelper.getSubGoal(mSubGoalsDAO, mSubGoalID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        title = mSubGoal instanceof Job ? ((Job) mSubGoal).getTitle() : ((Task) mSubGoal).getTitle();
        description = mSubGoal instanceof Job ? ((Job) mSubGoal).getDescription() : ((Task) mSubGoal).getDescription();

        subGoalTitle.setText(title);
        if (description != null) {
            subGoalDescription.setText(description);
        }

        if (mSubGoal instanceof Job) {

            cancelLastJobButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int bigGoalID = mSubGoal.getBigGoal().getId();
                    int completedQuantity = ((Job) mSubGoal).getCompletedQuantity();
                    double mSingleTaskProgressAmount = (100 / ((Job) mSubGoal).getGoalQuantity());

                    if (completedQuantity - 1 >= 0) {
                        try {
                            IntentionDAOHelper.updateJobCompletedQuantity((Dao<Job, Integer>) mSubGoalsDAO, (Job) mSubGoal, (completedQuantity - 1));
                            IntentionDAOHelper.updateJobProgress((Dao<Job, Integer>) mSubGoalsDAO, (Job) mSubGoal, mSingleTaskProgressAmount * -1);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), "One job have been canceled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "There are no more jobs to cancel", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        if (mSubGoal instanceof Task) {
            boolean isTaskComplete = ((Task) mSubGoal).getCompleteStatus();
            switchCompleteStatus.setChecked(isTaskComplete);
            if (isTaskComplete) {
                taskCompleteStatusText.setText(getResources().getString(R.string.task_complete_status_done));
            } else {
                taskCompleteStatusText.setText(getResources().getString(R.string.task_complete_status_in_progress));
            }
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bigGoalID = mSubGoal.getBigGoal().getId();

                HashMap<String, Object> intentionFields = new HashMap<>();
                intentionFields.put(Intention.FIELD_INTENTION_TITLE, String.valueOf(subGoalTitle.getText()));
                intentionFields.put(Intention.FIELD_INTENTION_DESCRIPTION, String.valueOf(subGoalDescription.getText()));
//                if (goalsQuantity != 0) {
//                    intentionFields.put(Intention.FIELD_INTENTION_GOAL_QUANTITY, Integer.parseInt(String.valueOf(repeatsQuantity.getText())));
//                }
                if (switchCompleteStatus != null) {
                    intentionFields.put(SubGoal.COMPLETE_STATUS, switchCompleteStatus.isChecked());
                }
                try {
                    IntentionDAOHelper.updateIntention(mSubGoalsDAO, (Intention) mSubGoal, intentionFields);
                    IntentionDAOHelper.updateBigGoalProgress(bigGoalID, mBigGoalsDAO, mJobsDAO, mTasksDAO);
                    mNoticeDialogListener.onDialogClick(this);
                    getDialog().dismiss();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        if (switchCompleteStatus != null) {
            switchCompleteStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isSwitchChecked = switchCompleteStatus.isChecked();
                    if (isSwitchChecked) {
                        taskCompleteStatusText.setText(getResources().getString(R.string.task_complete_status_done));
                    } else {
                        taskCompleteStatusText.setText(getResources().getString(R.string.task_complete_status_in_progress));
                    }
                }
            });
        }

        changeKeyboardVisibility();

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
            HashMap allDAO = dbHelper.getAllDAO();
            mBigGoalsDAO = (Dao) allDAO.get(Constants.BIG_GOALS_DAO);
            mJobsDAO = (Dao) allDAO.get(Constants.JOBS_DAO);
            mTasksDAO = (Dao) allDAO.get(Constants.TASKS_DAO);
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        changeKeyboardVisibility();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        changeKeyboardVisibility();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        FragmentManager fragmentManager = getFragmentManager();
//        SubGoalsListFragment fragment = (SubGoalsListFragment) fragmentManager.findFragmentById(R.id.sub_goals_recycler_view);
//        fragment.refreshRecyclerView();
    }

    private SubGoal createGoalType(ObjectiveType goalType) {
        switch (goalType) {
            case SIMPLE:
                return new Task();
            case CONTINUOUS:
                return new Job();
        }
        return null;
    }

    private void changeKeyboardVisibility() {
        InputMethodManager inputMM = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public interface NoticeDialogListener {
        void onDialogClick(View.OnClickListener dialogFragment);
    }
}
