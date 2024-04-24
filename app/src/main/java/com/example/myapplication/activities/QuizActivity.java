package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.ActivityQuizBinding;
import com.example.myapplication.model.Image;
import com.example.myapplication.util.DatabaseUtil;
import com.example.myapplication.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;




/**
 * QuizActivity is an Android activity responsible for displaying and managing a quiz game.
 * It uses the QuizViewModel to maintain the state of the quiz and interact with the database.
 * The activity dynamically adjusts the UI based on user interactions and quiz progress.
 */
public class QuizActivity extends AppCompatActivity {

    private QuizViewModel quizViewModel;
    private ActivityQuizBinding binding;
    private final int MAXTIME = 30; // Maximum time allowed for each question in seconds when in hard mode
    private Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable = this::updateTimer;

    /**
     * Called when the activity is starting. This is where most initialization should go:
     * creating views, binding data, etc.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     * Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewModel();
        setupBinding();
        setupInitialQuizState();
        observeQuizData();
        setupClickListeners();
    }

    /** Initializes the QuizViewModel that is used to manage the quiz's data. */
    private void initializeViewModel() {
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
    }

    /** Sets up data binding for activity views and layout. */
    private void setupBinding() {
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    /** Configures initial quiz settings based on intent extras, particularly the difficulty level. */
    private void setupInitialQuizState() {
        Intent intent = getIntent();
        quizViewModel.setHardMode("hard".equals(intent.getStringExtra("difficulty")));
        // Reset quiz state manually
        quizViewModel.resetQuiz();
    }

    /** Observes changes to quiz data such as image list updates from the database. */
    private void observeQuizData() {
        quizViewModel.getAllImages().observe(this, this::onImagesLoaded);
    }

    /**
     * Called when images are successfully loaded from the database.
     * It sets up the quiz order and moves to the first question.
     * @param images List of images to be used in the quiz.
     */
    private void onImagesLoaded(List<Image> images) {
        quizViewModel.setCurrentImages(images);
        initializeQuizOrder(images);
        next();
        if (quizViewModel.isHardMode()) {
            showTimer();
        }
    }

    /**
     * Randomizes the order of quiz questions and initializes the progress bar.
     * @param images List of images based on which the quiz order is created.
     */
    private void initializeQuizOrder(List<Image> images) {
        Stack<Integer> order = generateShuffledOrder(images.size());
        binding.progressBar.setMax(order.size());
        binding.progressBar.setProgress(0);
        quizViewModel.setOrder(order);
    }

    /**
     * Generates a randomized order of indices for quiz questions.
     * @param size Number of images.
     * @return A stack of shuffled indices.
     */
    private Stack<Integer> generateShuffledOrder(int size) {
        Stack<Integer> order = new Stack<>();
        for (int i = 0; i < size; i++) {
            order.push(i);
        }
        Collections.shuffle(order);
        return order;
    }

    /** Makes the timer visible on the UI and starts it if in hard mode. */
    private void showTimer() {
        binding.timer.setVisibility(View.VISIBLE);
        binding.timerProgress.setMax(MAXTIME);
        startTimer();
    }

    /** Sets up click listeners for interactive elements in the quiz. */
    private void setupClickListeners() {
        binding.option1.setOnClickListener(view -> handleOption(1));
        binding.option2.setOnClickListener(view -> handleOption(2));
        binding.option3.setOnClickListener(view -> handleOption(3));
        binding.backButton.setOnClickListener(view -> finish());
    }

    /**
     * Handles logic when an option is clicked.
     * Marks the option as checked, updates the progress, and displays the result.
     * @param option The selected option number.
     */
    private void handleOption(int option) {
        check(option);
        next();
    }

    /**
     * Checks the selected option against the correct answer and updates the quiz state.
     * @param option The option number selected by the user.
     */
    private void check(int option) {
        if (!quizViewModel.isChecked()) {
            quizViewModel.markChecked();
            updateProgress();
            displayResult(option);
        }
    }

    /** Updates the progress bar based on the number of questions answered. */
    private void updateProgress() {
        quizViewModel.incrementCounter();
        binding.progressBar.setProgress(quizViewModel.getCounter());
    }

    /**
     * Displays the result of the current question as a toast message.
     * @param option The option selected by the user.
     */
    private void displayResult(int option) {
        String message = getResultMessage(option);
        updateCorrectAnswersDisplay();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Constructs a result message based on whether the answer was correct, incorrect, or if time ran out.
     * @param option The option number selected by the user, or -1 if time ran out.
     * @return String containing the result message.
     */
    private String getResultMessage(int option) {
        String correctNameCapitalized = capitalize(quizViewModel.getCorrectName());
        if (option == quizViewModel.getCorrectOption()) {
            quizViewModel.incrementCorrectCounter();
            return "Correct! The right answer is: " + correctNameCapitalized;
        } else if (option == -1) {
            return "Timer ran out. The correct answer was: " + correctNameCapitalized;
        } else {
            return "Wrong answer. The correct answer was: " + correctNameCapitalized;
        }
    }

    /** Capitalizes the first letter of the provided string. */
    private String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /** Continuously updates the timer until it reaches 0, then moves to the next question. */
    @SuppressLint("SetTextI18n")
    private void updateTimer() {
        quizViewModel.incrementElapsedTime();
        int elapsedTime = quizViewModel.getElapsedTime();
        int timeRemaining = MAXTIME - elapsedTime;
        binding.timerProgress.setProgress(timeRemaining);
        binding.timerText.setText("Time: " + timeRemaining + "s");
        if (elapsedTime < MAXTIME) {
            timerHandler.postDelayed(timerRunnable, 1000);
        } else {
            check(-1);
            next();
        }
    }

    /** Starts or restarts the timer for a new question in hard mode. */
    private void startTimer() {
        timerHandler.removeCallbacks(timerRunnable);
        binding.timerText.setVisibility(View.VISIBLE);
        quizViewModel.setElapsedTime(0);
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    /** Stops the timer when the quiz is paused or completed. */
    private void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    /** Advances the quiz to the next question or ends the quiz if all questions have been answered. */
    private void next() {
        if (quizViewModel.getOrder().isEmpty()) {
            displayQuizCompletion();
        } else {
            prepareNextQuestion();
        }
    }

    /** Displays a completion message and updates the UI to reflect the quiz's end. */
    @SuppressLint("DefaultLocale")
    private void displayQuizCompletion() {
        int correctCounter = quizViewModel.getCorrectCounter();
        int counter = quizViewModel.getCounter();
        binding.correctAnswers.setText(String.format("You finished the quiz with %d correct answer%s out of %d question%s",
                correctCounter, plural(correctCounter), counter, plural(counter)));
        binding.quizImage.setVisibility(View.INVISIBLE);
        binding.option1.setVisibility(View.GONE);
        binding.option2.setVisibility(View.GONE);
        binding.option3.setVisibility(View.GONE);
        binding.backButton.setVisibility(View.VISIBLE);
        stopTimer();
    }

    /** Prepares the UI for the next question, updating options and the displayed image. */
    private void prepareNextQuestion() {
        List<Image> Images = quizViewModel.getCurrentImages();
        quizViewModel.setCurrent(quizViewModel.getOrder().pop());
        Image currentImage = Images.get(quizViewModel.getCurrent());
        quizViewModel.setCorrectName(currentImage.getName());
        List<String> options = generateOptions(Images, currentImage);
        updateUIForNewQuestion(options, currentImage);
    }

    /** Generates a list of options for the current question, ensuring one correct answer and random alternatives. */
    private List<String> generateOptions(List<Image> quizImages, Image currentImage) {
        List<String> options = new ArrayList<>();
        options.add(quizViewModel.getCorrectName());
        Random random = new Random();
        while (options.size() < 3) {
            String randomName = quizImages.get(random.nextInt(quizImages.size())).getName();
            if (!options.contains(randomName)) {
                options.add(randomName);
            }
            // Ensure there are always three options, even if it means adding empty strings
            else if (options.size() >= quizImages.size()) {
                options.add("");
            }
        }
        Collections.shuffle(options);
        quizViewModel.setCorrectOption(options.indexOf(currentImage.getName()) + 1);
        return options;
    }

    /** Updates the UI elements to reflect the new question, options, and the associated image. */
    private void updateUIForNewQuestion(List<String> options, Image currentImage) {
        binding.option1.setText(options.get(0));
        binding.option2.setText(options.get(1));
        binding.option3.setText(options.get(2));
        binding.quizImage.setImageBitmap(DatabaseUtil.getBitmapFromStorage(currentImage.getPath()));
        quizViewModel.setChecked(false);
        if (quizViewModel.isHardMode()) {
            startTimer();
        }
    }

    /** Updates the display of how many answers have been answered correctly and total questions attempted. */
    @SuppressLint("DefaultLocale")
    private void updateCorrectAnswersDisplay() {
        int correctCounter = quizViewModel.getCorrectCounter();
        int counter = quizViewModel.getCounter();
        binding.correctAnswers.setText(String.format("%d correct answer%s out of %d question%s",
                correctCounter, plural(correctCounter), counter, plural(counter)));
    }

    /** Helper function to return a plural suffix if necessary. */
    private String plural(int count) {
        return count != 1 ? "s" : "";
    }
    public int getCounter() {
        return quizViewModel.getCounter();
    }

    public int getCorrectCounter() {
        return quizViewModel.getCorrectCounter();
    }

    public int getCorrectOption() {
        return quizViewModel.getCorrectOption();
    }
}

