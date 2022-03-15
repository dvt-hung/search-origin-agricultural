package com.example.apptxng.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertHistoryProductActivity extends AppCompatActivity implements Factory_Adapter.IListenerFactory, IHistory {

    private ImageView img_Back_History, img_Insert_History, img_History;
    private TextView txt_ChoiceFactory_History, txt_ResultFactory_History;
    private EditText edt_Des_History;
    private Factory_Adapter factoryAdapter;
    private Dialog dialogInsertHistory;
    private Factory factoryTemp;
    private History historyTemp;
    private Uri uriTemp;
    private int idProduct;
    private History_Presenter historyPresenter;

    private final ActivityResultLauncher<Intent> launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null)
                        {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmapCamera = (Bitmap) bundle.get("data");

                            uriTemp = Common.getImageUri(InsertHistoryProductActivity.this, bitmapCamera);
                            Glide.with(InsertHistoryProductActivity.this).load(uriTemp).into(img_History);
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
                        Glide.with(InsertHistoryProductActivity.this).load(uriTemp).into(img_History);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_history_product);

        // init view: Ánh xạ view
        initView();

        // Get idProduct: nhận idProduct từ Detail Product gửi qua
        idProduct = getIntent().getIntExtra("idProduct",0);
        Log.e("id", "onCreate: " + idProduct );
    }

    // Init view
    private void initView() {
        img_Back_History            = findViewById(R.id.img_Back_History);
        img_Insert_History          = findViewById(R.id.img_Insert_History);
        img_History                 = findViewById(R.id.img_History);
        txt_ChoiceFactory_History   = findViewById(R.id.txt_ChoiceFactory_History);
        txt_ResultFactory_History   = findViewById(R.id.txt_ResultFactory_History);
        edt_Des_History             = findViewById(R.id.edt_Des_History);
        factoryAdapter              = new Factory_Adapter(this);
        factoryTemp                 = new Factory();
        historyTemp                 = new History();

        historyPresenter            = new History_Presenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initEvents();
    }



    // Khai báo các sự kiện trong activity
    private void initEvents() {
        /*
        * 1. Sự kiện của Back button: tắt activity
        * 2. Sự kiện của Insert button: gọi đến presenter kiểm tra dữ liệu và thêm vào csdl
        * 3. Sự kiển khi click vào text view chọn cơ sở: Mở dialog hiện thị danh sách cơ sở đã liên kết
        * 4. Sự kiện khi click vào image view: Check permission. Sau đó mở thư viện ảnh lên
        * */

        // 1. Back button
        img_Back_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        // 2. Insert button
        img_Insert_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Truyền dữ liệu vào biến factory
                // Set mô tả cho history
                String des = edt_Des_History.getText().toString().trim();
                historyTemp.setDescriptionHistory(des);

                // Set idFactory cho history
                historyTemp.setIdFactory(factoryTemp.getIdFactory());

                // Set idProduct cho history
                historyTemp.setIdProduct(idProduct);



                // Ngày của history
                Date date = Calendar.getInstance().getTime();
                String dateHistory = Common.dateFormat.format(date);
                historyTemp.setDateHistory(dateHistory);


                // Gọi đến Presenter
                historyPresenter.InsertHistory(historyTemp,uriTemp);

            }
        });

        // 3. Text view chọn cơ sở
        txt_ChoiceFactory_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceFactory();
            }
        });

        // 4. Image view chọn ảnh, chụp ảnh cho nhật ký
        img_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPickImage();
            }
        });

    }

    // Dialog pick image: mở dialog có 3 lựa chọn: Lấy ảnh từ camera, thư viện hoặc hủy
    private void showDialogPickImage() {
        dialogInsertHistory  = new Dialog(this);
        dialogInsertHistory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInsertHistory.setContentView(R.layout.dialog_bottom_pick_image);

        Window window = dialogInsertHistory.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        dialogInsertHistory.show();

        // Khai báo ánh xạ view trong dialog
        Button btn_Pick_Camera      = dialogInsertHistory.findViewById(R.id.btn_Pick_Camera);
        Button btn_Pick_Gallery     = dialogInsertHistory.findViewById(R.id.btn_Pick_Gallery);
        Button btn_Pick_Cancel      = dialogInsertHistory.findViewById(R.id.btn_Pick_Cancel);


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
                dialogInsertHistory.dismiss();

            }
        });

        // 2. Pick Gallery
        btn_Pick_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOpenGallery();
                dialogInsertHistory.dismiss();
            }
        });

        // 3. Pick Cancel
        btn_Pick_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInsertHistory.dismiss();
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
                        Toast.makeText(InsertHistoryProductActivity.this, "Bạn chưa cho phép truy cập thư viện ảnh", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(InsertHistoryProductActivity.this, "Bạn chưa cho phép mở camera", Toast.LENGTH_SHORT).show();
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

        dialogInsertHistory  = new Dialog(this);
        dialogInsertHistory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInsertHistory.setContentView(R.layout.dialog_choice_factory);

        Window window = dialogInsertHistory.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
        }

        dialogInsertHistory.show();

        // Khai báo, ánh xạ view trong dialog
        TextView txt_Insert_Factory             = dialogInsertHistory.findViewById(R.id.txt_Insert_Factory);
        RecyclerView recycler_Choice_Factory    = dialogInsertHistory.findViewById(R.id.recycler_Choice_Factory);

        recycler_Choice_Factory.setAdapter(factoryAdapter);

        /*
        * 1. Load danh sách cơ sở
        * 2. Chuyển sang activity cơ sở
        * */

        // 1. Chọn vào text view thêm cơ sở
        txt_Insert_Factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InsertHistoryProductActivity.this, FactoryActivity.class));
                dialogInsertHistory.dismiss();
            }
        });

        // 2. Load danh sách và chọn Factory
            // Set layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recycler_Choice_Factory.setLayoutManager(layoutManager);

        loadListFactory();

    }

    // Load list factory: Tải dữ liệu danh sách các cơ sở liên kết theo idUser
    private void loadListFactory() {
            ProgressDialog progressDialog = new ProgressDialog(InsertHistoryProductActivity.this);
            progressDialog.show();
            progressDialog.setMessage("Đang tải dữ liệu...");
            Common.api.getFactory(Common.currentUser.getIdUser())
                    .enqueue(new Callback<List<Factory>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Factory>> call, @NonNull Response<List<Factory>> response) {
                            factoryAdapter.setFactoryList(response.body());
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Factory>> call, @NonNull Throwable t) {
                            Toast.makeText(InsertHistoryProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
    }

    // Sự kiện khi chọn một cơ sở trong danh sách
    @Override
    public void onClickItemFactory(Factory factory) {
        factoryTemp = factory;
        txt_ResultFactory_History.setVisibility(View.VISIBLE);
        txt_ResultFactory_History.setText(factory.getNameFactory());
        dialogInsertHistory.dismiss();
    }

    // OVERRIED METHOD: interface: IHistory
    @Override
    public void successMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void failedMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void exceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void emptyValue() {
        Toast.makeText(this, R.string.title_error_empty, Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        idProduct = 0;
        factoryTemp = null;
        historyTemp = null;
        dialogInsertHistory = null;
        uriTemp = null;
    }
}