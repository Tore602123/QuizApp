package com.example.myapplication.database;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;


import androidx.lifecycle.LiveData;


import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import java.util.concurrent.Future;

import com.example.myapplication.model.Image;
import com.example.myapplication.model.ImageData;


public class ImageRepository {
    private LiveData<List<Image>> allImages;
    private ImageRoomDatabase database;
    private ImageDAO imageDAO;
    private Application application;
    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }

    public ImageRepository(Application application) {
        this.application = application;
        database = ImageRoomDatabase.getDatabase(application);
        imageDAO = database.imageDAO();
        allImages = imageDAO.getAllImages();
    }

    /**
     * Inserts several quiz images into the database.
     * @param quizImages an array of quiz image data to be added to the database
     */
    public void insertImages(final ImageData... quizImages) {
        ImageRoomDatabase.databaseWriteExecutor.execute(() -> {
            for (ImageData quizImageData : quizImages) {
                if (imageDAO.find(quizImageData.getName()).isEmpty()) {
                    String path = saveToInternalStorage(quizImageData);
                    Image image = new Image(quizImageData.getName(), path);
                    imageDAO.insertImage(image);
                }
            }
        });
    }

    /**
     * Inserts a quiz image into the database.
     * @param quizImageData the data for the quiz image
     * @return a boolean whether the quiz image was successfully added or not
     */
    public Future<Boolean> insertImage(final ImageData quizImageData) {
        return ImageRoomDatabase.databaseWriteExecutor.submit(() -> {
            if (imageDAO.find(quizImageData.getName()).isEmpty()) {
                String path = saveToInternalStorage(quizImageData);
                Image quizImage = new Image(quizImageData.getName(), path);
                imageDAO.insertImage(quizImage);
                return true;
            } else return false;
        });
    }

    /**
     * Deletes a quiz image.
     * @param name the name of the quiz image
     * @return a boolean whether the quiz image was successfully deleted or not
     */
    public Future<Boolean> deleteImage(final String name) {
        return database.databaseWriteExecutor.submit(() -> {
            List<Image> searchResults = imageDAO.find(name);
            if (searchResults.size() > 0) {
                imageDAO.deleteImage(name);
                deleteFromInternalStorage(searchResults.get(0).getPath());
                return imageDAO.find(name).size() == 0;
            } else return false;
        });
    }

    /**
     * Finds a quiz image.
     * @param name the name of the quiz image
     * @return the found quiz image
     */
    public Future<Image> findImage(final String name) {
        return database.databaseWriteExecutor.submit(() -> imageDAO.find(name).get(0));
    }

    /**
     * Clears the database.
     * @return nothing (enables use of .get() to block until the method finishes)
     */
    public Future<Void> clearDatabase() {
        return database.databaseWriteExecutor.submit(() -> {
            imageDAO.clearDatabase();

            ContextWrapper cw = new ContextWrapper(application.getApplicationContext());
            File directory = cw.getDir("images", Context.MODE_PRIVATE);
            for (File file : directory.listFiles()) {
                file.delete();
            }
            return null;
        });
    }

    /**
     * Saves a quiz image to the internal storage.
     * @param quizImageData the data for the quiz image
     * @return the path of the stored image
     */
    private String saveToInternalStorage(ImageData quizImageData) {
        String name = quizImageData.getName();
        Bitmap image = quizImageData.getBitmap();

        ContextWrapper cw = new ContextWrapper(application.getApplicationContext());
        // Path to the directory
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // Path to the image
        File path = new File(directory, name + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            // Write image to the output stream.
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path.getAbsolutePath();
    }

    /**
     * Deletes an image from the internal storage
     * @param path the path of the image
     * @return whether the image was deleted or not
     */
    private boolean deleteFromInternalStorage(String path) {
        return new File(path).delete();
    }
}
