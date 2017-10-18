package io.sokolvault13.turtlesway.presenters.bigGoalCreation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;

import io.sokolvault13.turtlesway.model.BigGoal;

import io.sokolvault13.turtlesway.presenters.bigGoalsList.BigGoalsListActivity;
import io.sokolvault13.turtlesway.presenters.GoalDialog;

import static io.sokolvault13.turtlesway.model.IntentionDAOHelper.createBigGoalRecord;

public class BigGoalCreationDialog extends GoalDialog {

    private EditText mBigGoalTitle;
    private EditText mBigGoalDescription;
    private Button mCreateBigGoalBtn;
    private Button mPickBigGoalEndDateBtn;
    private Date mBigGoalEndDate;
    private DatabaseHelper dbHelper;
    private Calendar mCalendar = Calendar.getInstance();
    private Date mDate;
    DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            mDate = new Date(mCalendar.getTimeInMillis());
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

            mPickBigGoalEndDateBtn.setText(String.format("%s", dateFormat.format(mDate)));
        }
    };
    private Dao<io.sokolvault13.turtlesway.model.BigGoal, Integer> bigGoalsDAO;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        HelperFactory.setHelper(getActivity());
        dbHelper = HelperFactory.getHelper();
        getBigGoalDao();
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_create_big_goal, container, false);
        mBigGoalTitle = (EditText) view.findViewById(R.id.textGetTitle);
        mBigGoalDescription = (EditText) view.findViewById(R.id.textGetDescription);
        mCreateBigGoalBtn = (Button) view.findViewById(R.id.createBigGoalBtn);
        mPickBigGoalEndDateBtn = (Button) view.findViewById(R.id.pickBigGoalEndDate);

        mDate = new Date(mCalendar.getTimeInMillis());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        mPickBigGoalEndDateBtn.setText(String.format("%s", dateFormat.format(mDate)));

//        TextInputLayout inputLayout = (TextInputLayout) view.findViewById(R.id.textGetTitleLayout);
//        inputLayout.setError("First name is required"); // show error
//        inputLayout.setError(null); // hide error

        mCreateBigGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBigGoal();
            }
        });

        mPickBigGoalEndDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), mOnDateSetListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        changeKeyboardVisibility();

        return view;
    }

    public void createBigGoal() {
        String title = String.valueOf(mBigGoalTitle.getText());
        String description = String.valueOf(mBigGoalDescription.getText());
        Date date = this.mDate;

        if (!title.isEmpty()) {
            try {
                createBigGoalRecord(new BigGoal(title, description, date), bigGoalsDAO);
//                int bigGoalId = bigGoal.getId();
//                Intent intent = SubGoalsListActivity.newIntent(getActivity(), bigGoalId);
                changeKeyboardVisibility();
                Intent intent = new Intent(getContext(), BigGoalsListActivity.class);
                startActivity(intent);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.no_title_exception), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void changeKeyboardVisibility() {
        InputMethodManager inputMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void getBigGoalDao() {
        try {
            bigGoalsDAO = dbHelper.getBigGoalDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
