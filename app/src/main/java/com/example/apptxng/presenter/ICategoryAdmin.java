package com.example.apptxng.presenter;

import com.example.apptxng.model.Category;

import java.util.List;

public interface ICategoryAdmin {
    void addSuccess(String message);
    void deleteSuccess(String message);
    void addFailed(String message);
    void deleteFailed(String message);
    void updateFailed(String message);
    void updateSuccess(String message);

    void Exception(String message);

    void getAllCategorySuccess(List<Category> categoryList);
}
