package com.example.apptxng.presenter;

import com.example.apptxng.model.ImageHistory;

import java.util.List;

public interface IImageHistory {
    void getImages(List<ImageHistory> images);
    void exception(String message);
}
