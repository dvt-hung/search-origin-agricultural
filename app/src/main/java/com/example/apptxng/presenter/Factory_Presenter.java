package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.ResponsePOST;
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

    public void insertFactory(Factory factory)
    {
        // Kiểm tra dữ liệu

        if (factory.getType_factory() == null || factory.getNameFactory().isEmpty()
            || factory.getPhoneFactory().isEmpty() || factory.getAddressFactory().isEmpty())
        {
            iFactory.emptyValue();
        }
        else
        {
            insertThread(factory);
        }
    }
    private synchronized void insertThread(Factory factory)
    {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Vui lòng chờ...");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Common.api.insertFactory(factory.getType_factory().getIdTypeFactory(),
                        factory.getNameFactory(), factory.getAddressFactory(), factory.getPhoneFactory(), factory.getIdUser())
                        .enqueue(new Callback<ResponsePOST>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                                ResponsePOST result = response.body();

                                assert result != null;
                                if (result.getStatus() == 1)
                                {
                                    iFactory.success(result.getMessage());
                                }
                                else
                                {
                                    iFactory.failed(result.getMessage());
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                                iFactory.Exception(t.getMessage());
                                dialog.dismiss();

                            }
                        });
            }
        }).start();
    }


    public void deleteFactory(int idFactory)
    {
        deleteThread(idFactory);
    }

    private synchronized void deleteThread(int id)
    {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Vui lòng chờ...");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Common.api.deleteFactory(id)
                        .enqueue(new Callback<ResponsePOST>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                                ResponsePOST result = response.body();

                                assert result != null;
                                if (result.getStatus() == 1)
                                {
                                    iFactory.success(result.getMessage());
                                    getFactory();
                                }
                                else
                                {
                                    iFactory.failed(result.getMessage());
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                                iFactory.Exception(t.getMessage());
                                dialog.dismiss();

                            }
                        });
            }
        }).start();
    }

    public void updateFactory(Factory factory)
    {
        if (factory.getAddressFactory().isEmpty() || factory.getNameFactory().isEmpty() || factory.getPhoneFactory().isEmpty())
        {
            iFactory.emptyValue();
        }
        else
        {
            updateThread(factory);
        }

    }

    private void updateThread(Factory factory) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Vui lòng chờ...");
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Common.api.updateFactory(factory.getIdFactory(), factory.getNameFactory(), factory.getAddressFactory()
                        , factory.getPhoneFactory(), factory.getType_factory().getIdTypeFactory())
                        .enqueue(new Callback<ResponsePOST>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                                ResponsePOST result = response.body();

                                assert result != null;
                                if (result.getStatus() == 1)
                                {
                                    iFactory.success(result.getMessage());
                                    getFactory();
                                }
                                else
                                {
                                    iFactory.failed(result.getMessage());
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                                iFactory.Exception(t.getMessage());
                                dialog.dismiss();

                            }
                        });
            }
        }).start();
    }
}
