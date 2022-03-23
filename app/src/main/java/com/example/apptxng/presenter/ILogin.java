package com.example.apptxng.presenter;

import com.example.apptxng.model.User;

public interface ILogin {

    void emptyValueLogin();
    void loginSuccess(User user);
    void loginFailed(String message);
    void exception(String message);
}
