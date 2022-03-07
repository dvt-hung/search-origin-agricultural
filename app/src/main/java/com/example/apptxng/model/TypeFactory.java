package com.example.apptxng.model;

public class TypeFactory {
    private int idTypeFactory;
    private String nameTypeFactory;

    public TypeFactory(int idTypeFactory, String nameTypeFactory) {
        this.idTypeFactory = idTypeFactory;
        this.nameTypeFactory = nameTypeFactory;
    }

    public int getIdTypeFactory() {
        return idTypeFactory;
    }

    public void setIdTypeFactory(int idTypeFactory) {
        this.idTypeFactory = idTypeFactory;
    }

    public String getNameTypeFactory() {
        return nameTypeFactory;
    }

    public void setNameTypeFactory(String nameTypeFactory) {
        this.nameTypeFactory = nameTypeFactory;
    }
}
