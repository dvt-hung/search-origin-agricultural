package com.example.apptxng.api;

import com.example.apptxng.model.User;
import com.example.apptxng.model.responsePOST;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    // Send OTP
    @FormUrlEncoded
    @POST("send_otp.php")
    Call<String> sendOTP(@Field("code") long code,
                       @Field("email") String email);

    //Sign Up
    @POST("insertUser.php")
    Call<responsePOST> signUpUser (@Body User user);

    // Login
    @FormUrlEncoded
    @POST("login.php")
    Call<User> login (@Field("email") String email,
                      @Field("passWord") String passWord);
}
