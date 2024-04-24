package com.example.myapplication.application;

import android.app.Application;

import com.example.myapplication.database.ImageRepository;

public class QuizApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initializes the repository.
         ImageRepository repo = new ImageRepository(this);
    }
}
