package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Employee_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.Account_Presenter;
import com.example.apptxng.presenter.IAccount;

import java.util.List;

public class AccountEmployeeActivity extends AppCompatActivity implements Employee_Adapter.AccountListener, IAccount {

    private Employee_Adapter employeeAdapter;
    private Account_Presenter accountPresenter;
    private ImageView img_Add_Account, img_Back_Account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_employee);

        // Ánh xạ view
        initView();
    }

    private void initView() {
        RecyclerView recycler_AccountEmployee       = findViewById(R.id.recycler_AccountEmployee);
        employeeAdapter                             = new Employee_Adapter(this);
        img_Add_Account                             = findViewById(R.id.img_Add_Account);
        img_Back_Account                            = findViewById(R.id.img_Back_Account);
        // Presenter
        accountPresenter                            = new Account_Presenter(this,this);

        // Set adapter
        recycler_AccountEmployee.setAdapter(employeeAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load list employee
        accountPresenter.getListEmployeeAccount(Common.currentUser.getIdUser());

        // Add button: Chuyển sang acitivy thêm tài khoản nhân viên
        img_Add_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountEmployeeActivity.this, CreateEmployeeActivity.class));
            }
        });

        // Back button: Đóng activity
        img_Back_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClickAccount(User user) {
        showDetailEmployee(user);
        Toast.makeText(this, "Show detail user", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickSwitch(User user, int status) {
        String messageDialog = "Cho phép tài khoản này truy cập";
        if (status == 0)
        {
            messageDialog = "Không cho phép tài khoản này truy cập";
        }
        showDialogAccept(user,messageDialog, status);
    }

    // * Dialog Accept
    private void showDialogAccept(User user, String messageDialog, int status) {
        // Create dialog
        Dialog dialogAccept = new Dialog(this);
        dialogAccept.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAccept.setContentView(R.layout.dialog_change_accept_admim);
        Window window = dialogAccept.getWindow();
        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
        }

        // init view on dialog
        TextView txt_Message_Accept_Dialog      = dialogAccept.findViewById(R.id.txt_Message_Accept_Dialog);
        Button btn_Cancel_UpdateAccept_Dialog   = dialogAccept.findViewById(R.id.btn_Cancel_UpdateAccept_Dialog);
        Button btn_Confirm_UpdateAccept_Dialog  = dialogAccept.findViewById(R.id.btn_Confirm_UpdateAccept_Dialog);

        // Set message for dialog
        txt_Message_Accept_Dialog.setText(messageDialog);

        // Hiền thị dialog
        dialogAccept.show();

        /*
         * 1. Cancel Button: Tắt dialog
         * 2. Confirm Button: Thay đổi quyền truy cập
         * */

        // Click cancel button
        btn_Cancel_UpdateAccept_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAccept.cancel();
            }
        });

        // Click confirm button
        btn_Confirm_UpdateAccept_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountPresenter.updateAcceptUser(user.getIdUser(),status);
                dialogAccept.cancel();
            }
        });
    }

    private void showDetailEmployee(User user) {
        // Config dialog
        Dialog dialogInfo = new Dialog(AccountEmployeeActivity.this);
        dialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInfo.setContentView(R.layout.dialog_info_customer);
        dialogInfo.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Init view dialog: Ánh xạ view dialog
        TextView txt_Email          = dialogInfo.findViewById(R.id.txt_Email);
        TextView txt_Name           = dialogInfo.findViewById(R.id.txt_Name);
        TextView txt_Phone          = dialogInfo.findViewById(R.id.txt_Phone);
        TextView txt_Address        = dialogInfo.findViewById(R.id.txt_Address);

        // Set value: Gán giá trị cho text view

        // Email
        Common.displayValueTextView(txt_Email,user.getEmail());
        // Name
        Common.displayValueTextView(txt_Name,user.getName());
        // Email
        Common.displayValueTextView(txt_Phone,user.getPhone());
        // Email
        Common.displayValueTextView(txt_Address,user.getAddress());

        // Show dialog
        dialogInfo.show();
    }


    // ******************** OVERRIDE METHOD - I.ACCOUNT ********************
    @Override
    public void listAccount(List<User> managerAccounts) {
        employeeAdapter.setEmployees(managerAccounts);
    }

    @Override
    public void exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successMessage(String message) {
        accountPresenter.getListEmployeeAccount(Common.currentUser.getIdUser());
    }

    @Override
    public void failedMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inCorrectPassOld() {

    }

    @Override
    public void inCorrectPassLength() {

    }

    @Override
    public void inCorrectPassConfirm() {

    }

    @Override
    public void emptyValue() {

    }
}