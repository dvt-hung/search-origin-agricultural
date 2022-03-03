package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.model.Balance;

import java.util.List;

public class Balance_Admin_Adapter extends RecyclerView.Adapter<Balance_Admin_Adapter.BalanceViewHolder> {

    private List<Balance> balanceList;
    private final IListenerBalance listenerBalance;

    public interface IListenerBalance
    {
        /*
        * Bắt hành vị click vào item balance;
        * */
        void onClickBalance(Balance balance);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBalanceList(List<Balance> list)
    {
        /*
        * Gán list cho adapter
        * */
        this.balanceList = list;
        notifyDataSetChanged();
    }

    public Balance_Admin_Adapter(IListenerBalance listenerBalance) {
        this.listenerBalance = listenerBalance;
    }

    @NonNull
    @Override
    public BalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_balance_admin,parent,false);
        return new BalanceViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BalanceViewHolder holder, int position) {
          Balance balance = balanceList.get(position);

          if (balance != null)
          {
              holder.txt_ID_Balance_Admin.setText("ID: " + balance.getIdBalance());
              holder.txt_Name_Balance_Admin.setText("Đơn vị: " + balance.getNameBalance());

              holder.layout_Balance_Admin.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      listenerBalance.onClickBalance(balance);
                  }
              });
          }

          /*
           - Lấy ra từng phần tử Balance trong list
           - Gán ID và Tên cho từng item
           - Bắt sự kiện click từng item
          */
    }

    @Override
    public int getItemCount() {
        if (balanceList != null)
        {
            return balanceList.size();
        }
        return 0;
    }

    public static class BalanceViewHolder extends RecyclerView.ViewHolder {
        private final TextView txt_ID_Balance_Admin;
        private final TextView txt_Name_Balance_Admin;
        private final LinearLayout layout_Balance_Admin;
        public BalanceViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ID_Balance_Admin    = itemView.findViewById(R.id.txt_ID_Balance_Admin);
            txt_Name_Balance_Admin  = itemView.findViewById(R.id.txt_Name_Balance_Admin);
            layout_Balance_Admin    = itemView.findViewById(R.id.layout_Balance_Admin);
        }
    }
}
