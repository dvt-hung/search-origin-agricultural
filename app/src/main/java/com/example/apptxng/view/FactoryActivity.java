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
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.presenter.Factory_Presenter;
import com.example.apptxng.presenter.IFactory;
import com.example.apptxng.presenter.ITypeFactory;
import com.example.apptxng.presenter.TypeFactory_Presenter;

import java.util.ArrayList;
import java.util.List;

public class FactoryActivity extends AppCompatActivity implements Factory_Adapter.IListenerFactory, IFactory, ITypeFactory {

    private ImageView img_Close_Factory,img_Add_Factory,img_Filter,img_Clear_Filter;
    private Factory_Presenter presenter;
    private Factory factoryTemp;
    private List<Factory> factories;
    private TextView txt_Name_TypeFactory,txt_Name_Factory, txt_NameOwn_Factory, txt_Address_Factory, txt_Phone_Factory, txt_Web_Factory;

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
        presenter                       = new Factory_Presenter(this,this);
        factories                       = new ArrayList<>();

        // Tải dữ liệu
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
        txt_Address_Factory.setText(factoryTemp.getAddressFactory());
        txt_Phone_Factory.setText(factoryTemp.getPhoneFactory());
        txt_Web_Factory.setText(factoryTemp.getWebFactory());
        txt_NameOwn_Factory.setText(factoryTemp.getOwnerFactory());
    }

    private void initEvents() {

        // 1. Close Button: Đóng activity
        img_Close_Factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });


    }

    // Lọc danh sách cơ sở theo loại cơ sở đã chọn
    private void filterTypeFactory(TypeFactory type) {
        int idTypeTemp = type.getIdTypeFactory();
        List<Factory> factoryTemp = new ArrayList<>();
        for(Factory f : factories)
        {
            if (f.getType_factory().getIdTypeFactory() == idTypeTemp)
            {
                factoryTemp.add(f);
            }
        }
    }


    // OVERRIDE METHOD: interface IListenerFactory
    @Override
    public void onClickItemFactory(Factory factory) {
        showDialogOptionFactory(factory);
    }



    @Override
    public void getFactory(List<Factory> list) {
            factories = list;
    }

    @Override
    public void infoFactory(Factory factory) {
        factoryTemp = factory;
        displayValue();

    }

    @Override
    public void getTypeFactory(List<TypeFactory> list) {
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
    private void showDialogOptionFactory(Factory factory) {
        // Tạo và cài đặt layout cho dialog
        Dialog dialogOptions = new Dialog(this);
        dialogOptions.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOptions.setContentView(R.layout.dialog_bottom_option);
        dialogOptions.getWindow().setGravity(Gravity.BOTTOM);
        dialogOptions.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogOptions.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khởi tạo và ảnh xạ view trong dialog Option
        Button btn_Update_DialogOption      = dialogOptions.findViewById(R.id.btn_Update_DialogOption);
        Button btn_Delete_DialogOption      = dialogOptions.findViewById(R.id.btn_Delete_DialogOption);
        Button btn_Cancel_DialogOption      = dialogOptions.findViewById(R.id.btn_Cancel_DialogOption);

        /*
         * 1. Chọn vào option Update: Chuyển sang activity cập nhật
         * 2. Chọn vào option Delete: Hiện thị dialog yêu cầu xác nhận lần cuối
         * 3. Chọn vào option Cancel: Đóng dialog
         * */

        // Hiện dialog
        dialogOptions.show();

        // 1. Update Button: Chuyển sang activity update
        btn_Update_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Chuyển đối tượng factory đã chọn sang activity update
                Bundle bundleUpdate = new Bundle();
                bundleUpdate.putSerializable("factory", factory);
                Intent intent = new Intent(new Intent(FactoryActivity.this, UpdateFactoryActivity.class));
                intent.putExtras(bundleUpdate);
                startActivity(intent);

                dialogOptions.dismiss();
            }
        });

        // 2. Delete Button: Mở dialog xác nhận
        btn_Delete_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDelete(factory);
                dialogOptions.dismiss();
            }
        });

        // 3. Cancel Button: Đóng dialog
        btn_Cancel_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOptions.dismiss();
            }
        });
    }


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