package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.apptxng.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FarmerActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    FrameLayout frame_Farmer;
    private BottomNavigationView navigation_Farmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);
        
        // Init view
        initView();

        // Hiển thị fragment home khi đang nhập thành công
        loadFragment(new Home_Farmer_Fragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation_Farmer.setOnItemSelectedListener(this);
    }

    // Thay đổi Fragment
    private boolean loadFragment (Fragment fragment)
    {
        if (fragment != null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_Farmer, fragment)
                    .commit();
        }
        return true;
    }

    // Ánh xạ view
    private void initView() {
        frame_Farmer        = findViewById(R.id.frame_Farmer);
        navigation_Farmer   = findViewById(R.id.navigation_Farmer);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fm = null;
        switch (item.getItemId())
        {
            case R.id.home_farmer:
                fm = new Home_Farmer_Fragment();
                break;
            case R.id.message_farmer:
                fm = new Message_Farmer_Fragment();
                break;
            case R.id.order_farmer:
                fm = new Order_Farmer_Fragment();
                break;
            case R.id.setting_farmer:
                fm = new Setting_Farmer_Fragment();
                break;
        }
        return loadFragment(fm);
    }

    @Override
    public void onBackPressed() {
        if (navigation_Farmer.getSelectedItemId() == R.id.home_farmer)
        {
            super.onBackPressed();
            finish();
        }
        else
        {
            navigation_Farmer.setSelectedItemId(R.id.home_farmer);
        }
    }
}