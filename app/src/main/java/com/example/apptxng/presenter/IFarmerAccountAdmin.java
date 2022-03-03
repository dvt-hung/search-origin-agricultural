package com.example.apptxng.presenter;

import com.example.apptxng.model.User;

import java.util.List;

public interface IFarmerAccountAdmin {
    void updateSuccess(String message);
    void updateFailed(String message);
    void listFarmer(List<User> list);
    void Exception(String message);
}
