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
import com.example.apptxng.model.History;
import com.example.apptxng.model.Product;
import com.example.apptxng.presenter.History_Presenter;
import com.example.apptxng.presenter.IHistory;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoriesActivity extends AppCompatActivity implements History_Adapter.IListenerHistory, IHistory {

    private ImageView img_Close_History_Product,img_Insert_History_Product;
    private History_Adapter historyAdapter;
    private History_Presenter historyPresenter;
    private Product productTemp;
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
        historyPresenter                    = new History_Presenter(this,this);

        // Adapter
        historyAdapter                      = new History_Adapter(this);
        recycler_History.setAdapter(historyAdapter);

        // Layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_History.setLayoutManager(layoutManager);

        // Kiểm tra quyền thêm lịch sử: Nếu idCurrent bằng với Current User thì được thêm
        if (!productTemp.getIdCurrent().equals(Common.currentUser.getIdUser()) )
        {
            img_Insert_History_Product.setVisibility(View.GONE);
        }

        // Kiểm tra quyền thêm của Nhân viên
        if(productTemp.getIdEmployee().equals(Common.currentUser.getIdUser()))
        {
            img_Insert_History_Product.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 0. Tải danh sách history
        historyPresenter.loadHistory(productTemp.getIdProduct());

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
    public void successMessage(String message) {

    }

    @Override
    public void failedMessage(String message) {

    }

    @Override
    public void exceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void emptyValue() {

    }

    @Override
    public void getHistory(List<History> histories) {
        historyAdapter.setHistoryList(histories);
    }
}