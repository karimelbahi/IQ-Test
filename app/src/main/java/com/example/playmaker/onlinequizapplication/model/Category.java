package com.example.playmaker.onlinequizapplication.model;

/**
 * Created by playmaker on 3/30/2018.
 */

public class Category {
    private String Name;
    private String Image;

    public Category() {
    }

    public Category(String Name, String image) {
        this.Name = Name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
