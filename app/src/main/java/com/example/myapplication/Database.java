package com.example.myapplication;

import android.net.Uri;
import java.util.ArrayList;

// The Database class is a singleton that manages a collection of Animal objects.
public class Database {

    // Tag for logging
    private final static String TAG = "Database";

    // The single instance of the Database class
    private static Database instance = null;

    // The internal storage for Animal objects
    private ArrayList<Animal> database = new ArrayList<>();

    // Private constructor to prevent instantiation
    private Database() {}

    // Returns the singleton instance of the Database, creating it if necessary.
    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    // Adds an Animal object to the database.
    public void add(Animal animal) {
        database.add(animal);
    }

    // Initializes the database with some predefined Animal objects.
    public void initializeDatabase() {
        if (!database.isEmpty()) {
            return; // Exit if data is already initialized
        }
        database.add(new Animal("Dog", getUri(R.drawable.dog)));
        database.add(new Animal("Broccoli", getUri(R.drawable.broccoli)));
        database.add(new Animal("Duck", getUri(R.drawable.duck)));
    }

    // Converts a drawable resource ID into a Uri.
    public Uri getUri(int imageID) {
        return Uri.parse("android.resource://com.example.myapplication/" + imageID);
    }

    // Retrieves an Animal object by index.
    public Animal getAnimal(int i) {
        return database.get(i);
    }

    // Retrieves the name of an Animal object by index.
    public String getAnimalName(int i) {
        return database.get(i).getName();
    }

    // Returns the entire database of Animals.
    public ArrayList<Animal> getDatabase() {
        return database;
    }

    // Sets the database of Animals.
    public void setDatabase(ArrayList<Animal> database) {
        this.database = database;
    }
}
