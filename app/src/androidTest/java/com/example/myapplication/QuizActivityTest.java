package com.example.myapplication;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.IdlingRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.myapplication.activities.QuizActivity;
import com.example.myapplication.viewmodel.QuizViewModel;
import com.example.myapplication.model.Animal;

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    private ActivityScenario<QuizActivity> quizActivityScenario;
    QuizViewModel viewModel;

    @Before
    public void setup() {
        Intent quizIntent = new Intent(ApplicationProvider.getApplicationContext(), QuizActivity.class);
        quizActivityScenario = ActivityScenario.launch(quizIntent);
        // Optional: Setup an Idling Resource if your activity has background threads
    }

    @Test
    public void testQuizActivity() {
        quizActivityScenario.onActivity(activity -> {
            int counterCorrect = viewModel.getScore().getValue();

            // Click on the correct button based on the button index
            onView(withId(getButtonIdByIndex(0))).perform(click());

            // Verify the updated text view after the first click
            onView(withId(R.id.textViewQuiz))
                    .check(matches(withText(counterCorrect + 1 + " right answers")));

            int wrongButton = (1);
            onView(withId(getButtonIdByIndex(wrongButton))).perform(click());

            // Verify the updated text view after the second click
            onView(withId(R.id.textViewQuiz))
                    .check(matches(withText(counterCorrect + 1 + " right answers")));
        });
    }

    private int getButtonIdByIndex(int index) {
        switch (index) {
            case 0:
                return R.id.button1;
            case 1:
                return R.id.button2;
            case 2:
                return R.id.button3;
            default:
                throw new IllegalArgumentException("Invalid button index");
        }
    }

    @After
    public void tearDown() {
    }
}