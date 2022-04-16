package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.apptxng.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ManagerActivity extends AppCompatActivity {

    BottomNavigationView navigation_Manager;
    FrameLayout frame_Manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        // init view
        navigation_Manager  = findViewById(R.id.navigation_Manager);
        frame_Manager       = findViewById(R.id.frame_Manager);

        // load fragment ban đầu
        loadFragment(new Products_Manager_Fragment());
    }


    // Thay đổi Fragment
    private boolean loadFragment (Fragment fragment)
    {
        if (fragment != null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_Manager, fragment)
                    .commit();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        navigation_Manager.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fm = null;
                switch (item.getItemId())
                {
                    case R.id.products_manager:
                        fm = new Products_Manager_Fragment();
                        break;
                    case R.id.setting_manager:
                        fm = new Setting_Manager_Fragment();
                        break;
                }
                return loadFragment(fm);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (navigation_Manager.getSelectedItemId() == R.id.products_manager)
        {
            super.onBackPressed();
            finish();
        }
        else
        {
            navigation_Manager.setSelectedItemId(R.id.manager_category_admin);
        }
    }
}