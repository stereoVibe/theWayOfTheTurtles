package io.sokolvault13.turtlesway;

import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.InstrumentationInfo;
import android.test.AndroidTestCase;

import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.dao.Dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;

import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.model.BigGoal;
import io.sokolvault13.turtlesway.model.IntentionDAOHelper;
import io.sokolvault13.turtlesway.model.SubGoal;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    private DatabaseHelper mDatabaseHelper;
    private Dao<SubGoal, Integer> mSubGoals;
    private Dao<BigGoal, Integer> mBigGoals;

    @Mock
    Context mContext;

    @Before
    public void initializeDAO() throws SQLException {
        mDatabaseHelper = new DatabaseHelper(mContext);
        mBigGoals = mDatabaseHelper.getBigGoalDAO();
    }

    @Test
    public void getBigGoalNPE() throws SQLException {
        mBigGoals = null;
        try {
            IntentionDAOHelper.createBigGoalRecord(new BigGoal("test"), mBigGoals);
            fail();
        } catch (NullPointerException e) {
            assertTrue(mBigGoals == null);
        }
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}