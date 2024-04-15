package com.example.myapplication.util;

import android.content.Context;
import android.content.Intent;

/**
 * Utility class providing helper functions to facilitate common operations
 * such as starting new activities.
 */
public abstract class Util {

    /**
     * Starts a new activity specified by the activityToStart parameter from the currentActivity context.
     *
     * @param currentActivity The current active Context from which to start the activity.
     * @param activityToStart The Class object of the activity to be started.
     */
    public static void startActivity(Context currentActivity, Class<?> activityToStart) {
        Intent intent = new Intent(currentActivity, activityToStart);
        currentActivity.startActivity(intent);
    }
}