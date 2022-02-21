package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.apptxng.R;
import com.example.apptxng.model.Category;

public class Products_Admin_Activity extends AppCompatActivity {

    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_admin);

        // Get category: Lấy category từ Fragment Category truyền qua
        Bundle bundleCategory = getIntent().getBundleExtra("bundleCategory");
        category = (Category) bundleCategory.getSerializable("category");


    }
}