package com.example.myapplication.viewmodel;

import android.app.Application;
import android.util.Log;


import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.database.AnimalRepository;
import com.example.myapplication.model.Animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LifecycleOwner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


public class QuizViewModel extends AndroidViewModel {
    private AnimalRepository repository;
    private MutableLiveData<List<Animal>> allAnimals;
    private MutableLiveData<Animal> currentAnimal;
    private MutableLiveData<List<Animal>> options;
    private MutableLiveData<Integer> score = new MutableLiveData<>(0);


    public QuizViewModel(Application application) {
        super(application);
        repository = new AnimalRepository(application);
        allAnimals = new MutableLiveData<>();
        currentAnimal = new MutableLiveData<>();
        options = new MutableLiveData<>();

    }

    public void initAnimals() {
        repository.getAllAnimals().observeForever(animals -> {
            if (animals != null && !animals.isEmpty()) {
                // Remove duplicates based on animal name using a Set
                List<Animal> uniqueAnimals = animals.stream()
                        .collect(Collectors.collectingAndThen(
                                Collectors.toMap(Animal::getName, Function.identity(), (existing, replacement) -> existing),
                                map -> new ArrayList<>(map.values())
                        ));
                allAnimals.setValue(uniqueAnimals);
                nextQuestion();  // Prepare the first question
            }
        });
    }

    public LiveData<List<Animal>> getOptions() {
        return options;
    }

    public LiveData<Animal> getCurrentAnimal() {
        return currentAnimal;
    }

    public void nextQuestion() {
        List<Animal> currentList = allAnimals.getValue();
        if (currentList != null && !currentList.isEmpty()) {
            Collections.shuffle(currentList);
            Animal questionAnimal = currentList.remove(0);
            currentList.add(questionAnimal);  // Add it back to the end of the list
            Log.d("QuizViewModel", "Current Question Animal: " + questionAnimal.getName());  // Log the current animal
            currentAnimal.setValue(questionAnimal);  // Set the first animal as the current question
            generateOptions(questionAnimal);  // Generate options for this animal
            allAnimals.setValue(new ArrayList<>(currentList));  // Update LiveData with the modified list
            Log.d("QuizViewModel", "All Animals before shuffle: " + currentList.stream().map(Animal::getName).collect(Collectors.joining(", ")));

        } else {
            Log.e("QuizViewModel", "No animals available for questions.");
        }
    }

    private void generateOptions(Animal correctAnimal) {
        List<Animal> currentList = new ArrayList<>(allAnimals.getValue());  // Clone the current list for manipulation
        if (currentList != null && currentList.size() >= 3) {
            currentList.remove(correctAnimal);  // Remove the correct animal
            Collections.shuffle(currentList);  // Shuffle to randomize

            List<Animal> choices = new ArrayList<>(currentList.subList(0, 2));  // Select two random animals
            if (!choices.contains(correctAnimal)) {
                choices.add(correctAnimal);  // Ensure the correct animal is added only if not already selected
            } else {
                // If the correct animal is somehow still in the choices, add another animal
                choices.add(currentList.get(2)); // Safely assuming there's at least a third distinct animal
            }
            Collections.shuffle(choices);  // Shuffle again to mix the correct answer into the options

            Log.d("QuizViewModel", "Options: " + choices.stream().map(Animal::getName).collect(Collectors.joining(", ")));  // Log the options
            options.postValue(choices);  // Post the updated options
        } else {
            Log.e("QuizViewModel", "Not enough animals to generate quiz options.");
        }
    }


    public void correctAnswer() {
        if (score.getValue() != null) {
            score.postValue(score.getValue() + 1);  // Increment the score
        }
    }

    public LiveData<Integer> getScore() {
        return score;
    }


    public boolean checkAnswer(Animal selectedAnimal) {
        Animal current = currentAnimal.getValue();
        return current != null && selectedAnimal.getID() == current.getID();
    }
}