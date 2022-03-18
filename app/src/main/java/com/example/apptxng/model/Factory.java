package com.example.apptxng.model;

import java.io.Serializable;

public class Factory implements Serializable {
    private int idFactory;
    private String nameFactory;
    private String addressFactory;
    private String phoneFactory;
    private int idUser;
    private TypeFactory type_factory;

    public Factory() {
    }

    public TypeFactory getType_factory() {
        return type_factory;
    }

    public void setType_factory(TypeFactory type_factory) {
        this.type_factory = type_factory;
    }

    public int getIdFactory() {
        return idFactory;
    }

    public void setIdFactory(int idFactory) {
        this.idFactory = idFactory;
    }

    public String getNameFactory() {
        return nameFactory;
    }

    public void setNameFactory(String nameFactory) {
        this.nameFactory = nameFactory;
    }

    public String getAddressFactory() {
        return addressFactory;
    }

    public void setAddressFactory(String addressFactory) {
        this.addressFactory = addressFactory;
    }

    public String getPhoneFactory() {
        return phoneFactory;
    }

    public void setPhoneFactory(String phoneFactory) {
        this.phoneFactory = phoneFactory;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
