package io.sokolvault13.biggoals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

import java.sql.Date;
import java.sql.SQLException;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.db.DatabaseHelper;
import io.sokolvault13.biggoals.db.HelperFactory;

import static io.sokolvault13.biggoals.IntentionDAOHelper.createBigGoalRecord;

public class CreateBigGoalActivity extends AppCompatActivity {
    private EditText mBigGoalTitle;
    private EditText mBigGoalDescription;
    private EditText mBigGoalEndDate;
    private Button mCreateBigGoalBtn;
    private DatabaseHelper dbHelper;
    private Dao<BigGoal, Integer> bigGoalsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HelperFactory.setHelper(getApplicationContext());
        dbHelper = HelperFactory.getHelper();
        getBigGoalDao();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_big_goal);
        mBigGoalTitle = (EditText) findViewById(R.id.textGetTitle);
        mBigGoalDescription = (EditText) findViewById(R.id.textGetDescriprtion);
        mBigGoalEndDate = (EditText) findViewById(R.id.textGetDate);
        mCreateBigGoalBtn = (Button) findViewById(R.id.createBigGoalBtn);

        mCreateBigGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mBigGoalTitle.getText().toString();
                String description = mBigGoalDescription.getText().toString();
//                Date date = mBigGoalEndDate.getText();

                try {
                    createBigGoalRecord(new BigGoal(title, description), bigGoalsDAO);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(CreateBigGoalActivity.this, BigGoalsListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private Dao<BigGoal, Integer> getBigGoalDao() {
        try {
            bigGoalsDAO = dbHelper.getBigGoalDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bigGoalsDAO;
    }
}
