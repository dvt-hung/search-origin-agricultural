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
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.presenter.Factory_Presenter;
import com.example.apptxng.presenter.IFactory;
import com.example.apptxng.presenter.ITypeFactory;
import com.example.apptxng.presenter.TypeFactory_Presenter;

import java.util.List;

public class UpdateFactoryActivity extends AppCompatActivity implements ITypeFactory, IFactory {
    private ImageView img_Back_UpdateFactory, img_UpdateFactory;
    private TextView txt_ChoiceTypeFactory, txt_ResultChoiceTypeFactory;
    private EditText edt_Name_Factory, edt_Phone_Factory, edt_Address_Factory;
    private List<TypeFactory> typeList;
    private Factory_Presenter factoryPresenter;
    private BottomDialogTypeFactory dialogTypeFactory;
    private Factory factoryTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_factory);

        initView();

        // Get intent
        Bundle bundle = getIntent().getExtras();
        factoryTemp = (Factory) bundle.getSerializable("factory");
    }

    private void initView() {

        img_Back_UpdateFactory      = findViewById(R.id.img_Back_UpdateFactory);
        img_UpdateFactory           = findViewById(R.id.img_UpdateFactory);
        txt_ChoiceTypeFactory       = findViewById(R.id.txt_ChoiceTypeFactory_Update);
        txt_ResultChoiceTypeFactory = findViewById(R.id.txt_ResultChoiceTypeFactory_Update);
        edt_Name_Factory            = findViewById(R.id.edt_Name_Factory_Update);
        edt_Phone_Factory           = findViewById(R.id.edt_Phone_Factory_Update);
        edt_Address_Factory         = findViewById(R.id.edt_Address_Factory_Update);
        factoryPresenter            = new Factory_Presenter(this,this);

        TypeFactory_Presenter typeFactory_presenter = new TypeFactory_Presenter(this);


        // Lấy danh sách type factory
        typeFactory_presenter.getTypeFactory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayValue();
        initEvents();
    }

    private void displayValue() {

        // Loại cơ sở
        txt_ResultChoiceTypeFactory.setVisibility(View.VISIBLE);
        txt_ResultChoiceTypeFactory.setText(factoryTemp.getType_factory().getNameTypeFactory());

        // Tên cơ sở
        edt_Name_Factory.setText(factoryTemp.getNameFactory());

        // Địa chị cơ sở
        edt_Address_Factory.setText(factoryTemp.getAddressFactory());

        // Số DT cơ sở
        edt_Phone_Factory.setText(factoryTemp.getPhoneFactory());
    }

    private void initEvents() {
        /*
        * 1. Back Button: Đóng activity
        * 2. Update Button: Cập nhật lại cơ sở
        * 3. Choice Type: Lựa chọn lại loại cơ sở
        * */

        img_Back_UpdateFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img_UpdateFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gán dữ liệu

                // Tên cơ sở
                String name = edt_Name_Factory.getText().toString().trim();
                factoryTemp.setNameFactory(name);

                // Địa chỉ cơ sở
                String address = edt_Address_Factory.getText().toString().trim();
                factoryTemp.setAddressFactory(address);

                // Số DT cơ sở
                String phone = edt_Phone_Factory.getText().toString().trim();
                factoryTemp.setPhoneFactory(phone);

                // Gọi đến presenter
                factoryPresenter.updateFactory(factoryTemp);
            }
        });

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
                factoryTemp.setType_factory((TypeFactory) obj);
                txt_ResultChoiceTypeFactory.setVisibility(View.VISIBLE);
                txt_ResultChoiceTypeFactory.setText(factoryTemp.getType_factory().getNameTypeFactory());
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
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void emptyValue() {
        Toast.makeText(this, R.string.title_error_empty , Toast.LENGTH_SHORT).show();

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