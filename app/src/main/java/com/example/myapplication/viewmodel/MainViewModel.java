package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.myapplication.database.ImageRepository;
import com.example.myapplication.model.Image;
import com.example.myapplication.model.ImageData;

import java.util.List;

public class MainViewModel extends BaseViewModel {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }
}
