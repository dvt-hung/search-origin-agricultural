package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.model.Category;

import java.util.List;

public class category_Admin_Adapter extends RecyclerView.Adapter<category_Admin_Adapter.CategoryAdminViewHolder> {

    private List<Category> categoryList;
    private final Context context;
    private final IListenerCategoryAdmin listenerCategoryAdmin;

    public interface IListenerCategoryAdmin{
        void onClickItemCategoryAdmin(Category category);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListCategory(List<Category> list)
    {
        this.categoryList = list;
        notifyDataSetChanged();
    }

    public category_Admin_Adapter(Context context, IListenerCategoryAdmin listenerCategoryAdmin) {
        this.context = context;
        this.listenerCategoryAdmin = listenerCategoryAdmin;
    }

    @NonNull
    @Override
    public CategoryAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_admin,parent,false);
        return new CategoryAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdminViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category != null)
        {
            holder.name_Category_Admin.setText(category.getNameCategory());
            Glide.with(context).load(category.getImageCategory()).into(holder.img_Category_Admin);
            holder.layout_Category_Admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerCategoryAdmin.onClickItemCategoryAdmin(category);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (categoryList != null)
        {
            return categoryList.size();

        }
        return 0;
    }

    public static class  CategoryAdminViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_Category_Admin;
        ImageView img_Category_Admin;
        TextView name_Category_Admin;
        public CategoryAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_Category_Admin   = itemView.findViewById(R.id.layout_Category_Admin);
            img_Category_Admin      = itemView.findViewById(R.id.img_Category_Admin);
            name_Category_Admin     = itemView.findViewById(R.id.name_Category_Admin);
        }
    }
}
