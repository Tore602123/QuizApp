package com.example.myapplication.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.R;
import com.example.myapplication.model.ImageData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DatabaseUtil {
    public static ImageData[] getDefaults(Resources resources) {
        // Default entries
        return new ImageData[]{
                new ImageData("Broccoli", BitmapFactory.decodeResource(resources, R.drawable.broccoli)),
                new ImageData("Dog", BitmapFactory.decodeResource(resources, R.drawable.dog)),
                new ImageData("Duck", BitmapFactory.decodeResource(resources, R.drawable.duck)),
        };
    }
    /**
     * Gets the bitmap from an image in the storage.
     * @param path the path of the image
     * @return the bitmap of the image
     */
    public static Bitmap getBitmapFromStorage(String path) {
        try {
            File f = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
