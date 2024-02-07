package com.example.myapplication;

import android.net.Uri;

// The Animal class represents an entity with a name and an associated image.
public class Animal {

    // Tag used for logging purposes
    private static final String TAG = "Animal";

    // The name of the animal
    private String name;
    // The URI of the animal's image
    private Uri image;

    // Constructor for creating an instance of Animal with a name and image.
    public Animal(String name, Uri image) {
        this.name = name;
        this.image = image;
    }

    // Overrides the toString method to provide a string representation of an Animal object.
    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", image=" + image +
                '}';
    }

    // Returns the name of the animal.
    public String getName() {
        return name;
    }

    // Sets the name of the animal.
    public void setName(String name) {
        this.name = name;
    }

    // Returns the URI of the animal's image.
    public Uri getImage() {
        return image;
    }

    // Sets the URI of the animal's image.
    public void setImage(Uri image) {
        this.image = image;
    }
}
