package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.database.AnimalRepository;
import com.example.myapplication.model.Animal;

import java.util.List;
import java.util.Random;

/**
 * ViewModel for managing quiz operations and data in the Quiz App.
 * This ViewModel tracks quiz progress, options, and results, and interfaces with the AnimalRepository for data operations.
 */
public class QuizViewModel extends BaseAnimalViewModel {
    private AnimalRepository animalRepository;
    LiveData<List<Animal>> animals;
    private int questionIndex = -1;
    private int totalQuestionsAnswered = 0;
    private int correctAnswersCount = 0;
    private int correctAnswerPosition;
    private final int MAX_QUESTIONS = 10;

    public QuizViewModel(Application application) {
        super(application);
        animalRepository = new AnimalRepository(application);
        animals = animalRepository.getAllAnimals();
    }

    public Animal getCurrentAnimal() {
        return animals.getValue().get(questionIndex);
    }

    public String getOptionName(int index) {
        // Implement logic to fetch the name based on shuffled index or random selection
        return animals.getValue().get(index).getName();
    }

    public void updateQuestionIndex() {
        if (questionIndex < MAX_QUESTIONS - 1) {
            questionIndex++;
            totalQuestionsAnswered++;
            shuffleAnswers();
        } else {
            resetQuiz();
        }
    }

    private void shuffleAnswers() {
        // Shuffle the answers, ensuring that the correct answer is randomly assigned
        correctAnswerPosition = new Random().nextInt(3);
    }

    public boolean isQuizOver() {
        return totalQuestionsAnswered >= MAX_QUESTIONS;
    }

    public int getCorrectAnswerPosition() {
        return correctAnswerPosition;
    }

    public String getCorrectAnswer() {
        return getCurrentAnimal().getName();
    }

    public int getTotalQuestionsAnswered() {
        return totalQuestionsAnswered;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    private void resetQuiz() {
        questionIndex = 0;
        totalQuestionsAnswered = 0;
        correctAnswersCount = 0;
    }
}