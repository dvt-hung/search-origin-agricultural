package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.History;

import java.util.Collections;
import java.util.List;

public class History_Adapter extends RecyclerView.Adapter<History_Adapter.HistoryViewHolder> {

    private List<History> historyList;
    private final IListenerHistory iListenerHistory;

    public interface IListenerHistory{
        void onClickHistoryItem(History history);
    }

    public History_Adapter(IListenerHistory iListenerHistory) {
        this.iListenerHistory = iListenerHistory;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHistoryList(List<History> list)
    {
        Common.sortDates(list);
        this.historyList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = historyList.get(position);

        if (history != null)
        {
            // Gán tên loại cơ sở
            holder.txt_TypeFactory.setText( history.getType_factory().getNameTypeFactory() );

            // Gán tên cơ sở
            holder.txt_NameFactory.setText( history.getFactory().getNameFactory());

            // Gán địa chỉ cơ sở
            holder.txt_AddressFactory.setText( history.getFactory().getAddressFactory());

            // Gán mô tả của lịch sử
            holder.txt_DesProduct.setText( history.getDescriptionHistory());

            // Gán ngày của lịch sử
            holder.txt_Date_History.setText(history.getDateHistory());

            // Gán ảnh cho lịch sử

            // Sự kiện khi click vào item history
            holder.layout_History.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListenerHistory.onClickHistoryItem(history);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        if (historyList != null)
        {
            return historyList.size();
        }
        return 0;
    }

    public static class  HistoryViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout layout_History;
        private final TextView txt_Date_History, txt_TypeFactory, txt_NameFactory, txt_AddressFactory, txt_DesProduct;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            layout_History          = itemView.findViewById(R.id.layout_History);
            txt_Date_History        = itemView.findViewById(R.id.txt_Date_History);
            txt_TypeFactory         = itemView.findViewById(R.id.txt_TypeFactory);
            txt_NameFactory         = itemView.findViewById(R.id.txt_NameFactory);
            txt_AddressFactory      = itemView.findViewById(R.id.txt_AddressFactory);
            txt_DesProduct          = itemView.findViewById(R.id.txt_DesProduct);
        }
    }
}
