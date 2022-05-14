package com.example.apptxng.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.Account_Presenter;
import com.example.apptxng.presenter.IAccount;

import java.util.List;

public class Setting_Customer_Fragment extends Fragment implements IAccount {

    private LinearLayout layout_Info_Setting_Customer,layout_Password_Setting_Customer, layout_LogOut_Setting_Customer;
    private Dialog dialogSettingCustomer;
    private TextView txt_Error_ChangePassword_Dialog;
    private Account_Presenter accountPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View viewSetting =  inflater.inflate(R.layout.fragment_setting__customer_, container, false);

        // initView: Ánh xạ view Setting Fragment
        initView(viewSetting);

        return viewSetting;
    }

    private void initView(View viewSetting) {
        layout_Info_Setting_Customer              = viewSetting.findViewById(R.id.layout_Info_Setting_Customer);
        layout_Password_Setting_Customer          = viewSetting.findViewById(R.id.layout_Password_Setting_Customer);
        layout_LogOut_Setting_Customer            = viewSetting.findViewById(R.id.layout_LogOut_Setting_Customer);

        // Account presenter
        accountPresenter                          = new Account_Presenter(this, requireActivity());
    }


    @Override
    public void onResume() {
        super.onResume();

        // Các sự kiện trong view
        initEvents();
    }

    private void initEvents() {
        // 1. Hiển thị thông tin cá nhân
        layout_Info_Setting_Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), InformationActivity.class));
            }
        });

        // 2. Thay đổi mật khẩu
        layout_Password_Setting_Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangePassword();
            }
        });

        // 3. Đăng xuất
        layout_LogOut_Setting_Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSignOut();
            }
        });
    }

    // Dialog đăng xuất
    private void showDialogSignOut() {
        dialogSettingCustomer = new Dialog(requireActivity());
        dialogSettingCustomer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSettingCustomer.setContentView(R.layout.dialog_sign_out);
        dialogSettingCustomer.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogSettingCustomer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // Khai báo, ánh xạ view trong dialog sign out
        Button btn_Cancel_SignOut_Dialog    = dialogSettingCustomer.findViewById(R.id.btn_Cancel_SignOut_Dialog);
        Button btn_Confirm_SignOut_Dialog   = dialogSettingCustomer.findViewById(R.id.btn_Confirm_SignOut_Dialog);

        dialogSettingCustomer.show();

        /*
         * 1. Khi chọn vào Cancel Button: đóng dialog
         * 2. Khi chọn vào Confirm Button: Chuyển đến login activity và set currentUser = null
         * */

        // 1. Cancel Button
        btn_Cancel_SignOut_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSettingCustomer.cancel();
                dialogSettingCustomer = null;
            }
        });

        // 2. Confirm Button
        btn_Confirm_SignOut_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),LoginActivity.class));
                requireActivity().finishAffinity();
                Common.currentUser = null;
                dialogSettingCustomer = null;
            }
        });
    }

    // Dialog đổi mật khẩu
    private void showDialogChangePassword() {

        // Khởi tạo dialog
        dialogSettingCustomer = new Dialog(requireActivity());
        dialogSettingCustomer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSettingCustomer.setContentView(R.layout.dialog_change_password);
        dialogSettingCustomer.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogSettingCustomer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo, ánh xạ view trong dialog

        EditText edt_Password_Old_Dialog            = dialogSettingCustomer.findViewById(R.id.edt_Password_Old_Dialog);
        EditText edt_Password_New_Dialog            = dialogSettingCustomer.findViewById(R.id.edt_Password_New_Dialog);
        EditText edt_Password_NewConfirm_Dialog     = dialogSettingCustomer.findViewById(R.id.edt_Password_NewConfirm_Dialog);
        txt_Error_ChangePassword_Dialog             = dialogSettingCustomer.findViewById(R.id.txt_Error_ChangePassword_Dialog);
        Button btn_Cancel_ChangePassword_Dialog     = dialogSettingCustomer.findViewById(R.id.btn_Cancel_ChangePassword_Dialog);
        Button btn_Confirm_ChangePassword_Dialog    = dialogSettingCustomer.findViewById(R.id.btn_Confirm_ChangePassword_Dialog);

        // Hiển thị dialog
        dialogSettingCustomer.show();

        /*
         * 1. Khi người dùng click Cancel Button: Tắt dialog
         * 2. Khi người dùng click Confirm Button: Đổi mật khẩu
         *   - Kiểm tra mật khẩu hiện tại đúng chưa.
         *   - Kiểm tra 2 mật khẩu mới có khớp nhau chưa
         *   - Kiểm tra mật khẩu mới có rỗng hay không, độ dài phải > 6 kí tự
         *   - Thực hiện thay đổi mật khẩu
         * */

        // 1. Cancel Button
        btn_Cancel_ChangePassword_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSettingCustomer.cancel();
                dialogSettingCustomer = null;
            }
        });

        // 2. Confirm Button
        btn_Confirm_ChangePassword_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu người dùng nhập
                String passWordOld          = edt_Password_Old_Dialog.getText().toString().trim();
                String passWordNew          = edt_Password_New_Dialog.getText().toString().trim();
                String passWordNewConfirm   = edt_Password_NewConfirm_Dialog.getText().toString().trim();
                // Kiểm tra dữ liệu có rỗng hay không
                if (passWordOld.isEmpty() || passWordNew.isEmpty() || passWordNewConfirm.isEmpty())
                {
                    txt_Error_ChangePassword_Dialog.setVisibility(View.VISIBLE);
                    txt_Error_ChangePassword_Dialog.setText(R.string.title_error_empty);
                }
                else
                {
                    accountPresenter.change_Password(passWordOld,passWordNew,passWordNewConfirm);
                }
            }
        });
    }

    @Override
    public void listAccount(List<User> managerAccounts) {

    }

    @Override
    public void exception(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successMessage(String message) {
        dialogSettingCustomer.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void failedMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inCorrectPassOld() {
        txt_Error_ChangePassword_Dialog.setVisibility(View.VISIBLE);
        txt_Error_ChangePassword_Dialog.setText(R.string.inCorrectPassOld);
    }

    @Override
    public void inCorrectPassConfirm() {
        txt_Error_ChangePassword_Dialog.setVisibility(View.VISIBLE);
        txt_Error_ChangePassword_Dialog.setText(R.string.inCorrectPassConfirm);
    }

    @Override
    public void emptyValue() {

    }

    @Override
    public void inCorrectPassLength() {
        txt_Error_ChangePassword_Dialog.setVisibility(View.VISIBLE);
        txt_Error_ChangePassword_Dialog.setText(R.string.inCorrectPassLength);
    }
}