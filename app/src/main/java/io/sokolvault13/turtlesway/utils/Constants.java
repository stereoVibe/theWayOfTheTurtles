package io.sokolvault13.turtlesway.utils;

import io.sokolvault13.turtlesway.BuildConfig;

/**
 * Created by Vault on 11/07/16.
 */
public final class Constants {

    public static final String FIREBASE_URL = BuildConfig.FIREBASE_ROOT_URL;

    public static final String EXTRA_BIG_GOAL_ID = "io.sokolvault.turtlesway.big_goal_id";

    public static final String ARG_BIG_GOAL_ID = "big_goal_id";

    /* Strings for getAllDAO() method */
    public static final String BIG_GOALS_DAO = "BigGoalsDAO";
    public static final String JOBS_DAO = "JobsDAO";
    public static final String TASKS_DAO = "TasksDAO";
}
