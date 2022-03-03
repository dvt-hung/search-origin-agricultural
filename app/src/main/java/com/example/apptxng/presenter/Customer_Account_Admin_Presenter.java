package com.example.apptxng.presenter;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.model.ResponsePOST;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Customer_Account_Admin_Presenter {
    private final ICustomerAccountAdmin iAccountCustomerAdmin;

    public Customer_Account_Admin_Presenter(ICustomerAccountAdmin iAccountCustomerAdmin) {
        this.iAccountCustomerAdmin = iAccountCustomerAdmin;
    }

    public void updateAcceptUser(int idUser, int accept){
        Common.api.updateAcceptUser(idUser,accept)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();
                        assert responsePOST != null;
                        if (responsePOST.getStatus() == 1)
                        {
                            iAccountCustomerAdmin.updateSuccess(responsePOST.getMessage());
                        }
                        else
                        {
                            iAccountCustomerAdmin.updateFailed(responsePOST.getMessage());
                        }
                        getListCustomer();
                    }

                    @Override
                    public void onFailure(Call<ResponsePOST> call, Throwable t) {
                        iAccountCustomerAdmin.Exception(t.getMessage());
                    }
                });
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
