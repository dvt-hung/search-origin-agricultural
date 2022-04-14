package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import gun0912.tedbottompicker.TedBottomPicker;

public class AdminActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    BottomNavigationView navigation_Admin;
    FrameLayout frame_Admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

   
        // Init view
        initView();

        // cài đặt fragment ban đầu
        loadFragment(new Category_Admin_Fragment());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Chọn item của bottom navigation
        navigation_Admin.setOnItemSelectedListener(this);

    }

    // Thay đổi Fragment
    private boolean loadFragment (Fragment fragment)
    {
        if (fragment != null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_Admin, fragment)
                    .commit();
        }
        return true;
    }

    // Init view
    private void initView() {
        navigation_Admin    = findViewById(R.id.navigation_Admin);
        frame_Admin         = findViewById(R.id.frame_Admin);
    }

    // Select item navigation
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fm = null;
        switch (item.getItemId())
        {
            case R.id.manager_category_admin:
                fm = new Category_Admin_Fragment();
                break;
            case R.id.manager_message_admin:
                 fm = new Chat_Admin_Fragment();
                break;
            case R.id.manager_account_admin:
                fm = new Account_Admin_Fragment();
                break;
            case R.id.manager_setting_admin:
                fm = new Setting_Admin_Fragment();
                break;

        }
        return loadFragment(fm);
    }

    @Override
    public void onBackPressed() {

        if (navigation_Admin.getSelectedItemId() == R.id.manager_category_admin)
        {
            super.onBackPressed();
            finish();
        }
        else
        {
            navigation_Admin.setSelectedItemId(R.id.manager_category_admin);
        }
    }
}