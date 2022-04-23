package com.example.apptxng.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.adapter.Product_Adapter;
import com.example.apptxng.bottom_dialog.BottomDialogChoiceFactory;
import com.example.apptxng.bottom_dialog.BottomDialogTypeFactory;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.presenter.IProduct;
import com.example.apptxng.presenter.Product_Presenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Products_Admin_Fragment extends Fragment implements Product_Adapter.IProductAdapterListener, IProduct {
    private RecyclerView recycler_Product_Admin;
    private ImageView img_Filter_Product_Admin;
    private Product_Adapter productAdapter;
    private Product_Presenter productPresenter;
    private List<Product> productList;
    private List<Category> categoryList;
    private Category categoryTemp;
    private BottomDialogTypeFactory dialogTypeFactory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_products__admin_, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        recycler_Product_Admin      = view.findViewById(R.id.recycler_Product_Admin);
        img_Filter_Product_Admin    = view.findViewById(R.id.img_Filter_Product_Admin);

        // Presenter
        productPresenter            = new Product_Presenter(this,requireActivity());

        // Adapter
        productAdapter              = new Product_Adapter(requireActivity(),this);
        recycler_Product_Admin.setAdapter(productAdapter);

        // Layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(),2, LinearLayoutManager.VERTICAL,false);
        recycler_Product_Admin.setLayoutManager(layoutManager);

        // Call API
        loadData();
    }

    private void loadData() {
        // Get products
        productPresenter.getProductAdmin();

        // Get category
        Common.api.getAllCategory()
                .enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                        categoryList = response.body();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        
        img_Filter_Product_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogChoiceType();
            }
        });
    }

    // Hiển thị bottom sheet dialog lựa chọn loại cơ sở
    private void showBottomDialogChoiceType() {
         dialogTypeFactory = new BottomDialogTypeFactory(categoryList, new ChoiceType_Adapter.IListenerChoiceType() {
            @Override
            public void onClickChoiceType(Object obj) {
                categoryTemp = (Category) obj;

                productAdapter.setProductList(filterProduct());
                dialogTypeFactory.dismiss();
            }
        });
        dialogTypeFactory.show(requireActivity().getSupportFragmentManager(),dialogTypeFactory.getTag());
    }

    private List<Product> filterProduct()
    {
        ProgressDialog dialog = Common.createProgress(requireActivity());
        dialog.show();
        List<Product> listNew = new ArrayList<>();
        for(Product p : productList)
        {
            if( p.getCategory().getIdCategory() ==  categoryTemp.getIdCategory())
            {
                listNew.add(p);
            }
        }
        dialog.dismiss();
        return listNew;
    }


    @Override
    public void onClickProduct(Product product) {
        Bundle bundleProduct = new Bundle();
        bundleProduct.putSerializable("product",product);
        Intent intent = new Intent(requireActivity(), Detail_Product_Customer_Activity.class);
        intent.putExtras(bundleProduct);
        startActivity(intent);
    }

    @Override
    public void getProducts(List<Product> list) {
        productList = list;
        productAdapter.setProductList(productList);
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }
}