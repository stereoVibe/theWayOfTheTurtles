package io.sokolvault13.biggoals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.Model.ObjectiveType;
import io.sokolvault13.biggoals.Model.SubGoal;
import io.sokolvault13.biggoals.Model.Task;
import io.sokolvault13.biggoals.db.HelperFactory;
import io.sokolvault13.biggoals.Model.DatabaseHelper;

import static io.sokolvault13.biggoals.IntentionDAOHelper.*;

public class BigGoalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_goals);

    }

    @Override
    protected void onStart() {
        super.onStart();

        HelperFactory.setHelper(getApplicationContext());
        DatabaseHelper dbHelper = HelperFactory.getHelper();

        try {
            Dao<BigGoal, Integer> bigGoalsDAO = dbHelper.getBigGoalDAO();
            Dao<SubGoal, Integer> subGoalDAO = dbHelper.getSubGoalDAO();
            Dao<Task, Integer> tasksDao = dbHelper.getTaskDAO();

            BigGoal bigGoal = createBigGoalRecord(new BigGoal("Первая Цель"), bigGoalsDAO);
            BigGoal bigGoal1 = createBigGoalRecord(new BigGoal("Вторая Цель"), bigGoalsDAO);
            BigGoal bigGoal2 = createBigGoalRecord(new BigGoal("Третья Цель", "Описание"), bigGoalsDAO);

            SubGoal subGoal = bigGoal.createSubGoal("Первая подцель", ObjectiveType.SIMPLE);
            SubGoal subGoal2 = bigGoal.createSubGoal("Вторая подцель", ObjectiveType.CONTINIUS);

            subGoalDAO.create(subGoal);
            subGoalDAO.create(subGoal2);

            ArrayList<BigGoal> arrayBigGoalsList = (ArrayList<BigGoal>) getIntentionList(bigGoalsDAO);
            ArrayList<SubGoal> arraySubGoalsList = (ArrayList<SubGoal>) getIntentionList(subGoalDAO);
            ArrayList<Task> arrayTasksList = (ArrayList<Task>) getIntentionList(tasksDao);

            Log.d("arrayList", String.valueOf(arrayBigGoalsList.size()));

            Log.i("dbHelper", " " + bigGoalsDAO.queryForId(bigGoal.getId()));
            Log.i("dbHelper", "Is SubGoals in memory is empty? :" + String.valueOf(bigGoal.getSubGoals() == null));
            Log.i("dbHelper", "Is SubGoals in DAO is empty? Size is :" + String.valueOf(bigGoal.getSubGoals().size()));
            bigGoal.getSubGoals().add(subGoal);
            // Получить объект БЦ по id
//            BigGoal bigGoal1 = bigGoalsDAO.queryForId(1);

            Log.i("dbHelper", "Is SubGoals empty? Size is :" + String.valueOf(bigGoal.getSubGoals().size()));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        HelperFactory.releaseHelper();
        super.onStop();
    }
}
