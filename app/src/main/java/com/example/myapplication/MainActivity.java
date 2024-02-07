package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// MainActivity serves as the entry point for the application, offering navigation options to other activities.
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    // Button fields for navigating to different activities
    private Button btnDatabase, btnQuiz, btnAddEntry;
    // Singleton database instance for initializing and accessing the application's data
    private final Database database = Database.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        // Pre-populates the database with initial data
        database.initializeDatabase();

        // Initialize buttons by finding them by ID from the layout
        btnDatabase = findViewById(R.id.btnDatabase);
        btnQuiz = findViewById(R.id.btnQuiz);
        btnAddEntry = findViewById(R.id.btnAddEntry);

        // Set the onClickListener for buttons to handle click events
        btnDatabase.setOnClickListener(this);
        btnAddEntry.setOnClickListener(this);
        btnQuiz.setOnClickListener(this);
    }

    // Handles click events for each button by navigating to the respective activities
    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick-Button clicked: " + view.getResources().getResourceEntryName(view.getId()));

        int id = view.getId();
        if (id == R.id.btnDatabase) {
            // Navigate to DatabaseActivity to view the list of Animals
            startActivity(new Intent(this, DatabaseActivity.class));
        } else if (id == R.id.btnQuiz) {
            // Navigate to QuizActivity (assuming this activity is for quizzes)
            startActivity(new Intent(this, QuizActivity.class));
        } else if (id == R.id.btnAddEntry) {
            // Navigate to AddEntryActivity to add a new Animal entry
            startActivity(new Intent(this, AddEntryActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // This method can be used to refresh UI or data when the activity resumes
    }
}
