package com.example.apptxng.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Product_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.presenter.IProduct;
import com.example.apptxng.presenter.Product_Presenter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;


public class Product_Farmer_Fragment extends Fragment implements IProduct, Product_Adapter.IProductAdapterListener {

    private View viewProduct;

    private TextView txt_Empty_Product_Farmer;
    private ImageView img_Add_Product_Farmer,img_Scan_Product_Farmer;
    private Product_Presenter productPresenter;
    private Product_Adapter productAdapter;
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
                startActivity(new Intent(requireActivity(), InsertProductActivity.class));
            }
        });

        // 2. Hiển thị danh sách sản phẩm
        productPresenter.getProductFarmer(Common.currentUser.getIdUser());


        // 3. Scan: Chuyển sang activity scan
        img_Scan_Product_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionCamera();
            }
        });
    }

    // Kiểm tra quyền Camera
    private void checkPermissionCamera()
    {
            Dexter.withContext(requireContext())
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
            @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                startActivity(new Intent(requireActivity(), ScanActivity.class));
            }
            @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                /* ... */}
            @Override public void onPermissionRationaleShouldBeShown(PermissionRequest
            permission, PermissionToken token) {
                token.continuePermissionRequest();/* ... */}
        }).check();
    }


    //  INIT VIEW: ánh xạ view
    private void initView() {
        txt_Empty_Product_Farmer    = viewProduct.findViewById(R.id.txt_Empty_Product_Farmer);
        RecyclerView recycler_Product_Farmer = viewProduct.findViewById(R.id.recycler_Product_Farmer);
        img_Add_Product_Farmer      = viewProduct.findViewById(R.id.img_Add_Product_Farmer);
        img_Scan_Product_Farmer     = viewProduct.findViewById(R.id.img_Scan_Product_Farmer);
        productPresenter            = new Product_Presenter(this,requireActivity());
        productAdapter              = new Product_Adapter(viewProduct.getContext(),this);


        // Set layout manager cho recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(viewProduct.getContext(),2, LinearLayoutManager.VERTICAL,false);
        recycler_Product_Farmer.setLayoutManager(layoutManager);

        // Set adapter cho recycler view
        recycler_Product_Farmer.setAdapter(productAdapter);

    }

    // OVERRIDE METHOD: interface IProduct

    @Override
    public void getProducts(List<Product> list) {
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
        Intent intentDetailProduct = new Intent(requireActivity(), Detail_Product_Activity.class);
        intentDetailProduct.putExtras(bundleProduct);
        startActivity(intentDetailProduct);
    }



}