package io.sokolvault13.turtlesway.presenters.biggoalcreation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

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
import io.sokolvault13.turtlesway.presenters.biggoalslist.BigGoalsListActivity;

import static io.sokolvault13.turtlesway.model.IntentionDAOHelper.createBigGoalRecord;
import static io.sokolvault13.turtlesway.model.IntentionDAOHelper.getIntentionList;

public class BigGoalCreationFragment extends Fragment {
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
    private Dao<BigGoal, Integer> bigGoalsDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        HelperFactory.setHelper(getActivity());
        dbHelper = HelperFactory.getHelper();
        getBigGoalDao();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_big_goal, container, false);
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

        return view;
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onStart() {
        if (dbHelper == null){
            HelperFactory.setHelper(getActivity());
            dbHelper = HelperFactory.getHelper();
        }
        super.onStart();
    }

    @Override
    public void onPause() {
        HelperFactory.releaseHelper();
        super.onPause();
    }

    private void getBigGoalDao() {
        try {
            bigGoalsDAO = dbHelper.getBigGoalDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createBigGoal() {
        String title = mBigGoalTitle.getText().toString();
        String description = mBigGoalDescription.getText().toString();
        Date date = this.mDate;

        try {
            if (getIntentionList(bigGoalsDAO).size() == 0) {
                createBigGoalRecord(new BigGoal(title, description, date), bigGoalsDAO);
//                int bigGoalId = bigGoal.getId();
//                Intent intent = SubGoalsListActivity.newIntent(getActivity(), bigGoalId);
                Intent intent = new Intent(getContext(), BigGoalsListActivity.class);
                startActivity(intent);
            } else {
                createBigGoalRecord(new BigGoal(title, description, date), bigGoalsDAO);
                getActivity().onBackPressed();
                getActivity().finish();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
