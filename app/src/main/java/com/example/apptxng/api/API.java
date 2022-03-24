package com.example.apptxng.api;

import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.History;
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


    // * Reload
    @GET("reload_info.php")
    Call<User> reloadInfo (@Query("idUser") int idUser);

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

    // * Info
        // Change Info
        @Multipart
        @POST("update_info.php")
        Call<ResponsePOST> changeInfo (
                @Part("idUser")             RequestBody idUser,
                @Part("name")               RequestBody name,
                @Part("phone")              RequestBody phone,
                @Part("nameFarm")           RequestBody nameFarm,
                @Part("address")            RequestBody address,
                @Part("imageOld")           RequestBody imageOld,
                @Part MultipartBody.Part    imageNew
        );
    // *Product

        // Get Product
        @GET("get_products.php")
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

        // Update Product
        @Multipart
        @POST("update_product.php")
        Call<ResponsePOST> updateProduct (
                @Part("idProduct")          RequestBody idProduct,
                @Part("nameProduct")        RequestBody nameProduct,
                @Part("priceProduct")       RequestBody priceProduct,
                @Part("descriptionProduct") RequestBody descriptionProduct,
                @Part("quantityProduct")    RequestBody quantityProduct,
                @Part("imgOld_Product")     RequestBody imgOld_Product,
                @Part("idCategory")         RequestBody idCategory,
                @Part("idBalance")          RequestBody idBalance,
                @Part MultipartBody.Part                imageProduct
        );

        // Delete Product
        @FormUrlEncoded
        @POST("delete_product.php")
        Call<ResponsePOST> deleteProduct(
                @Field("idProduct") int idProduct,
                @Field("imgOld_Product") String imgOld_Product);
    //  ******* FACTORY **********
        // Get factory by idUser
        @GET("get_factory.php")
        Call<List<Factory>> getFactory(@Query("idUser") int idUser);

        // Insert factory
        @FormUrlEncoded
        @POST("insert_factory.php")
        Call<ResponsePOST> insertFactory(
                @Field("idTypeFactory")     int idTypeFactory,
                @Field("nameFactory")       String nameFactory,
                @Field("addressFactory")    String addressFactory,
                @Field("phoneFactory")      String phoneFactory,
                @Field("idUser")            int idUser
        );

        // Delete factory
        @FormUrlEncoded
        @POST("delete_factory.php")
        Call<ResponsePOST> deleteFactory(
                @Field("idFactory")     int idFactory
        );

    // Update factory
        @FormUrlEncoded
        @POST("update_factory.php")
        Call<ResponsePOST> updateFactory(
                @Field("idFactory")         int idFactory,
                @Field("nameFactory")       String nameFactory,
                @Field("addressFactory")    String addressFactory,
                @Field("phoneFactory")      String phoneFactory,
                @Field("idTypeFactory")     int idTypeFactory
        );

    // ******** HISTORY **********

    // Insert history
    @Multipart
    @POST("insert_history.php")
    Call<ResponsePOST> insertHistory (
            @Part("idProduct")                      RequestBody idProduct,
            @Part("idFactory")                      RequestBody idFactory,
            @Part("descriptionHistory")             RequestBody descriptionHistory,
            @Part("dateHistory")                    RequestBody dateHistory,
            @Part MultipartBody.Part                imageHistory
    );

    // Get history
    @GET("get_history.php")
    Call<List<History>> getHistory(@Query("idProduct") int idProduct);

    // Delete history
    @FormUrlEncoded
    @POST("delete_history.php")
    Call<ResponsePOST> deleteHistory(
            @Field("idHistory") int idHistory,
            @Field("imageHistory") String imageHistory
    );

    // Insert history
    @Multipart
    @POST("update_history.php")
    Call<ResponsePOST> updateHistory (
            @Part("idHistory")                      RequestBody idHistory,
            @Part("idFactory")                      RequestBody idFactory,
            @Part("descriptionHistory")             RequestBody descriptionHistory,
            @Part("imageHistoryOld")                RequestBody imageHistoryOld,
            @Part MultipartBody.Part                imageHistoryNew
    );

}
