package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import com.example.myapplication.model.Animal;
import com.example.myapplication.database.AnimalRepository;

/**
 * Base ViewModel for handling common repository operations across different parts of the application.
 */
public abstract class BaseAnimalViewModel extends AndroidViewModel {

    protected final AnimalRepository repository;
    protected final LiveData<List<Animal>> allAnimals;

    public BaseAnimalViewModel(Application application) {
        super(application);
        repository = new AnimalRepository(application);
        allAnimals = repository.getAllAnimals();
    }
    public LiveData<List<Animal>> getAllAnimals() {
        return allAnimals;
    }
    public void insertAnimal(Animal animal) {
        repository.insertAnimal(animal);
    }
    public void findAnimal(String name) {
        repository.findAnimal(name);
    }
    public void deleteAnimal(Animal animal) {
        repository.deleteAnimal(animal);
    }
}