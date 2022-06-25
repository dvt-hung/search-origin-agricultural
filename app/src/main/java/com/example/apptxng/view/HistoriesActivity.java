package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.History_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.History;
import com.example.apptxng.model.Product;
import com.example.apptxng.presenter.Histories_Activity_Presenter;
import com.example.apptxng.presenter.History_Presenter;
import com.example.apptxng.presenter.IHistoriesActivity;
import com.example.apptxng.presenter.IHistory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HistoriesActivity extends AppCompatActivity implements History_Adapter.IListenerHistory, IHistoriesActivity {

    private ImageView img_Close_History_Product,img_Insert_History_Product;
    private History_Adapter historyAdapter;
    private Factory factoryTemp;
    private Product productTemp;
    private Histories_Activity_Presenter historiesActivityPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histories);

        // Nhận idProduct
        productTemp = (Product) getIntent().getExtras().getSerializable("product");

        // init view: Ánh xạ view
        initView();
    }



    private void initView() {
        img_Close_History_Product           = findViewById(R.id.img_Close_History_Product);
        img_Insert_History_Product          = findViewById(R.id.img_Insert_History_Product);
        RecyclerView recycler_History       = findViewById(R.id.recycler_History_Product);

        historiesActivityPresenter          = new Histories_Activity_Presenter(this,this);

        // Adapter
        historyAdapter                      = new History_Adapter(this);
        recycler_History.setAdapter(historyAdapter);

        // Layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_History.setLayoutManager(layoutManager);

        // Kiểm tra quyền thêm lịch sử: Nếu idCurrent bằng với Current User thì được thêm
        if (productTemp.getIdUser().equals(Common.currentUser.getIdUser()))
        {
            img_Insert_History_Product.setVisibility(View.VISIBLE);
        }

        // Kiểm tra quyền thêm của Nhân viên
        if(productTemp.getIdEmployee() != null && productTemp.getIdEmployee().equals(Common.currentUser.getIdUser()))
        {
            img_Insert_History_Product.setVisibility(View.VISIBLE);
        }

        // Nếu là nhân viên thì truyển vào idOwner
        if (Common.currentUser.getIdRole() == Common.ID_ROLE_EMPLOYEE)
        {
            historiesActivityPresenter.getInfoFactory(Common.currentUser.getIdOwner());
        }
        else
        {
            historiesActivityPresenter.getInfoFactory(Common.currentUser.getIdUser());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 0. Tải danh sách history + load info factory
        historiesActivityPresenter.loadHistory(productTemp.getIdProduct());
        // 1. Close button: Trở về activity trước
        img_Close_History_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Insert button: Chuyển sang activity insert history
        img_Insert_History_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoriesActivity.this, InsertHistoryActivity.class);
                intent.putExtra("idProduct",productTemp.getIdProduct());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClickHistoryItem(History history) {
        Bundle bundleHis = new Bundle();
        bundleHis.putSerializable("history",history);
        Intent intent = new Intent(HistoriesActivity.this, DetailHistoryActivity.class);
        intent.putExtras(bundleHis);
        startActivity(intent);
    }


    @Override
    public void exceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getHistory(List<History> histories) {
        for (int i = histories.size() - 1; i >= 0 ; i--) {
            History his = histories.get(i);
            /*
            * 1. Là đang ở tại cơ sở tức: history.idFactory = factoryTemp.idFactory , history.idFactoryRe = factoryTemp.idFactory
            * 2. Là đã chuyển đi cơ sở khác tức: history.idFactory = factoryTemp.idFactory, history.idFactoryRe != factoryTemp.idFactory
            * 3. Là được nhận từ cơ sở khác: history.idFactory != factoryTemp.idFactory, history.idFactoryRe = factoryTemp.idFactory
            * */
            if (his.getFactory().getIdFactory() == factoryTemp.getIdFactory() && his.getIdFactoryReceive() == factoryTemp.getIdFactory()
            || his.getFactory().getIdFactory() == factoryTemp.getIdFactory() && his.getIdFactoryReceive() != factoryTemp.getIdFactory()
            || his.getFactory().getIdFactory() != factoryTemp.getIdFactory() && his.getIdFactoryReceive() == factoryTemp.getIdFactory())
            {
                filterList(i,histories);
                break;
            }
        }



    }

    private void filterList(int index, List<History> historyList) {
        List<History> histories = new ArrayList<>();
        for (int i = 0; i <= index; i++) {
            histories.add(historyList.get(i));
        }
        historyAdapter.setHistoryList(histories);
    }

    @Override
    public void infoFactory(Factory factory) {
        factoryTemp = factory;
    }
}