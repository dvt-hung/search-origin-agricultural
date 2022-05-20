package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Statistic_Activity_Presenter {
    private final IStatistic iStatistic;
    private final Context context;

    public Statistic_Activity_Presenter(IStatistic iStatistic, Context context) {
        this.iStatistic = iStatistic;
        this.context = context;
    }

    // ***** LẤY RA INFO FACTORY *****
    public void getInfoFactory(){
        // Create Progress Dialog
        ProgressDialog progressDialog = Common.createProgress(context);
        progressDialog.show();

        // Call API
        Common.api.getFactoryByID(Common.currentUser.getIdUser())
                .enqueue(new Callback<Factory>() {
                    @Override
                    public void onResponse(@NonNull Call<Factory> call, @NonNull Response<Factory> response) {
                        iStatistic.infoFactory(response.body());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Factory> call, @NonNull Throwable t) {
                        iStatistic.exception(t.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }


    // ***** LẤY RA CÁC SẢN PHẨM ĐÃ ĐƯỢC CHUYỂN ĐI *****
    public void getProductChanged(int idFactory){
        // Create Progress Dialog
        ProgressDialog progressDialog = Common.createProgress(context);
        progressDialog.show();

        // Call API
        Common.api.getProductChanged(idFactory)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        iStatistic.listProducts(response.body());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        iStatistic.exception(t.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }


    // ***** LẤY RA CÁC SẢN PHẨM THEO NHÂN VIÊN ĐANG QUẢN LÝ *****
    public synchronized void getProductEmployee(String idEmployee) {
        // Create Progress Dialog
        ProgressDialog progressDialog = Common.createProgress(context);
        progressDialog.show();

        // Call API
        Common.api.getProductByEmployee(idEmployee)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        iStatistic.listProducts(response.body());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        iStatistic.exception(t.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }

    // ***** LẤY RA DANH SÁCH NHÂN VIÊN *****
    public synchronized void getListEmployeeAccount(String idOwner) {
        // Create Progress Dialog
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();

        // Call API
        Common.api.getListEmployee(idOwner)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                        iStatistic.listAccount(response.body());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                        iStatistic.exception(t.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    // ***** LẤY RA DANH SÁCH SẢN PHẨM THEO NGÀY CỦA QUẢN LÝ CƠ SỞ *****
    public synchronized void getProductByDateManager(int idFactory, String dateProduct) {
        // Create Progress Dialog
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();

        // Call API
        Common.api.getProductByDateManager(idFactory,dateProduct)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        iStatistic.listProducts(response.body());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        iStatistic.exception(t.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    // ***** LẤY RA DANH SÁCH SẢN PHẨM THEO NGÀY CỦA NÔNG DÂN *****
    public synchronized void getProductByDateFarmer(String idUser, String dateProduct) {
        // Create Progress Dialog
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();

        // Call API
        Common.api.getProductByDateFarmer(idUser,dateProduct)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        iStatistic.listProducts(response.body());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        iStatistic.exception(t.getMessage());
                        dialog.dismiss();
                    }
                });
    }
}
