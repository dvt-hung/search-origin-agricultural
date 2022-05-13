package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.adapter.Factory_Adapter;
import com.example.apptxng.bottom_dialog.BottomDialogTypeFactory;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.presenter.Factory_Presenter;
import com.example.apptxng.presenter.IFactory;
import com.example.apptxng.presenter.ITypeFactory;
import com.example.apptxng.presenter.TypeFactory_Presenter;

import java.util.ArrayList;
import java.util.List;

public class FactoryActivity extends AppCompatActivity implements  IFactory {

    private ImageView img_Close_Factory;
    private Factory_Presenter presenter;
    private Factory factoryTemp;
    private TextView txt_Name_TypeFactory,txt_Name_Factory, txt_NameOwn_Factory, txt_Address_Factory, txt_Phone_Factory, txt_Web_Factory;
    private Button btn_Change_Info_Factory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory);

        // Ánh xạ view
        initView();
    }

    private void initView() {
        img_Close_Factory               = findViewById(R.id.img_Close_Factory);
        txt_Name_TypeFactory            = findViewById(R.id.txt_Name_TypeFactory_Farmer);
        txt_Name_Factory                = findViewById(R.id.txt_Name_Factory_Farmer);
        txt_NameOwn_Factory             = findViewById(R.id.txt_NameOwn_Factory_Farmer);
        txt_Address_Factory             = findViewById(R.id.txt_Address_Factory_Farmer);
        txt_Phone_Factory               = findViewById(R.id.txt_Phone_Factory_Farmer);
        txt_Web_Factory                 = findViewById(R.id.txt_Web_Factory_Farmer);
        btn_Change_Info_Factory         = findViewById(R.id.btn_Change_Info_Factory);
        presenter                       = new Factory_Presenter(this,this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.getFactoryByID();

        // init events: Khai báo các event trong activity
        initEvents();
    }

    private void displayValue() {
        txt_Name_TypeFactory.setText(factoryTemp.getType_factory().getNameTypeFactory());
        txt_Name_Factory.setText(factoryTemp.getNameFactory());

        // Address
        txt_Address_Factory.setText(displayInfoValueString(factoryTemp.getAddressFactory()));
        txt_Phone_Factory.setText(displayInfoValueString(factoryTemp.getPhoneFactory()));
        txt_Web_Factory.setText(displayInfoValueString(factoryTemp.getWebFactory()));
        txt_NameOwn_Factory.setText(displayInfoValueString(factoryTemp.getOwnerFactory()));
    }

    // Check check and display value
    public String displayInfoValueString(String value)
    {
        if (value == null || value.equals(" ") || value.isEmpty())
        {
            return "Đang cập nhật";
        }
        return value;
    }

    private void initEvents() {

        // 1. Close Button: Đóng activity
        img_Close_Factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });

        // 2. Change info button: Chuyển sang activity thay đổi info
        btn_Change_Info_Factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đối tượng Factory sang activity
                Bundle bundle = new Bundle();
                bundle.putSerializable("factory", factoryTemp);
                Intent intent = new Intent(FactoryActivity.this,ChangeInfoFactoryActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

    }


    @Override
    public void getFactory(List<Factory> list) {
    }

    @Override
    public void infoFactory(Factory factory) {
        factoryTemp = factory;
        displayValue();
    }


    @Override
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void emptyValue() {

    }

    @Override
    public void success(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    // Show dialog: Lựa chọn chỉnh sửa hoặc xóa cơ sở liên kết

    // Dialog xác nhận là có xóa hay không
    private void showDialogDelete(Factory factory) {
        // Khởi tạo dialog
        Dialog dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setContentView(R.layout.dialog_delete);
        dialogDelete.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo và ánh xạ view của dialog update
        Button btn_Cancel_DeleteCategory_Dialog     = dialogDelete.findViewById(R.id.btn_Cancel_Delete_Dialog);
        Button btn_Confirm_DeleteCategory_Dialog    = dialogDelete.findViewById(R.id.btn_Confirm_Delete_Dialog);
        TextView txt_Title_Delete_Dialog            = dialogDelete.findViewById(R.id.txt_Title_Delete_Dialog);
        TextView txt_Message_Delete_Dialog          = dialogDelete.findViewById(R.id.txt_Message_Delete_Dialog);

        // Gán Title, Message cho dialog
        txt_Title_Delete_Dialog.setText(R.string.title_delete_factory);
        txt_Message_Delete_Dialog.setText(R.string.title_question_delete_factory);

        // Hiển thị dialog
        dialogDelete.show();

        /*
         * 1. Khi chọn Confirm Button: Sẽ xóa đi đơn vị tính này
         * 2. Khu chọn Cancel Button: Sẽ tắt đi dialog
         * */

        //1. Confirm Button
        btn_Confirm_DeleteCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteFactory(factory.getIdFactory());
                dialogDelete.cancel();
            }
        });

        // 2. Cancel Button
        btn_Cancel_DeleteCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDelete.cancel();
            }
        });


    }
}