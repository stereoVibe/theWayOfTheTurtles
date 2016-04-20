package io.sokolvault13.biggoals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
    }

    @Override
    protected void onStop() {
        HelperFactory.releaseHelper();
        super.onStop();
    }

}
