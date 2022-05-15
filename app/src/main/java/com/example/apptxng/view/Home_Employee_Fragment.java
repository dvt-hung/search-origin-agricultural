package com.example.apptxng.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Product_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.presenter.IProduct;
import com.example.apptxng.presenter.Product_Presenter;

import java.util.List;


public class Home_Employee_Fragment extends Fragment implements IProduct, Product_Adapter.IProductAdapterListener {

    private RecyclerView recycler_Product_Employee;
    private Product_Presenter productPresenter;
    private Product_Adapter productAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewEmployee = inflater.inflate(R.layout.fragment_home__employee_, container, false);

        initView(viewEmployee);
        return viewEmployee;
    }

    private void initView(View viewEmployee) {
        recycler_Product_Employee       = viewEmployee.findViewById(R.id.recycler_Product_Employee);
        productPresenter                = new Product_Presenter(this,requireActivity());
        productAdapter                  = new Product_Adapter(requireActivity(),this);

        // Set adapter cho Recycler view
        recycler_Product_Employee.setAdapter(productAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        // Load danh sách sản phẩm của Employee
        productPresenter.getProductEmployee(Common.currentUser.getIdUser());
    }

    @Override
    public void getProducts(List<Product> list) {
        productAdapter.setProductList(list);
    }

    @Override
    public void Exception(String message) {

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
}