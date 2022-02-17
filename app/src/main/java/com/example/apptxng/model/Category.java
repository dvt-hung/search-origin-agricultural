package com.example.apptxng.model;

public class Category {
    private int idCategory;
    private String nameCategory;
    private String imageCategory;

    public Category() {
    }

    public Category(int idCategory, String nameCategory, String imageCategory) {
        this.idCategory = idCategory;
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

    public String getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(String imageCategory) {
        this.imageCategory = imageCategory;
    }
}
