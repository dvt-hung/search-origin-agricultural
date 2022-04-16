package com.example.apptxng.presenter;

import com.example.apptxng.model.User;

import java.util.List;

public interface IAccount {
    void listManagerAccount(List<User> managerAccounts);
    void exception(String message);
    void successMessage(String message);
    void failedMessage(String message);
}
