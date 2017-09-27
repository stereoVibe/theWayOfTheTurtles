package io.sokolvault13.turtlesway;

import android.content.Context;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.Random;

import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.model.Job;
import io.sokolvault13.turtlesway.model.SubGoal;
import io.sokolvault13.turtlesway.model.Task;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {
    private DatabaseHelper mDatabaseHelper;
    @Mock
    Context mContext;

    @Before
    public void initializeDAO(){
        mDatabaseHelper = new DatabaseHelper(mContext);
    }

    @Test
    public void getBigGoalDAOTest() throws SQLException {
        assertTrue(mDatabaseHelper.getBigGoalDAO() != null);
    }
    @Test
    public void getJobDAOTest() throws SQLException {
        assertTrue(mDatabaseHelper.getJobDAO() != null);
    }
    @Test
    public void getTaskDAOTest() throws SQLException {
        assertTrue(mDatabaseHelper.getTaskDAO() != null);
    }

    @Test
    public void getSubGoalDAOTest() throws SQLException {
        SubGoal subGoal = getRandomSubGoal(0, 1000);
        for (int i = 0; i <= 1000; i++) {
            assertTrue(mDatabaseHelper.getSubGoalDAO(subGoal) != null);
        }
    }

    private SubGoal getRandomSubGoal(int min, int max) {
        int randomInt;
        SubGoal subGoal = null;
        Random r = new Random();
        randomInt = r.nextInt((max - min) + 1) + min;

        if (randomInt % 2 == 0) {
            subGoal = new Job();
        } else if (randomInt % 2 != 0) {
            subGoal = new Task();
        }
        return subGoal;
    }
}
