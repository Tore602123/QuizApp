package com.example.myapplication.util;

import android.content.Context;
import android.content.Intent;

import java.util.Map;

/**
 * Utility class providing helper functions to facilitate common operations
 * such as starting new activities.
 */
public class Util {

    /**
     * Starts an activity with no extra data in the intent.
     * @param currentActivity the activity the method is called from
     * @param newActivity the activity to be started
     */
    public static void startActivity(Context currentActivity, Class newActivity) {
        startActivity(currentActivity, new Intent(currentActivity, newActivity));
    }

    /**
     * Starts an activity with a single extra data in the intent.
     * @param currentActivity the activity the method is called from
     * @param newActivity the activity to be started
     * @param msgName the name of the extra data
     * @param msgValue - the value of the extra data
     */
    public static void startActivity(Context currentActivity, Class newActivity, String msgName, Object msgValue) {
        Intent intent = new Intent(currentActivity, newActivity);
        if (msgValue.getClass() == String.class) intent.putExtra(msgName, (String) msgValue);
        else if (msgValue.getClass() == Boolean.class) intent.putExtra(msgName, (Boolean) msgValue);
        startActivity(currentActivity, intent);
    }

    /**
     * Starts an activity with several extra data in the intent.
     * @param currentActivity the activity the method is called from
     * @param newActivity the activity to be started
     * @param extras extra data to be added to the intent
     */
    public static void startActivity(Context currentActivity, Class newActivity, Map<String, Object> extras) {
        Intent intent = new Intent(currentActivity, newActivity);
        extras.forEach((key, value) -> {
            if (value.getClass() == String.class) intent.putExtra(key, (String) value);
            else if (value.getClass() == Boolean.class) intent.putExtra(key, (Boolean) value);
        });
        startActivity(currentActivity, intent);
    }

    /**
     *
     * @param currentActivity the activity the method is called from
     * @param intent intent created from the public methods
     */
    private static void startActivity(Context currentActivity, Intent intent) {
        currentActivity.startActivity(intent);
    }
}
