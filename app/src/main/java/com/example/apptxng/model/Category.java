package com.example.apptxng.model;

import android.net.Uri;

public class Category {
    private int idCategory;
    private String nameCategory;
    private Uri imageCategory;

    public Category() {
    }

    public Category( String nameCategory, Uri imageCategory) {
        this.nameCategory = nameCategory;
        this.imageCategory = imageCategory;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public Uri getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(Uri imageCategory) {
        this.imageCategory = imageCategory;
    }
}
