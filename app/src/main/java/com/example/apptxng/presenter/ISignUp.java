package com.example.apptxng.presenter;

import com.example.apptxng.model.responsePOST;

public interface ISignUp {

    void emailError();
    void errorLengthPassword();
    void incorrectPassword();
    void incorrectCode();
    void isSuccess();
    void isFailed(String message);
    void sendOTPSuccess();
    void sendOTPFailed();
}
