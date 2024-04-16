package com.example.myapplication.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.model.Animal;
import com.example.myapplication.util.Util;
import com.example.myapplication.viewmodel.MainViewModel;

import java.io.ByteArrayOutputStream;
import java.util.List;


/**
 * Main activity for the Quiz App, which serves as the entry point of the application.
 * This activity allows users to start different quizzes and add entries to the database.
 */
public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeButtons();
        initializeViewModel();
    }

    /**
     * Initializes buttons and sets click listeners for each.
     */
    private void initializeButtons() {
        Button startQuizButton = findViewById(R.id.playquiz);
        startQuizButton.setOnClickListener(view ->
                Util.startActivity(MainActivity.this, QuizActivity.class));

        Button databaseButton = findViewById(R.id.db);
        databaseButton.setOnClickListener(view ->
                Util.startActivity(MainActivity.this, DatabaseActivity.class));

        Button addButton = findViewById(R.id.add);
        addButton.setOnClickListener(view ->
                Util.startActivity(MainActivity.this, AddEntryActivity.class));
    }

    /**
     * Initializes the ViewModel and sets up observers for data changes.
     */
    private void initializeViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getAllAnimals().observe(this, this::handleAnimalListUpdate);
    }

    /**
     * Handles updates to the list of animals. If there are fewer than 3 animals, initial entries are added.
     * @param animals the current list of animals
     */
    private void handleAnimalListUpdate(List<Animal> animals) {
        if (animals.size() < 3) {
            addInitialEntries();
        }
    }

    /**
     * Adds initial entries to the database if there are less than three animals.
     */
    public void addInitialEntries() {
        insertAnimalEntry(R.drawable.duck, "Duck");
        insertAnimalEntry(R.drawable.dog, "Dog");
        insertAnimalEntry(R.drawable.broccoli, "Broccoli");
    }

    /**
     * Inserts a new animal entry into the database.
     * @param imageResId The resource ID of the image.
     * @param name The name of the animal.
     */
    private void insertAnimalEntry(int imageResId, String name) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        mainViewModel.insertAnimal(new Animal(name, byteArray));
    }
}