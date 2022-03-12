package com.example.apptxng.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Product_Farmer_Presenter {

    private final IProductFarmer iProductFarmer;

    public Product_Farmer_Presenter(IProductFarmer iProductFarmer) {
        this.iProductFarmer = iProductFarmer;
    }

    public void getProducts()
    {
        Log.e("a", "getProducts: " + Common.currentUser.getIdUser() );
        Common.api.getProducts(Common.currentUser.getIdUser())
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                       iProductFarmer.getProduct(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        iProductFarmer.Exception(t.getMessage());
                    }
                });
    }
}
