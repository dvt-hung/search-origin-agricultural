package com.example.apptxng.model;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.apptxng.api.API;
import com.example.apptxng.api.Retrofit_Client;

public class Common {
    public static User currentUser;
    public static final String URL = "http://192.168.3.117/txng/";
    public static final API api = Retrofit_Client.getRetrofit(Common.URL).create(API.class);


}
