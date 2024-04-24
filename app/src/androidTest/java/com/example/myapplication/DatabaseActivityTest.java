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

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.activities.DatabaseActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.atomic.AtomicInteger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseActivityTest {

    private ActivityScenario<DatabaseActivity> activityScenario;
    private static final String TEST_IMAGE_NAME = "Cat";

    /**
     * Sets up the testing environment before each test.
     * Initializes the Intents framework and launches the activity under test.
     */
    @Before
    public void setUp() {
        Intents.init();
        activityScenario = ActivityScenario.launch(DatabaseActivity.class);
    }

    /**
     * Tests adding an image to the database.
     */
    @Test
    public void testAddImage() {
        // Capture the initial number of images in the database.
        AtomicInteger atomicBefore = new AtomicInteger(-1);
        activityScenario.onActivity(activity -> {
            activity.getAllImages().observe(activity, images -> atomicBefore.set(images.size()));
        });
        int before = atomicBefore.get();

        // Simulate button click to add an image.
        onView(withId(R.id.db_add_button)).perform(click());

        // Mock the intent for selecting an image from the gallery and simulate a successful image selection.
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Uri imageUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.cat);
        Intent galleryIntent = new Intent();
        galleryIntent.setData(imageUri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, galleryIntent);
        intending(hasAction(Intent.ACTION_PICK)).respondWith(result);

        // Perform the image addition operations through the UI.
        onView(withId(R.id.add_item_input)).perform(replaceText(TEST_IMAGE_NAME));
        onView(withId(R.id.gallery_button)).perform(click());
        onView(withId(R.id.add_button)).perform(click());

        // Capture the number of images after adding one.
        AtomicInteger atomicAfter = new AtomicInteger(-1);
        activityScenario.onActivity(activity -> {
            activity.getAllImages().observe(activity, images -> atomicAfter.set(images.size()));
        });
        int after = atomicAfter.get();

        // Verify the image count increased by one.
        assertEquals(before + 1, after);
    }

    /**
     * Tests deleting an image from the database.
     */
    @Test
    public void testDeleteImage() {
        // Capture the initial number of images in the database.
        AtomicInteger atomicBefore = new AtomicInteger(-1);
        activityScenario.onActivity(activity -> {
            activity.getAllImages().observe(activity, images -> atomicBefore.set(images.size()));
        });
        int before = atomicBefore.get();

        // Perform deletion of the test image added previously.
        onView(allOf(withId(R.id.item_delete), hasSibling(withText(TEST_IMAGE_NAME)))).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        // Capture the number of images after deletion.
        AtomicInteger atomicAfter = new AtomicInteger(-1);
        activityScenario.onActivity(activity -> {
            activity.getAllImages().observe(activity, images -> atomicAfter.set(images.size()));
        });
        int after = atomicAfter.get();

        // Verify the image count decreased by one.
        assertEquals(before - 1, after);
    }

    /**
     * Cleans up the testing environment after each test.
     */
    @After
    public void tearDown() {
        if (activityScenario != null) {
            activityScenario.close();
        }
        Intents.release();
    }
}