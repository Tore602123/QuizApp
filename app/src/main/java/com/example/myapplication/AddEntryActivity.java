package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

// The AddEntryActivity allows users to add a new entry with an image and text to the database.
public class AddEntryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AddEntryActivity.class.getSimpleName();
    // Singleton instance of the database for storing entries
    private static final Database database = Database.getInstance();

    // UI Components
    private ImageView imageView;
    private Button addEntryButton, chooseImageButton;
    private EditText editText;
    // Uri for the selected image
    private Uri imageUri;
    // Launcher for the image chooser activity
    private ActivityResultLauncher<Intent> imageChooserResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the layout of this activity
        setContentView(R.layout.activity_add_entry);

        Log.d(TAG, "onCreate");

        // Initialize UI components
        imageView = findViewById(R.id.imageView);
        addEntryButton = findViewById(R.id.btnAddEntry);
        chooseImageButton = findViewById(R.id.btnChooseImage);
        editText = findViewById(R.id.edtText);

        // Set click listeners for buttons
        addEntryButton.setOnClickListener(this);
        chooseImageButton.setOnClickListener(this);

        // Setup the result launcher for choosing images
        setupImageChooser();
    }

    // Configures the ActivityResultLauncher for selecting an image from the document storage
    private void setupImageChooser() {
        imageChooserResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                imageUri = result.getData().getData();
                // Persist access permission across device reboots
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Log.d(TAG, "Image URI: " + imageUri);
                // Set the selected image to the imageView
                imageView.setImageURI(imageUri);
            }
        });
    }

    @Override
    public void onClick(View view) {
        // Handle button clicks
        int id = view.getId();
        if (id == R.id.btnAddEntry) {
            // Add the entry to the database
            addEntry();
        } else if (id == R.id.btnChooseImage) {
            // Open the image selector
            selectImage();
        } else {
            // Log unhandled clicks for debugging
            Log.w(TAG, "Unhandled view clicked: " + view);
        }
    }

    // Adds a new entry with the provided text and image to the database
    private void addEntry() {
        String text = editText.getText().toString();
        // Validate input fields are not empty
        if (text.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        Animal animal = new Animal(text, imageUri);
        database.add(animal);
        Toast.makeText(this, "Added to database", Toast.LENGTH_SHORT).show();
        // Navigate back to the database activity
        startActivity(new Intent(this, DatabaseActivity.class));
    }

    // Launches an intent to select an image from the device
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        imageChooserResultLauncher.launch(intent);
    }
}
