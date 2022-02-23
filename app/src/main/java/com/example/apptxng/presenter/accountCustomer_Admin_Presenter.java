package com.example.apptxng.presenter;

import android.util.Log;

import com.example.apptxng.api.API;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class accountCustomer_Admin_Presenter {
    private final IAccountCustomerAdmin iAccountCustomerAdmin;

    public accountCustomer_Admin_Presenter(IAccountCustomerAdmin iAccountCustomerAdmin) {
        this.iAccountCustomerAdmin = iAccountCustomerAdmin;
    }

    public void getListCustomer(){
        Common.api.getListCustomer()
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        iAccountCustomerAdmin.listCustomer(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        iAccountCustomerAdmin.Exception(t.getMessage());
                    }
                });
    };
}
