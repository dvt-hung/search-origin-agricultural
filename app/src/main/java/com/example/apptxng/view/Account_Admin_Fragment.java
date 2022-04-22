package com.example.apptxng.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Account_Admin_Adapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class Account_Admin_Fragment extends Fragment {

    private View viewAccount;
    private TabLayout tabLayout_Account;
    private ViewPager2 viewPager_Account;
    private Account_Admin_Adapter account_admin_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewAccount = inflater.inflate(R.layout.fragment_account__admin_, container, false);

        // Init view
        initView(viewAccount);

        // Set adapter viewPager
        viewPager_Account.setAdapter(account_admin_adapter);

        // Set tilte
        new TabLayoutMediator(tabLayout_Account, viewPager_Account, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    if (position == 0)
                    {
                        tab.setText("Nông dân");
                    }
                    else if (position == 1)
                    {
                        tab.setText("Quản lý");
                    }
                    else
                    {
                        tab.setText("Khách hàng");
                    }
            }
        }).attach();

        return viewAccount;
    }

    private void initView(View viewAccount) {
        tabLayout_Account = viewAccount.findViewById(R.id.tabLayout_Account_Admin);
        viewPager_Account = viewAccount.findViewById(R.id.viewPager_Account_Admin);
        account_admin_adapter = new Account_Admin_Adapter((FragmentActivity) viewAccount.getContext());
    }
}