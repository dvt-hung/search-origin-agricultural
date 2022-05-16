package com.example.apptxng.presenter;

import android.content.Context;
import android.net.Uri;

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

public class Update_Product_Presenter {

    private final IUpdateProduct iUpdateProduct;
    private final Context context;

    public Update_Product_Presenter(IUpdateProduct iUpdateProduct, Context context) {
        this.iUpdateProduct = iUpdateProduct;
        this.context = context;
    }

    public void getCategory(){
        Common.api.getAllCategory()
                .enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                        iUpdateProduct.getCategory(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                        iUpdateProduct.Exception(t.getMessage());
                    }
                });
    }


    public void getBalance(){
        Common.api.getBalance()
                .enqueue(new Callback<List<Balance>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Balance>> call, @NonNull Response<List<Balance>> response) {
                            iUpdateProduct.getBalance(response.body());
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Balance>> call, @NonNull Throwable t) {
                            iUpdateProduct.Exception(t.getMessage());
                        }
                    });
    }

    public synchronized void updateProduct(Product product, Uri imageNew){
        //Kiểm tra dữ liệu
        if ( product.getBalance().getIdBalance() == 0 || product.getCategory().getIdCategory() == 0||product.getNameProduct() == null || product.getDescriptionProduct() == null || product.getPriceProduct() == 0 || product.getQuantityProduct() == 0)
        {
            iUpdateProduct.emptyValue();
        }

        RequestBody imgOld_Product = null;
        RequestBody requestBodyImage = null;
        MultipartBody.Part requestPartImage = null;
        if (imageNew != null)
        {
            File file = new File(Common.getRealPathFromURI(imageNew,context));

            String filePath = file.getAbsolutePath();
            String[] arraySplitPath = filePath.split("\\.");

            String nameProductNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];

            requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            requestPartImage = MultipartBody.Part.createFormData("imageProduct", nameProductNew,requestBodyImage);

            imgOld_Product = RequestBody.create(MediaType.parse("multipart/form-data"),product.getImageProduct());
        }

        RequestBody nameProduct             = RequestBody.create(MediaType.parse("multipart/form-data"),product.getNameProduct());
        RequestBody priceProduct            = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getPriceProduct()));
        RequestBody descriptionProduct      = RequestBody.create(MediaType.parse("multipart/form-data"),product.getDescriptionProduct());
        RequestBody quantityProduct         = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getQuantityProduct()));
        RequestBody idCategory              = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getCategory().getIdCategory()));
        RequestBody idBalance               = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getBalance().getIdBalance()));
        RequestBody idEmployee               = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getIdEmployee()));
        RequestBody idProduct               = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(product.getIdProduct()));
        RequestBody ingredientProduct       = RequestBody.create(MediaType.parse("multipart/form-data"),valueEmpty(product.getIngredientProduct()));
        RequestBody useProduct              = RequestBody.create(MediaType.parse("multipart/form-data"),valueEmpty(product.getUseProduct()));
        RequestBody guideProduct            = RequestBody.create(MediaType.parse("multipart/form-data"),valueEmpty(product.getGuideProduct()));
        RequestBody conditionProduct        = RequestBody.create(MediaType.parse("multipart/form-data"),valueEmpty(product.getConditionProduct()));


        Common.api.updateProduct(idProduct,nameProduct,priceProduct,descriptionProduct,quantityProduct,imgOld_Product,idCategory,idBalance,idEmployee,
                ingredientProduct,useProduct,guideProduct,conditionProduct,requestPartImage)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();

                        assert responsePOST != null;
                        if (responsePOST.getStatus() == 1)
                        {
                            iUpdateProduct.success(responsePOST.getMessage());
                        }
                        else
                        {
                            iUpdateProduct.failed(responsePOST.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iUpdateProduct.Exception(t.getMessage());
                    }
                });

    }

    private String valueEmpty(String val)
    {
        if (val == null || val.equals(" ") || val.isEmpty())
        {
            return "";
        }
        return val;
    }
}
