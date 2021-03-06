package com.example.apptxng.api;

import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Banner;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.History;
import com.example.apptxng.model.ImageHistory;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.SupplyChain;
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
    @FormUrlEncoded
    @POST("insert_user.php")
    Call<ResponsePOST> signUpUser (
            @Field("phone") String phone,
            @Field("idUser") String idUser,
            @Field("name") String name,
            @Field("passWord") String passWord,
            @Field("accept") int accept,
            @Field("idRole") int idRole,
            @Field("idTypeFactory") int idTypeFactory,
            @Field("nameFactory") String nameFactory,
            @Field("idOwner") String idOwner
            );

    // *Login: done
    @FormUrlEncoded
    @POST("login.php")
    Call<User> login (@Field("phone") String phone,
                      @Field("passWord") String passWord);


    // *Change Password: done
    @FormUrlEncoded
    @POST("change_password.php")
    Call<ResponsePOST> change_password (
                    @Field("passNew") String passNew,
                    @Field("idUser") String idUser);

    // ************** BANNER ****************
    // *Get list banner
    @GET("get_banner.php")
    Call<List<Banner>> getBanner();

    // *Insert banner
    @Multipart
    @POST("insert_banner.php")
    Call<ResponsePOST> insertBanner (
            @Part List<MultipartBody.Part>          imageHistory
    );

    // *Delete banner
    @FormUrlEncoded
    @POST("delete_banner.php")
    Call<ResponsePOST> deleteBanner (
            @Field("idBanner") int idBanner,
            @Field("image_Banner") String image_Banner);

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

        // Get list manager: Done
        @GET("get_list_manager.php")
        Call<List<User>> getListManager();

        // Get list employee: Done
        @GET("get_list_employee.php")
        Call<List<User>> getListEmployee(@Query("idOwner") String idOwner);

        // Get info user by idUser: Done
        @GET("get_info_user.php")
        Call<User> getInfoUser(@Query("idUser") String idUser);

    // Update accept user: Done
        @FormUrlEncoded
        @POST("update_accept_user.php")
        Call<ResponsePOST> updateAcceptUser(
                @Field("idUser") String idUser,
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

        // Add Type Factory
        @FormUrlEncoded
        @POST("add_type_factory.php")
        Call<ResponsePOST> addTypeFactory(@Field("nameTypeFactory") String nameTypeFactory);

        // Update Type Factory
        @FormUrlEncoded
        @POST("update_type_factory.php")
        Call<ResponsePOST> updateTypeFactory(
                @Field("idTypeFactory")   int idTypeFactory,
                @Field("nameTypeFactory") String nameTypeFactory);

        // Delete type factory
        @FormUrlEncoded
        @POST("delete_type_factory.php")
        Call<ResponsePOST> deleteTypeFactory(@Field("idTypeFactory") int idTypeFactory);


    // ========= FARMER =============

    // * Info
        // Change Info
        @Multipart
        @POST("update_info.php")
        Call<ResponsePOST> changeInfo (
                @Part("idUser")             RequestBody idUser,
                @Part("name")               RequestBody name,
                @Part("address")            RequestBody address,
                @Part("email")              RequestBody email,
                @Part("imageOld")           RequestBody imageOld,
                @Part MultipartBody.Part    imageNew
        );

    // ******** Product ***********
        // Get Product
        @GET("get_products.php")
        Call<List<Product>> getProducts(@Query("idUser") String idUser); // Farmer

        @GET("get_product.php")
        Call<Product> getProductByIdProduct(@Query("idProduct") String idProduct); // Scan

        @GET("get_products_manager.php")
        Call<List<Product>> getProductManager(@Query("idUser") String idUser); // Manager

        @GET("get_products_all.php")
        Call<List<Product>> getProductAll(); // Admin

        @GET("get_products_hot.php")
        Call<List<Product>> getProductHot(); // Customer

        @GET("get_products_by_category.php")
        Call<List<Product>> getProductByCategory(@Query("idCategory") int idCategory); // Customer

        @GET("get_product_by_employee.php")
        Call<List<Product>> getProductByEmployee(@Query("idEmployee") String idEmployee); // Employee
    // Insert Product
        @Multipart
        @POST("add_product.php")
        Call<ResponsePOST> addProduct (
                @Part("idProduct")          RequestBody idProduct,
                @Part("nameProduct")        RequestBody nameProduct,
                @Part("priceProduct")       RequestBody priceProduct,
                @Part("descriptionProduct") RequestBody descriptionProduct,
                @Part("quantityProduct")    RequestBody quantityProduct,
                @Part("idUser")             RequestBody idUser,
                @Part("idCategory")         RequestBody idCategory,
                @Part("idBalance")          RequestBody idBalance,
                @Part("idEmployee")         RequestBody idEmployee,
                @Part("dateProduct")        RequestBody dateProduct,
                @Part("ingredientProduct")  RequestBody ingredientProduct,
                @Part("useProduct")         RequestBody useProduct,
                @Part("guideProduct")       RequestBody guideProduct,
                @Part("conditionProduct")   RequestBody conditionProduct,
                @Part MultipartBody.Part                imageProduct,
                @Part("idHistory")              RequestBody idHistory,
                @Part("idFactory")              RequestBody idFactory,
                @Part("descriptionHistory")   RequestBody descriptionHistory
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
                @Part("idEmployee")         RequestBody idEmployee,
                @Part("ingredientProduct")  RequestBody ingredientProduct,
                @Part("useProduct")         RequestBody useProduct,
                @Part("guideProduct")       RequestBody guideProduct,
                @Part("conditionProduct")   RequestBody conditionProduct,
                @Part MultipartBody.Part                imageProduct
        );

        // Delete Product
        @FormUrlEncoded
        @POST("delete_product.php")
        Call<ResponsePOST> deleteProduct(
                @Field("idProduct") String idProduct,
                @Field("imgOld_Product") String imgOld_Product);

        // Insert QR Code
        @Multipart
        @POST("add_qr_product.php")
        Call<ResponsePOST> addQRCode(
                @Part("idProduct")          RequestBody idProduct,
                @Part MultipartBody.Part    qrCode
        );
    //  ******* FACTORY **********
        // Get factory
        @GET("get_factory.php")
        Call<List<Factory>> getFactory();

        @GET("get_factory_filter.php")
        Call<List<Factory>> getFactoryFilter();
        // Get info factory by idUser
        @GET("get_info_factory.php")
        Call<Factory> getFactoryByID(@Query("idUser") String idUser);

        // Insert factory
        @FormUrlEncoded
        @POST("insert_factory.php")
        Call<ResponsePOST> insertFactory(
                @Field("idTypeFactory")     int idTypeFactory,
                @Field("nameFactory")       String nameFactory,
                @Field("addressFactory")    String addressFactory,
                @Field("phoneFactory")      String phoneFactory,
                @Field("idUser")            String idUser
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

        // Update info factory
        @FormUrlEncoded
        @POST("update_info_factory.php")
        Call<ResponsePOST> updateInfoFactory(
                @Field("idFactory")         int idFactory,
                @Field("nameFactory")       String nameFactory,
                @Field("addressFactory")    String addressFactory,
                @Field("phoneFactory")      String phoneFactory,
                @Field("ownerFactory")      String ownerFactory,
                @Field("webFactory")        String webFactory
        );

    // ******** HISTORY **********

    // Insert history
    @Multipart
    @POST("insert_history.php")
    Call<ResponsePOST> insertHistory (
            @Part("idHistory")                      RequestBody idHistory,
            @Part("idProduct")                      RequestBody idProduct,
            @Part("idFactory")                      RequestBody idFactory,
            @Part("idUser")                         RequestBody idUser,
            @Part("idOwnerNew")                     RequestBody idOwnerNew,
            @Part("idAuthor")                       RequestBody idAuthor,
            @Part("changeFactory")                  RequestBody changeFactory,
            @Part("descriptionHistory")             RequestBody descriptionHistory,
            @Part("dateHistory")                    RequestBody dateHistory,
            @Part("idFactoryReceive")               RequestBody idFactoryReceive,
            @Part("idTypeFactory")                  RequestBody idTypeFactory,
            @Part List<MultipartBody.Part>          imageHistory
    );

    // Get history
    @GET("get_history.php")
    Call<List<History>> getHistory(@Query("idProduct") String idProduct);

    // Delete history: Ch???nh s???a l???i
    @FormUrlEncoded
    @POST("delete_history.php")
    Call<ResponsePOST> deleteHistory(
            @Field("idHistory") String idHistory,
            @Field("imageHistory") String imageHistory
    );


    @POST("delete_history.php")
    Call<ResponsePOST> deleteHistory(
            @Body List<ImageHistory>    list);


    // Update history
    @Multipart
    @POST("update_history.php")
    Call<ResponsePOST> updateHistory (
            @Part("idHistory")                      RequestBody idHistory,
            @Part("idFactory")                      RequestBody idFactory,
            @Part("descriptionHistory")             RequestBody descriptionHistory,
            @Part("imageHistoryOld")                RequestBody imageHistoryOld,
            @Part MultipartBody.Part                imageHistoryNew
    );

    // Update description history
    @FormUrlEncoded
    @POST("update_des_history.php")
    Call<ResponsePOST> updateDesHistory(
            @Field("idHistory")             String idHistory,
            @Field("descriptionHistory")    String descriptionHistory
    );

    // ******** IMAGE HISTORY **********
    // Get image history
        @GET("get_image_history.php")
        Call<List<ImageHistory>> getImageHistory(@Query("idHistory") String idHistory);

    // Delete image history
    @FormUrlEncoded
    @POST("delete_image_history.php")
    Call<ResponsePOST> deleteImageHistory(
            @Field("idImageHistory")         int idImageHistory,
            @Field("imageHistory")           String imageHistory);

    // Insert image history
    @Multipart
    @POST("insert_image_history.php")
    Call<ResponsePOST> insertImageHistory (
            @Part("idHistory")                      RequestBody idHistory,
            @Part List<MultipartBody.Part>          imageHistory
    );

    // ********** SUPPLY CHAIN **********
    // Get supply chain: l???y chu???i cung ???ng c???a s???n ph???m
        @GET("get_supply_chain.php")
        Call<List<SupplyChain>> getSupplyChain(@Query("idProduct") String idProduct);


        // ********** STATISTIC PRODUCTS **********
    // L???Y RA DANH S??CH S???N PH???M ???? ???????C CHUY???N ??I THEO IDFACTORY
    @GET("get_product_changed.php")
    Call<List<Product>> getProductChanged(@Query("idUser") String idUser);

    // L???Y RA DANH S??CH S???N PH???M THEO NG??Y ???? CH???N C???A C?? S??? KH??C
    @GET("get_product_by_date_manager.php")
    Call<List<Product>> getProductByDateManager(
            @Query("idUser") String idUser,
            @Query("dateProduct") String dateProduct);

    // L???Y RA DANH S??CH S???N PH???M THEO NG??Y ???? CH???N C???A N??NG D??N
    @GET("get_product_by_date_farmer.php")
    Call<List<Product>> getProductByDateFarmer(
            @Query("idFactory")   int idFactory,
            @Query("dateProduct") String dateProduct);
}
