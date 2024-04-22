package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AnimalRepository;
import com.example.myapplication.model.Animal;
import com.example.myapplication.view.AnimalAdapter;
import com.example.myapplication.viewmodel.AddEntryViewModel;
import com.example.myapplication.viewmodel.DatabaseViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Activity for managing a database of animals.
 */
public class DatabaseActivity extends AppCompatActivity {

    private AnimalAdapter animalAdapter;
    private DatabaseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        initializeViewModel();

        // Setup RecyclerView with the adapter
        setupRecyclerView();

        // Observe changes in animal data
        observeAnimalData();

        // Setup UI interactions
        setupUIInteractions();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        animalAdapter = new AnimalAdapter(new AnimalRepository(getApplication()));
        recyclerView.setAdapter(animalAdapter);
    }

    private void observeAnimalData() {
        viewModel.getAllAnimals().observe(this, animals -> {
            animals.sort(Comparator.comparing(Animal::getName));
            animalAdapter.setAnimals(animals);
        });
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
    }
    private void setupUIInteractions() {
        findViewById(R.id.deletebtn).setOnClickListener(view -> deleteMarkedAnimals());
        findViewById(R.id.addbtn).setOnClickListener(view -> startActivity(new Intent(this, AddEntryActivity.class)));
        findViewById(R.id.sort).setOnClickListener(view -> reverseSortAnimals());
    }

    /**
     * Deletes all animals marked for deletion in the adapter and updates the UI.
     */
    private void deleteMarkedAnimals() {
        animalAdapter.deleteMarkedAnimals();
        observeAnimalData(); // Refresh data observation to reflect changes
    }

    /**
     * Reverses the current sort order of animals in the adapter.
     */
    private void reverseSortAnimals() {
        List<Animal> reversedAnimals = animalAdapter.getAnimals();
        Collections.reverse(reversedAnimals);
        animalAdapter.setAnimals(reversedAnimals);
    }
}