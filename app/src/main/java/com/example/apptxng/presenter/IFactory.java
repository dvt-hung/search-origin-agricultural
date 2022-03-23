package com.example.apptxng.presenter;

import com.example.apptxng.model.Factory;

import java.util.List;

public interface IFactory {
    void getFactory(List<Factory> list);
    void Exception(String message);
    void emptyValue();
    void success(String message);
    void failed(String message);
}
