package com.example.apptxng.presenter;

import com.example.apptxng.model.Factory;
import com.example.apptxng.model.History;

import java.util.List;

public interface IHistoriesActivity {
    void exceptionMessage(String message);

    void getHistory(List<History> histories);

    void infoFactory(Factory factory);
}
