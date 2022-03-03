package com.example.apptxng.model;

public class Balance {
    private int idBalance;
    private String nameBalance;

    public Balance(String nameBalance) {
        this.nameBalance = nameBalance;
    }

    public int getIdBalance() {
        return idBalance;
    }

    public void setIdBalance(int idBalance) {
        this.idBalance = idBalance;
    }

    public String getNameBalance() {
        return nameBalance;
    }

    public void setNameBalance(String nameBalance) {
        this.nameBalance = nameBalance;
    }
}
