package com.example.apptxng.presenter;

import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.User;

import java.util.List;

public interface IInsertProduct {
    void Exception(String message);
    void getCategory(List<Category> List);
    void getBalance(List<Balance> list);
    void emptyValue();
    void infoFactory (Factory factory);
    void addProductSuccess(String message);
    void addProductFailed(String message);
    void listEmployee (List<User> list);
}
