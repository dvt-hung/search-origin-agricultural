package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Product_Presenter {
    private final IProduct iProduct;
    private final Context context;


    public Product_Presenter(IProduct iProduct, Context context) {
        this.iProduct = iProduct;
        this.context = context;
    }

    public synchronized void getProductFarmer()
    {
        // Progress dialog
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();

        Common.api.getProducts(Common.currentUser.getIdUser())
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        iProduct.getProducts(response.body());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        iProduct.Exception(t.getMessage());
                        dialog.dismiss();

                    }
                });
    }

    public synchronized void getProductManager()
    {
        // Progress dialog
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();

        Common.api.getProductManager(Common.currentUser.getIdUser())
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        iProduct.getProducts(response.body());
                        dialog.dismiss();

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        iProduct.Exception(t.getMessage());
                        dialog.dismiss();

                    }
                });

    }
}
