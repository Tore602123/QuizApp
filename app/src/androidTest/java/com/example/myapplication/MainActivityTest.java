package com.example.myapplication;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.myapplication.R;
import com.example.myapplication.activities.AddEntryActivity;
import com.example.myapplication.activities.DatabaseActivity;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.activities.QuizActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void testStartQuizButton() {
        // Perform a click on the start quiz button

        onView(withId(R.id.playquiz)).perform(click());
        // Verifies that QuizActivity is started
        intended(hasComponent(QuizActivity.class.getName()));
    }

    @Test
    public void testDatabaseButton() {
        // Perform a click on the database button
        onView(withId(R.id.db)).perform(click());
        // Verifies that DatabaseActivity is started
        intended(hasComponent(DatabaseActivity.class.getName()));
    }

    @Test
    public void testAddEntryButton() {
        // Perform a click on the add button
        onView(withId(R.id.add)).perform(click());
        // Verifies that AddEntryActivity is started
        intended(hasComponent(AddEntryActivity.class.getName()));
    }
}