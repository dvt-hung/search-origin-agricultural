package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Banner_Adapter;
import com.example.apptxng.model.Banner;
import com.example.apptxng.presenter.Banner_Presenter;
import com.example.apptxng.presenter.IBanner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class InsertBannerActivity extends AppCompatActivity implements Banner_Adapter.IBannerListener, IBanner {

    private ImageView img_Insert_Banner, img_Close_Banner,img_Open_InsertBanner;
    private Banner_Adapter bannerAdapter;
    private final List<Uri> uriListBanner = new ArrayList<>();
    private Banner_Presenter bannerPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_banner);

        // init view
        initView();
        //
        openPickImages();
    }

    private void initView() {
        img_Close_Banner                    = findViewById(R.id.img_Close_Banner);
        img_Insert_Banner                   = findViewById(R.id.img_Insert_Banner);
        img_Open_InsertBanner               = findViewById(R.id.img_Open_InsertBanner);
        RecyclerView recycler_InsertBanner  = findViewById(R.id.recycler_InsertBanner);

        // Presenter
        bannerPresenter                     = new Banner_Presenter(this,this);

        // Adapter
        bannerAdapter                       = new Banner_Adapter(this,this);
        recycler_InsertBanner.setAdapter(bannerAdapter);
    }

    private void openPickImages() {
        TedBottomPicker.with(InsertBannerActivity.this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        uriListBanner.addAll(uriList);
                        bannerAdapter.setBannerList(uriListBanner);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 1. Close button
        img_Close_Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Mở thư viên ảnh lại
        img_Open_InsertBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPickImages();
            }
        });

        // 3. Insert button: Thêm ảnh vào DB
        img_Insert_Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bannerPresenter.insertBanner(uriListBanner);
            }
        });
    }



    // IBannerListener
    @Override
    public void onClickDelete(Banner banner) {

    }

    @Override
    public void onClickDeleteUri(int position) {
        uriListBanner.remove(position);
        bannerAdapter.notifyItemRemoved(position);
    }

    @Override
    public void getBanner(List<Banner> bannerList) {

    }

    @Override
    public void exception(String message) {

    }

    @Override
    public void emptyValue() {
        Toast.makeText(this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void failedMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}