package com.example.apptxng.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apptxng.R;


public class Product_Farmer_Fragment extends Fragment {

    private View viewProduct;

    private TextView txt_Empty_Product_Farmer;
    private RecyclerView recycler_Product_Farmer;
    private ImageView img_Add_Product_Farmer;
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
        * 1. Khi chưa có sản phẩm nào thì hiển thị text view. Nếu có thì ẩn text view đi
        * 2. Thêm sản phẩm: Chuyển sang activity thêm sản phẩm
        * 3. Hiển thị danh sách sản phẩm
        * */

        // 1. Hiển thị text view khi không có sản phẩm nào.


        // 2. Thêm sản phẩm: Chuyển sang activity thêm
        img_Add_Product_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),InsertProductFarmerActivity.class));
            }
        });

        // 3. Hiển thị danh sách sản phẩm
    }

    private void initView() {
        txt_Empty_Product_Farmer    = viewProduct.findViewById(R.id.txt_Empty_Product_Farmer);
        recycler_Product_Farmer     = viewProduct.findViewById(R.id.recycler_Product_Farmer);
        img_Add_Product_Farmer      = viewProduct.findViewById(R.id.img_Add_Product_Farmer);

    }
}