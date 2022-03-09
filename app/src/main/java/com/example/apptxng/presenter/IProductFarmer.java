package com.example.apptxng.presenter;

import com.example.apptxng.model.Product;

import java.util.List;

public interface IProductFarmer {

    void getProduct(List<Product> list);
    void Exception(String message);
}
