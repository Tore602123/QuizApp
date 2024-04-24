package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.database.ImageRepository;
import com.example.myapplication.model.Image;
import com.example.myapplication.model.ImageData;

import java.util.List;
import java.util.concurrent.Future;

public class BaseViewModel extends AndroidViewModel {
    private ImageRepository repository;
    private LiveData<List<Image>> allImages;


    public BaseViewModel(@NonNull Application application) {
        super(application);
        repository = new ImageRepository(application);
        allImages = repository.getAllImages();
    }

    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }

    public Future<Boolean> insert(ImageData image) {
        return repository.insertImage(image);
    }

    public Future<Boolean> delete(String name) {
        return repository.deleteImage(name);
    }

    public void insertSeveral(ImageData... images) {
        repository.insertImages(images);
    }

    public Future<Void> deleteAll() {
        return repository.clearDatabase();
    }
}
