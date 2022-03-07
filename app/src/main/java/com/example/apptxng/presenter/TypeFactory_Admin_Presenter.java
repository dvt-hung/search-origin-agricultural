package com.example.apptxng.presenter;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.TypeFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeFactory_Admin_Presenter {

    private final ITypeFactoryAdmin iTypeFactoryAdmin;

    public TypeFactory_Admin_Presenter(ITypeFactoryAdmin iTypeFactoryAdmin) {
        this.iTypeFactoryAdmin = iTypeFactoryAdmin;
    }

    // Load list linked
    public void loadLinked(){
        Common.api.getTypeFactory()
                .enqueue(new Callback<List<TypeFactory>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<TypeFactory>> call, @NonNull Response<List<TypeFactory>> response) {
                        iTypeFactoryAdmin.getLinked(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<TypeFactory>> call, Throwable t) {
                        iTypeFactoryAdmin.Exception(t.getMessage());
                    }
                });
    }
}
