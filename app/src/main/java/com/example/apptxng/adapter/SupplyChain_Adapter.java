package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.model.SupplyChain;

import java.util.List;

public class SupplyChain_Adapter extends RecyclerView.Adapter<SupplyChain_Adapter.SupplyChainViewHolder> {

    private List<SupplyChain> supplyChainList;


    @SuppressLint("NotifyDataSetChanged")
    public void setSupplyChainList(List<SupplyChain> list)
    {
        this.supplyChainList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SupplyChainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suplly_chain,parent,false);
        return new SupplyChainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplyChainViewHolder holder, int position) {
        // Lấy ra từng chuỗi cung ứng
        SupplyChain SC = supplyChainList.get(position);
        if (SC != null)
        {
            holder.txt_Supply_Chain.setText(SC.getNameTypeFactory());
        }
    }

    @Override
    public int getItemCount() {
        if(supplyChainList != null)
        {
            return supplyChainList.size();
        }
        return 0;
    }

    public static class SupplyChainViewHolder extends RecyclerView.ViewHolder {
        private final TextView txt_Supply_Chain;
        public SupplyChainViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Supply_Chain    = itemView.findViewById(R.id.txt_Supply_Chain);
        }
    }
}
