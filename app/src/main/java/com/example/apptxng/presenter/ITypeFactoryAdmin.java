package com.example.apptxng.presenter;

import com.example.apptxng.model.TypeFactory;

import java.util.List;

public interface ITypeFactoryAdmin {
    void getLinked(List<TypeFactory> list);
    void Exception(String message);
}
