package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.TypeFactory_Admin_Adapter;
import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.presenter.ITypeFactory;
import com.example.apptxng.presenter.TypeFactory_Presenter;

import java.util.List;

public class TypeFactory__Activity extends AppCompatActivity implements TypeFactory_Admin_Adapter.IListenerLinked, ITypeFactory {

    private ImageView img_Back_Linked_Admin, img_Add_Linked_Admin;
    private RecyclerView recycler_Linked_Admin;
    private TypeFactory_Admin_Adapter typeFactoryAdminAdapter;
    private TypeFactory_Presenter typeFactoryPresenter;
    private Dialog dialogTypeFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typefactory_admin);

        // Init view: Ảnh xạ view
        initView();

        // Tạo layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Linked_Admin.setLayoutManager(layoutManager);

        // Tạo item decoration cho recycler view
        RecyclerView.ItemDecoration itemDecoration  = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recycler_Linked_Admin.addItemDecoration(itemDecoration);

        // Gán adapter cho recycler
        recycler_Linked_Admin.setAdapter(typeFactoryAdminAdapter);

        // Load danh sách liên kết
        loadLinked();
    }

    // Hiển thị danh sách liên kết
    private void loadLinked() {
        typeFactoryPresenter.getTypeFactory();
    }


    @Override
    protected void onResume() {
        super.onResume();

        /*
        * 1. Back Image: tắt activity Linked_Admin_Activity
        * 2. Add Image: Mở dialog thêm liên kết
        * */

        // 1. Back Image
        img_Back_Linked_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Add Image: Mở dialog
        img_Add_Linked_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddTypeFactory();
            }
        });
    }

    // initView: Ảnh xạ view
    private void initView() {
        img_Back_Linked_Admin       = findViewById(R.id.img_Back_TypeFactory_Admin);
        img_Add_Linked_Admin        = findViewById(R.id.img_Add_TypeFactory_Admin);
        recycler_Linked_Admin       = findViewById(R.id.recycler_TypeFactory_Admin);
        typeFactoryAdminAdapter     = new TypeFactory_Admin_Adapter(this);
        typeFactoryPresenter        = new TypeFactory_Presenter(this,this);
    }


    // Override method: viết lại phương thức của IListenerLinked
    @Override
    public void onClickLinked(TypeFactory typeFactory) {
        showDialogOption(typeFactory);
    }



    // Override method: viết lại phương thức của ILinkedAdmin
    @Override
    public void getTypeFactory(List<TypeFactory> list) {
        typeFactoryAdminAdapter.setLinkList(list);
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        dialogTypeFactory.dismiss();
    }

    @Override
    public void failedMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    // Hiện Dialog thêm loại cơ sở
    private void showDialogAddTypeFactory() {
        // Tạo và cài đặt layout cho dialog
        dialogTypeFactory = new Dialog(this);
        dialogTypeFactory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTypeFactory.setContentView(R.layout.dialog_one_edittext);
        dialogTypeFactory.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogTypeFactory.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txt_Title_Dialog               = dialogTypeFactory.findViewById(R.id.txt_Title_Dialog);
        EditText edt_Content_Dialog             = dialogTypeFactory.findViewById(R.id.edt_Content_Dialog);
        Button btn_Cancel_AddBalance_Dialog     = dialogTypeFactory.findViewById(R.id.btn_Cancel_Dialog);
        Button btn_Confirm_AddBalance_Dialog    = dialogTypeFactory.findViewById(R.id.btn_Confirm_Dialog);

        // Set title dialog
        txt_Title_Dialog.setText(R.string.title_add_type_factory);
        edt_Content_Dialog.setHint(R.string.title_input_name_type_factory);
        // Hiển thị dialog
        dialogTypeFactory.show();

        /*
         * 1. Khi chọn "Xác nhận": Thêm đơn vị tính mới vào csdl
         * 2. Khi chọn "Hủy": Đóng dialog
         * */

        // 1. Xác nhận thêm
        btn_Confirm_AddBalance_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameTypeFactory = edt_Content_Dialog.getText().toString();
                if (nameTypeFactory.isEmpty())
                {
                    Toast.makeText(TypeFactory__Activity.this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    typeFactoryPresenter.addTypeFactory(nameTypeFactory);
                    dialogTypeFactory.cancel();
                }
            }
        });

        // 2. Hủy thêm
        btn_Cancel_AddBalance_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTypeFactory.cancel();
            }
        });
    }

    private void showDialogOption(TypeFactory typeFactory) {
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
         * 1. Chọn vào option Update: Hiển thị dialog nhập tên mới cho đơn vị tính
         * 2. Chọn vào option Delete: Ẩn
         * 3. Chọn vào option Cancel: Đóng dialog
         * */

        // Hiện dialog
        dialogOptions.show();

        //  1. Option Update
        btn_Update_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOptions.dismiss();
                showDialogUpdateTypeFactory(typeFactory);
            }
        });

        // 2. Option Delete
        btn_Delete_DialogOption.setVisibility(View.GONE);
        btn_Delete_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOptions.dismiss();
                //showDialogDeleteTypeFactory(typeFactory);
            }
        });

        // 3. Option Cancel
        btn_Cancel_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOptions.dismiss();
            }
        });
    }

    private void showDialogDeleteTypeFactory(TypeFactory typeFactory) {
        // Khởi tạo dialog
        dialogTypeFactory = new Dialog(this);
        dialogTypeFactory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTypeFactory.setContentView(R.layout.dialog_delete);
        dialogTypeFactory.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogTypeFactory.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo và ánh xạ view của dialog update
        Button btn_Cancel_DeleteCategory_Dialog     = dialogTypeFactory.findViewById(R.id.btn_Cancel_Delete_Dialog);
        Button btn_Confirm_DeleteCategory_Dialog    = dialogTypeFactory.findViewById(R.id.btn_Confirm_Delete_Dialog);
        TextView txt_Title_Delete_Dialog            = dialogTypeFactory.findViewById(R.id.txt_Title_Delete_Dialog);
        TextView txt_Message_Delete_Dialog          = dialogTypeFactory.findViewById(R.id.txt_Message_Delete_Dialog);

        // Gán Title, Message cho dialog
        txt_Title_Delete_Dialog.setText(R.string.title_delete_type_factory);
        txt_Message_Delete_Dialog.setText(R.string.title_question_delete_tFactory);

        // Hiển thị dialog
        dialogTypeFactory.show();

        /*
         * 1. Khi chọn Confirm Button: Sẽ xóa đi đơn vị tính này
         * 2. Khu chọn Cancel Button: Sẽ tắt đi dialog
         * */

        //1. Confirm Button
        btn_Confirm_DeleteCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeFactoryPresenter.deleteTypeFactory(typeFactory.getIdTypeFactory());
                dialogTypeFactory.dismiss();
            }
        });

        // 2. Cancel Button
        btn_Cancel_DeleteCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTypeFactory.cancel();
            }
        });
    }


    private void showDialogUpdateTypeFactory(TypeFactory typeFactory) {
        // Tạo và cài đặt layout cho dialog
        dialogTypeFactory = new Dialog(this);
        dialogTypeFactory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTypeFactory.setContentView(R.layout.dialog_one_edittext);
        dialogTypeFactory.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogTypeFactory.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txt_Title_Dialog               = dialogTypeFactory.findViewById(R.id.txt_Title_Dialog);
        EditText edt_Content_Dialog             = dialogTypeFactory.findViewById(R.id.edt_Content_Dialog);
        Button btn_Cancel_AddBalance_Dialog     = dialogTypeFactory.findViewById(R.id.btn_Cancel_Dialog);
        Button btn_Confirm_AddBalance_Dialog    = dialogTypeFactory.findViewById(R.id.btn_Confirm_Dialog);

        // Set dữ liệu ban đầu
        edt_Content_Dialog.setText(typeFactory.getNameTypeFactory());

        // Set title dialog
        txt_Title_Dialog.setText(R.string.title_update_type_factory);
        edt_Content_Dialog.setHint(R.string.title_input_name_type_factory);
        // Hiển thị dialog
        dialogTypeFactory.show();

        /*
         * 1. Khi chọn "Xác nhận": Thêm đơn vị tính mới vào csdl
         * 2. Khi chọn "Hủy": Đóng dialog
         * */

        // 1. Xác nhận thêm
        btn_Confirm_AddBalance_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameTypeFactory = edt_Content_Dialog.getText().toString();
                if (nameTypeFactory.isEmpty())
                {
                    Toast.makeText(TypeFactory__Activity.this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    typeFactoryPresenter.updateTypeFactory(typeFactory.getIdTypeFactory(),nameTypeFactory);
                    dialogTypeFactory.cancel();
                }
            }
        });

        // 2. Hủy thêm
        btn_Cancel_AddBalance_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTypeFactory.cancel();
            }
        });
    }
}