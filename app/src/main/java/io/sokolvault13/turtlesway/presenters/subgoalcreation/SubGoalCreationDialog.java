package io.sokolvault13.turtlesway.presenters.subgoalcreation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
import io.sokolvault13.turtlesway.model.BigGoal;
import io.sokolvault13.turtlesway.model.Intention;
import io.sokolvault13.turtlesway.model.IntentionDAOHelper;
import io.sokolvault13.turtlesway.model.Job;
import io.sokolvault13.turtlesway.model.ObjectiveType;
import io.sokolvault13.turtlesway.model.Task;
import io.sokolvault13.turtlesway.presenters.GoalDialog;
import io.sokolvault13.turtlesway.utils.Constants;

public class SubGoalCreationDialog extends GoalDialog {
    NoticeDialogListener mNoticeDialogListener;
    private DatabaseHelper dbHelper;
    private EditText mSubGoalTitle,
            mSubGoalDescription,
            mRepeatsQuantity;
    private Button mCreateGoalBtn;
    private SwitchCompat mChooseGoalTypeBtn;
    private int mBigGoalID;
    private Dao mBigGoalsDAO,
            mJobsDAO,
            mTasksDAO;
    private boolean isGoalTypeSimple = true;

    public static SubGoalCreationDialog newInstance(int bigGoalId) {
        SubGoalCreationDialog dialog = new SubGoalCreationDialog();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_BIG_GOAL_ID, bigGoalId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mBigGoalID = getArguments().getInt(Constants.ARG_BIG_GOAL_ID);
        HelperFactory.setHelper(getActivity());
        super.onCreate(savedInstanceState);
        dbHelper = HelperFactory.getHelper();
        try {
            HashMap allDAO = dbHelper.getAllDAO();
            mBigGoalsDAO = (Dao) allDAO.get(Constants.BIG_GOALS_DAO);
            mJobsDAO = (Dao) allDAO.get(Constants.JOBS_DAO);
            mTasksDAO = (Dao) allDAO.get(Constants.TASKS_DAO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.dialog_create_sub_goal, container, false);
        mSubGoalTitle = (EditText) view.findViewById(R.id.textGetTitle);
        mSubGoalDescription = (EditText) view.findViewById(R.id.textGetDescription);

        mRepeatsQuantity = (EditText) view.findViewById(R.id.repeatsQuantity);
        mCreateGoalBtn = (Button) view.findViewById(R.id.add_sub_goal_btn);
        mChooseGoalTypeBtn = (SwitchCompat) view.findViewById(R.id.switch_create_task_job);

        mChooseGoalTypeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                TextView repeatsQtyTitle = (TextView) view.findViewById(R.id.repeatsQuantityTitle);

                if (isChecked) {
                    repeatsQtyTitle.setEnabled(true);
                    mRepeatsQuantity.setEnabled(true);
                    isGoalTypeSimple = false;
                } else {
                    repeatsQtyTitle.setEnabled(false);
                    mRepeatsQuantity.setEnabled(false);
                    isGoalTypeSimple = true;
                }
            }
        });

        mCreateGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (createSubGoal()) {
                        getDialog().dismiss();
                    }
                    mNoticeDialogListener.onDialogClick(this);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mNoticeDialogListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    protected boolean createSubGoal() throws SQLException {

        BigGoal bigGoal = IntentionDAOHelper.getBigGoal(mBigGoalsDAO, mBigGoalID);
        String title = String.valueOf(mSubGoalTitle.getText());
        String description = String.valueOf(mSubGoalDescription.getText());
        String repeatsQuantity = String.valueOf(mRepeatsQuantity.getText());
        int quantity;

        if (repeatsQuantity.isEmpty()) {
            quantity = 1;
        } else {
            quantity = Integer.parseInt(repeatsQuantity);
        }

        if (title.isEmpty()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_title_exception), Toast.LENGTH_SHORT).show();
            return false;
        } else {

            if (isGoalTypeSimple) {
                HashMap<String, Object> goalDetails = Intention.prepareSubGoal(title, description, null, 0);
                new Task().createSubGoal(ObjectiveType.SIMPLE, bigGoal, goalDetails, mTasksDAO);
            } else {
                HashMap<String, Object> goalDetails = Intention.prepareSubGoal(title, description, null, quantity);
                new Job().createSubGoal(ObjectiveType.CONTINUOUS, bigGoal, goalDetails, mJobsDAO);
            }
            return true;
        }
    }

    @Override
    protected void changeKeyboardVisibility() {
        InputMethodManager inputMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
