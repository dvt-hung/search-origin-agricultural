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
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;

import java.util.List;

public class Account_Adapter extends RecyclerView.Adapter<Account_Adapter.AccountViewHolder> {

    private List<User> listUser;
    private final AccountListener accountListener;
    public interface AccountListener{
        void onClickAccount(User user);
        void onClickSwitch(User user, int status);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEmployees(List<User> userList)
    {
        this.listUser = userList;
        notifyDataSetChanged();
    }


    public Account_Adapter(AccountListener accountListener) {
        this.accountListener = accountListener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewEmployee = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account,parent,false);
        return new AccountViewHolder(viewEmployee);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        User user = listUser.get(position);

        if (user != null)
        {
            // Name
            holder.txt_Name_Account.setText(user.getName());

            // Email
            Common.displayValueTextView(holder.txt_Email_Account, user.getEmail());

            // Phone
            holder.txt_Phone_Account.setText(user.getPhone());

            // Set value switch
            boolean checked = user.isAccept() != 0;
            holder.switch_Account.setChecked(checked);

            // Switch
            holder.switch_Account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = user.isAccept() != 0;
                    holder.switch_Account.setChecked(checked);
                    int status = 0;
                    if (!checked)
                    {
                        status = 1;
                    }
                    accountListener.onClickSwitch(user,status);
                }
            });

            // Layout
            holder.layout_Account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    accountListener.onClickAccount(user);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (listUser != null)
        {
            return listUser.size();
        }
        return 0;
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        private final TextView txt_Name_Account;
        private final TextView txt_Email_Account;
        private final TextView txt_Phone_Account;
        private final LinearLayout layout_Account;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private final Switch switch_Account;
        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Name_Account        = itemView.findViewById(R.id.txt_Name_Account);
            txt_Email_Account       = itemView.findViewById(R.id.txt_Email_Account);
            txt_Phone_Account       = itemView.findViewById(R.id.txt_Phone_Account);
            layout_Account          = itemView.findViewById(R.id.layout_Account);
            switch_Account          = itemView.findViewById(R.id.switch_Account);
        }
    }
}
