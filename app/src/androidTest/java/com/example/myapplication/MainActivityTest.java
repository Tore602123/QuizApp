package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.activities.DatabaseActivity;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.activities.QuizActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private ActivityScenario<MainActivity> activityScenario;


    /**
     * Sets up the testing environment before each test.
     * Initializes the Intents framework and launches the activity under test.
     */
    @Before
    public void setUp(){
        Intents.init();
        activityScenario = ActivityScenario.launch(MainActivity.class);
    }
    /**
     * Tests if clicking on the quiz button navigates to the QuizActivity.
     */
    @Test
    public void testNavigationToQuizActivity() {
        onView(withId(R.id.quiz_button)).perform(click());
        intended(hasComponent(QuizActivity.class.getName()));
    }

    /**
     * Tests if clicking on the database button navigates to the DatabaseActivity.
     */
    @Test
    public void testNavigationToDatabaseActivity() {
        onView(withId(R.id.db_button)).perform(click());
        intended(hasComponent(DatabaseActivity.class.getName()));
    }
    /**
     * Cleans up the testing environment after each test.
     */
    @After
    public void cleanUp() {
        Intents.release();
        activityScenario.close();
    }
}