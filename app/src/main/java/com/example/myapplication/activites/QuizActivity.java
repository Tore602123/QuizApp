package com.example.myapplication.activites;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.model.Animal;
import com.example.myapplication.viewmodel.QuizViewModel;

public class QuizActivity extends AppCompatActivity {

    private QuizViewModel qViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        qViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        setupButtonListeners();
        updateQuizContent();
    }

    private void setupButtonListeners() {
        findViewById(R.id.button1).setOnClickListener(v -> handleButtonPress(0));
        findViewById(R.id.button2).setOnClickListener(v -> handleButtonPress(1));
        findViewById(R.id.button3).setOnClickListener(v -> handleButtonPress(2));
    }

    private void handleButtonPress(int buttonIndex) {
        boolean correct = qViewModel.getCorrectAnswerPosition() == buttonIndex;
        provideFeedback(correct);
        qViewModel.updateQuestionIndex();
        updateQuizContent();
    }

    private void provideFeedback(boolean correct) {
        View view = findViewById(R.id.QuizLayout);
        ImageView imageView = findViewById(R.id.imageViewQuiz);
        int colorFrom = Color.WHITE;
        int colorTo = correct ? Color.GREEN : Color.RED;
        String animationType = correct ? "correct_animation" : "incorrect_animation";

        animateBackgroundColor(view, colorFrom, colorTo);
        animateImage(imageView, animationType);

        if (!correct) {
            Toast.makeText(getApplicationContext(), "Correct answer was: " + qViewModel.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
        }
    }

    private void animateBackgroundColor(View view, int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(1000);
        colorAnimation.addUpdateListener(animation -> view.setBackgroundColor((int) animation.getAnimatedValue()));
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.setRepeatCount(1);
        colorAnimation.start();
    }

    private void animateImage(ImageView imageView, String animationType) {
        @SuppressLint("DiscouragedApi") Animation animation = AnimationUtils.loadAnimation(this, getResources().getIdentifier(animationType, "anim", getPackageName()));
        imageView.startAnimation(animation);
    }

    private void updateQuizContent() {
        if (qViewModel.isQuizOver()) {
            finishQuiz();
            return;
        }

        Animal animal = qViewModel.getCurrentAnimal();
        updateUIWithAnimal(animal);
    }

    private void updateUIWithAnimal(Animal animal) {
        ImageView imageView = findViewById(R.id.imageViewQuiz);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(animal.getImage(), 0, animal.getImage().length));

        TextView textView = findViewById(R.id.textViewQuiz);
        textView.setText(qViewModel.getCorrectAnswersCount() + " right answers out of " + qViewModel.getTotalQuestionsAnswered() + " questions");

        updateAnswerButtons();
    }

    private void updateAnswerButtons() {
        ((Button) findViewById(R.id.button1)).setText(qViewModel.getOptionName(0));
        ((Button) findViewById(R.id.button2)).setText(qViewModel.getOptionName(1));
        ((Button) findViewById(R.id.button3)).setText(qViewModel.getOptionName(2));
    }

    private void finishQuiz() {
        Toast.makeText(getApplicationContext(), "Quiz Over! You scored " + qViewModel.getCorrectAnswersCount(), Toast.LENGTH_LONG).show();
    }
}