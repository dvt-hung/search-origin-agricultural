package com.example.apptxng.model;

public class History {
    private int idHistory;
    private int idProduct;
    private int idFactory;
    private String descriptionHistory;
    private String dateHistory;
    private String imageHistory;

    public History() {
    }

    public int getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(int idHistory) {
        this.idHistory = idHistory;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdFactory() {
        return idFactory;
    }

    public void setIdFactory(int idFactory) {
        this.idFactory = idFactory;
    }

    public String getDescriptionHistory() {
        return descriptionHistory;
    }

    public void setDescriptionHistory(String descriptionHistory) {
        this.descriptionHistory = descriptionHistory;
    }

    public String getDateHistory() {
        return dateHistory;
    }

    public void setDateHistory(String dateHistory) {
        this.dateHistory = dateHistory;
    }

    public String getImageHistory() {
        return imageHistory;
    }

    public void setImageHistory(String imageHistory) {
        this.imageHistory = imageHistory;
    }
}
