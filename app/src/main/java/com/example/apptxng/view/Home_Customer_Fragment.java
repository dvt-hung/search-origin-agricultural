package com.example.apptxng.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Banner_Adapter;
import com.example.apptxng.adapter.Category_Adapter;
import com.example.apptxng.adapter.Product_Adapter;
import com.example.apptxng.model.Banner;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.presenter.Banner_Presenter;
import com.example.apptxng.presenter.IBanner;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home_Customer_Fragment extends Fragment implements  Category_Adapter.ICategoryListener, Product_Adapter.IProductAdapterListener {

    private Banner_Adapter banner_Adapter;
    private ViewPager2 viewPager_Banner;
    private List<Banner> banners;
    private Category_Adapter category_adapter;
    private Product_Adapter product_adapter;

    private final Handler handlerBanner = new Handler();
    private final Runnable runnableBanner = new Runnable() {
        @Override
        public void run() {
            if (viewPager_Banner.getCurrentItem() == banners.size() - 1)
            {
                viewPager_Banner.setCurrentItem(0);
            }
            else
            {
                viewPager_Banner.setCurrentItem(viewPager_Banner.getCurrentItem() + 1);
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home__customer_, container, false);

        initView(view);
        return view;
    }


    private void initView(View view) {
        viewPager_Banner                            = view.findViewById(R.id.viewPager_Banner);
        CircleIndicator3 circle_Banner              = view.findViewById(R.id.circle_Banner);
        RecyclerView recycler_category_Customer     = view.findViewById(R.id.recycler_category_Customer);
        RecyclerView recycler_Product_Hot           = view.findViewById(R.id.recycler_Product_Hot);

        // Adapter Product
        product_adapter                             = new Product_Adapter(requireActivity(),this);
        recycler_Product_Hot.setAdapter(product_adapter);
        recycler_Product_Hot.setNestedScrollingEnabled(false);
        // Get Product
        getProducts();


        // Adapter Category
        category_adapter                            = new Category_Adapter(requireActivity(),this);
        recycler_category_Customer.setAdapter(category_adapter);

        // Get category
        getListCategory();
        
        // Adapter view pager
        banner_Adapter                      = new Banner_Adapter(requireActivity());
        viewPager_Banner.setAdapter(banner_Adapter);

        // Get banner
        getBanner();
        // Set circle cho view pager
        circle_Banner.setViewPager(viewPager_Banner);

        // Auto slide
        viewPager_Banner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handlerBanner.removeCallbacks(runnableBanner);
                handlerBanner.postDelayed(runnableBanner,3000);
            }
        });
    }

    private synchronized void getProducts() {
        Common.api.getProductHot()
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

    private synchronized void getListCategory() {
        // Call API get category
        Common.api.getAllCategory()
                .enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                        category_adapter.setCategories(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {

                    }
                });
    }

    private synchronized void getBanner()
    {
        Common.api.getBanner()
                .enqueue(new Callback<List<Banner>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Banner>> call, @NonNull Response<List<Banner>> response) {
                        banners = response.body();
                        banner_Adapter.setList(banners);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Banner>> call, @NonNull Throwable t) {

                    }
                });
    }


    // ************* CATEGORY ADAPTER ***************
    @Override
    public void onClickCategory(Category category) {

    }

    // ************* PRODUCT ADAPTER ***************
    @Override
    public void onClickProduct(Product product) {

    }
}