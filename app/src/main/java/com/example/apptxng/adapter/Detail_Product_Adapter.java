package com.example.apptxng.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.apptxng.model.Product;
import com.example.apptxng.view.History_Product_Customer_Fragment;
import com.example.apptxng.view.Info_Product_Customer_Fragment;

public class Detail_Product_Adapter extends FragmentStateAdapter {

    private final Product product;
    public Detail_Product_Adapter(@NonNull FragmentActivity fragmentActivity, Product product) {
        super(fragmentActivity);
        this.product = product;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
        {
            return new Info_Product_Customer_Fragment(product);
        }
        else
        {
            return new History_Product_Customer_Fragment(product);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
