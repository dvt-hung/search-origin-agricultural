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
import android.util.Log;
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
import com.example.apptxng.adapter.Balance_Admin_Adapter;
import com.example.apptxng.model.Balance;
import com.example.apptxng.presenter.IBalanceAdmin;
import com.example.apptxng.presenter.Balance_Admin_Presenter;

import java.util.List;

public class Balance_Admin_Activity extends AppCompatActivity implements IBalanceAdmin, Balance_Admin_Adapter.IListenerBalance {

    private RecyclerView recycler_Balance_Admin;
    private ImageView img_Back_Balance_Admin, img_Add_Balance_Admin;
    private Balance_Admin_Presenter balancePresenter;
    private Balance_Admin_Adapter balanceAdapter;
    private ProgressDialog progressBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_admin);

        // Init view
        initView();

        // Gán adapter cho recycler view
        recycler_Balance_Admin.setAdapter(balanceAdapter);

        // Lấy list balance
        getBalanceAdmin();
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*
        * 1. Sự kiện Back: Khi click vào "img_Back_Balance_Admin" sẽ đóng activity
        * 2. Sự kiện Add: Khi click vào "img_Add_Balance_Admin" sẽ mở dialog thêm đơn vị tính
        * */

        // 1. Sự kiện Back
        img_Back_Balance_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Sự kiện Add
        img_Add_Balance_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddBalance();
            }
        });


    }

    // Hiện Dialog thêm đơn vị tính
    private void showDialogAddBalance() {
        // Tạo và cài đặt layout cho dialog
        Dialog dialogAdd = new Dialog(this);
        dialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAdd.setContentView(R.layout.dialog_add_balance_admin);
        dialogAdd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edt_Name_AddBalance_Dialog     = dialogAdd.findViewById(R.id.edt_Name_AddBalance_Dialog);
        Button btn_Cancel_AddBalance_Dialog     = dialogAdd.findViewById(R.id.btn_Cancel_AddBalance_Dialog);
        Button btn_Confirm_AddBalance_Dialog    = dialogAdd.findViewById(R.id.btn_Confirm_AddBalance_Dialog);

        // Hiển thị dialog
        dialogAdd.show();

        /*
         * 1. Khi chọn "Xác nhận": Thêm đơn vị tính mới vào csdl
         * 2. Khi chọn "Hủy": Đóng dialog
         * */

        // 1. Xác nhận thêm
        btn_Confirm_AddBalance_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameBalance = edt_Name_AddBalance_Dialog.getText().toString();
                if (nameBalance.isEmpty())
                {
                    Toast.makeText(Balance_Admin_Activity.this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBalance.show();
                    balancePresenter.addBalance(nameBalance);
                    dialogAdd.cancel();
                }
            }
        });

        // 2. Hủy thêm
        btn_Cancel_AddBalance_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAdd.cancel();
            }
        });
    }

    // Lấy danh sách đơn vị tính
    private void getBalanceAdmin() {
        balancePresenter.getBalance();
    }

    // Khởi tạo và ảnh xạ View
    private void initView() {
        recycler_Balance_Admin    = findViewById(R.id.recycler_Balance_Admin);
        img_Back_Balance_Admin    = findViewById(R.id.img_Back_Balance_Admin);
        img_Add_Balance_Admin     = findViewById(R.id.img_Add_Balance_Admin);
        balancePresenter          = new Balance_Admin_Presenter(this);

        // Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Balance_Admin.setLayoutManager(layoutManager);

        // Item Decoration Recycler View
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recycler_Balance_Admin.addItemDecoration(decoration);

        // Balance Adapter
        balanceAdapter = new Balance_Admin_Adapter(this);

        // Progress dialog
        progressBalance = new ProgressDialog(this);
        progressBalance.setMessage("Vui lòng chờ");
    }

    @Override
    public void getBalance(List<Balance> list) {
        balanceAdapter.setBalanceList(list);
        Log.e("b", "getBalance: " + list.size() );
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addBalanceMessage(String message) {
        progressBalance.cancel();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateBalanceMessage(String message) {
        progressBalance.cancel();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteBalanceMessage(String message) {
        progressBalance.cancel();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClickBalance(Balance balance) {
       // Sự kiện Click Item: Khi chọn vào 1 item nào đó sẽ hiện bottom option: Chỉnh sửa, Xóa
        showDialogOption(balance);
    }


    // Show dialog Option
    private void showDialogOption(Balance balance) {
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
        * 2. Chọn vào option Delete: Hiện thị dialog yêu cầu xác nhận lần cuối
        * 3. Chọn vào option Cancel: Đóng dialog
        * */

        // Hiện dialog
        dialogOptions.show();

        //  1. Option Update
        btn_Update_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    dialogOptions.cancel();
                    showDialogUpdateBalance(balance);
            }
        });

        // 2. Option Delete
        btn_Delete_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOptions.cancel();
                showDialogDeleteBalance(balance);
            }
        });

        // 3. Option Cancel
        btn_Cancel_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOptions.cancel();
            }
        });
    }


    // 1. Dialog update balance
    private void showDialogUpdateBalance(Balance balance) {

        // Khởi tạo dialog
        Dialog dialogUpdate = new Dialog(this);
        dialogUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdate.setContentView(R.layout.dialog_update_balance_admin);
        dialogUpdate.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogUpdate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo, ánh xạ view trong dialog update
        EditText edt_Name_UpdateBalance_Dialog  = dialogUpdate.findViewById(R.id.edt_Name_UpdateBalance_Dialog);
        Button btn_Cancel_UpdateBalance_Dialog  = dialogUpdate.findViewById(R.id.btn_Cancel_UpdateBalance_Dialog);
        Button btn_Confirm_UpdateBalance_Dialog = dialogUpdate.findViewById(R.id.btn_Confirm_UpdateBalance_Dialog);

        // Gán tên hiện tại của đơn vị tính
        edt_Name_UpdateBalance_Dialog.setText(balance.getNameBalance());

        //Hiện dialog
        dialogUpdate.show();

        /*
        * 1. Khi chọn vào Confirm Button: Kiểm tra dữ liệu có rỗng hay không. Nếu không thì sửa đổi
        * 2. Khi chọn vào Cancel Button: tắt dialog
        * */

        // 1. Confirm Button
        btn_Confirm_UpdateBalance_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String nameBalance = edt_Name_UpdateBalance_Dialog.getText().toString().trim();

                    if (nameBalance.isEmpty())
                    {
                        Toast.makeText(Balance_Admin_Activity.this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressBalance.show();
                        balance.setNameBalance(nameBalance);
                        balancePresenter.updateBalance(balance);
                        dialogUpdate.cancel();
                    }
            }
        });

        // 2. Cancel Button
        btn_Cancel_UpdateBalance_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdate.cancel();
            }
        });
    }

    // 2. Dialog delete balance
    private void showDialogDeleteBalance(Balance balance) {
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
        txt_Title_Delete_Dialog.setText(R.string.title_delete_balance);
        txt_Message_Delete_Dialog.setText(R.string.title_question_delete_balance);

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
                progressBalance.show();
                balancePresenter.deleteBalance(balance);
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