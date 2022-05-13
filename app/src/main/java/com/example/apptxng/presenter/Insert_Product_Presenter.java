package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.FileUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.ResponsePOST;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Insert_Product_Presenter {

    private final IInsertProduct iInsertProduct;
    private final Context context;

    public Insert_Product_Presenter(IInsertProduct iInsertProduct, Context context) {
        this.iInsertProduct = iInsertProduct;
        this.context = context;
    }

    public void getCategory(){
        Common.api.getAllCategory()
                .enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                        iInsertProduct.getCategory(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                        iInsertProduct.Exception(t.getMessage());
                    }
                });
    }


    public void getBalance(){
        Common.api.getBalance()
                .enqueue(new Callback<List<Balance>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Balance>> call, @NonNull Response<List<Balance>> response) {
                            iInsertProduct.getBalance(response.body());
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Balance>> call, @NonNull Throwable t) {
                            iInsertProduct.Exception(t.getMessage());
                        }
                    });
    }

    public synchronized void insertProduct(Product product){
        //Kiểm tra dữ liệu
        if (product.getBalance().getIdBalance() == 0 || product.getCategory().getIdCategory() == 0 || product.getImageProduct() == null)
        {
            iInsertProduct.emptyValue();
        }

        Uri uriImageProduct = Uri.parse(product.getImageProduct());

        File file = new File(Common.getRealPathFromURI(uriImageProduct,context));
        String filePath = file.getAbsolutePath();

        String[] arraySplitPath = filePath.split("\\.");

        String nameProductNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];

        RequestBody idProduct               = RequestBody.create(MediaType.parse("multipart/form-data"),product.getIdProduct());
        RequestBody nameProduct             = RequestBody.create(MediaType.parse("multipart/form-data"),product.getNameProduct());
        RequestBody priceProduct            = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getPriceProduct()));
        RequestBody descriptionProduct      = RequestBody.create(MediaType.parse("multipart/form-data"),product.getDescriptionProduct());
        RequestBody quantityProduct         = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getQuantityProduct()));
        RequestBody idUser                  = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getIdUser()));
        RequestBody idCategory              = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getCategory().getIdCategory()));
        RequestBody idBalance               = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getBalance().getIdBalance()));
        RequestBody dateProduct             = RequestBody.create(MediaType.parse("multipart/form-data"),product.getDateProduct());
        RequestBody ingredientProduct       = RequestBody.create(MediaType.parse("multipart/form-data"),valueEmpty(product.getIngredientProduct()));
        RequestBody useProduct              = RequestBody.create(MediaType.parse("multipart/form-data"),valueEmpty(product.getUseProduct()));
        RequestBody guideProduct            = RequestBody.create(MediaType.parse("multipart/form-data"),valueEmpty(product.getGuideProduct()));
        RequestBody conditionProduct        = RequestBody.create(MediaType.parse("multipart/form-data"),valueEmpty(product.getConditionProduct()));

        RequestBody requestBodyImage        = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part requestPartImage = MultipartBody.Part.createFormData("imageProduct", nameProductNew,requestBodyImage);

        // Create Progress
        ProgressDialog progressDialog = Common.createProgress(context);
        progressDialog.show();

        // Call API
        Common.api.addProduct(idProduct,nameProduct,priceProduct,descriptionProduct,quantityProduct, idUser,
                idCategory, idBalance, dateProduct,ingredientProduct, useProduct, guideProduct, conditionProduct, requestPartImage)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();

                        assert responsePOST != null;
                        if (responsePOST.getStatus() == 1)
                        {
                            iInsertProduct.addProductSuccess(responsePOST.getMessage());
                        }
                        else
                        {
                            iInsertProduct.addProductFailed(responsePOST.getMessage());
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iInsertProduct.Exception(t.getMessage());
                        progressDialog.dismiss();
                    }
                });

    }

    //Kiểm tra dữ liệu có rỗng hay không
    private String valueEmpty(String val)
    {
        if (val == null || val.equals(" ") || val.isEmpty())
        {
            return "";
        }
        return val;
    }
}
