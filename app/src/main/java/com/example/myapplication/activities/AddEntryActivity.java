package com.example.myapplication.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;

import com.example.myapplication.databinding.ActivityAddEntryBinding;
import com.example.myapplication.viewmodel.AddEntryViewModel;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.model.ImageData;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.concurrent.Future;

/**
 * Activity to add a new entry with an image and name.
 */
/**
 * AddEntryActivity provides functionality to add a new entry consisting of a name and an image to a database.
 * It supports image selection from the device's gallery and input of a name for the image before saving.
 */
public class AddEntryActivity extends AppCompatActivity {

    private ActivityAddEntryBinding binding;
    private AddEntryViewModel addEntryViewModel;
    private Bitmap bitmap;
    private String name;
    private ImageView image;

    // Launcher for starting the image picker activity
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    handleImagePickerResult(result);
                }
            }
    );

    /**
     * Initializes the activity, sets up UI components, and prepares listeners.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then
     *                           this Bundle contains the most recent data, otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        setupListeners();
    }

    /**
     * Sets up user interface components and initializes ViewModel.
     */
    private void setupUI() {
        binding = ActivityAddEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEntryViewModel = new ViewModelProvider(this).get(AddEntryViewModel.class);
        image = binding.addItemImage;
    }

    /**
     * Configures listeners for UI elements including text input, buttons for gallery access, and submission.
     */
    private void setupListeners() {
        binding.addItemInput.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
            }
        });

        binding.galleryButton.setOnClickListener(view -> launchImagePicker());
        binding.addButton.setOnClickListener(view -> attemptAddEntry());
    }

    /**
     * Launches an Intent to pick an image from the user's gallery.
     */
    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        launcher.launch(intent);
    }

    /**
     * Handles the result from the image picker activity, setting the selected image on the ImageView.
     * @param result The result from the activity.
     */
    private void handleImagePickerResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                loadImageFromUri(imageUri);
            } else {
                Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Loads an image from the specified URI into the ImageView.
     * @param imageUri The URI of the image to load.
     */
    private void loadImageFromUri(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
            image.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Image file not found!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Attempts to add a new entry to the database after validating the input data.
     */
    private void attemptAddEntry() {
        if (isValidEntry()) {
            addEntryToDatabase();
        } else {
            alertIncompleteInfo();
        }
    }

    /**
     * Checks if the name and image have been properly entered and selected.
     * @return True if valid, False otherwise.
     */
    private boolean isValidEntry() {
        return name != null && !name.isEmpty() && image.getDrawable() != null;
    }

    /**
     * Adds the entry to the database and handles the result, displaying appropriate messages.
     */
    private void addEntryToDatabase() {
        Future<Boolean> success = addEntryViewModel.insert(new ImageData(name, bitmap));
        try {
            if (success.get()) {
                Toast.makeText(getApplicationContext(), "Item added to database!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "This item already exists in the database!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to complete database operation", e);
        }
    }

    /**
     * Alerts the user if required information is missing, either the name or the image.
     */
    private void alertIncompleteInfo() {
        if (name == null || name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You need to enter a name!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "You need to pick an image!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Adapter class for TextWatcher with default implementations to simplify creation of listeners for text changes.
     */
    private static abstract class TextWatcherAdapter implements TextWatcher {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
}