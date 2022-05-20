package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.SupplyChain_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.SupplyChain;
import com.example.apptxng.presenter.Factory_Presenter;
import com.example.apptxng.presenter.IFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplyChainActivity extends AppCompatActivity  {

    private String idProductTemp;
    private ImageView img_Back_SupplyChain;
    private SupplyChain_Adapter supplyChainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_chain);

        // Nhận idProduct
        idProductTemp = getIntent().getStringExtra("idProduct");

        // init view: Ánh xạ view
        img_Back_SupplyChain                = findViewById(R.id.img_Back_SupplyChain);
        RecyclerView recycler_Supply_Chain  = findViewById(R.id.recycler_Supply_Chain);
        supplyChainAdapter                  = new SupplyChain_Adapter();

        // Gán adapter cho recycler view
        recycler_Supply_Chain.setAdapter(supplyChainAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Load danh sách cung ứng về
        getListSupplyChain();

        // Back Button: Đóng activity
        img_Back_SupplyChain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private synchronized void getListSupplyChain() {
        // Progress dialog
        ProgressDialog progressDialog = Common.createProgress(this);
        progressDialog.show();

        // Gọi API
        Common.api.getSupplyChain(idProductTemp)
                .enqueue(new Callback<List<SupplyChain>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<SupplyChain>> call, @NonNull Response<List<SupplyChain>> response) {
                        assert response.body() != null;
                        supplyChainAdapter.setSupplyChainList(response.body());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<SupplyChain>> call, @NonNull Throwable t) {

                        progressDialog.dismiss();
                        Toast.makeText(SupplyChainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}