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
import com.example.apptxng.presenter.ISupplyChainActivity;
import com.example.apptxng.presenter.SupplyChain_Activity_Presenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplyChainActivity extends AppCompatActivity implements ISupplyChainActivity {

    private String idProductTemp;
    private ImageView img_Back_SupplyChain;
    private SupplyChain_Adapter supplyChainAdapter;
    private Factory factoryTemp;
    private SupplyChain_Activity_Presenter supply_Chain_Presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_chain);

        // Nhận idProduct
        idProductTemp = getIntent().getStringExtra("idProduct");

        // init view: Ánh xạ view
        initView();
    }

    private void initView() {
        img_Back_SupplyChain                = findViewById(R.id.img_Back_SupplyChain);
        RecyclerView recycler_Supply_Chain  = findViewById(R.id.recycler_Supply_Chain);
        supplyChainAdapter                  = new SupplyChain_Adapter();
        supply_Chain_Presenter             = new SupplyChain_Activity_Presenter(this, this);
        // Gán adapter cho recycler view
        recycler_Supply_Chain.setAdapter(supplyChainAdapter);



        // Kiểm tra loại người dùng
        // Nếu là nhân viên thì truyển vào idOwner
        if (Common.currentUser.getIdRole() == Common.ID_ROLE_EMPLOYEE)
        {
            supply_Chain_Presenter.getInfoFactory(Common.currentUser.getIdOwner());
        }
        else
        {
            supply_Chain_Presenter.getInfoFactory(Common.currentUser.getIdUser());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        supply_Chain_Presenter.getSupplyChains(idProductTemp);

        // Back Button: Đóng activity
        img_Back_SupplyChain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void supplyChains(List<SupplyChain> supplyChainList) {
       filterSupplyChain(supplyChainList);
    }

    private void filterSupplyChain(List<SupplyChain> list) {
        List<SupplyChain> newList = new ArrayList<>();
        for ( SupplyChain sc : list)
        {
            if (sc.getIdTypeFactory() == factoryTemp.getType_factory().getIdTypeFactory())
            {
                newList.add(sc);
                break;
            }
            else
            {
                newList.add(sc);
            }
        }
        supplyChainAdapter.setSupplyChainList(newList);
    }

    @Override
    public void exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void infoFactory(Factory factory) {
        factoryTemp = factory;
    }
}