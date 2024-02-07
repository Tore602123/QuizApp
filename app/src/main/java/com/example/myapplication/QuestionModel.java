package com.example.myapplication;

import android.net.Uri;

// QuestionModel represents a quiz question with multiple choice answers.
public class QuestionModel {
    // URI for the question content, could point to an image or other resource.
    private Uri question;
    // Text for the three answer options.
    private String option1, option2, option3;
    // The index of the correct answer (e.g., 1 for option1, 2 for option2, 3 for option3).
    private int correctAnsNo;

    // Constructor for creating a question model instance.
    public QuestionModel(Uri question, String option1, String option2, String option3, int correctAnsNo) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.correctAnsNo = correctAnsNo;
    }

    // Returns the URI of the question.
    public Uri getQuestion() {
        return question;
    }

    // Sets the URI of the question.
    public void setQuestion(Uri question) {
        this.question = question;
    }

    // Returns the text for option 1.
    public String getOption1() {
        return option1;
    }

    // Sets the text for option 1.
    public void setOption1(String option1) {
        this.option1 = option1;
    }

    // Returns the text for option 2.
    public String getOption2() {
        return option2;
    }

    // Sets the text for option 2.
    public void setOption2(String option2) {
        this.option2 = option2;
    }

    // Returns the text for option 3.
    public String getOption3() {
        return option3;
    }

    // Sets the text for option 3.
    public void setOption3(String option3) {
        this.option3 = option3;
    }

    // Returns the index of the correct answer.
    public int getCorrectAnsNo() {
        return correctAnsNo;
    }

    // Sets the index of the correct answer.
    public void setCorrectAnsNo(int correctAnsNo) {
        this.correctAnsNo = correctAnsNo;
    }
}
