package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Images_Adapter;
import com.example.apptxng.model.ImageHistory;
import com.example.apptxng.presenter.IImageHistory;
import com.example.apptxng.presenter.ImageHistory_Presenter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class ImageHistoryActivity extends AppCompatActivity implements Images_Adapter.IListenerImages,IImageHistory {

    private ImageView img_Back_Image_History, img_Insert_Image_History;
    private Button btn_Images_New;
    private Images_Adapter imagesAdapter,imagesAdapterNew;
    private ImageHistory_Presenter imageHistoryPresenter;
    private final List<Uri> listPhoto = new ArrayList<>();
    private List<ImageHistory> imageHistories = new ArrayList<>();
    private String idHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_history);

        // init view: Ánh xạ view
        initView();

        // Nhận idHistory
        idHistory = getIntent().getStringExtra("idHistory");
    }

    private void initView() {
        RecyclerView recycler_Images_History = findViewById(R.id.recycler_Images_History);
        RecyclerView recycler_Images_New_History = findViewById(R.id.recycler_Images_New_History);
        img_Back_Image_History              = findViewById(R.id.img_Back_Image_History);
        img_Insert_Image_History            = findViewById(R.id.img_Insert_Image_History);
        btn_Images_New                      = findViewById(R.id.btn_Images_New);
        imagesAdapter                       = new Images_Adapter(this,this);
        imagesAdapterNew                    = new Images_Adapter(this);
        imageHistoryPresenter               = new ImageHistory_Presenter(this,this);
        // Adapter cho recycler view
        recycler_Images_History.setAdapter(imagesAdapter);
        recycler_Images_New_History.setAdapter(imagesAdapterNew);

        // Layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManagerNew = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recycler_Images_History.setLayoutManager(layoutManager);
        recycler_Images_New_History.setLayoutManager(layoutManagerNew);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Load hình ảnh ban đầu
        imageHistoryPresenter.getImageHistory(idHistory);

        // 1. btn_Images_New: Chọn hình ảnh mới
        btn_Images_New.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionPickImage();
            }
        });

        // 2. Đóng activity
        img_Back_Image_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 3. Thêm ảnh mới vào database
        img_Insert_Image_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertImageHistory();
            }
        });
    }

    private void insertImageHistory() {
        // Kiểm tra đã chọn hình ảnh mới chưa
        if (listPhoto.isEmpty())
        {
            Toast.makeText(this, R.string.no_images, Toast.LENGTH_SHORT).show();
        }
        else
        {
            imageHistoryPresenter.insertImageHistory(listPhoto,idHistory);
        }

    }

    private void checkPermissionPickImage() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                /* ... */
                if (report.areAllPermissionsGranted())
                {
                    openPickImages();
                }
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void openPickImages() {
        TedBottomPicker.with(ImageHistoryActivity.this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        listPhoto.addAll(uriList);
                        imagesAdapterNew.setUriList(listPhoto);
                    }
                });
    }


    @Override
    public void getImages(List<ImageHistory> images) {
        imageHistories = images;
        imagesAdapter.setUriList(imageHistories);
    }

    @Override
    public void exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ImageHistoryActivity.this, Detail_Product_Customer_Activity.class);
        setResult(RESULT_OK,intent);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void failed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickImage(ImageHistory imageHistory) {
        showDialogDelete(imageHistory);
    }

    // Show dialog delete: Xác nhận có muốn xóa hay không
    private void showDialogDelete(ImageHistory imageHistory) {
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
                if (imageHistories.size() == 1)
                {
                    Toast.makeText(ImageHistoryActivity.this, "Nhật ký phải có ít nhất 1 ảnh", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    imageHistoryPresenter.deleteImageHistory(imageHistory);
                }
                dialogDelete.dismiss();
            }
        });
    }
}