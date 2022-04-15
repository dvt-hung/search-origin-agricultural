package com.example.apptxng.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String idProduct;
    private String nameProduct;
    private int priceProduct;
    private String imageProduct;
    private String descriptionProduct;
    private int quantityProduct;
    private int quantitySold;
    private String idUser;
    private Category category;
    private Balance balance;
    private String dateProduct;
    private String qrProduct;

    public Product() {
    }

    public Product(String idProduct, String nameProduct, int priceProduct, String imageProduct, String descriptionProduct, int quantityProduct, int quantitySold, String idUser, Category category, Balance balance, String dateProduct) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.imageProduct = imageProduct;
        this.descriptionProduct = descriptionProduct;
        this.quantityProduct = quantityProduct;
        this.quantitySold = quantitySold;
        this.idUser = idUser;
        this.category = category;
        this.balance = balance;
        this.dateProduct = dateProduct;
    }

    public String getQrProduct() {
        return qrProduct;
    }

    public void setQrProduct(String qrProduct) {
        this.qrProduct = qrProduct;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        this.descriptionProduct = descriptionProduct;
    }

    public int getQuantityProduct() {
        return quantityProduct;
    }

    public void setQuantityProduct(int quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public String getDateProduct() {
        return dateProduct;
    }

    public void setDateProduct(String dateProduct) {
        this.dateProduct = dateProduct;
    }
}
