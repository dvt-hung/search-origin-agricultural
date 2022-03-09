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
    private List<Product> productList = new ArrayList<>();
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
        showDialogOption(product);
    }


    // Show dialog option
    private void showDialogOption(Product product) {
        Dialog dialogOptions = new Dialog(viewProduct.getContext());
        dialogOptions.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOptions.setContentView(R.layout.dialog_bottom_product_farmer);
        dialogOptions.getWindow().setGravity(Gravity.BOTTOM);
        dialogOptions.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogOptions.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo ảnh xạ view cho dialog
        Button btn_Detail_OptionProduct     = dialogOptions.findViewById(R.id.btn_Detail_OptionProduct);
        Button btn_History_OptionProduct    = dialogOptions.findViewById(R.id.btn_History_OptionProduct);
        Button btn_Update_OptionProduct     = dialogOptions.findViewById(R.id.btn_Update_OptionProduct);
        Button btn_Delete_OptionProduct     = dialogOptions.findViewById(R.id.btn_Delete_OptionProduct);

        // Hiện dialog
        dialogOptions.show();

        /*
        * 1. Detail Button: Mở dialog hiển thị tất cả các thông tin của Sản Phẩm
        * 2. History Button: Mở sang activity History
        * 3. Update Button: Mở sang activity Update
        * 4. Delete Button: Mở dialog xác nhận xóa
        * */

        // 1. Detail Button
        btn_Detail_OptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDetail(product);
                dialogOptions.cancel();
            }
        });


        // 2. History Button
        btn_History_OptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 3. Update Button
        btn_Update_OptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 4. Delete Button
        btn_Delete_OptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void showDialogDetail(Product product) {
        dialogProduct = new Dialog(viewProduct.getContext());
        dialogProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProduct.setCanceledOnTouchOutside(false);
        dialogProduct.setContentView(R.layout.dialog_details_product);
        dialogProduct.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khởi tạo và ánh xạ view cho dialog
        TextView txt_Name_Detail_Product            = dialogProduct.findViewById(R.id.txt_Name_Detail_Product);
        TextView txt_Price_Detail_Product           = dialogProduct.findViewById(R.id.txt_Price_Detail_Product);
        TextView txt_Quantity_Detail_Product        = dialogProduct.findViewById(R.id.txt_Quantity_Detail_Product);
        TextView txt_QuantitySold_Detail_Product    = dialogProduct.findViewById(R.id.txt_QuantitySold_Detail_Product);
        TextView txt_Date_Detail_Product            = dialogProduct.findViewById(R.id.txt_Date_Detail_Product);
        TextView txt_Des_Detail_Product             = dialogProduct.findViewById(R.id.txt_Des_Detail_Product);
        ImageView img_Detail_Product                = dialogProduct.findViewById(R.id.img_Detail_Product);
        Button btn_Cancel_Detail_Product            = dialogProduct.findViewById(R.id.btn_Cancel_Detail_Product);

        dialogProduct.show();

        // Gán dữ liệu cho view
        txt_Name_Detail_Product.setText(product.getNameProduct());

        txt_Price_Detail_Product.setText(Common.numberFormat.format(product.getPriceProduct()));

        txt_Quantity_Detail_Product.setText(String.valueOf(product.getQuantityProduct()));

        txt_QuantitySold_Detail_Product.setText(String.valueOf(product.getQuantitySold()));

        txt_Date_Detail_Product.setText(product.getDateProduct());

        txt_Des_Detail_Product.setText(product.getDescriptionProduct());

        Glide.with(viewProduct.getContext()).load(product.getImageProduct()).error(R.drawable.logo).into(img_Detail_Product);

        btn_Cancel_Detail_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProduct.cancel();
            }
        });
    }
}