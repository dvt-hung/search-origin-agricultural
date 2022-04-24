package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.CategoryViewHolder> {

    private List<Category> categories;
    private final Context context;
    private final ICategoryListener iCategoryListener;


    public Category_Adapter(Context context, ICategoryListener iCategoryListener) {
        this.context = context;
        this.iCategoryListener = iCategoryListener;
    }

    public interface ICategoryListener{
        void onClickCategory(Category category);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setCategories(List<Category> category)
    {
        this.categories = category;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_customer,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        if (category != null)
        {
            holder.txt_Name_Category.setText(category.getNameCategory());
            Glide.with(context).load(category.getImageCategory()).error(R.drawable.logo).into(holder.img_Category);

            // Event click
            holder.layout_Category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iCategoryListener.onClickCategory(category);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (categories != null)
        {
            return categories.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img_Category;
        private final TextView txt_Name_Category;
        private final LinearLayout layout_Category;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Category            = itemView.findViewById(R.id.img_Category);
            txt_Name_Category       = itemView.findViewById(R.id.txt_Name_Category);
            layout_Category         = itemView.findViewById(R.id.layout_Category);
        }
    }
}
