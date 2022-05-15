package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.model.Common;

public class InformationActivity extends AppCompatActivity {

    private ImageView img_Farmer_Info,img_Back_Info_Farmer;
    private TextView txt_Email_Info_Farmer, txt_Name_Info_Farmer, txt_Phone_Info_Farmer,txt_Address_Info_Farmer;
    private Button btn_Change_Info_Farmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        // init view: Ánh xạ view
        initView();

    }



    private void initView() {
        img_Farmer_Info             = findViewById(R.id.img_Farmer_Info);
        img_Back_Info_Farmer        = findViewById(R.id.img_Back_Info_Farmer);
        txt_Email_Info_Farmer       = findViewById(R.id.txt_Email_Info_Farmer);
        txt_Name_Info_Farmer        = findViewById(R.id.txt_Name_Info_Farmer);
        txt_Phone_Info_Farmer       = findViewById(R.id.txt_Phone_Info_Farmer);
        txt_Address_Info_Farmer     = findViewById(R.id.txt_Address_Info_Farmer);
        btn_Change_Info_Farmer      = findViewById(R.id.btn_Change_Info_Farmer);
        // Hiển thị dữ liệu của user
        displayValue();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Các sự kiện trong activity
        initEvents();

    }


    private void displayValue() {
        Log.e("a", "displayValue: " + Common.currentUser.getImage() );
        Glide.with(this).load(Common.currentUser.getImage()).error(R.drawable.logo).into(img_Farmer_Info);

        txt_Email_Info_Farmer.setText(Common.currentUser.displayInfoValueString(Common.currentUser.getEmail()));
//
        txt_Name_Info_Farmer.setText(Common.currentUser.displayInfoValueString(Common.currentUser.getName()));
//
        txt_Phone_Info_Farmer.setText(Common.currentUser.displayInfoValueString(Common.currentUser.getPhone()));
//
        txt_Address_Info_Farmer.setText(Common.currentUser.displayInfoValueString(Common.currentUser.getAddress()));
    }

    private void initEvents() {

        /*
        * 1. Back: Khi ấn sẽ tắt activity đi
        * 2. Change Info Button: Chuyển sang activity thay đổi thông tin
        * */

        // 1. Back Button
        img_Back_Info_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Change Info Button
        btn_Change_Info_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InformationActivity.this, ChangeInfoActivity.class));
                finish();
            }
        });
    }
}