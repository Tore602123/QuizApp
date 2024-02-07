package com.example.myapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final long COUNTDOWN_IN_MILLIS = 20000; // 20 seconds for countdown

    private ImageView tvQuestion;
    private TextView tvScore, tvQuestionNo, tvTimer;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3;
    private Button btnNext;
    private ColorStateList dfrbColour;
    private CountDownTimer countDownTimer;
    private boolean answered;
    private int totalQuestions;
    private int questionCounter = 0;
    private int score = 0;
    private QuestionModel currentQuestion;
    private List<QuestionModel> questionsList;
    private final Database database = Database.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Log.d(TAG, "onCreate");

        // Initialization
        tvQuestion = findViewById(R.id.imageViewQuestion);
        tvScore = findViewById(R.id.textScore);
        tvQuestionNo = findViewById(R.id.textQuestionNo);
        tvTimer = findViewById(R.id.textTimer);
        radioGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        btnNext = findViewById(R.id.btnNext);
        dfrbColour = rb1.getTextColors();

        questionsList = new ArrayList<>();
        addQuestions();
        totalQuestions = questionsList.size();
        showNextQuestion();

        btnNext.setOnClickListener(view -> {
            if (!answered) {
                if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                    checkAnswer();
                } else {
                    Toast.makeText(QuizActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNextQuestion();
            }
        });
    }

    private void checkAnswer() {
        answered = true;
        if(countDownTimer != null) {
            countDownTimer.cancel(); // Cancel the current timer
        }
        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNo = radioGroup.indexOfChild(rbSelected) + 1;
        if (answerNo == currentQuestion.getCorrectAnsNo()) {
            score++;
            tvScore.setText("Score: " + score);
        }
        updateAnswerColors();
        if (questionCounter < totalQuestions) {
            btnNext.setText("Next");
        } else {
            btnNext.setText("Finish");
        }
    }

    private void updateAnswerColors() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        switch (currentQuestion.getCorrectAnsNo()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                break;
        }
    }

    private void showNextQuestion() {
        if(countDownTimer != null) {
            countDownTimer.cancel(); // Ensure the timer is reset before starting a new one
        }
        rb1.setTextColor(dfrbColour);
        rb2.setTextColor(dfrbColour);
        rb3.setTextColor(dfrbColour);

        if (questionCounter < totalQuestions) {
            currentQuestion = questionsList.get(questionCounter);

            // Assume setImageURI is replaced or correctly handled to set the image
            tvQuestion.setImageURI(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            btnNext.setText("Submit");
            tvQuestionNo.setText("Question: " + questionCounter + "/" + totalQuestions);
            answered = false;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(COUNTDOWN_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("00:" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                showNextQuestion();
            }
        }.start();
    }

    private void addQuestions() {
        // Simplified for clarity; Assume database.getDatabase() provides valid data
        Random random = new Random();
        int rndQ1 = random.nextInt(database.getDatabase().size());
        int rndQ2, rndQ3;
        do { rndQ2 = random.nextInt(database.getDatabase().size()); } while (rndQ1 == rndQ2);
        do { rndQ3 = random.nextInt(database.getDatabase().size()); } while (rndQ1 == rndQ3 || rndQ2 == rndQ3);

        questionsList.add(new QuestionModel(database.getAnimal(rndQ1).getImage(), database.getAnimalName(rndQ2), database.getAnimalName(rndQ1), database.getAnimalName(rndQ3), 2));
        questionsList.add(new QuestionModel(database.getAnimal(rndQ2).getImage(), database.getAnimalName(rndQ3), database.getAnimalName(rndQ2), database.getAnimalName(rndQ1), 2));
        questionsList.add(new QuestionModel(database.getAnimal(rndQ3).getImage(), database.getAnimalName(rndQ1), database.getAnimalName(rndQ2), database.getAnimalName(rndQ3), 3));
    }

    private void finishQuiz() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
