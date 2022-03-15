package com.example.apptxng.presenter;

public interface IHistory {

    void successMessage(String message);
    void failedMessage(String message);
    void exceptionMessage(String message);
    void emptyValue();
}
