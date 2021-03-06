package com.example.apptxng.model;

import java.io.Serializable;

public class Factory implements Serializable {
    private int idFactory;
    private String nameFactory;
    private String addressFactory;
    private String phoneFactory;
    private String webFactory;
    private String ownerFactory;
    private String idUser;
    private TypeFactory type_factory;

    public Factory() {
    }

    public String getOwnerFactory() {
        return ownerFactory;
    }

    public void setOwnerFactory(String ownerFactory) {
        this.ownerFactory = ownerFactory;
    }

    public String getWebFactory() {
        return webFactory;
    }

    public void setWebFactory(String webFactory) {
        this.webFactory = webFactory;
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

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
