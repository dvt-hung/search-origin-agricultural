package com.example.apptxng.api;

import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.TypeFactory;
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
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    // *Change Password: done
    @FormUrlEncoded
    @POST("change_password.php")
    Call<ResponsePOST> change_password (
                    @Field("email") String email,
                    @Field("passNew") String passNew,
                    @Field("idUser") int idUser);


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

        // Update balance
        @FormUrlEncoded
        @POST("update_balance.php")
        Call<ResponsePOST> updateBalance(
                @Field("idBalance") int idBalance,
                @Field("nameBalance") String nameBalance);

        // Delete balance
        @FormUrlEncoded
        @POST("delete_balance.php")
        Call<ResponsePOST> deleteBalance(@Field("idBalance") int idBalance);


    // *Factory - ADMIN
        // Load linked
        @GET("get_type_factory.php")
        Call<List<TypeFactory>> getTypeFactory();

    // ========= FARMER =============

    // *Product

        // Get Product
        @GET("test.php")
        Call<List<Product>> getProducts(@Query("idUser") int idUser);

        // Insert Product
        @Multipart
        @POST("add_product.php")
        Call<ResponsePOST> addProduct (
                @Part("nameProduct")        RequestBody nameProduct,
                @Part("priceProduct")       RequestBody priceProduct,
                @Part("descriptionProduct") RequestBody descriptionProduct,
                @Part("quantityProduct")    RequestBody quantityProduct,
                @Part("idUser")             RequestBody idUser,
                @Part("idCategory")         RequestBody idCategory,
                @Part("idBalance")          RequestBody idBalance,
                @Part("dateProduct")        RequestBody dateProduct,
                @Part MultipartBody.Part                imageProduct
        );
}
