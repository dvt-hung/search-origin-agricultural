package com.example.apptxng.presenter;

import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;

import java.util.List;

public interface IInsertProduct {
    void Exception(String message);
    void getCategory(List<Category> List);
    void getBalance(List<Balance> list);
    void emptyValue();

    void addProductSuccess(String message);
    void addProductFailed(String message);
}
