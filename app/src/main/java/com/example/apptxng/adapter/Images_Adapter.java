package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;

import java.util.List;

public class Images_Adapter extends RecyclerView.Adapter<Images_Adapter.ImageViewHolder> {

    private List<Uri> uriList;
    private final Context context;
    public interface IListenerImages{
        void onClickDeleteImage(int position);
    }


    public Images_Adapter(Context context) {
        this.context = context;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setUriList(List<Uri> list)
    {
        this.uriList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder,int position) {
        int p = holder.getAdapterPosition();
        Uri u = uriList.get(position);
        Glide.with(context).load(u).into(holder.imageItem);

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriList.remove(p);
                notifyItemRemoved(p);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (uriList != null)
        {
            return uriList.size();
        }
        return 0;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageItem, imageDelete;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem           = itemView.findViewById(R.id.imageItem);
            imageDelete         = itemView.findViewById(R.id.imageDelete);
        }
    }
}
