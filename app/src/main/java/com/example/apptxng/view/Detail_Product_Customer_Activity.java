package com.example.apptxng.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.adapter.Detail_Product_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.ResponsePOST;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_Product_Customer_Activity extends AppCompatActivity {

    private ImageView img_Close_Detail, img_Detail_Product, img_QR_Product;
    private TextView txt_Name_Detail_Product,txt_Price_Product;
    private Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product_customer);

        // Get Bundle: Nhận đối tượng product đã truyền qua
        product = (Product) getIntent().getExtras().getSerializable("product");
        // initView
        initView();
    }

    private void initView() {
        img_Close_Detail            = findViewById(R.id.img_Close_Detail);
        img_Detail_Product          = findViewById(R.id.img_Detail_Product);
        img_QR_Product              = findViewById(R.id.img_QR_Product);
        txt_Price_Product           = findViewById(R.id.txt_Price_Product);
        txt_Name_Detail_Product     = findViewById(R.id.txt_Name_Detail_Product);
        ViewPager2 viewPager_Detail_Product = findViewById(R.id.viewPager_Detail_Product);
        TabLayout tabLayout_Detail_Product = findViewById(R.id.tabLayout_Detail_Product);

        Detail_Product_Adapter detailProductAdapter = new Detail_Product_Adapter(this, product);


        // Set adapter viewPager
        viewPager_Detail_Product.setAdapter(detailProductAdapter);

        // Set tilte
        new TabLayoutMediator(tabLayout_Detail_Product, viewPager_Detail_Product, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0)
                {
                    tab.setText("Thông tin");
                    tab.setIcon(R.drawable.ic_info_detail);
                }
                else
                {
                    tab.setText("Nhật ký");
                    tab.setIcon(R.drawable.ic_history_detail);
                }
            }
        }).attach();
    }



    @Override
    protected void onResume() {
        super.onResume();

        // Hiển thị dữ liệu của sản phẩm
        if (product != null)
        {
            displayValue();
        }

        // init events
        initEvents();
    }

    private void displayValue() {

        // Ảnh của sản phẩm
        Glide.with(this).load(product.getImageProduct()).error(R.drawable.logo).into(img_Detail_Product);

        // Ảnh QR của sản phẩm
        Glide.with(this).load(product.getQrProduct()).error(R.drawable.logo).into(img_QR_Product);

        // Tên của sản phẩm
        txt_Name_Detail_Product.setText(product.getNameProduct());

        // Giá của sản phẩm
        txt_Price_Product.setText(Common.numberFormat.format(product.getPriceProduct()));
    }


    private void initEvents() {

        // 1. Close button: đóng activity
        img_Close_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        // 3. QR Code: Hiển thị QR Code to
        img_QR_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayQRCode();
            }
        });
    }

    private void displayQRCode() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.display_image);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Image view
        ImageView imgQR = dialog.findViewById(R.id.img_Display);

        // Load image
        Glide.with(Detail_Product_Customer_Activity.this).load(product.getQrProduct()).into(imgQR);

        dialog.show();

    }


}