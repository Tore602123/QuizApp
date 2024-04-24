package com.example.myapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.myapplication.model.Image;

@Dao
public interface ImageDAO {

    /**
     * Gets all of the quiz images.
     * @return a list of all items in the database
     */
    @Query("SELECT * FROM quiz_images")
    LiveData<List<Image>> getAllImages();

    /**
     * Gets all the names of the quiz images
     * @return a list of all names in the database
     */
    @Query("SELECT name FROM quiz_images")
    LiveData<List<String>> getAllNames();

    /**
     * Finds any quiz image with the given name in the database.
     * @param name the name of the quiz image
     * @return list of all quiz images found with the name
     */
    @Query("SELECT * FROM quiz_images WHERE lower(name) = lower(:name)")
    List<Image> find(String name);

    /**
     * Inserts a quiz image into the database.
     * @param quizImage the quiz image t    o be inserted
     */
    @Insert
    void insertImage(Image quizImage);

    /**
     * Removes a quiz image from the database.
     * @param name the name of the quiz image to be removed
     */
    @Query("DELETE FROM quiz_images WHERE lower(name) = lower(:name)")
    void deleteImage(String name);

    /**
     * Clears the entire database.
     */
    @Query("DELETE FROM quiz_images")
    void clearDatabase();
}
