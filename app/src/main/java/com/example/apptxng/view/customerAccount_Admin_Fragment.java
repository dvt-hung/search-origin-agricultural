package com.example.apptxng.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.account_customer_admin_adapter;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.IAccountCustomerAdmin;
import com.example.apptxng.presenter.accountCustomer_Admin_Presenter;

import java.util.List;


public class customerAccount_Admin_Fragment extends Fragment implements IAccountCustomerAdmin {

    private View viewAccountCustomer;
    private account_customer_admin_adapter accountCustomerAdminAdapter;
    private accountCustomer_Admin_Presenter customerPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewAccountCustomer = inflater.inflate(R.layout.fragment_account_customer__admin_, container, false);
        customerPresenter = new accountCustomer_Admin_Presenter(this);
        // Init view
        RecyclerView recycler_AccountCustomer_Admin = viewAccountCustomer.findViewById(R.id.recycler_AccountCustomer_Admin);

        // Set adapter
        accountCustomerAdminAdapter = new account_customer_admin_adapter();
        recycler_AccountCustomer_Admin.setAdapter(accountCustomerAdminAdapter);

        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(viewAccountCustomer.getContext(),RecyclerView.VERTICAL,false);
        recycler_AccountCustomer_Admin.setLayoutManager(layoutManager);

        // Load list customer
        loadCustomer();
        return viewAccountCustomer;
    }

    private void loadCustomer() {
        customerPresenter.getListCustomer();
    }

    @Override
    public void listCustomer(List<User> list) {
        accountCustomerAdminAdapter.setListAccountCustomer(list);
    }

    @Override
    public void Exception(String message) {

    }
}