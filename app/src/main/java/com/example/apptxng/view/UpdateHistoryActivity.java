package com.example.apptxng.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.adapter.Factory_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.History;
import com.example.apptxng.presenter.History_Presenter;
import com.example.apptxng.presenter.IHistory;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateHistoryActivity extends AppCompatActivity implements Factory_Adapter.IListenerFactory, IHistory {

    private ImageView img_Back_History, img_Update_History, img_History;
    private TextView txt_ChoiceFactory_History, txt_ResultFactory_History;
    private EditText edt_Des_History;
    private History historyTemp;
    private List<Factory> listTemp;
    private BottomDialogChoiceFactory choiceFactory;
    private Dialog dialogUpdateHistory;
    private Uri uriTemp;
    private History_Presenter presenter;

    private final ActivityResultLauncher<Intent> launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null)
                    {
                        Bundle bundle = result.getData().getExtras();
                        Bitmap bitmapCamera = (Bitmap) bundle.get("data");

                        uriTemp = Common.getImageUri(UpdateHistoryActivity.this, bitmapCamera);
                        Glide.with(UpdateHistoryActivity.this).load(uriTemp).into(img_History);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> launcherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null)
                    {
                        uriTemp = result.getData().getData();
                        Glide.with(UpdateHistoryActivity.this).load(uriTemp).into(img_History);
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_history);

        initView();

    }

    // Init view
    private void initView() {
        img_Back_History            = findViewById(R.id.img_Back_History);
        img_Update_History          = findViewById(R.id.img_Update_History);
        img_History                 = findViewById(R.id.img_History);
        txt_ChoiceFactory_History   = findViewById(R.id.txt_ChoiceFactory_History);
        txt_ResultFactory_History   = findViewById(R.id.txt_ResultFactory_History);
        edt_Des_History             = findViewById(R.id.edt_Des_History);
        loadListFactory();
        // Get idProduct: nhận idProduct từ Detail Product gửi qua
        Bundle bundleHistory = getIntent().getExtras();
        historyTemp = (History) bundleHistory.getSerializable("history");
        displayValue();

        // Khởi tạo Presenter
        presenter = new History_Presenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();


        initEvents();
    }


    // Hiển thị dữ liệu ban đầu của nhật ký
    private void displayValue() {

        // Ảnh của nhật ký

        // Cơ sở hiện tại
        txt_ResultFactory_History.setText(historyTemp.getFactory().getNameFactory());

        // Mô tả hiện tại
        edt_Des_History.setText(historyTemp.getDescriptionHistory());
    }

    // Khai báo các sự kiện trong activity
    private void initEvents() {

        /*
        * 1. Back Button: Tắt activity
        * 2. Update Button: Tiến hành cập nhật nhật ký
        * 3. Choice Factory: Mở bottom dialog lựa chọn các cơ sở
        * 4. Image: Khi chọn vào ảnh sẽ hiển thị lựa chọn camera hoặc thư viện
        * */

        // 1. Back Button
        img_Back_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Update Button
        img_Update_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Set dữ liệu
                String des = edt_Des_History.getText().toString().trim();
                historyTemp.setDescriptionHistory(des);

                presenter.UpdateHistory(historyTemp,uriTemp);
            }
        });

        // 3. Choice factory
        txt_ChoiceFactory_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceFactory();
            }
        });

        // 4. Image Click
        img_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPickImage();
            }
        });

    }

    private void showDialogPickImage() {
        dialogUpdateHistory  = new Dialog(this);
        dialogUpdateHistory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdateHistory.setContentView(R.layout.dialog_bottom_pick_image);

        Window window = dialogUpdateHistory.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        dialogUpdateHistory.show();

        // Khai báo ánh xạ view trong dialog
        Button btn_Pick_Camera      = dialogUpdateHistory.findViewById(R.id.btn_Pick_Camera);
        Button btn_Pick_Gallery     = dialogUpdateHistory.findViewById(R.id.btn_Pick_Gallery);
        Button btn_Pick_Cancel      = dialogUpdateHistory.findViewById(R.id.btn_Pick_Cancel);


        /*
         * 1. Pick Camera: Kiểm tra quyền camera và ghi vào bộ nhớ. Nếu cho phép thì mở camera lên và nhận kết quả chụp xong
         * 2. Pick Gallery: Kiểm tra quyền truy cập thư viện. Nếu cho phép thì mở thư viện và nhận kết quả
         * 3. Pick Cancel: Hủy dialog
         * */

        // 1. Pick Camera
        btn_Pick_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermissionOpenCamera();
                dialogUpdateHistory.dismiss();

            }
        });

        // 2. Pick Gallery
        btn_Pick_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOpenGallery();
                dialogUpdateHistory.dismiss();
            }
        });

        // 3. Pick Cancel
        btn_Pick_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdateHistory.dismiss();
            }
        });
    }

    // Check permission: Kiểm tra quyền truy cập vào thư viện ảnh
    private void checkPermissionOpenGallery() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        openGallery();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(UpdateHistoryActivity.this, "Bạn chưa cho phép truy cập thư viện ảnh", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    // Check permission: Kiểm tra quyền camera và ghi vào bộ nhớ
    private void checkPermissionOpenCamera()
    {
        Dexter.withContext(this).withPermissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted())
                        {
                            openCamera();
                        }
                        else
                        {
                            Toast.makeText(UpdateHistoryActivity.this, "Bạn chưa cho phép mở camera", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    private void openCamera() {
        Intent intentCamera = new Intent();
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        launcherCamera.launch(intentCamera);
    }


    // Open gallery: nếu đã cho phép thì mở thư viện ảnh
    private void openGallery() {
        Intent intentGallery = new Intent();
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_PICK);
        launcherGallery.launch(intentGallery);
    }


    // Dialog Choice Factory: Mở dialog hiển thị các cơ sở đã liên kết
    private void showDialogChoiceFactory() {
        choiceFactory = new BottomDialogChoiceFactory(listTemp,this);
        choiceFactory.show(getSupportFragmentManager(),choiceFactory.getTag());
    }

    private void loadListFactory() {
        ProgressDialog progressDialog = new ProgressDialog(UpdateHistoryActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Đang tải dữ liệu...");
        Common.api.getFactory()
                .enqueue(new Callback<List<Factory>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Factory>> call, @NonNull Response<List<Factory>> response) {
                        listTemp = response.body();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Factory>> call, @NonNull Throwable t) {
                        Toast.makeText(UpdateHistoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }




    @Override
    public void onClickItemFactory(Factory factory) {
        historyTemp.setFactory(factory);
        txt_ResultFactory_History.setText(factory.getNameFactory());
        choiceFactory.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        historyTemp = null;
        choiceFactory = null;
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

    @Override
    public void exceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void emptyValue() {
        Toast.makeText(this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getHistory(List<History> histories) {

    }
}