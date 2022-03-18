package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.view.InsertHistoryProductActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Factory_Presenter {

    private final IFactory iFactory;
    private final Context context;

    public Factory_Presenter(IFactory iFactory, Context context) {
        this.iFactory = iFactory;
        this.context = context;

    }

    public void getFactory()
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setMessage("Đang tải dữ liệu...");
        Common.api.getFactory(Common.currentUser.getIdUser())
                .enqueue(new Callback<List<Factory>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Factory>> call, @NonNull Response<List<Factory>> response) {
                        iFactory.getFactory(response.body());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Factory>> call, @NonNull Throwable t) {
                        iFactory.Exception(t.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }
}
