package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.ResponsePOST;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateQRCodeActivity extends AppCompatActivity {

    private String valueQR;

    private ImageView img_QRCode, img_Save_QRCode, img_Finish_QRCode;
    private Uri uriQR;
    private boolean allowed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qrcode);

        // Nhận idProduct
        valueQR = getIntent().getStringExtra("idProduct");

        // Init view
        img_QRCode              = findViewById(R.id.img_QRCode);
        img_Save_QRCode         = findViewById(R.id.img_Save_QRCode);
        img_Finish_QRCode       = findViewById(R.id.img_Finish_QRCode);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Tạo QR Code
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            String keyQR = "idProduct?";
            Bitmap bitmapQR = barcodeEncoder.encodeBitmap(keyQR + valueQR, BarcodeFormat.QR_CODE, 560, 560);
            uriQR = Common.getImageUri(CreateQRCodeActivity.this,bitmapQR);
            img_QRCode.setImageURI(uriQR);
        } catch(Exception e) {
            Toast.makeText(CreateQRCodeActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
        }
    }

    // Get uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    protected void onResume() {
        super.onResume();

        addQRCodeProduct();

        // 1. Save image: Save hình ảnh về máy
        img_Save_QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        // 2. Finish: Thêm vào db và đóng activity
        img_Finish_QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Update QR Code của sản phẩm
    private synchronized void addQRCodeProduct() {
        RequestBody idProduct               = RequestBody.create(MediaType.parse("multipart/form-data"),valueQR);

        // Create file from URI
        File file = new File(Common.getRealPathFromURI(uriQR,CreateQRCodeActivity.this));
        String filePath = file.getAbsolutePath();

        String[] arraySplitPath = filePath.split("\\.");

        String nameQR = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];

        RequestBody requestBodyImage        = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part requestPartImage = MultipartBody.Part.createFormData("qrProduct", nameQR,requestBodyImage);

        // Tạo progress dialog
        ProgressDialog dialog = Common.createProgress(CreateQRCodeActivity.this);
        dialog.show();
        // Tạo thread mới để add QR Code
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Call API
                Common.api.addQRCode(idProduct, requestPartImage)
                        .enqueue(new Callback<ResponsePOST>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                                    dialog.dismiss();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                                Toast.makeText(CreateQRCodeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            }
        }).start();
    }

    // Check permission: Kiểm tra quyền được lưu ảnh
    private void check() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        saveQRCode();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        /* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();/* ... */}
                }).check();
    }

    private void saveQRCode() {
        Drawable drawable = img_QRCode.getDrawable();
        // Get the bitmap from drawable object
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        // Initializing a new file
        File file;

        // Get the external storage directory path
        String path = Environment.getExternalStorageDirectory().toString();


        // Create a file to save the image
        file = new File(path, System.currentTimeMillis() +".jpg");

        if (file.exists())
        {
            file.delete();
        }
        try{

            OutputStream stream = null;

            stream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            stream.flush();

            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        Toast.makeText(this, ("Lưu QR Code thành công"), Toast.LENGTH_SHORT).show();
    }
}