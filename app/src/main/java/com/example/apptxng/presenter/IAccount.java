package com.example.apptxng.presenter;

import com.example.apptxng.model.User;

import java.util.List;

public interface IAccount {
    void listAccount(List<User> managerAccounts);
    void exception(String message);
    void successMessage(String message);
    void failedMessage(String message);
    void inCorrectPassOld();
    void inCorrectPassLength();
    void inCorrectPassConfirm();
    void emptyValue();

}
