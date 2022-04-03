package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.ImageHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageHistory_Presenter {

    private final IImageHistory iImageHistory;
    private final Context context;


    public ImageHistory_Presenter(IImageHistory iImageHistory, Context context) {
        this.iImageHistory = iImageHistory;
        this.context = context;
    }

    public synchronized void getImageHistory(String idHistory)
    {
        // Progress dialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Vui lòng chờ...");
        progressDialog.show();
       new Thread(new Runnable() {
           @Override
           public void run() {
               Log.e("a", "run: " + idHistory);
                Common.api.getImageHistory(idHistory)
                        .enqueue(new Callback<List<ImageHistory>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<ImageHistory>> call, @NonNull Response<List<ImageHistory>> response) {
                                iImageHistory.getImages(response.body());
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<ImageHistory>> call, @NonNull Throwable t) {
                                iImageHistory.exception(t.getMessage());
                                progressDialog.dismiss();
                            }
                        });
           }
       }).start();
    }
}
