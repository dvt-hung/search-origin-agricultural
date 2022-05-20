package com.example.apptxng.presenter;

import com.example.apptxng.model.Factory;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.User;

import java.util.List;

public interface IStatistic {
    void listProducts(List<Product> products);
    void exception (String message);
    void infoFactory (Factory factory);
    void listAccount(List<User> userList);
}
