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

    public Balance_Admin_Presenter(IBalanceAdmin iBalanceAdmin) {
        this.iBalanceAdmin = iBalanceAdmin;
    }

    // 1. Lấy tất cả các đơn vị tính hiển tại
    public void getBalance()
    {
        Common.api.getBalance()
                .enqueue(new Callback<List<Balance>>() {
                    @Override
                    public void onResponse(Call<List<Balance>> call, Response<List<Balance>> response) {
                        iBalanceAdmin.getBalance(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Balance>> call, Throwable t) {
                        iBalanceAdmin.Exception(t.getMessage());
                    }
                });
    }

    // 2. Add Balance: Truyền tên của đơn vị tính vào để thêm đơn vị mới
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

    // 3. Update Balance: Truyền tên mới và id của đơn vị vào để sửa đổi
    public void updateBalance(Balance balance)
    {
        Common.api.updateBalance(balance.getIdBalance(), balance.getNameBalance())
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();
                        assert responsePOST != null;
                        iBalanceAdmin.updateBalanceMessage(responsePOST.getMessage());
                        getBalance();
                    }

                    @Override
                    public void onFailure(Call<ResponsePOST> call, Throwable t) {
                        iBalanceAdmin.Exception(t.getMessage());
                    }
                });
    }

    // 4. Delete Balance: Truyền id của Balance vào để xóa
    public void deleteBalance(Balance balance)
    {
        Common.api.deleteBalance(balance.getIdBalance())
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();
                        assert responsePOST != null;
                        iBalanceAdmin.deleteBalanceMessage(responsePOST.getMessage());
                        getBalance();
                    }

                    @Override
                    public void onFailure(Call<ResponsePOST> call, Throwable t) {
                        iBalanceAdmin.Exception(t.getMessage());
                    }
                });
    }
}
