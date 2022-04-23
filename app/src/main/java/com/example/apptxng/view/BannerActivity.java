package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Banner_Adapter;
import com.example.apptxng.model.Banner;
import com.example.apptxng.model.ImageHistory;
import com.example.apptxng.presenter.Banner_Presenter;
import com.example.apptxng.presenter.IBanner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class BannerActivity extends AppCompatActivity implements Banner_Adapter.IBannerListener, IBanner {

    private ImageView img_Add_Banner, img_Close_Banner;
    private Banner_Adapter bannerAdapter;
    private Banner_Presenter bannerPresenter;
    private List<Banner> banners;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        
        initView();
    }

    private void initView() {
        img_Add_Banner                  = findViewById(R.id.img_Add_Banner);
        img_Close_Banner                = findViewById(R.id.img_Close_Banner);
        RecyclerView recycler_Banner    = findViewById(R.id.recycler_Banner);

        // Presenter
        bannerPresenter                 = new Banner_Presenter(this,this);

        // Adapter
        bannerAdapter                   = new Banner_Adapter(this,this);
        recycler_Banner.setAdapter(bannerAdapter);


    }


    @Override
    protected void onResume() {
        super.onResume();
        // Call API
        bannerPresenter.getBanner();

        // 1. Close button: Đóng activity
        img_Close_Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Add button: Chuyển sang activity thêm ảnh
        img_Add_Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionPickImage();
            }
        });
    }

    //Check permission
    private void checkPermissionPickImage() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(report.areAllPermissionsGranted())
                {
                    startActivity(new Intent(BannerActivity.this,InsertBannerActivity.class));
                }
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }


    // IBannerListener
    @Override
    public void onClickDelete(Banner banner) {
        showDialogDelete(banner);
    }

    @Override
    public void onClickDeleteUri(int position) {

    }

    private void showDialogDelete(Banner banner) {
        Dialog dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setContentView(R.layout.dialog_delete);
        dialogDelete.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Ánh xạ view
        TextView txt_Title_Delete_Dialog        = dialogDelete.findViewById(R.id.txt_Title_Delete_Dialog);
        TextView txt_Message_Delete_Dialog      = dialogDelete.findViewById(R.id.txt_Message_Delete_Dialog);
        Button btn_Cancel_Delete_Dialog         = dialogDelete.findViewById(R.id.btn_Cancel_Delete_Dialog);
        Button btn_Confirm_Delete_Dialog        = dialogDelete.findViewById(R.id.btn_Confirm_Delete_Dialog);

        txt_Title_Delete_Dialog.setText(R.string.delete_image);
        txt_Message_Delete_Dialog.setText(R.string.question_delete_image);

        dialogDelete.show();

        /*
         * 1. Cancel Button: Tắt dialog xóa đi
         * 2. Confirm Button: Tiển hành xóa sản phẩm. Nếu thành công thì Toast lên và đóng activity
         * */

        // 1. Cancel Button
        btn_Cancel_Delete_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDelete.dismiss();
            }
        });


        // 2. Confirm Button
        btn_Confirm_Delete_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (banners.size() == 1)
                {
                    Toast.makeText(BannerActivity.this, "Có ít nhất 1 ảnh cho banner", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    bannerPresenter.deleteBanner(banner);
                }
                dialogDelete.dismiss();
            }
        });
    }



    // IBanner
    @Override
    public void getBanner(List<Banner> bannerList) {
        banners = bannerList;
        bannerAdapter.setBannerList(banners);
    }

    @Override
    public void exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void emptyValue() {

    }

    @Override
    public void successMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}