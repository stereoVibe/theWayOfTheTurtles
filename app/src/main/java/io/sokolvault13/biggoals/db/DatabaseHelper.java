package io.sokolvault13.biggoals.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import io.sokolvault13.biggoals.Model.BigGoal;
import io.sokolvault13.biggoals.Model.SubGoal;
import io.sokolvault13.biggoals.Model.Task;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "big_goals.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<BigGoal, Integer> mBigGoalDAO = null;
    private Dao<SubGoal, Integer> mSubGoalDAO = null;
    private Dao<Task, Integer> mTasksDAO = null;

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, BigGoal.class);
            TableUtils.createTable(connectionSource, SubGoal.class);
            TableUtils.createTable(connectionSource, Task.class);

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

    public Dao<SubGoal, Integer> getSubGoalDAO() throws SQLException {
        if (mSubGoalDAO == null){
            mSubGoalDAO = DaoManager.createDao(connectionSource, SubGoal.class);
        }
        return mSubGoalDAO;
    }

    public Dao<Task, Integer> getTaskDAO() throws SQLException {
        if (mTasksDAO == null){
            mTasksDAO = DaoManager.createDao(connectionSource, Task.class);
        }
        return mTasksDAO;
    }

    @Override
    public void close() {
        super.close();
        this.mBigGoalDAO = null;
        this.mSubGoalDAO = null;
        this.mTasksDAO = null;
    }
}
