package com.example.apptxng.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class History implements Serializable {
    private String idHistory;
    private String idProduct;
    private String descriptionHistory;
    private String dateHistory;
    private Factory factory;
    private TypeFactory type_factory;

    public History() {
    }


    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    public TypeFactory getType_factory() {
        return type_factory;
    }

    public void setType_factory(TypeFactory type_factory) {
        this.type_factory = type_factory;
    }

    public String getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(String idHistory) {
        this.idHistory = idHistory;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
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

}
