package io.sokolvault13.biggoals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.db.DatabaseHelper;
import io.sokolvault13.biggoals.db.HelperFactory;

import static io.sokolvault13.biggoals.Model.IntentionDAOHelper.createBigGoalRecord;

public class BigGoalCreationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BigGoalCreationFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int layoutId = R.layout.activity_create_big_goal;
        int toolbarResourceId = R.id.big_goals_list_toolbar;
        int toolbarLayoutId = R.layout.action_bar_create_big_goal;
        int toolbarMenuId = R.menu.create_big_goal_menu_toolbar;
        assignResources(layoutId, toolbarLayoutId, toolbarResourceId, toolbarMenuId, false);

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.continue_creation:
                Toast.makeText(BigGoalCreationActivity.this, "Button Pressed!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
//        HelperFactory.releaseHelper();
    }




}
