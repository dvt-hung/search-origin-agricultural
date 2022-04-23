package com.example.apptxng.presenter;

import com.example.apptxng.model.Banner;

import java.util.List;

public interface IBanner {
    void getBanner(List<Banner> bannerList);
    void exception(String message);
    void emptyValue();
    void successMessage(String message);
    void failedMessage(String message);
}
