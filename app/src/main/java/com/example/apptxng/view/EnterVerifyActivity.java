package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.apptxng.R;
import com.example.apptxng.model.User;

public class EnterVerifyActivity extends AppCompatActivity {

    private User userTemp;
    private int idTypeFactory;
    private String nameFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_verify);

        // Nhận User
        userTemp = (User) getIntent().getExtras().getSerializable("user");

        // Nhận idTypeFactory
        idTypeFactory = getIntent().getIntExtra("idTypeFactory", 0);

        // Nhận nameFactory
        nameFactory = getIntent().getStringExtra("nameFactory");


    }
}