package com.example.apptxng.api;

import com.example.apptxng.model.Category;
import com.example.apptxng.model.User;
import com.example.apptxng.model.responsePOST;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    // Add category
    @Multipart
    @POST("add_category.php")
    Call<responsePOST> addCategory (
            @Part("nameCategory") RequestBody nameCategory,
            @Part MultipartBody.Part imgCategory
            );

    // Get all category
    @GET("get_category.php")
    Call<List<Category>> getAllCategory();

    // Delete category
    @FormUrlEncoded
    @POST("delete_category.php")
    Call<responsePOST> deleteCategory(
            @Field("idCategory") int idCategory
    );

    // Update category
    @Multipart
    @POST("update_category.php")
    Call<responsePOST> updateCategory (
            @Part("idCategory") RequestBody idCategory,
            @Part("imgOld_Category") RequestBody imgOld_Category,
            @Part("nameCategory") RequestBody nameCategory,
            @Part MultipartBody.Part imgCategory
    );
}
