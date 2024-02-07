package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;

// DatabaseActivity displays a list of Animals and provides options to add new entries and sort the list.
public class DatabaseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DatabaseActivity";

    // UI components
    Button btnAddEntry2, btnSort;
    RecyclerView recyclerView;

    // Database instance for retrieving and manipulating animal data
    private final Database database = Database.getInstance();

    // Adapter for the RecyclerView
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        Log.d(TAG, "onCreate: Database");

        // Initialize UI components
        btnAddEntry2 = findViewById(R.id.btnAddEntry2);
        btnSort = findViewById(R.id.btnSort);
        recyclerView = findViewById(R.id.recyclerView);

        // Set click listeners for buttons
        btnAddEntry2.setOnClickListener(this);
        btnSort.setOnClickListener(this);

        // Setup RecyclerView with its adapter and layout manager
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Other initialization code can be added here...
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick-button: " + view.getResources().getResourceEntryName(view.getId()));
        int id = view.getId();
        if (id == R.id.btnAddEntry2) {
            // Navigate to AddEntryActivity to add a new animal entry
            Intent intent = new Intent(this, AddEntryActivity.class);
            startActivity(intent);
        } else if (id == R.id.btnSort) {
            // Sort the list of animals by name in ascending order
            Collections.sort(database.getDatabase(), new Comparator<Animal>() {
                @Override
                public int compare(Animal animal, Animal t1) {
                    return animal.getName().toLowerCase().compareTo(t1.getName().toLowerCase());
                }
            });
            // Notify the adapter that the dataset has changed to refresh the list
            myAdapter.notifyDataSetChanged();
            // Optional: Log sorted list
            for (Animal a : database.getDatabase()) {
                Log.d(TAG, a.toString());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // Refresh the list to reflect any changes
        myAdapter.notifyDataSetChanged();
    }
}
