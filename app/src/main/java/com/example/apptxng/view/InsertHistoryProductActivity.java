package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Factory_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertHistoryProductActivity extends AppCompatActivity implements Factory_Adapter.IListenerFactory {

    private ImageView img_Back_History, img_Insert_History, img_History;
    private TextView txt_ChoiceFactory_History, txt_ResultFactory_History;
    private EditText edt_Des_History;
    private Factory_Adapter factoryAdapter;
    private Dialog dialogChoiceFactory;
    private Factory factoryTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_history_product);

        // init view: Ánh xạ view
        initView();

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

            }
        });

    }



    // Dialog Choice Factory: Mở dialog hiển thị các cơ sở đã liên kết
    private void showDialogChoiceFactory() {

        dialogChoiceFactory  = new Dialog(this);
        dialogChoiceFactory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChoiceFactory.setContentView(R.layout.dialog_choice_factory);

        Window window = dialogChoiceFactory.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
        }

        dialogChoiceFactory.show();

        // Khai báo, ánh xạ view trong dialog
        TextView txt_Insert_Factory             = dialogChoiceFactory.findViewById(R.id.txt_Insert_Factory);
        RecyclerView recycler_Choice_Factory    = dialogChoiceFactory.findViewById(R.id.recycler_Choice_Factory);

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
                dialogChoiceFactory.dismiss();
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
        dialogChoiceFactory.dismiss();
    }
}