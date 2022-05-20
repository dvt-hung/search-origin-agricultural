package com.example.apptxng.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.adapter.Factory_Adapter;
import com.example.apptxng.adapter.Product_Adapter;
import com.example.apptxng.bottom_dialog.BottomDialogChoiceFactory;
import com.example.apptxng.bottom_dialog.BottomDialogTypeFactory;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.Factory_Presenter;
import com.example.apptxng.presenter.IFactory;
import com.example.apptxng.presenter.IProduct;
import com.example.apptxng.presenter.Product_Presenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Products_Admin_Fragment extends Fragment implements Product_Adapter.IProductAdapterListener,IFactory,IProduct, Factory_Adapter.IListenerFactory {
    private ImageView img_Filter_Product_Admin;
    private Product_Adapter productAdapter;
    private Product_Presenter productPresenter;
    private List<Product> productList;
    private List<Category> categoryList;
    private List<Factory> factoryList;
    private Category categoryTemp;
    private BottomDialogTypeFactory dialogCategory;
    private Factory_Presenter factoryPresenter;
    private BottomDialogChoiceFactory dialogFarmer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_products__admin_, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recycler_Product_Admin = view.findViewById(R.id.recycler_Product_Admin);
        img_Filter_Product_Admin    = view.findViewById(R.id.img_Filter_Product_Admin);

        // Presenter
        productPresenter            = new Product_Presenter(this,requireActivity());
        factoryPresenter            = new Factory_Presenter(this,requireActivity());
        // Adapter
        productAdapter              = new Product_Adapter(requireActivity(),this);
        recycler_Product_Admin.setAdapter(productAdapter);

        // Layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(),2, LinearLayoutManager.VERTICAL,false);
        recycler_Product_Admin.setLayoutManager(layoutManager);

        // Call API
        loadData();
    }

    // TẢI DỮ LIỆU BAN ĐẦU
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

        // Get factory - filter
        factoryPresenter.getFactoryFilterByAdmin();
    }

    @Override
    public void onResume() {
        super.onResume();
        
        img_Filter_Product_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOptionFilter();
            }
        });
    }

    private void showDialogOptionFilter() {
        // Config dialog
        Dialog dialogInfo = new Dialog(requireActivity());
        dialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInfo.setContentView(R.layout.dialog_filter_admin);
        dialogInfo.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogInfo.getWindow().setGravity(Gravity.CENTER);

        // Init view
        TextView txt_Filter_ByCategory          = dialogInfo.findViewById(R.id.txt_Filter_ByCategory);
        TextView txt_Filter_ByFarmer            = dialogInfo.findViewById(R.id.txt_Filter_ByFarmer);

        // Show dialog
        dialogInfo.show();

        /*
        * 1. Filter By Category: Hiển thị bottom dialog lựa chọn category để thống kê
        * */
        txt_Filter_ByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogCategory();
                dialogInfo.dismiss();
            }
        });
        
        /*
        * 2. Filter By Farmer: Hiển thị bottom dialog lựa chọn nhà vườn
        * */
        txt_Filter_ByFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogFarmer();
                dialogInfo.dismiss();
            }
        });
    }

    // Lấy ra danh sách nông dân
    private void showBottomDialogFarmer() {
        dialogFarmer = new BottomDialogChoiceFactory(factoryList,this);
        dialogFarmer.show(requireActivity().getSupportFragmentManager(), dialogFarmer.getTag());
    }

    // Hiển thị bottom sheet dialog lựa chọn loại cơ sở
    private void showBottomDialogCategory() {
        dialogCategory = new BottomDialogTypeFactory(categoryList, new ChoiceType_Adapter.IListenerChoiceType() {
            @Override
            public void onClickChoiceType(Object obj) {
                categoryTemp = (Category) obj;
                productAdapter.setProductList(filterProduct());
                dialogCategory.dismiss();
            }
        });
        dialogCategory.show(requireActivity().getSupportFragmentManager(),dialogCategory.getTag());
    }

    // Lọc sản phẩm theo danh mục
    private List<Product> filterProduct(){
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


    // ************* OVERRIDE METHOD: ADAPTER PRODUCT ******************
    @Override
    public void onClickProduct(Product product) {
        Bundle bundleProduct = new Bundle();
        bundleProduct.putSerializable("product",product);
        Intent intent = new Intent(requireActivity(), Detail_Product_Customer_Activity.class);
        intent.putExtras(bundleProduct);
        startActivity(intent);
    }

    // ************* OVERRIDE METHOD: I-PRODUCT ******************
    @Override
    public void getProducts(List<Product> list) {
        productList = list;
        productAdapter.setProductList(productList);
    }


    // ************* OVERRIDE METHOD: I-FACTORY ******************
    @Override
    public void getFactory(List<Factory> list) {
        factoryList = list;
    }

    @Override
    public void infoFactory(Factory factory) {

    }

    @Override
    public void Exception(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void emptyValue() {

    }

    @Override
    public void success(String message) {

    }

    @Override
    public void failed(String message) {

    }

    // ************* OVERRIDE METHOD: BOTTOM DIALOG FACTORY ******************
    @Override
    public void onClickItemFactory(Factory factory) {
        productPresenter.getProductFarmer(factory.getIdUser());
        dialogFarmer.dismiss();
    }
}