package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Images_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.History;
import com.example.apptxng.model.ImageHistory;
import com.example.apptxng.presenter.IImageHistory;
import com.example.apptxng.presenter.ImageHistory_Presenter;

import java.util.List;

public class DetailHistoryActivity extends AppCompatActivity implements IImageHistory {

    private History historyTemp;
    private Images_Adapter adapter;
    private ImageHistory_Presenter imageHistoryPresenter;
    private TextView txt_Date_History_Detail, txt_Des_History_Detail, txt_TypeFactory_History_Detail,
            txt_NameFactory_History_Detail, txt_AddressFactory_History_Detail,txt_PhoneFactory_History_Detail, txt_WebFactory_History_Detail;
    private ImageView img_Back_Detail_History,img_Option_Detail_History;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        // init view: Ánh xạ view
        initView();

        // Nhận history
        Bundle receiveHistory = getIntent().getExtras();
        historyTemp = (History) receiveHistory.getSerializable("history");

    }

    private void initView() {
        RecyclerView recycler_Images_History                = findViewById(R.id.recycler_Images_History);
        txt_Date_History_Detail                             = findViewById(R.id.txt_Date_History_Detail);
        txt_Des_History_Detail                              = findViewById(R.id.txt_Des_History_Detail);
        txt_TypeFactory_History_Detail                      = findViewById(R.id.txt_TypeFactory_History_Detail);
        txt_NameFactory_History_Detail                      = findViewById(R.id.txt_NameFactory_History_Detail);
        txt_AddressFactory_History_Detail                   = findViewById(R.id.txt_AddressFactory_History_Detail);
        txt_PhoneFactory_History_Detail                     = findViewById(R.id.txt_PhoneFactory_History_Detail);
        txt_WebFactory_History_Detail                       = findViewById(R.id.txt_WebFactory_History_Detail);
        img_Back_Detail_History                             = findViewById(R.id.img_Back_Detail_History);
        img_Option_Detail_History                           = findViewById(R.id.img_Option_Detail_History);
        // Presenter
        imageHistoryPresenter = new ImageHistory_Presenter(this, this);

        // Gán adapter cho recycler view
        adapter   = new Images_Adapter(this);
        recycler_Images_History.setAdapter(adapter);

        // Tạo layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recycler_Images_History.setLayoutManager(layoutManager);


    }



    @Override
    protected void onResume() {
        super.onResume();
        // Hiển thị dữ liệu
        displayValue();
        // Load images: Tải hình ảnh của history
        imageHistoryPresenter.getImageHistory(historyTemp.getIdHistory());
        
        // init events
        initEvents();
    }

    private void initEvents() {

        img_Back_Detail_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img_Option_Detail_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void displayValue() {
        txt_Date_History_Detail.setText(historyTemp.getDateHistory());
        txt_Des_History_Detail.setText(historyTemp.getDescriptionHistory());
        txt_TypeFactory_History_Detail.setText(historyTemp.getType_factory().getNameTypeFactory());
        txt_NameFactory_History_Detail.setText(historyTemp.getFactory().getNameFactory());
        txt_AddressFactory_History_Detail.setText(historyTemp.getFactory().getAddressFactory());
        txt_PhoneFactory_History_Detail.setText(historyTemp.getFactory().getPhoneFactory());

        if (!historyTemp.getFactory().getIdUser().equals(Common.currentUser.getIdUser()))
        {
            img_Option_Detail_History.setVisibility(View.GONE);
        }
    }

    @Override
    public void getImages(List<ImageHistory> images) {
        adapter.setUriList(images);
    }

    @Override
    public void exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}