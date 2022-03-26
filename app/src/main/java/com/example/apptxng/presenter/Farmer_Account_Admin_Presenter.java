package com.example.apptxng.presenter;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.model.ResponsePOST;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Farmer_Account_Admin_Presenter {

    private final IFarmerAccountAdmin iFarmerAccountAdmin;

    public Farmer_Account_Admin_Presenter(IFarmerAccountAdmin iFarmerAccountAdmin) {
        this.iFarmerAccountAdmin = iFarmerAccountAdmin;
    }

    // Update accept user
    public void updateAcceptUser(String idUser, int accept){
        Common.api.updateAcceptUser(idUser,accept)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();
                        assert responsePOST != null;
                        if (responsePOST.getStatus() == 1)
                        {
                            iFarmerAccountAdmin.updateSuccess(responsePOST.getMessage());
                        }
                        else
                        {
                            iFarmerAccountAdmin.updateFailed(responsePOST.getMessage());
                        }
                        getListFarmer();
                    }

                    @Override
                    public void onFailure(Call<ResponsePOST> call, Throwable t) {
                            iFarmerAccountAdmin.Exception(t.getMessage());
                    }
                });
    }

    // Get list account farmer
    public void getListFarmer()
    {
        Common.api.getListFarmer()
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        iFarmerAccountAdmin.listFarmer(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        iFarmerAccountAdmin.Exception(t.getMessage());
                    }
                });
    }

}
