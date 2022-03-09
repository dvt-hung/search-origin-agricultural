package com.example.apptxng.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        iInsertProduct.getCategory(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                        iInsertProduct.Exception(t.getMessage());
                    }
                });
    }


    public void getBalance(){
        Common.api.getBalance()
                .enqueue(new Callback<List<Balance>>() {
                        @Override
                        public void onResponse(Call<List<Balance>> call, Response<List<Balance>> response) {
                            iInsertProduct.getBalance(response.body());
                        }

                        @Override
                        public void onFailure(Call<List<Balance>> call, Throwable t) {
                            iInsertProduct.Exception(t.getMessage());
                        }
                    });
    }

    public void insertProduct(Product product){
        //Kiểm tra dữ liệu
        if (product.getIdBalance() == 0 || product.getIdCategory() == 0 || product.getImageProduct() == null)
        {
            iInsertProduct.emptyValue();
        }

        Uri uriImageProduct = Uri.parse(product.getImageProduct());
        File file = new File(Common.getRealPathFromURI(uriImageProduct,context));

        String filePath = file.getAbsolutePath();
        String[] arraySplitPath = filePath.split("\\.");

        String nameProductNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];

        Log.e("p", "insertProduct1: " + product.getNameProduct() +" - " + product.getPriceProduct()  + " - " + product.getDescriptionProduct());
        Log.e("p", "insertProduct2: " + product.getQuantityProduct() +" - " + product.getIdUser()  + " - " + product.getIdCategory());
        Log.e("p", "insertProduct3: " + product.getIdBalance() +" - " + product.getDateProduct() );

        RequestBody nameProduct             = RequestBody.create(MediaType.parse("multipart/form-data"),product.getNameProduct());
        RequestBody priceProduct            = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getPriceProduct()));
        RequestBody descriptionProduct      = RequestBody.create(MediaType.parse("multipart/form-data"),product.getDescriptionProduct());
        RequestBody quantityProduct         = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getQuantityProduct()));
        RequestBody idUser                  = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getIdUser()));
        RequestBody idCategory              = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getIdCategory()));
        RequestBody idBalance               = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getIdBalance()));
        RequestBody dateProduct             = RequestBody.create(MediaType.parse("multipart/form-data"),product.getDateProduct());

        RequestBody requestBodyImage        = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part requestPartImage = MultipartBody.Part.createFormData("imageProduct", nameProductNew,requestBodyImage);



        Common.api.addProduct(nameProduct,priceProduct,descriptionProduct,quantityProduct, idUser, idCategory, idBalance, dateProduct, requestPartImage)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
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
                    }

                    @Override
                    public void onFailure(Call<ResponsePOST> call, Throwable t) {
                        iInsertProduct.Exception(t.getMessage());
                    }
                });


    }
}
