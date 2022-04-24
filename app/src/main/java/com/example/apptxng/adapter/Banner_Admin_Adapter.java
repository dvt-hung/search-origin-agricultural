package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.model.Banner;

import java.util.List;

public class Banner_Admin_Adapter extends RecyclerView.Adapter<Banner_Admin_Adapter.BannerViewHolder> {

    private final Context context;
    private final IBannerListener iBannerListener;
    List<?> bannerList;

    public interface IBannerListener{
        void onClickDelete(Banner banner);
        void onClickDeleteUri(int position);
    }

    public Banner_Admin_Adapter(Context context, IBannerListener iBannerListener) {
        this.context = context;
        this.iBannerListener = iBannerListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBannerList(List<?> list)
    {
        this.bannerList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_admin,parent,false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        if(bannerList.get(position).getClass() == Banner.class)
        {
            Banner banner = (Banner) bannerList.get(position);
            // Gán giá trị
            Glide.with(context).load(banner.getImage_Banner()).error(R.drawable.logo).into(holder.img_Banner);

            // Sự kiện xóa ảnh
            holder.txt_Delete_Banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iBannerListener.onClickDelete(banner);
                }
            });
        }
        else
        {
            Glide.with(context).load(bannerList.get(position)).error(R.drawable.logo).into(holder.img_Banner);
            holder.txt_Delete_Banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iBannerListener.onClickDeleteUri(holder.getAdapterPosition());
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (bannerList != null)
        {
            return bannerList.size();
        }
        return 0;
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img_Banner;
        private final TextView txt_Delete_Banner;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Delete_Banner       = itemView.findViewById(R.id.txt_Delete_Banner);
            img_Banner              = itemView.findViewById(R.id.img_Banner);
        }
    }
}
