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

    @Insert
    void insertAll(Animal... animals);

    @Insert
    public void insert(Animal a);

    @Delete
    void delete(Animal animal);






    /**
     * Get all animals
     * @return All animals
     */
    @Query("SELECT * FROM animals")
    LiveData<List<Animal>> getAll();

    /**
     * Get all animal names
     *
     * @return List of all animal names
     */
    @Query("SELECT name FROM animals")
    LiveData<List<String>> getNames();

    @Query("SELECT * FROM animals WHERE name = :name")
    List<Animal> find(String name);

    // Add a method to count all animals
    @Query("SELECT COUNT(*) FROM animals")
    int countAnimals();

    @Query("SELECT * FROM animals WHERE name = :name LIMIT 1")
    Animal findByName(String name);
}