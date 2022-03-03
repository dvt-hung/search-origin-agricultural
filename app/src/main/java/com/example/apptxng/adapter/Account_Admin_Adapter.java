package com.example.apptxng.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.apptxng.view.CustomerAccount_Admin_Fragment;
import com.example.apptxng.view.FarmerAccount_Admin_Fragment;

public class Account_Admin_Adapter extends FragmentStateAdapter {

    public Account_Admin_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new CustomerAccount_Admin_Fragment();
        }
        return new FarmerAccount_Admin_Fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
