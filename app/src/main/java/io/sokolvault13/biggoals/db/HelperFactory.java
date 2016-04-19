package io.sokolvault13.biggoals.db;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import io.sokolvault13.biggoals.Model.DatabaseHelper;

public class HelperFactory {
    private static DatabaseHelper sDatabaseHelper;

    public static DatabaseHelper getHelper(){
        return sDatabaseHelper;
    }

    public static void setHelper(Context context){
        sDatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        sDatabaseHelper = null;
    }
}
