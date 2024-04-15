package com.example.myapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import com.example.myapplication.model.Animal;

/**
 * Data Access Object (DAO) for managing animal entities in the database.
 * Provides methods for inserting, deleting, and querying animal data.
 */
@Dao
public interface AnimalDAO {

    /**
     * Inserts one or more animals into the database.
     * @param animals One or more Animal objects to be inserted.
     */
    @Insert
    void insertAll(Animal... animals);

    /**
     * Deletes a specific animal from the database.
     * @param animal The Animal object to be deleted.
     */
    @Delete
    void delete(Animal animal);

    /**
     * Retrieves all animals stored in the database.
     * @return A LiveData list containing all Animal objects.
     */
    @Query("SELECT * FROM animals")
    LiveData<List<Animal>> getAll();

    /**
     * Retrieves the names of all animals stored in the database.
     * @return A LiveData list containing the names of all animals.
     */
    @Query("SELECT name FROM animals")
    LiveData<List<String>> getNames();

    /**
     * Finds animals by their name.
     * @param name The name of the animal to find.
     * @return A list of Animal objects that match the name.
     */
    @Query("SELECT * FROM animals WHERE name = :name")
    List<Animal> find(String name);
}