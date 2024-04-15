package com.example.myapplication.viewmodel;

import android.app.Application;
/**
 * ViewModel supporting the UI for adding, deleting, and finding animals in the database.
 * Encapsulates data handling operations to maintain separation of concerns between the UI and data layer.
 * Extends BaseAnimalViewModel to use common repository operations.
 */
public class MainViewModel extends BaseAnimalViewModel {

    /**
     * Constructor initializes the repository and retrieves a LiveData list of all animals from the database.
     * Utilizes the base class to initialize repository and live data of animals.
     * @param application The application instance containing the global application state.
     */
    public MainViewModel(Application application) {
        super(application);
    }
}