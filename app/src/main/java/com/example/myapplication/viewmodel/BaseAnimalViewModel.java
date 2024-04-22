package com.example.myapplication.viewmodel;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.test.espresso.idling.CountingIdlingResource;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.myapplication.R;
import com.example.myapplication.model.Animal;
import com.example.myapplication.database.AnimalRepository;

/**
 * Base ViewModel for handling common repository operations across different parts of the application.
 */
public abstract class BaseAnimalViewModel extends AndroidViewModel {
    private CountingIdlingResource idlingResource = new CountingIdlingResource("DataLoader");
    protected AnimalRepository repository;
    protected final LiveData<List<Animal>> allAnimals;

    private Resources resources;
    public BaseAnimalViewModel(Application application) {
        super(application);
        repository = new AnimalRepository(application);
        allAnimals = repository.getAllAnimals();
        this.resources = application.getResources();
    }


    // Helper method to add an animal only if it does not exist
    void addAnimalIfNotPresent(List<Animal> animals, int drawableId, String name) {
        boolean exists = animals.stream().anyMatch(animal -> animal.getName().equals(name));
        if (!exists) {
            insertAnimalEntry(drawableId, name);
        }
    }

    // Converts the drawable resource to byte[] and inserts the new Animal
    private void insertAnimalEntry(int imageResId, String name) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, imageResId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        repository.insertAnimal(new Animal(name, byteArray));
    }

    protected void addInitialEntries(List<Animal> animals) {
        addAnimalIfNotPresent(animals, R.drawable.duck, "Duck");
        addAnimalIfNotPresent(animals, R.drawable.dog, "Dog");
        addAnimalIfNotPresent(animals, R.drawable.broccoli, "Broccoli");
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

    public LiveData<List<Animal>> getAllAnimals() {
        idlingResource.increment();
        LiveData<List<Animal>> animalsLiveData = repository.getAllAnimals();
        animalsLiveData.observeForever(new Observer<List<Animal>>() {
            @Override
            public void onChanged(List<Animal> animals) {
                if (animals != null) {
                    idlingResource.decrement();
                }
            }
        });
        return animalsLiveData;
    }


    public CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }

}