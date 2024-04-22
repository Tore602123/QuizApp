package com.example.myapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.model.Animal;
import com.example.myapplication.viewmodel.AddEntryViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Activity to add a new animal entry.
 */
public class AddEntryActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    private String name = "";
    private ImageView imagePreview;
    private EditText nameInput;
    private Bitmap selectedBitmap;
    private AddEntryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        viewModel = new ViewModelProvider(this).get(AddEntryViewModel.class);

        initializeViewModel();
        setupViews();
        setupListeners();
    }

    /**
     * Initializes the ViewModel for adding entries.
     */
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(AddEntryViewModel.class);
    }

    /**
     * Sets up UI components by finding views and configuring them.
     */
    private void setupViews() {
        imagePreview = findViewById(R.id.imageanimal);
        nameInput = findViewById(R.id.addAnimalName);
    }

    /**
     * Sets up listeners for UI interactions.
     */
    private void setupListeners() {
        findViewById(R.id.gallery).setOnClickListener(view -> pickImageFromGallery());
        nameInput.addTextChangedListener(new SimpleTextWatcher(s -> name = s));
        findViewById(R.id.addentrybtn).setOnClickListener(view -> addAnimalEntry());
    }

    /**
     * Starts an intent to pick an image from the gallery.
     */
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * Adds the new animal entry to the database.
     */
    private void addAnimalEntry() {
        if (selectedBitmap != null && !name.isEmpty()) {
            new Thread(() -> {
                byte[] byteArray = bitmapToByteArray(selectedBitmap);
                viewModel.insertAnimal(new Animal(name, byteArray));
                runOnUiThread(this::finish);
            }).start();
        }
    }

    /**
     * Converts a Bitmap to a byte array.
     *
     * @param bitmap The Bitmap to convert.
     * @return The byte array.
     */
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        // Calculate the scaling factor based on the desired width or height
        int maxWidth = 800; // or whatever width you deem appropriate
        int maxHeight = 800; // or whatever height you deem appropriate
        float scale = Math.min(((float) maxWidth / bitmap.getWidth()), ((float) maxHeight / bitmap.getHeight()));

        // Create a new scaled bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * scale), (int) (bitmap.getHeight() * scale), true);

        // Compress the scaled bitmap to a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream); // Compress quality set to 75

        return stream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            loadBitmapFromUri(imageUri);
        }
    }

    /**
     * Loads a Bitmap from a Uri and sets it to the ImageView.
     *
     * @param imageUri The URI of the image to load.
     */
    private void loadBitmapFromUri(Uri imageUri) {
        try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
            selectedBitmap = BitmapFactory.decodeStream(inputStream);
            imagePreview.setImageBitmap(selectedBitmap);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File not found. Please try another image.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading image.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * SimpleTextWatcher simplifies TextWatcher interface for afterTextChanged use.
     */
    private class SimpleTextWatcher implements TextWatcher {
        private final Consumer<String> afterChanged;

        SimpleTextWatcher(Consumer<String> afterChanged) {
            this.afterChanged = afterChanged;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        public void afterTextChanged(Editable s) {
            afterChanged.accept(s.toString());
        }
    }
}
