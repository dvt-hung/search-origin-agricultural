package com.example.apptxng.presenter;

import com.example.apptxng.model.Category;

import java.util.List;

public interface ICategoryAdmin {
    void addSuccess(String message);
    void addFailed(String message);
    void addException(String message);
    void getAllCategorySuccess(List<Category> categoryList);
}
