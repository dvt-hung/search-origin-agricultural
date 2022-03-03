package com.example.apptxng.api;

import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.User;
import com.example.apptxng.model.ResponsePOST;

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

    // *Send OTP: done
    @FormUrlEncoded
    @POST("send_otp.php")
    Call<String> sendOTP(@Field("code") long code,
                       @Field("email") String email);

    // *Sign Up: done
    @POST("insert_user.php")
    Call<ResponsePOST> signUpUser (@Body User user);

    // *Login: done
    @FormUrlEncoded
    @POST("login.php")
    Call<User> login (@Field("email") String email,
                      @Field("passWord") String passWord);

    // *CATEGORY - ADMIN
        // Add category: Done
        @Multipart
        @POST("add_category.php")
        Call<ResponsePOST> addCategory (
                @Part("nameCategory") RequestBody nameCategory,
                @Part MultipartBody.Part imgCategory
                );

        // Get all category: Done
        @GET("get_category.php")
        Call<List<Category>> getAllCategory();

        // Delete category: Done
        @FormUrlEncoded
        @POST("delete_category.php")
        Call<ResponsePOST> deleteCategory(
                @Field("idCategory") int idCategory,
                @Field("imageCategory") String imageCategory

        );

        // Update category
        @Multipart
        @POST("update_category.php")
        Call<ResponsePOST> updateCategory (
                @Part("idCategory") RequestBody idCategory,
                @Part("imgOld_Category") RequestBody imgOld_Category,
                @Part("nameCategory") RequestBody nameCategory,
                @Part MultipartBody.Part imgCategory
        );


    // *ACCOUNT - ADMIN
        // Get list customer: Done
        @GET("get_list_customer.php")
        Call<List<User>> getListCustomer();

        // Get list farmer: Done
        @GET("get_list_farmer.php")
        Call<List<User>> getListFarmer();

        // Update accept user: Done
        @FormUrlEncoded
        @POST("update_accept_user.php")
        Call<ResponsePOST> updateAcceptUser(
                @Field("idUser") int idUser,
                @Field("accept") int accept
        );

    // *BALANCE - ADMIN
        // Load balance
        @GET("get_balance.php")
        Call<List<Balance>> getBalance();

        // Add balance
        @FormUrlEncoded
        @POST("add_balance.php")
        Call<ResponsePOST> addBalance(@Field("nameBalance") String nameBalance);
}
