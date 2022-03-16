package com.example.apptxng.presenter;

import com.example.apptxng.model.History;

import java.util.List;

public interface IHistory {

    void successMessage(String message);
    void failedMessage(String message);
    void exceptionMessage(String message);
    void emptyValue();
    void getHistory(List<History> histories);
}
