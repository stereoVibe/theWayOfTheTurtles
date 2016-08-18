package io.sokolvault13.turtlesway.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;

import io.sokolvault13.turtlesway.model.BigGoal;
import io.sokolvault13.turtlesway.model.Intention;
import io.sokolvault13.turtlesway.model.Job;
import io.sokolvault13.turtlesway.model.SubGoal;
import io.sokolvault13.turtlesway.model.Task;
import io.sokolvault13.turtlesway.utils.Constants;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "big_goals.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<BigGoal, Integer> mBigGoalDAO = null;
    private Dao<Job, Integer> mJobDAO = null;
    private Dao<Task, Integer> mTasksDAO = null;
    private Dao<? extends SubGoal, Integer> mSubGoalDAO = null;

    private Class[] models = {
            BigGoal.class,
            Job.class,
            Task.class
    };

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, final ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");

            for (Class c : models) {
                TableUtils.createTable(connectionSource, c);
            }

//            Dao<BigGoal, Integer> dao = getBigGoalDAO();
//            BigGoal bigGoal = new BigGoal("First goal");
//            dao.create(bigGoal);
//            QueryBuilder<BigGoal, Integer> queryBuilder = dao.queryBuilder();
//            PreparedQuery<BigGoal> preparedQuery = queryBuilder.where().eq("id", bigGoal.getId()).prepare();
//            Log.i("dbHelper"," " + bigGoal.getId());
//            bigGoal = dao.queryForFirst(preparedQuery);
//            Log.i("dbHelper", " " + bigGoal.getTitle());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<BigGoal, Integer> getBigGoalDAO() throws SQLException {
        if (mBigGoalDAO == null){
            mBigGoalDAO = DaoManager.createDao(connectionSource, BigGoal.class);
        }
        return mBigGoalDAO;
    }

    public Dao<Job, Integer> getJobDAO() throws SQLException {
        if (mJobDAO == null){
            mJobDAO = DaoManager.createDao(connectionSource, Job.class);
        }
        return mJobDAO;
    }

    public Dao<Task, Integer> getTaskDAO() throws SQLException {
        if (mTasksDAO == null){
            mTasksDAO = DaoManager.createDao(connectionSource, Task.class);
        }
        return mTasksDAO;
    }

    public Dao<? extends SubGoal, Integer> getSubGoalDAO(SubGoal subGoal) throws SQLException {
        mSubGoalDAO = subGoal instanceof Task ? getTaskDAO() : getJobDAO();
        return mSubGoalDAO;
    }

    public HashMap<String, Dao<? extends Intention, Integer>> getAllDAO() throws SQLException {
        HashMap<String, Dao<? extends Intention, Integer>> daosMap = new HashMap<>();
        daosMap.put(Constants.BIG_GOALS_DAO, getBigGoalDAO());
        daosMap.put(Constants.JOBS_DAO, getJobDAO());
        daosMap.put(Constants.TASKS_DAO, getTaskDAO());
//        Map syncDaosMap = Collections.synchronizedMap(daosMap);
        return daosMap;
    }

    @Override
    public void close() {
        super.close();
        this.mBigGoalDAO = null;
        this.mJobDAO = null;
        this.mTasksDAO = null;
    }
}
