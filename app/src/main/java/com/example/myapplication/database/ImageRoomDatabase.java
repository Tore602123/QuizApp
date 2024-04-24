package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.myapplication.model.Image;

@Database(entities = {Image.class}, version = 1)
public abstract class ImageRoomDatabase extends RoomDatabase {

    public abstract ImageDAO imageDAO();
    private static ImageRoomDatabase INSTANCE;
    private static int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ImageRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ImageRoomDatabase.class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ImageRoomDatabase.class, "image_database").build();

            }
        }
        return INSTANCE;
    }
}