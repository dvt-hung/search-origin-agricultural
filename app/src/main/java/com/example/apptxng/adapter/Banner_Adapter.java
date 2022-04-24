package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.model.Banner;

import java.util.List;

public class Banner_Adapter extends RecyclerView.Adapter<Banner_Adapter.BannerCustomerViewHolder>{

    private List<Banner> list;
    private final Context context;


    @SuppressLint("NotifyDataSetChanged")
    public Banner_Adapter( Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Banner> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public BannerCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner,parent,false);
        return new BannerCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerCustomerViewHolder holder, int position) {
        Banner banner = list.get(position);

        Glide.with(context).load(banner.getImage_Banner()).error(R.drawable.logo).into(holder.img_Banner);
    }

    @Override
    public int getItemCount() {
        if(list != null)
        {
            return list.size();
        }
        return 0;
    }

    public static class BannerCustomerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img_Banner;
        public BannerCustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Banner = itemView.findViewById(R.id.img_Banner);
        }
    }
}
