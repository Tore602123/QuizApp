package com.example.myapplication;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.example.myapplication.activities.QuizActivity;
import com.example.myapplication.database.AnimalRepository;
import com.example.myapplication.viewmodel.QuizViewModel;

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    @Rule
    public ActivityScenarioRule<QuizActivity> activityRule = new ActivityScenarioRule<>(QuizActivity.class);
    private CountingIdlingResource idlingResource;

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity -> {
            QuizViewModel viewModel = new ViewModelProvider(activity).get(QuizViewModel.class);
            idlingResource = viewModel.getIdlingResource();
            IdlingRegistry.getInstance().register(idlingResource);
        });
    }
    @Test
    public void testDataLoading() {
        onView(withId(R.id.button1)).check(matches(withText("Data loaded")));
    }
    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}