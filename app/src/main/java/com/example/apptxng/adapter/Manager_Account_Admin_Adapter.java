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

public class Manager_Account_Admin_Adapter extends RecyclerView.Adapter<Manager_Account_Admin_Adapter.ManagerViewHolder> {

    private List<User> listManager;
    private final IManagerListener iManagerListener;
    public interface IManagerListener{
        void onClickSwitch(User manager, int status);
        void onClickManger(User manager);
    }

    public Manager_Account_Admin_Adapter(IManagerListener iManagerListener) {
        this.iManagerListener = iManagerListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListManager(List<User> list)
    {
        this.listManager = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_account,parent,false);
        return new ManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerViewHolder holder, int position) {
        User manager = listManager.get(position);

        if (manager != null)
        {
            holder.txt_Name_Manager_Account.setText(manager.getName());
            holder.txt_Phone_Manager_Account.setText(manager.getPhone());
            boolean checked = manager.isAccept() != 0;
            holder.switch_Manager_Account.setChecked(checked);

            Common.displayValueTextView(holder.txt_Email_Manager_Account,manager.getEmail());
            // Change accept
            // Change accept
            holder.switch_Manager_Account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = manager.isAccept() != 0;
                    holder.switch_Manager_Account.setChecked(checked);
                    int status = 0;
                    if (!checked)
                    {
                        status = 1;
                    }
                    iManagerListener.onClickSwitch(manager, status);
                }
            });

            // onClick Manager
            holder.layout_Manger_Account_Admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iManagerListener.onClickManger(manager);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (listManager != null)
        {
            return listManager.size();
        }
        return 0;
    }

    public static class ManagerViewHolder extends RecyclerView.ViewHolder {
        private final TextView txt_Name_Manager_Account;
        private final TextView txt_Email_Manager_Account;
        private final TextView txt_Phone_Manager_Account;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private final Switch switch_Manager_Account;
        private final LinearLayout layout_Manger_Account_Admin;
        public ManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Name_Manager_Account        = itemView.findViewById(R.id.txt_Name_Manager_Account);
            txt_Email_Manager_Account       = itemView.findViewById(R.id.txt_Email_Manager_Account);
            txt_Phone_Manager_Account       = itemView.findViewById(R.id.txt_Phone_Manager_Account);
            switch_Manager_Account          = itemView.findViewById(R.id.switch_Manager_Account);
            layout_Manger_Account_Admin     = itemView.findViewById(R.id.layout_Manger_Account_Admin);
        }
    }
}
