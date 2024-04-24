package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.activities.QuizActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    private static final String DIFFICULTY_EASY = "easy";  // Difficulty level used to launch the quiz.

    public ActivityScenario<QuizActivity> activityScenario; // Handles the lifecycle and state of QuizActivity.

    /**
     * Set up the test environment before each test.
     * This method initializes the activity scenario with a specific difficulty setting.
     */
    @Before
    public void setUp(){
        activityScenario = launchQuizActivity();
    }

    /**
     * Prepares and launches the QuizActivity with the provided difficulty.
     *
     * @return ActivityScenario for controlling and testing the activity.
     */
    private ActivityScenario<QuizActivity> launchQuizActivity() {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), QuizActivity.class);
        intent.putExtra("difficulty", QuizActivityTest.DIFFICULTY_EASY);  // Add the difficulty level to the intent.
        return ActivityScenario.launch(intent);  // Launch the activity with the specified intent.
    }

    /**
     * Test to ensure the correct option selection updates counters appropriately.
     * Verifies that selecting the correct answer increments both the correct counter and the attempt counter by 1.
     */
    @Test
    public void test_correctOptionPicked() {
        activityScenario.onActivity(activity -> new Thread(() -> {
            int correctOption = activity.getCorrectOption();
            Log.d("QuizActivityTest", "Correct option: " + correctOption);
            int choice = getChoiceForCorrectOption(correctOption);

            // Simulate clicking on the correct option and verify the outcome.
            onView(withId(choice)).perform(click());
            assertTrue(activity.getCorrectCounter() == 1 && activity.getCounter() == 1);
        }).start());
    }

    /**
     * Test to ensure the incorrect option selection does not increment the correct counter.
     * Verifies that selecting an incorrect answer increments only the attempt counter by 1.
     */
    @Test
    public void test_wrongOptionPicked() {
        activityScenario.onActivity(activity -> new Thread(() -> {
            int correctOption = activity.getCorrectOption();
            Log.d("QuizActivityTest", "Correct option: " + correctOption);
            int choice = getChoiceForIncorrectOption(correctOption);

            // Simulate clicking on the incorrect option and verify the outcome.
            onView(withId(choice)).perform(click());
            assertTrue(activity.getCorrectCounter() == 0 && activity.getCounter() == 1);
        }).start());
    }

    /**
     * Returns the UI element ID corresponding to the correct option.
     * @param correctOption the index of the correct option.
     * @return the UI element ID of the correct option.
     */
    private int getChoiceForCorrectOption(int correctOption) {
        switch (correctOption) {
            case 1:
                return R.id.option_1;
            case 2:
                return R.id.option_2;
            case 3:
                return R.id.option_3;
            default:
                fail("Correct option out of range.");  // Throw an error if the index is invalid.
                return -1;  // Compiler requires a return statement.
        }
    }

    /**
     * Returns the UI element ID corresponding to an incorrect option.
     * @param correctOption the index of the correct option to avoid.
     * @return the UI element ID of an incorrect option.
     */
    private int getChoiceForIncorrectOption(int correctOption) {
        switch (correctOption) {
            case 1:
                return R.id.option_2;
            case 2:
                return R.id.option_3;
            case 3:
                return R.id.option_1;
            default:
                fail("Correct option out of range.");  // Throw an error if the index is invalid.
                return -1;  // Compiler requires a return statement.
        }
    }
}