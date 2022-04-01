package com.example.apptxng.presenter;

public interface ISignUp {

    void emailError();
    void emptyValue();
    void errorLengthPassword();
    void incorrectPassword();
    void incorrectCode();
    void isSuccess();
    void isFailed(String message);
    void sendOTPSuccess();
    void sendOTPFailed();
}
