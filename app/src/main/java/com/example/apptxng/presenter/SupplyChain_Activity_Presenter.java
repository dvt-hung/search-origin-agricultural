package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.SupplyChain;
import com.example.apptxng.view.SupplyChainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplyChain_Activity_Presenter {

    private final ISupplyChainActivity iSupplyChainActivity;
    private final Context context;

    public SupplyChain_Activity_Presenter(ISupplyChainActivity iSupplyChainActivity, Context context) {
        this.iSupplyChainActivity = iSupplyChainActivity;
        this.context = context;
    }

    public void getSupplyChains(String idProductTemp)
    {
        // Progress dialog
        ProgressDialog progressDialog = Common.createProgress(context);
        progressDialog.show();

        // Gọi API
        Common.api.getSupplyChain(idProductTemp)
                .enqueue(new Callback<List<SupplyChain>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<SupplyChain>> call, @NonNull Response<List<SupplyChain>> response) {
                        assert response.body() != null;
                        iSupplyChainActivity.supplyChains(response.body());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<SupplyChain>> call, @NonNull Throwable t) {
                        iSupplyChainActivity.exception(t.getMessage());
                        progressDialog.dismiss();
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
                        iSupplyChainActivity.infoFactory(response.body());

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Factory> call, @NonNull Throwable t) {
                        iSupplyChainActivity.exception(t.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }
}
