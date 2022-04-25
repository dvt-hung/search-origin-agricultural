package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Product_Adapter;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Product_By_Category_Activity extends AppCompatActivity implements Product_Adapter.IProductAdapterListener {

    private Category categoryReceive;
    private ImageView img_Back;
    private Product_Adapter product_adapter;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_by_category);

        // Get category
        categoryReceive = (Category) getIntent().getExtras().getSerializable("category");

        initView();
    }

    private void initView() {
        RecyclerView recycler_Products  = findViewById(R.id.recycler_Products);
        img_Back                        = findViewById(R.id.img_Back);
        title                           = findViewById(R.id.title);
        // Set title
        title.setText(categoryReceive.getNameCategory());

        // Adapter
        product_adapter                 = new Product_Adapter(this,this);
        recycler_Products.setAdapter(product_adapter);
        
        // Get products
        getProducts();
    }

    private void getProducts() {
        // Call API
        Common.api.getProductByCategory(categoryReceive.getIdCategory())
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        product_adapter.setProductList(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClickProduct(Product product) {
        // Chuyển sang activity chi tiết sản phẩm
        Bundle bundleProduct = new Bundle();
        bundleProduct.putSerializable("product",product);

        Intent intent = new Intent(Product_By_Category_Activity.this,Detail_Product_Customer_Activity.class);
        intent.putExtras(bundleProduct);
        startActivity(intent);
    }
}