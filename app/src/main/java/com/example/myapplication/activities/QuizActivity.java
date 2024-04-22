package com.example.myapplication.activities;


import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.database.AnimalRepository;
import com.example.myapplication.model.Animal;
import com.example.myapplication.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private QuizViewModel viewModel;
    private ImageView questionImage;
    private Button option1, option2, option3;
    private TextView scoreDisplay, timerDisplay;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewModel();

        viewModel.getAllAnimals().observe(this, animals -> {
            if (animals != null && !animals.isEmpty()) {
                setupUI();  // Call setupUI here to ensure it runs after data is initialized
            }
            viewModel.initAnimals();
        });
    }

    private void setupUI() {
        setContentView(R.layout.activity_quiz);
        questionImage = findViewById(R.id.imageViewQuiz);
        option1 = findViewById(R.id.button1);
        option2 = findViewById(R.id.button2);
        option3 = findViewById(R.id.button3);
        scoreDisplay = findViewById(R.id.scoreTextView);
        timerDisplay = findViewById(R.id.timerTextView);
        setupObservers();
    }
    private void setupObservers() {
        viewModel.getCurrentAnimal().observe(this, animal -> {
            if (animal != null && animal.getImage() != null) {
                questionImage.setImageBitmap(BitmapFactory.decodeByteArray(animal.getImage(), 0, animal.getImage().length));
                resetAndStartTimer(); // Reset and start timer for new question
            }
        });

        viewModel.getOptions().observe(this, animals -> {
            if (animals != null && animals.size() == 3) {
                option1.setText(animals.get(0).getName());
                option1.setOnClickListener(v -> checkAnswer(animals.get(0)));

                option2.setText(animals.get(1).getName());
                option2.setOnClickListener(v -> checkAnswer(animals.get(1)));

                option3.setText(animals.get(2).getName());
                option3.setOnClickListener(v -> checkAnswer(animals.get(2)));
            }
        });

        viewModel.getScore().observe(this, score -> {
            scoreDisplay.setText("Score: " + score);  // Update the score display
        });
    }
    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);
    }

    private void resetAndStartTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerDisplay.setText("Time left: " + millisUntilFinished / 1000 + "s");
            }
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                viewModel.nextQuestion();
            }
        }.start();
    }
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();  // Make sure to cancel the timer when the activity is destroyed
        }
    }
    private void checkAnswer(Animal selectedAnimal) {
        boolean isCorrect = viewModel.checkAnswer(selectedAnimal);
        if (isCorrect) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            viewModel.correctAnswer(); // Increment the score
        } else {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
        }
        viewModel.nextQuestion(); // Load next question regardless of answer correctness
    }



}