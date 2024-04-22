package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.intent.Intents;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.core.IsNot.not;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


import com.example.myapplication.activities.DatabaseActivity;
import com.example.myapplication.database.AnimalRepository;
import com.example.myapplication.activities.AddEntryActivity;
import com.example.myapplication.viewmodel.AddEntryViewModel;
import com.example.myapplication.viewmodel.QuizViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddEntryActivityTest {
    @Rule
    public IntentsTestRule<AddEntryActivity> intentsTestRule = new IntentsTestRule<>(AddEntryActivity.class, true, true);
    private AnimalRepository repository;
    private AddEntryViewModel viewModel;
    public ActivityScenarioRule<AddEntryActivity> activityRule = new ActivityScenarioRule<>(AddEntryActivity.class);
    private CountingIdlingResource idlingResource;

    @Before
    public void setupRepository() {
        Application application = (Application) intentsTestRule.getActivity().getApplicationContext();
         repository = new AnimalRepository(application);

        // Stub all external intents
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, createImageReturnIntent()));
    }

    private Intent createImageReturnIntent() {
        // Create an intent that simulates picking an image from the gallery
        Intent resultData = new Intent();
        Uri imageUri = Uri.parse("android.resource://" + InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName() + "/" + R.drawable.cat);
        resultData.setData(imageUri);
        return resultData;
    }
    @Test
    public void testAddEntryToDatabase() {

        int initialCount = repository.getAnimalCount();

        // Stub the intent for picking an image from the gallery
        intending(hasAction(Intent.ACTION_PICK)).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, createImageReturnIntent()));

        // Perform actions in your activity
        // Enter the name into the EditText
        onView(withId(R.id.addAnimalName)).perform(typeText("Cat"), closeSoftKeyboard());

        // Click to open gallery and select the image
        onView(withId(R.id.gallery)).perform(click());

        // Click to confirm adding the entry
        onView(withId(R.id.addentrybtn)).perform(click());

        // Here you should wait for the database operation to complete
        // Using an IdlingResource is recommended

        // Fetch count again after the operation
        int finalCount = repository.getAnimalCount();

        assertEquals("Entry count should be incremented by 1", initialCount + 1, finalCount);

    }

    @After
    public void tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}