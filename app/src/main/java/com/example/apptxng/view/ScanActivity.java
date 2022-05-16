package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity {


    private SurfaceView surface_Scan;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private ImageView img_Back_Scan;
    private TextView txt_Error_Scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_farmer);

        // init view
        initView();
        
    }

    private void initView() {
        img_Back_Scan           = findViewById(R.id.img_Back_Scan);
        surface_Scan            =  findViewById(R.id.surface_Scan);
        txt_Error_Scan          = findViewById(R.id.txt_Error_Scan);

        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();
        scanQR();

    }

    @Override
    protected void onResume() {
        super.onResume();

        img_Back_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void scanQR() {
        // Surface
        surface_Scan.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surface_Scan.getHolder());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        // Barcode
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qr = detections.getDetectedItems();
                if (qr.size() != 0)
                {
                    String result = qr.valueAt(0).displayValue;
                    String[] arrValue = result.split("\\?");
                    surface_Scan.post(new Runnable() {
                        @Override
                        public void run() {
                            if (arrValue[0].equals("idProduct"))
                            {
                                // Call api
                                getProductByIdProduct(arrValue[1]);
                                cameraSource.stop();
                            }
                            else
                            {
                                txt_Error_Scan.setVisibility(View.VISIBLE);
                                txt_Error_Scan.setText(R.string.error_qr);
                            }

                        }
                    });
                }
            }
        });
    }

    private void getProductByIdProduct(String s) {
        ProgressDialog dialog = Common.createProgress(this);
        dialog.show();
        Common.api.getProductByIdProduct(s)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                        Product product = response.body();
                        assert product != null;

                        Bundle bundleProduct = new Bundle();
                        bundleProduct.putSerializable("product",product);

                        //Check type user
                        Intent intent;
                        if(Common.currentUser.getIdRole() == 4 || Common.currentUser.getIdRole() == 1)
                        {
                            intent = new Intent(ScanActivity.this, Detail_Product_Customer_Activity.class);
                        }
                        else
                        {
                            intent = new Intent(ScanActivity.this, Detail_Product_Activity.class);
                        }
                        intent.putExtras(bundleProduct);
                        startActivity(intent);
                        finish();
                            dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                        dialog.dismiss();
                    }
                });
    }

}