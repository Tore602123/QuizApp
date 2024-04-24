package com.example.myapplication.viewmodel;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Future;

import com.example.myapplication.database.ImageRepository;
import com.example.myapplication.model.Image;
import com.example.myapplication.model.ImageData;

public class AddEntryViewModel extends BaseViewModel {

    public AddEntryViewModel(@NonNull Application application) {
        super(application);
    }
}
