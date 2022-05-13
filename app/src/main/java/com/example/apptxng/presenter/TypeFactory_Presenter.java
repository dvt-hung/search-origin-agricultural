package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.ResponsePOST;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.view.FactoryActivity;
import com.example.apptxng.view.TypeFactory__Activity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeFactory_Presenter {

    private final ITypeFactory iTypeFactory;
    private Context context;

    public TypeFactory_Presenter(ITypeFactory iTypeFactory) {
        this.iTypeFactory = iTypeFactory;
    }

    public TypeFactory_Presenter(ITypeFactory iTypeFactory, Context context) {
        this.iTypeFactory = iTypeFactory;
        this.context = context;
    }

    // Load list linked
    public void getTypeFactory(){
        Common.api.getTypeFactory()
                .enqueue(new Callback<List<TypeFactory>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<TypeFactory>> call, @NonNull Response<List<TypeFactory>> response) {
                        iTypeFactory.getTypeFactory(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<TypeFactory>> call, Throwable t) {
                        iTypeFactory.Exception(t.getMessage());
                    }
                });
    }

    // Add type factory
    public synchronized void addTypeFactory(String nameTypeFactory)
    {
        // Tạo progress dialog
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();
        Common.api.addTypeFactory(nameTypeFactory)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST result = response.body();
                        assert  result != null;
                        if (result.getStatus() == 1)
                        {
                            iTypeFactory.successMessage(result.getMessage());
                            getTypeFactory();
                        }
                        else
                        {
                            iTypeFactory.failedMessage(result.getMessage());
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iTypeFactory.Exception(t.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    // update type factory
    public synchronized void updateTypeFactory(int idTypeFactory,String nameTypeFactory)
    {
        // Tạo progress dialog
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();
        Common.api.updateTypeFactory(idTypeFactory,nameTypeFactory)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST result = response.body();
                        assert  result != null;
                        if (result.getStatus() == 1)
                        {
                            iTypeFactory.successMessage(result.getMessage());
                            getTypeFactory();
                        }
                        else
                        {
                            iTypeFactory.failedMessage(result.getMessage());
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iTypeFactory.Exception(t.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    // update type factory
    public synchronized void deleteTypeFactory(int idTypeFactory)
    {
        // Tạo progress dialog
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();
        Common.api.deleteTypeFactory(idTypeFactory)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST result = response.body();
                        assert  result != null;
                        if (result.getStatus() == 1)
                        {
                            iTypeFactory.successMessage(result.getMessage());
                            getTypeFactory();
                        }
                        else
                        {
                            iTypeFactory.failedMessage(result.getMessage());
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iTypeFactory.Exception(t.getMessage());
                        dialog.dismiss();
                    }
                });
    }

}
