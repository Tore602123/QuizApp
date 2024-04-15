package com.example.myapplication.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Represents an animal with a name and image stored in the database.
 * Implements Comparable to allow sorting based on the animal's name.
 */
@Entity(tableName = "animals")
public class Animal implements Comparable<Animal> {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int ID;  // Unique identifier for the animal

    @ColumnInfo(name = "name")
    private String name;  // Name of the animal

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;  // Image of the animal stored as a byte array

    @Ignore
    private boolean markedForDeletion = false;  // Flag to mark the animal for deletion, not persisted in the database

    /**
     * Constructor for creating an animal object.
     * @param name  Name of the animal.
     * @param image Image of the animal stored as a byte array.
     */
    public Animal(String name, byte[] image) {
        this.name = name;
        this.image = image;
    }

    // Getters and setters for the fields
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isMarkedForDelete() {
        return markedForDeletion;
    }

    public void setMarkedForDelete(boolean markedForDeletion) {
        this.markedForDeletion = markedForDeletion;
    }

    /**
     * Compares this animal to another animal based on their names.
     * @param other The other animal to compare to.
     * @return a negative integer, zero, or a positive integer as this animal
     *         is less than, equal to, or greater than the specified animal.
     */
    @Override
    public int compareTo(Animal other) {
        return this.name.compareTo(other.getName());
    }
}