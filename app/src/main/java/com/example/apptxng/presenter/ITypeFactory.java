package com.example.apptxng.presenter;

import com.example.apptxng.model.TypeFactory;

import java.util.List;

public interface ITypeFactory {
    void getTypeFactory(List<TypeFactory> list);
    void Exception(String message);
    void successMessage(String message);
    void failedMessage(String message);
}
