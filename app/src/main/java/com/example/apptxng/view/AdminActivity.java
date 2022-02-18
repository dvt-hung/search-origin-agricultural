package com.example.apptxng.view;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.apptxng.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminActivity extends AppCompatActivity {

    BottomNavigationView navigation_Admin;
    FrameLayout frame_Admin;
    private final int FRAGMENT_CATEGORY = 1;
    private final int FRAGMENT_PRODUCT = 2;
    private final int FRAGMENT_ACCOUNT = 3;
    private final int FRAGMENT_SETTING = 4;
    private int FRAGMENT_CURRENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Init view
        initView();

        // cài đặt fragment ban đầu
        navigation_Admin.setSelectedItemId(R.id.manager_category_admin);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_Admin,new Category_Admin_Fragment(), "currentFragment")
                .commit();
        FRAGMENT_CURRENT = FRAGMENT_CATEGORY;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Chọn item của bottom navigation
        navigation_Admin.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fm = getSupportFragmentManager().findFragmentByTag("currentFragment");
                switch (item.getItemId())
                {
                    case R.id.manager_category_admin:
                        if (FRAGMENT_CURRENT != FRAGMENT_CATEGORY && fm != null)
                        {
                            replace_Fragment(fm, new Category_Admin_Fragment());
                            FRAGMENT_CURRENT = FRAGMENT_CATEGORY;
                        }
                        break;
                    case R.id.manager_product_admin:
                        if (FRAGMENT_CURRENT != FRAGMENT_PRODUCT && fm != null)
                        {
                            replace_Fragment(fm, new Product_Admin_Fragment());
                            FRAGMENT_CURRENT = FRAGMENT_PRODUCT;
                        }
                        break;
                    case R.id.manager_account_admin:
                        if (FRAGMENT_CURRENT != FRAGMENT_ACCOUNT)
                        {
                            replace_Fragment(fm, new Account_Admin_Fragment());
                            FRAGMENT_CURRENT = FRAGMENT_ACCOUNT;
                        }
                        break;
                    case R.id.manager_setting_admin:
                        if (FRAGMENT_CURRENT != FRAGMENT_SETTING)
                        {
                            replace_Fragment(fm, new Setting_Admin_Fragment());
                            FRAGMENT_CURRENT = FRAGMENT_CATEGORY;
                        }
                        break;

                }
                return true;
            }
        });

    }

    // Thay đổi Fragment
    private void replace_Fragment(Fragment fragmentCurrent, Fragment fragmentNew)
    {
        FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
                manager.remove(fragmentCurrent)
                        .add(R.id.frame_Admin, fragmentNew, "currentFragment")
                        .addToBackStack(null)
                        .commit();
    }

    // Init view
    private void initView() {
        navigation_Admin    = findViewById(R.id.navigation_Admin);
        frame_Admin         = findViewById(R.id.frame_Admin);
    }
}