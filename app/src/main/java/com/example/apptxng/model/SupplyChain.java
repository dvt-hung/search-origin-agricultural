package com.example.apptxng.model;

public class SupplyChain {
    private int idSupplyChain;
    private int idTypeFactory;
    private String nameTypeFactory;

    public int getIdSupplyChain() {
        return idSupplyChain;
    }

    public void setIdSupplyChain(int idSupplyChain) {
        this.idSupplyChain = idSupplyChain;
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
