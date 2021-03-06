package com.example.apptxng.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.ChangeInfo_Farmer_Presenter;
import com.example.apptxng.presenter.IChangeInfo_Farmer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ChangeInfoActivity extends AppCompatActivity implements IChangeInfo_Farmer {

    private ImageView img_ChangeInfo_Farmer,img_Back_ChangeInfo_Farmer;
    private EditText edt_Owner_ChangeInfo_Farmer,edt_Address_ChangeInfo_Farmer, edt_Email_ChangeInfo_Farmer;
    private Button btn_Confirm_ChangeInfo_Farmer;
    private User userTemp;
    private Uri uriChangeInfo;
    private ChangeInfo_Farmer_Presenter ChangeInfoPresenter;
    private ProgressDialog progressChangeInfo;

    private final ActivityResultLauncher<Intent> intentGalleryInfo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null)
                    {
                        uriChangeInfo = result.getData().getData();
                        img_ChangeInfo_Farmer.setImageURI(uriChangeInfo);
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        
        // init view: ??nh x??? view
        initView();
    }

    private void initView() {
        img_ChangeInfo_Farmer               = findViewById(R.id.img_ChangeInfo_Farmer);
        img_Back_ChangeInfo_Farmer          = findViewById(R.id.img_Back_ChangeInfo_Farmer);
        edt_Owner_ChangeInfo_Farmer         = findViewById(R.id.edt_Owner_ChangeInfo_Farmer);
        edt_Address_ChangeInfo_Farmer       = findViewById(R.id.edt_Address_ChangeInfo_Farmer);
        btn_Confirm_ChangeInfo_Farmer       = findViewById(R.id.btn_Confirm_ChangeInfo_Farmer);
        edt_Email_ChangeInfo_Farmer         = findViewById(R.id.edt_Email_ChangeInfo_Farmer);
        ChangeInfoPresenter                 = new ChangeInfo_Farmer_Presenter(this,this);
        progressChangeInfo                  = new ProgressDialog(this);
        progressChangeInfo.setMessage("Vui l??ng ch???...");

        userTemp = Common.currentUser;

        displayValueInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        

        // init events: C??c s??? trong activity
        initEvents();
    }

    // Hi???n th??? d??? li???u ban ?????u c???a User
    private void displayValueInfo() {
        Glide.with(this).load(userTemp.getImage()).error(R.drawable.logo).into(img_ChangeInfo_Farmer);


        //Edit text: T??n
        edt_Owner_ChangeInfo_Farmer.setText(displayValueToEditText(userTemp.getName()));

        //Edit text: ?????a ch???
        edt_Address_ChangeInfo_Farmer.setText(displayValueToEditText(userTemp.getAddress()));

        //Edit text: email
        edt_Email_ChangeInfo_Farmer.setText(displayValueToEditText(userTemp.getEmail()));
    }

    // Ki???m tra d??? li???u c?? null hay kh??ng. N???u null th?? hi???n th??? kho???ng tr???ng
    private String displayValueToEditText(String val)
    {
        if (val == null || val.equals(" "))
        {
            return "";
        }
        return val;
    }

    // Khai b??o c??c s??? ki???n trong activity
    private void initEvents() {

        // 1. Back Button: ????ng activity
        img_Back_ChangeInfo_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Ch???n ???nh m???i cho user
        img_ChangeInfo_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOpenGallery();
            }
        });

        // 3. Confirm Button: Ti???n h??nh c???p nh???t
        btn_Confirm_ChangeInfo_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String owner        = edt_Owner_ChangeInfo_Farmer.getText().toString().trim();
                String address      = edt_Address_ChangeInfo_Farmer.getText().toString().trim();
                String email        = edt_Email_ChangeInfo_Farmer.getText().toString().trim();

                userTemp.setName(owner);
                userTemp.setAddress(address);
                userTemp.setEmail(email);

                ChangeInfoPresenter.changeInfo_Farmer(userTemp,uriChangeInfo);
                progressChangeInfo.show();
            }
        });
    }

    // Xin quy???n truy c???p th?? vi???n
    private void checkPermissionOpenGallery() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        openGallery();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    // M??? th?? vi???n ???nh
    private void openGallery() {
        Intent intentGallery = new Intent();
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_PICK);
        intentGalleryInfo.launch(intentGallery);
    }

    @Override
    public void emptyValue() {
        Toast.makeText(this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
        progressChangeInfo.cancel();
    }

    @Override
    public void success(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressChangeInfo.cancel();
        finish();
        uriChangeInfo = null;
        Common.currentUser = userTemp;
    }

    @Override
    public void failed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressChangeInfo.cancel();
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressChangeInfo.cancel();

    }
}