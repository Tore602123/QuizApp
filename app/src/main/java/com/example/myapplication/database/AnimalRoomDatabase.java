package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.model.Animal;

/**
 * Singleton class that represents the Room database for this application.
 * Defines the database configuration and serves as the main access point to the persisted data.
 */
@Database(entities = {Animal.class}, version = 1, exportSchema = false)
public abstract class AnimalRoomDatabase extends RoomDatabase {

    // Data access object for Animal entities.
    public abstract AnimalDAO animalDAO();

    // Singleton instance of the database.
    private static volatile AnimalRoomDatabase INSTANCE;

    /**
     * Gets the singleton instance of the database.
     * @param context The context used for creating or retrieving the database.
     * @return The singleton instance of AnimalRoomDatabase.
     */
    public static AnimalRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AnimalRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AnimalRoomDatabase.class, "animals_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this simplified code.
                            // .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
