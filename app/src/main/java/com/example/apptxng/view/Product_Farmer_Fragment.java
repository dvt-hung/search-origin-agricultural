package com.example.apptxng.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.adapter.Product_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.presenter.IProductFarmer;
import com.example.apptxng.presenter.Product_Farmer_Presenter;

import java.util.ArrayList;
import java.util.List;


public class Product_Farmer_Fragment extends Fragment implements IProductFarmer, Product_Adapter.IProductAdapterListener {

    private View viewProduct;

    private TextView txt_Empty_Product_Farmer;
    private RecyclerView recycler_Product_Farmer;
    private ImageView img_Add_Product_Farmer;
    private Product_Farmer_Presenter productFarmerPresenter;
    private Product_Adapter productAdapter;
    private Dialog dialogProduct;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewProduct =  inflater.inflate(R.layout.fragment_product__farmer_, container, false);
        
        // Init view: Khởi tạo view + ánh xạ 
        initView();

        return viewProduct;
    }


    @Override
    public void onResume() {
        super.onResume();

        /*
        * 1. Thêm sản phẩm: Chuyển sang activity thêm sản phẩm
        * 2. Hiển thị danh sách sản phẩm
        * */


        // 1. Thêm sản phẩm: Chuyển sang activity thêm
        img_Add_Product_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),InsertProductFarmerActivity.class));
            }
        });

        // 2. Hiển thị danh sách sản phẩm
        productFarmerPresenter.getProducts();
    }


    //  INIT VIEW: ánh xạ view
    private void initView() {
        txt_Empty_Product_Farmer    = viewProduct.findViewById(R.id.txt_Empty_Product_Farmer);
        recycler_Product_Farmer     = viewProduct.findViewById(R.id.recycler_Product_Farmer);
        img_Add_Product_Farmer      = viewProduct.findViewById(R.id.img_Add_Product_Farmer);
        productFarmerPresenter      = new Product_Farmer_Presenter(this);
        productAdapter              = new Product_Adapter(viewProduct.getContext(),this);


        // Set layout manager cho recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(viewProduct.getContext(),2, LinearLayoutManager.VERTICAL,false);
        recycler_Product_Farmer.setLayoutManager(layoutManager);

        // Set adapter cho recycler view
        recycler_Product_Farmer.setAdapter(productAdapter);

    }

    // OVERRIDE METHOD: interface IProductFarmer
    @Override
    public void getProduct(List<Product> list) {
        if (list.size() > 0)
        {
            txt_Empty_Product_Farmer.setVisibility(View.GONE); // Nếu có sản phẩm thì sẽ ẩn text view đi
            productAdapter.setProductList(list);
        }
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // OVERRIDE METHOD: interface IProductAdapterListener
    @Override
    public void onClickProduct(Product product) {
        // Khi click vào sản phẩm sẽ chuyển sang activity chi tiết sản phẩm
        Bundle bundleProduct = new Bundle();
        bundleProduct.putSerializable("product",product);

        Intent intentDetailProduct = new Intent(requireActivity(),DetailProductFarmerActivity.class);
        intentDetailProduct.putExtra("b_product",bundleProduct);
        startActivity(intentDetailProduct);
    }



}