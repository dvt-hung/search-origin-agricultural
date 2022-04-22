package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.apptxng.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class CustomerActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private FloatingActionButton btn_Scan;
    private BottomNavigationView navigation_Customer;
    private FrameLayout frame_Customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        // init view
        initView();

        // cài đặt fragment ban đầu
        loadFragment(new Home_Customer_Fragment());
    }

    private void initView() {
        btn_Scan                = findViewById(R.id.btn_Scan);
        navigation_Customer     = findViewById(R.id.navigation_Customer);
        frame_Customer          = findViewById(R.id.frame_Customer);

    }


    @Override
    protected void onResume() {
        super.onResume();


        // bottom navigation click
        navigation_Customer.setOnItemSelectedListener(this);

        // button scan
        btn_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionCamera();
            }
        });
    }

    // Kiểm tra quyền Camera
    private void checkPermissionCamera()
    {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        startActivity(new Intent(CustomerActivity.this, ScanActivity.class));
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        /* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                                     permission, PermissionToken token) {
                        token.continuePermissionRequest();/* ... */}
                }).check();
    }


    // Thay đổi Fragment
    private boolean loadFragment (Fragment fragment)
    {
        if (fragment != null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_Customer, fragment)
                    .commit();
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fm = null;
        switch (item.getItemId())
        {
            case R.id.menu_home_customer:
                fm = new Home_Customer_Fragment();
                break;
            case R.id.menu_setting_customer:
                fm = new Setting_Customer_Fragment();

        }
        return loadFragment(fm);
    }

    @Override
    public void onBackPressed() {
        if(navigation_Customer.getSelectedItemId() == R.id.menu_home_customer)
        {
            super.onBackPressed();
            finish();
        }
        else
        {
            navigation_Customer.setSelectedItemId(R.id.menu_home_customer);
        }
    }
}