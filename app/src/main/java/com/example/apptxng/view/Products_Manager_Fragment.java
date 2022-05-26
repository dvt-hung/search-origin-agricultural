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


public class Products_Manager_Fragment extends Fragment implements Product_Adapter.IProductAdapterListener, IProduct {

    private TextView txt_Empty_Product_Manager;
    private Product_Adapter productAdapter;
    private ImageView img_Scan_Product_Manager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products__manager_, container, false);
        
        //Ánh xạ view
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recycler_Product_Manager       = view.findViewById(R.id.recycler_Product_Manager);
        txt_Empty_Product_Manager                   = view.findViewById(R.id.txt_Empty_Product_Manager);
        img_Scan_Product_Manager                    = view.findViewById(R.id.img_Scan_Product_Manager);
        // Presenter
        Product_Presenter productPresenter          = new Product_Presenter(this, requireActivity());

        // Adapter product
        productAdapter                              = new Product_Adapter(requireActivity(),this);
        recycler_Product_Manager.setAdapter(productAdapter);

        // Set layout manager cho recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(),2, LinearLayoutManager.VERTICAL,false);
        recycler_Product_Manager.setLayoutManager(layoutManager);

        productPresenter.getProductFarmer(Common.currentUser.getIdUser());
    }

    @Override
    public void onResume() {
        super.onResume();
        img_Scan_Product_Manager.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onClickProduct(Product product) {

        // Khi click vào sản phẩm sẽ chuyển sang activity chi tiết sản phẩm
        Bundle bundleProduct = new Bundle();
        bundleProduct.putSerializable("product",product);

        Intent intentDetailProduct = new Intent(requireActivity(), Detail_Product_Activity.class);
        intentDetailProduct.putExtras(bundleProduct);
        startActivity(intentDetailProduct);
    }


    // Interface: IProduct
    @Override
    public void getProducts(List<Product> list) {
        if (list != null)
        {
            productAdapter.setProductList(list);
            txt_Empty_Product_Manager.setVisibility(View.GONE);
        }
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }
}