package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.bottom_dialog.BottomDialogTypeFactory;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.presenter.Factory_Presenter;
import com.example.apptxng.presenter.IFactory;
import com.example.apptxng.presenter.ITypeFactory;
import com.example.apptxng.presenter.TypeFactory_Presenter;

import java.util.List;

public class InsertFactoryActivity extends AppCompatActivity implements ITypeFactory, IFactory {

    private ImageView img_Back_InsertFactory, img_InsertFactory;
    private TextView txt_ChoiceTypeFactory, txt_ResultChoiceTypeFactory;
    private EditText edt_Name_Factory, edt_Phone_Factory, edt_Address_Factory;
    private BottomDialogTypeFactory dialogTypeFactory;
    private List<TypeFactory> typeList;
    private TypeFactory typeFactoryTemp;
    private Factory factoryTemp;
    private Factory_Presenter factoryPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_factory);

        // Khai báo, ánh xạ view
        initView();
    }

    // Ánh xạ view
    private void initView() {
        img_Back_InsertFactory      = findViewById(R.id.img_Back_InsertFactory);
        img_InsertFactory           = findViewById(R.id.img_InsertFactory);
        txt_ChoiceTypeFactory       = findViewById(R.id.txt_ChoiceTypeFactory);
        txt_ResultChoiceTypeFactory = findViewById(R.id.txt_ResultChoiceTypeFactory);
        edt_Name_Factory            = findViewById(R.id.edt_Name_Factory);
        edt_Phone_Factory           = findViewById(R.id.edt_Phone_Factory);
        edt_Address_Factory         = findViewById(R.id.edt_Address_Factory);
        factoryTemp                 = new Factory();

        factoryPresenter            = new Factory_Presenter(this,this);
        TypeFactory_Presenter typeFactory_presenter = new TypeFactory_Presenter(this);


        // Lấy danh sách type factory
        typeFactory_presenter.getTypeFactory();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Khai báo các sự kiện trong activity
        initEvents();
    }

    // Khai báo các sự kiển
    private void initEvents() {

        /*
        * 1. Back button: Tắt activity
        * 2. Insert button: Thêm cơ sở liên kết vào csdl và tắt activity đi
        * 3. Choice Type Factory: Mở bottom dialog hiển thị các loại cơ sở và chọn hiển thị lên text view result
        * */

        // 1. Back Button: Tắt activity
        img_Back_InsertFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Insert Button: Thêm cơ sở
        img_InsertFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gán dữ liệu cho factory temp

                // Type Factory
                factoryTemp.setType_factory(typeFactoryTemp);

                // Name Factory
                String name = edt_Name_Factory.getText().toString().trim();
                factoryTemp.setNameFactory(name);

                // Address Factory
                String address = edt_Address_Factory.getText().toString().trim();
                factoryTemp.setAddressFactory(address);

                // Phone Factory
                String phone = edt_Phone_Factory.getText().toString().trim();
                factoryTemp.setPhoneFactory(phone);

                // ID Factory
                factoryTemp.setIdUser(Common.currentUser.getIdUser());

                // Call presenter
                factoryPresenter.insertFactory(factoryTemp);
            }
        });

        // 3. Choice type factory
        txt_ChoiceTypeFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogChoiceType();
            }
        });
    }


    // Hiển thị bottom sheet dialog lựa chọn loại cơ sở
    private void showBottomDialogChoiceType() {
        dialogTypeFactory = new BottomDialogTypeFactory(typeList, new ChoiceType_Adapter.IListenerChoiceType() {
            @Override
            public void onClickChoiceType(Object obj) {
                dialogTypeFactory.dismiss();
                typeFactoryTemp = (TypeFactory) obj;
                txt_ResultChoiceTypeFactory.setVisibility(View.VISIBLE);
                txt_ResultChoiceTypeFactory.setText(typeFactoryTemp.getNameTypeFactory());
            }
        });
        dialogTypeFactory.show(getSupportFragmentManager(),dialogTypeFactory.getTag());
    }

    @Override
    public void getTypeFactory(List<TypeFactory> list) {
        typeList = list;
    }

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
    public void successMessage(String message) {

    }

    @Override
    public void failedMessage(String message) {

    }

    @Override
    public void emptyValue() {
        Toast.makeText(this, R.string.title_error_empty + "Insert", Toast.LENGTH_SHORT).show();
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
}