package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.History;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Histories_Activity_Presenter {
    private final IHistoriesActivity iHistoriesActivity;
    private final Context context;

    public Histories_Activity_Presenter(IHistoriesActivity iHistoriesActivity, Context context) {
        this.iHistoriesActivity = iHistoriesActivity;
        this.context = context;
    }



    // Tải danh sách sản phẩm
    public synchronized void loadHistory(String idProduct)
    {
        //Tạo progress dialog
        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Vui lòng đợi...");
        progress.show();

        // Gọi api
        Common.api.getHistory(idProduct)
                .enqueue(new Callback<List<History>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<History>> call, @NonNull Response<List<History>> response) {
                        iHistoriesActivity.getHistory(response.body());
                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<History>> call, @NonNull Throwable t) {
                        iHistoriesActivity.exceptionMessage(t.getMessage());
                        progress.dismiss();
                    }
                });
    }

    // ***** LẤY RA INFO FACTORY *****
    public void getInfoFactory(String idUser){
        // Create Progress Dialog
        ProgressDialog progressDialog = Common.createProgress(context);
        progressDialog.show();

        // Call API
        Common.api.getFactoryByID(idUser)
                .enqueue(new Callback<Factory>() {
                    @Override
                    public void onResponse(@NonNull Call<Factory> call, @NonNull Response<Factory> response) {
                        iHistoriesActivity.infoFactory(response.body());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Factory> call, @NonNull Throwable t) {
                        iHistoriesActivity.exceptionMessage(t.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }

}
