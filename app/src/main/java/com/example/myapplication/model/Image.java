package com.example.myapplication.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_images")
public class Image implements Comparable<Image> {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private String path;

    public Image(String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(Image qi) {
        return name.compareTo(qi.getName());
    }
}

