package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.model.User;

import java.util.List;

public class account_customer_admin_adapter extends RecyclerView.Adapter<account_customer_admin_adapter.AccountCustomerViewHolder> {

    private List<User> listAccountCustomer;


    @SuppressLint("NotifyDataSetChanged")
    public void setListAccountCustomer(List<User> listCustomer)
    {
        this.listAccountCustomer = listCustomer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccountCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_customer,parent,false);
        return new AccountCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountCustomerViewHolder holder, int position) {
        User userCustomer = listAccountCustomer.get(position);

        if (userCustomer != null)
        {
            holder.txt_Name_Customer_Account.setText(userCustomer.getName());
            holder.txt_Email_Customer_Account.setText(userCustomer.getEmail());
            holder.switch_Customer_Account.setChecked(userCustomer.isAccept());
            if (userCustomer.getPhone() == null || userCustomer.getAddress() == null)
            {
                holder.txt_Phone_Customer_Account.setText(R.string.title_error_empty_user);
            }
            else
            {
                holder.txt_Phone_Customer_Account.setText(userCustomer.getPhone());
            }

        }
    }

    @Override
    public int getItemCount() {
        if (listAccountCustomer != null){
            return listAccountCustomer.size();
        }
        return 0;
    }

    public static class AccountCustomerViewHolder extends RecyclerView.ViewHolder {
        private final TextView txt_Name_Customer_Account;
        private final TextView txt_Email_Customer_Account;
        private final TextView txt_Phone_Customer_Account;

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private final Switch switch_Customer_Account;
        public AccountCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Name_Customer_Account       = itemView.findViewById(R.id.txt_Name_Customer_Account);
            txt_Email_Customer_Account      = itemView.findViewById(R.id.txt_Email_Customer_Account);
            txt_Phone_Customer_Account      = itemView.findViewById(R.id.txt_Phone_Customer_Account);
            switch_Customer_Account         = itemView.findViewById(R.id.switch_Customer_Account);
        }
    }
}
