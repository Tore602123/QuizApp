package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Stack;
import com.example.myapplication.model.Image;
import com.example.myapplication.database.ImageRepository;

public class QuizViewModel extends BaseViewModel{
    private List<Image> currentImages;
    private Stack<Integer> order;
    private boolean hardMode;
    private boolean checked;
    private int current;
    private int counter;
    private int correctCounter;
    private int correctOption;
    private String correctName;
    private int elapsedTime;

    public QuizViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Image> getCurrentImages() {
        return currentImages;
    }
    public void setCurrentImages(List<Image> currentImages) {
        this.currentImages = currentImages;
    }
    public Stack<Integer> getOrder() {
        return order;
    }

    public void markChecked(){
        this.checked=isChecked();
    }
    public void setOrder(Stack<Integer> order) {
        this.order = order;
    }

    public boolean isHardMode() {
        return hardMode;
    }

    public void setHardMode(boolean hardMode) {
        this.hardMode = hardMode;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void incrementCounter() {
        counter++;
    }

    public int getCorrectCounter() {
        return correctCounter;
    }

    public void setCorrectCounter(int correctCounter) {
        this.correctCounter = correctCounter;
    }

    public void incrementCorrectCounter() {
        correctCounter++;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public String getCorrectName() {
        return correctName;
    }

    public void setCorrectName(String correctName) {
        this.correctName = correctName;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    public void incrementElapsedTime() {
        elapsedTime++;
    }

    public void resetQuiz(){
        current=-1;
        counter=0;
        correctCounter=0;
        checked=false;
    }


}
