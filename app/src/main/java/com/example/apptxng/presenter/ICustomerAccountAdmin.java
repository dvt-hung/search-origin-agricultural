package com.example.apptxng.presenter;

import com.example.apptxng.model.User;

import java.util.List;

public interface ICustomerAccountAdmin {
    void updateSuccess(String message);
    void updateFailed(String message);
    void listCustomer(List<User> list);
    void Exception(String message);
}
