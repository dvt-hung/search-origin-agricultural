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
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.ProductViewHolder>{

    private List<Product> productList;
    private final Context context;
    private final IProductAdapterListener iListener;

    // Interface: Sự kiện click khi chọn vào product
    public interface IProductAdapterListener{
        void onClickProduct(Product product);
    }


    public Product_Adapter(Context context, IProductAdapterListener iListener) {
        this.context = context;
        this.iListener = iListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductList(List<Product> list)
    {
        this.productList = list;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        if (product != null)
        {
            holder.txt_Name_Product.setText(product.getNameProduct());
            holder.txt_Price_Product.setText(Common.numberFormat.format(product.getPriceProduct()));
            Glide.with(context).load(product.getImageProduct()).into(holder.img_Product);

            //Sự kiện click product
            holder.layout_Product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListener.onClickProduct(product);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (productList != null)
        {
            return productList.size();
        }
        return 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout layout_Product;
        private final ImageView img_Product;
        private final TextView txt_Name_Product;
        private final TextView txt_Price_Product;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            layout_Product      = itemView.findViewById(R.id.layout_Product);
            img_Product         = itemView.findViewById(R.id.img_Product);
            txt_Name_Product    = itemView.findViewById(R.id.txt_Name_Product);
            txt_Price_Product   = itemView.findViewById(R.id.txt_Price_Product);
        }
    }
}
