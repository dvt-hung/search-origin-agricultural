package com.example.apptxng.presenter;

import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;

import java.util.List;

public interface IUpdateProduct {
    void Exception(String message);
    void getCategory(List<Category> List);
    void getBalance(List<Balance> list);
    void emptyValue();
    void success(String message);
    void failed(String message);

}
