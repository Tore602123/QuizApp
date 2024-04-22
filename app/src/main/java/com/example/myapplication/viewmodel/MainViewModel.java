package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.database.AnimalRepository;
import com.example.myapplication.model.Animal;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class MainViewModel extends BaseAnimalViewModel {

    private MutableLiveData<Boolean> isEnoughAnimals = new MutableLiveData<>();

    public LiveData<Boolean> getIsEnoughAnimals() {
        return isEnoughAnimals;
    }
    /**
     * Constructor initializes the repository and retrieves a LiveData list of all animals from the database.
     * Utilizes the base class to initialize repository and live data of animals.
     * @param application The application instance containing the global application state.
     */
    public MainViewModel(Application application) {
        super(application);

    }

    public void ensureAnimalsReady() {
        repository.getAllAnimals().observeForever(animals -> {
            // Collect unique animal names to avoid duplicates.
            Set<String> uniqueAnimalNames = animals.stream()
                    .map(Animal::getName)
                    .collect(Collectors.toSet());

            // Check if there are enough unique animals to start a quiz
            boolean enoughUnique = uniqueAnimalNames.size() >= 3;
            isEnoughAnimals.setValue(enoughUnique);

            // If not enough unique animals, add initial entries
            if (!enoughUnique) {
                addInitialEntries(animals);
            }
        });
    }
}