package com.example.apptxng.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_Client {

    private static Retrofit retrofitClient = null;

    public static Retrofit getRetrofit(String URL)
    {
        if (retrofitClient == null)
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofitClient = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return  retrofitClient;
    }

}
