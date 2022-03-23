package com.example.apptxng.presenter;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.view.FactoryActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeFactory_Presenter {

    private final ITypeFactory iTypeFactory;

    public TypeFactory_Presenter(ITypeFactory iTypeFactory) {
        this.iTypeFactory = iTypeFactory;
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
}
