package com.example.myapplication.viewmodel;

import android.app.Application;

/**
 * ViewModel for managing database operations on the Animal data model.
 * Provides a clean API for the UI to interact with the underlying data layer.
 * This class extends BaseAnimalViewModel to inherit common repository methods.
 */
public class DatabaseViewModel extends BaseAnimalViewModel {

    /**
     * Constructor initializes the repository and loads all animals from the database.
     * Utilizes the base class to initialize repository and live data of animals.
     * @param application The application instance containing the global application state.
     */
    public DatabaseViewModel(Application application) {
        super(application);
        
    }
}