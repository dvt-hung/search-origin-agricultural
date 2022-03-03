package com.example.apptxng.presenter;

import android.util.Log;

import com.example.apptxng.api.API;
import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.ResponsePOST;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Balance_Admin_Presenter {

    private final IBalanceAdmin iBalanceAdmin;
    List<Balance> balances = new ArrayList<>();

    public Balance_Admin_Presenter(IBalanceAdmin iBalanceAdmin) {
        this.iBalanceAdmin = iBalanceAdmin;
    }

    public void addBalance(String nameBalance)
    {
        Common.api.addBalance(nameBalance)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();
                        assert responsePOST != null;
                        iBalanceAdmin.addBalanceMessage(responsePOST.getMessage());
                        getBalance();
                    }

                    @Override
                    public void onFailure(Call<ResponsePOST> call, Throwable t) {
                        iBalanceAdmin.Exception(t.getMessage());
                    }
                });
    }

    public void getBalance()
    {
        Common.api.getBalance()
                .enqueue(new Callback<List<Balance>>() {
                    @Override
                    public void onResponse(Call<List<Balance>> call, Response<List<Balance>> response) {
                        balances = response.body();
                        iBalanceAdmin.getBalance(balances);
                    }

                    @Override
                    public void onFailure(Call<List<Balance>> call, Throwable t) {
                        iBalanceAdmin.Exception(t.getMessage());
                    }
                });
    }
}
