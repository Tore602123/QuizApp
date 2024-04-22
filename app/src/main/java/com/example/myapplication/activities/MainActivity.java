package com.example.myapplication.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

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
        initializeViewModel();
        initializeButtons();
    }

    private void setupQuizButton() {
        Button startQuizButton = findViewById(R.id.playquiz);
        mainViewModel.getIsEnoughAnimals().observe(this, isEnough -> {
            startQuizButton.setEnabled(isEnough);
            if (isEnough) {
                startQuizButton.setOnClickListener(view ->
                        Util.startActivity(MainActivity.this, QuizActivity.class));
            } else {
                startQuizButton.setOnClickListener(view ->
                        Toast.makeText(MainActivity.this, "Not enough animals for a quiz! Please wait...", Toast.LENGTH_SHORT).show());
            }
        });
        mainViewModel.ensureAnimalsReady();
    }

    /**
     * Initializes buttons and sets click listeners for each.
     */
    private void initializeButtons() {
        setupQuizButton();

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
    }
}