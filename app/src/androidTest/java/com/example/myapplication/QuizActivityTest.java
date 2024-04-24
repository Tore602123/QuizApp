package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;


import com.example.myapplication.activities.QuizActivity;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.atomic.AtomicInteger;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    // Constants
    private static final String DIFFICULTY_EASY = "easy";

    public ActivityScenario<QuizActivity> activityScenario;
    @Before
    public void setUp(){
        activityScenario = launchQuizActivity(DIFFICULTY_EASY);
    }


    private ActivityScenario<QuizActivity> launchQuizActivity(String difficulty) {
        // Prepare an intent for the QuizActivity with an added difficulty level
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), QuizActivity.class);
        intent.putExtra("difficulty", difficulty);
        return ActivityScenario.launch(intent);
    }
    @Test
    public void test_correctOptionPicked() {

        activityScenario.onActivity(activity -> {
            new Thread(() -> {
                int correctOption = activity.getCorrectOption();
                int choice = getChoiceForCorrectOption(correctOption);

                // Perform click on the correct option and assert the result
                onView(withId(choice)).perform(click());
                assertTrue(activity.getCorrectCounter() == 1 && activity.getCounter() == 1);
            }).start();
        });
    }

    @Test
    public void test_wrongOptionPicked() {
        activityScenario.onActivity(activity -> {
            new Thread(() -> {
                int correctOption = activity.getCorrectOption();
                int choice = getChoiceForIncorrectOption(correctOption);

                // Perform click on the incorrect option and assert the result
                onView(withId(choice)).perform(click());
                assertTrue(activity.getCorrectCounter() == 0 && activity.getCounter() == 1);
            }).start();
        });
    }


    private int getChoiceForCorrectOption(int correctOption) {
        // Determine the UI element ID based on the correct option
        switch (correctOption) {
            case 0:
                return R.id.option_1;
            case 1:
                return R.id.option_2;
            case 2:
                return R.id.option_3;
            default:
                fail("Correct option out of range.");
                return -1; // This line will not execute, but required to fix compiler error.
        }
    }

    private int getChoiceForIncorrectOption(int correctOption) {
        // Determine the UI element ID for an incorrect option
        switch (correctOption) {
            case 0:
                return R.id.option_2;
            case 1:
                return R.id.option_3;
            case 2:
                return R.id.option_1;
            default:
                fail("Correct option out of range.");
                return -1; // This line will not execute, but required to fix compiler error.
        }
    }
}
