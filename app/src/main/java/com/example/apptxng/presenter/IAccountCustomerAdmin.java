package com.example.apptxng.presenter;

import com.example.apptxng.model.User;

import java.util.List;

public interface IAccountCustomerAdmin {
    void listCustomer(List<User> list);
    void Exception(String message);
}
