package com.example.myapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;

import android.view.View;


import androidx.lifecycle.ViewModelProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.myapplication.activities.DatabaseActivity;

import com.example.myapplication.viewmodel.DatabaseViewModel;

@RunWith(AndroidJUnit4.class)
public class DatabaseActivityTest {
    @Rule
    public ActivityScenarioRule<DatabaseActivity> activityRule = new ActivityScenarioRule<>(DatabaseActivity.class);
    private CountingIdlingResource idlingResource;

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity -> {
            DatabaseViewModel viewModel = new ViewModelProvider(activity).get(DatabaseViewModel.class);
            idlingResource = viewModel.getIdlingResource();
            IdlingRegistry.getInstance().register(idlingResource);
        });
    }
    @Test
    public void markItemAndDeleteTest() {
        // Ensure the RecyclerView is visible on screen
        onView(withId(R.id.recycler_view))
                .check(matches(isDisplayed()));

        int itemPosition = 1; // Index of the item to interact with

        // Click the checkbox named 'markedForDelete' inside the RecyclerView item
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(itemPosition,
                        clickChildViewWithId(R.id.markedfordelete)));

        // Click the delete button outside of the RecyclerView
        onView(withId(R.id.deletebtn)).perform(click());
    }

    // Helper method to perform click on a specific child view by ID within RecyclerView
    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified ID.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View childView = view.findViewById(id);
                childView.performClick();
            }
        };
    }
    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

}
