package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.model.User;

import java.util.List;

public class Farmer_Account_Admin_Adapter extends RecyclerView.Adapter<Farmer_Account_Admin_Adapter.AccountCustomerViewHolder> {

    private List<User> listAccountFarmer;
    private IFarmerAccountListener IFarmerListener;
    public interface IFarmerAccountListener{
        void onClickFarmer(User user);
        void onClickSwitch(User user, int status);
    }

    public Farmer_Account_Admin_Adapter(IFarmerAccountListener IFarmerListener) {
        this.IFarmerListener = IFarmerListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListAccountFarmer(List<User> listCustomer)
    {
        this.listAccountFarmer = listCustomer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccountCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farmer_account,parent,false);
        return new AccountCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountCustomerViewHolder holder, int position) {
        User userFarmer = listAccountFarmer.get(position);

        if (userFarmer != null)
        {
            holder.txt_Name_Farmer_Account.setText(userFarmer.getName());
            holder.txt_Phone_Farmer_Account.setText(userFarmer.getPhone());
            boolean checked = userFarmer.isAccept() != 0;
            holder.switch_Farmer_Account.setChecked(checked);
            if (userFarmer.getEmail() == null )
            {
                holder.txt_Email_Farmer_Account.setText(R.string.title_error_empty_user);
            }
            else
            {
                holder.txt_Email_Farmer_Account.setText(userFarmer.getPhone());
            }

            // Click item
            holder.layout_Farmer_Account_Admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IFarmerListener.onClickFarmer(userFarmer);
                }
            });

            // Click switch
            holder.switch_Farmer_Account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = userFarmer.isAccept() != 0;
                    holder.switch_Farmer_Account.setChecked(checked);
                    int status = 0;
                    if (!checked)
                    {
                        status = 1;
                    }
                    IFarmerListener.onClickSwitch(userFarmer, status);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (listAccountFarmer != null){
            return listAccountFarmer.size();
        }
        return 0;
    }

    public static class AccountCustomerViewHolder extends RecyclerView.ViewHolder {
        private final TextView txt_Name_Farmer_Account;
        private final TextView txt_Email_Farmer_Account;
        private final TextView txt_Phone_Farmer_Account;
        private final LinearLayout layout_Farmer_Account_Admin;

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private final Switch switch_Farmer_Account;
        public AccountCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Name_Farmer_Account                 = itemView.findViewById(R.id.txt_Name_Farmer_Account);
            txt_Email_Farmer_Account                = itemView.findViewById(R.id.txt_Email_Farmer_Account);
            txt_Phone_Farmer_Account                = itemView.findViewById(R.id.txt_Phone_Farmer_Account);
            layout_Farmer_Account_Admin             = itemView.findViewById(R.id.layout_Farmer_Account_Admin);
            switch_Farmer_Account                   = itemView.findViewById(R.id.switch_Farmer_Account);
        }
    }
}
