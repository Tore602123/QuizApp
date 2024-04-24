package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.database.ImageRepository;
import com.example.myapplication.databinding.ActivityDatabaseBinding;
import com.example.myapplication.model.Image;
import com.example.myapplication.model.ImageData;
import com.example.myapplication.util.DatabaseUtil;
import com.example.myapplication.util.Util;
import com.example.myapplication.view.ImageAdapter;
import com.example.myapplication.viewmodel.DatabaseViewModel;
import com.example.myapplication.viewmodel.MainViewModel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * Activity for displaying and managing a database of images.
 */
/**
 * DatabaseActivity manages and displays a list of images stored in a database.
 * It allows users to add, sort, and reset the list of images.
 */
public class DatabaseActivity extends AppCompatActivity {

    private ImageAdapter adapter;
    private ActivityDatabaseBinding binding;

    private boolean isSortedDescending = false;
    private DatabaseViewModel databaseViewModel;

    /**
     * Initializes the activity, setting up the UI and data bindings.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatabaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeViewModel();
        initializeAdapter();
        setupRecyclerView();
        setupViewModel();
        setupListeners();
    }

    /**
     * Initializes the adapter with a new ImageRepository instance.
     */
    private void initializeAdapter() {
        adapter = new ImageAdapter(new ImageRepository(getApplication()));

    }
    private void initializeViewModel() {
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
    }

    /**
     * Sets up the RecyclerView with a LinearLayoutManager and attaches the adapter.
     */
    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    /**
     * Configures the ViewModel for managing the database and observes changes to the image list.
     */
    private void setupViewModel() {
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        databaseViewModel.getAllImages().observe(this, images -> {
            adapter.setItems(images);
            adapter.notifyDataSetChanged();
        });
    }

    /**
     * Sets up listeners for UI elements like buttons for sorting and adding images.
     */
    private void setupListeners() {
        binding.dbSortButton.setOnClickListener(view -> toggleSortOrder());
        binding.dbAddButton.setOnClickListener(view -> Util.startActivity(this, com.example.myapplication.activities.AddEntryActivity.class));
    }

    /**
     * Displays a sorting dialog allowing the user to choose the sort order for the images.
     */
    private void toggleSortOrder() {
        isSortedDescending = !isSortedDescending; // Toggle the sorting order
        sort(isSortedDescending);
    }

    /**
     * Sorts the current list of images either in ascending or descending order.
     * @param descending true if the list should be sorted in descending order, false for ascending.
     */
    private void sort(boolean descending) {
        List<Image> currentImages = adapter.getItems();
        if (descending) {
            Collections.sort(currentImages, Collections.reverseOrder());
        } else {
            Collections.sort(currentImages);
        }
        adapter.setItems(currentImages);
        adapter.notifyDataSetChanged();
    }

    /**
     * Inflates the menu for the activity.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_db, menu);
        return true;
    }

    /**
     * Handles item selections from the options menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            showResetDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a confirmation dialog to reset the database to its default state.
     */
    private void showResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to reset the database? (This cannot be undone)")
                .setPositiveButton("Yes", (dialog, id) -> resetDatabase())
                .setNegativeButton("No", null)
                .setTitle("Reset prompt")
                .create()
                .show();
    }

    /**
     * Resets the database to default entries after confirming through a dialog.
     */
    private void resetDatabase() {
        try {
            databaseViewModel.deleteAll().get(); // Waits for the deletion to complete
            databaseViewModel.insertSeveral(DatabaseUtil.getDefaults(getResources())); // Inserts default images
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to reset database", e);
        }
    }

    /**
     * Ensures the RecyclerView updates its display when the activity resumes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    public LiveData<List<Image>> getAllImages() {
        return databaseViewModel.getAllImages();
    }

}