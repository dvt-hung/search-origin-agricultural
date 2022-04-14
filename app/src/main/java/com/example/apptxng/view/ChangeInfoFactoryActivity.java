package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.model.Factory;
import com.example.apptxng.presenter.Factory_Presenter;
import com.example.apptxng.presenter.IFactory;

import java.util.List;

public class ChangeInfoFactoryActivity extends AppCompatActivity implements IFactory {

    private EditText edt_Name_Factory,edt_Owner_Factory, edt_Address_Factory,edt_Phone_Factory, edt_Website_Factory;
    private Button btn_Confirm_Change;
    private ImageView img_Close_Factory;
    private Factory factoryTemp;
    private Factory_Presenter factoryPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info_factory);

        // Nhận factory
        factoryTemp = (Factory) getIntent().getExtras().getSerializable("factory");
        // init view: Ánh xạ
        initView();

        // Hiển thị dữ liệu ban đầu
        displayValue();
    }

    private void displayValue() {

        // name factory
       checkValue(edt_Name_Factory,factoryTemp.getNameFactory());

        // owner factory
        checkValue(edt_Owner_Factory,factoryTemp.getOwnerFactory());

        // address factory
        checkValue(edt_Address_Factory,factoryTemp.getAddressFactory());

        // phone factory
        checkValue(edt_Phone_Factory,factoryTemp.getPhoneFactory());

        // owner factory
        checkValue(edt_Website_Factory,factoryTemp.getWebFactory());

    }

    private void checkValue(EditText edt, String val)
    {
        if (val == null || val.isEmpty())
        {
            edt.setText(R.string.title_non_value);
        }
        else
        {
            edt.setText(val);
        }
    }

    private void initView() {
        edt_Name_Factory        = findViewById(R.id.edt_Name_Factory);
        edt_Owner_Factory       = findViewById(R.id.edt_Owner_Factory);
        edt_Address_Factory     = findViewById(R.id.edt_Address_Factory);
        edt_Phone_Factory       = findViewById(R.id.edt_Phone_Factory);
        edt_Website_Factory     = findViewById(R.id.edt_Website_Factory);
        btn_Confirm_Change      = findViewById(R.id.btn_Confirm_Change);
        img_Close_Factory       = findViewById(R.id.img_Close_Factory);

        factoryPresenter        = new Factory_Presenter(this,this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 1. Close button: Đóng activity
        img_Close_Factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Thay đổi thông tin
        btn_Confirm_Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu
                String nameFactory      = edt_Name_Factory.getText().toString().trim();
                String addressFactory   = edt_Address_Factory.getText().toString().trim();
                String phoneFactory     = edt_Phone_Factory.getText().toString().trim();
                String webFactory       = edt_Website_Factory.getText().toString().trim();
                String ownerFactory     = edt_Owner_Factory.getText().toString().trim();

                factoryTemp.setNameFactory(nameFactory);
                factoryTemp.setAddressFactory(addressFactory);
                factoryTemp.setPhoneFactory(phoneFactory);
                factoryTemp.setWebFactory(webFactory);
                factoryTemp.setOwnerFactory(ownerFactory);

                // Gọi đến presenter
                factoryPresenter.updateInfoFactory(factoryTemp);
            }
        });
    }


        // *************** Interface IFACTORY ****************
    @Override
    public void getFactory(List<Factory> list) {

    }

    @Override
    public void infoFactory(Factory factory) {

    }

    @Override
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void emptyValue() {
        Toast.makeText(this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void failed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        factoryTemp = null;
    }

}