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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.adapter.Factory_Adapter;
import com.example.apptxng.adapter.Images_Adapter;
import com.example.apptxng.bottom_dialog.BottomDialogChoiceFactory;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.History;
import com.example.apptxng.presenter.History_Presenter;
import com.example.apptxng.presenter.IHistory;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertHistoryActivity extends AppCompatActivity implements Factory_Adapter.IListenerFactory, IHistory {

    private ImageView img_Back_History, img_Insert_History, img_History;
    private TextView txt_ChoiceFactory_History, txt_ResultFactory_History;
    private EditText edt_Des_History;
    private RecyclerView recycler_Images_History;
    private Factory factoryTemp, factoryCurrent;
    private History historyTemp;
    private Uri uriTemp;
    private String idProduct;
    private History_Presenter historyPresenter;
    private List<Factory> listTemp = new ArrayList<>();
    private List<Uri> listPhoto = new ArrayList<>();
    private BottomDialogChoiceFactory choiceFactory;
    private Images_Adapter imagesAdapter;

    private final ActivityResultLauncher<Intent> launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null)
                        {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmapCamera = (Bitmap) bundle.get("data");

                            uriTemp = Common.getImageUri(InsertHistoryActivity.this, bitmapCamera);
                            Glide.with(InsertHistoryActivity.this).load(uriTemp).into(img_History);
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
                        Glide.with(InsertHistoryActivity.this).load(uriTemp).into(img_History);
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
        idProduct = getIntent().getStringExtra("idProduct");
    }

    // Init view
    private void initView() {
        img_Back_History            = findViewById(R.id.img_Back_History);
        img_Insert_History          = findViewById(R.id.img_Insert_History);
        img_History                 = findViewById(R.id.img_History);
        txt_ChoiceFactory_History   = findViewById(R.id.txt_ChoiceFactory_History);
        txt_ResultFactory_History   = findViewById(R.id.txt_ResultFactory_History);
        edt_Des_History             = findViewById(R.id.edt_Des_History);
        recycler_Images_History     = findViewById(R.id.recycler_Images_History);
        factoryTemp                 = new Factory();
        historyTemp                 = new History();
        imagesAdapter               = new Images_Adapter(this);
        historyPresenter            = new History_Presenter(this, this);

        loadListFactory();
        recycler_Images_History.setAdapter(imagesAdapter);

        // Layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recycler_Images_History.setLayoutManager(layoutManager);
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

                // Set idProduct cho history
                historyTemp.setIdProduct(idProduct);

                // Ngày của history
                Date date = Calendar.getInstance().getTime();
                String dateHistory = Common.dateFormat.format(date);
                historyTemp.setDateHistory(dateHistory);

                // Set id của history
                String idHistory = "History" + Calendar.getInstance().getTime().getTime();
                historyTemp.setIdHistory(idHistory);

                // Gọi đến Presenter
                historyPresenter.InsertHistory(historyTemp,listPhoto);
            }
        });

        // 3. Text view chọn cơ sở
        txt_ChoiceFactory_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceCurrentFactory();
                //showDialogChoiceFactory();
            }
        });

        // 4. Image view chọn ảnh, chụp ảnh cho nhật ký
        img_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionPickImage();
            }
        });

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
        TedBottomPicker.with(InsertHistoryActivity.this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        if (uriList != null)
                        {
                            recycler_Images_History.setVisibility(View.VISIBLE);
                            listPhoto.addAll(uriList);
                            imagesAdapter.setUriList(listPhoto);
                        }
                    }
                });
    }

    private void showDialogChoiceCurrentFactory()
    {
        Dialog dialogChoice = new Dialog(this);
        dialogChoice.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChoice.setContentView(R.layout.dialog_current_factory);

        Window window = dialogChoice.getWindow();

        if (window != null)
        {
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        dialogChoice.show();

        // Ánh xạ view
        RadioGroup radio_group      = dialogChoice.findViewById(R.id.radio_group);
        /*
        * 1. Current: Set Factory = cơ sở hiện tại
        * 2. Other: Mở dialog lựa chọn cơ sở khác
        * */

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if ( i == R.id.radio_other)
                {
                    showDialogChoiceFactory();
                }
                else 
                {
                    historyTemp.setFactory(factoryCurrent);
                    txt_ResultFactory_History.setVisibility(View.VISIBLE);
                    txt_ResultFactory_History.setText(factoryCurrent.getNameFactory());
                }
                dialogChoice.dismiss();
            }
        });

    }


    // Dialog Choice Factory: Mở dialog hiển thị các cơ sở đã liên kết
    private void showDialogChoiceFactory() {
        choiceFactory = new BottomDialogChoiceFactory(listTemp,this);
        choiceFactory.show(getSupportFragmentManager(),choiceFactory.getTag());
    }

    // Load list factory: Tải dữ liệu danh sách các cơ sở liên kết theo idUser
    private void loadListFactory() {
            ProgressDialog progressDialog = new ProgressDialog(InsertHistoryActivity.this);
            progressDialog.show();
            progressDialog.setMessage("Đang tải dữ liệu...");
            Common.api.getFactory()
                    .enqueue(new Callback<List<Factory>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Factory>> call, @NonNull Response<List<Factory>> response) {

                            assert response.body() != null;
                            for (Factory f : response.body())
                            {
                                if (f.getIdUser().equals(Common.currentUser.getIdUser()))
                                {
                                    factoryCurrent = f;
                                    listTemp.remove(f);
                                }
                                else
                                {
                                    listTemp.add(f);
                                }
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Factory>> call, @NonNull Throwable t) {
                            Toast.makeText(InsertHistoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
    }

    // Sự kiện khi chọn một cơ sở trong danh sách
    @Override
    public void onClickItemFactory(Factory factory) {
        historyTemp.setFactory(factory);
        txt_ResultFactory_History.setVisibility(View.VISIBLE);
        txt_ResultFactory_History.setText(factory.getNameFactory());
        choiceFactory.dismiss();
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
    public void getHistory(List<History> histories) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        factoryTemp = null;
        historyTemp = null;
        uriTemp = null;
        listTemp = null;
    }



}