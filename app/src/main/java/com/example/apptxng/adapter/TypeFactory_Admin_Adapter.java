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
import com.example.apptxng.model.TypeFactory;

import java.util.List;

public class TypeFactory_Admin_Adapter extends RecyclerView.Adapter<TypeFactory_Admin_Adapter.LinkedViewHolder> {

    private List<TypeFactory> linkList;
    private final IListenerLinked listenerLinked;

    public interface IListenerLinked
    {
        /*
        * Bắt hành vị click vào item balance;
        * */
        void onClickLinked(TypeFactory typeFactory);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLinkList(List<TypeFactory> list)
    {
        /*
        * Gán list cho adapter
        * */
        this.linkList = list;
        notifyDataSetChanged();
    }

    public TypeFactory_Admin_Adapter(IListenerLinked listenerLinked) {
        this.listenerLinked = listenerLinked;
    }


    @NonNull
    @Override
    public LinkedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linked_admin,parent,false);
        return new LinkedViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinkedViewHolder holder, int position) {

        TypeFactory typeFactory = linkList.get(position);

        if (typeFactory != null)
        {
            holder.txt_ID_Linked_Admin.setText("ID: " + typeFactory.getIdTypeFactory());
            holder.txt_Name_Linked_Admin.setText("Loại cơ sở: " + typeFactory.getNameTypeFactory());

            holder.layout_Linked_Admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerLinked.onClickLinked(typeFactory);
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
        if (linkList != null)
        {
            return linkList.size();
        }
        return 0;
    }

    public static class LinkedViewHolder extends RecyclerView.ViewHolder {
        private final TextView txt_ID_Linked_Admin;
        private final TextView txt_Name_Linked_Admin;
        private final LinearLayout layout_Linked_Admin;
        public LinkedViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ID_Linked_Admin    = itemView.findViewById(R.id.txt_ID_Linked_Admin);
            txt_Name_Linked_Admin  = itemView.findViewById(R.id.txt_Name_Linked_Admin);
            layout_Linked_Admin    = itemView.findViewById(R.id.layout_Linked_Admin);
            }
        }
    }

