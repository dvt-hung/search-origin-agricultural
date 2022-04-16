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

public class Setting_Manager_Fragment extends Fragment implements IAccount {

    private LinearLayout layout_Info_Setting_Manager, layout_Password_Setting_Manager, layout_LogOut_Setting_Manager;
    private TextView txt_Error_ChangePassword_Dialog;
    private Account_Presenter accountPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting__manager_, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        layout_Info_Setting_Manager         = view.findViewById(R.id.layout_Info_Setting_Manager);
        layout_Password_Setting_Manager     = view.findViewById(R.id.layout_Password_Setting_Manager);
        layout_LogOut_Setting_Manager       = view.findViewById(R.id.layout_LogOut_Setting_Manager);
        accountPresenter                    = new Account_Presenter(this,requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        * 1. Info: Chuyển sáng activity quản lý thông tin của cơ sở
        * 2. Password: Mở dialog thay đổi mật khẩu
        * 3. LogOut: Mở dialog xác nhận
        *
        * */

        // 1. Info Layout
        layout_Info_Setting_Manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 2. Change password layout
        layout_Password_Setting_Manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangePassword();
            }
        });

        // 3. Log out layout
        layout_LogOut_Setting_Manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSignOut();
            }
        });

    }

    // Dialog đăng xuất
    private void showDialogSignOut() {

        Dialog dialogLogOut = new Dialog(requireActivity());
        dialogLogOut.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogOut.setContentView(R.layout.dialog_sign_out);
        dialogLogOut.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogLogOut.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // Khai báo, ánh xạ view trong dialog sign out
        Button btn_Cancel_SignOut_Dialog    = dialogLogOut.findViewById(R.id.btn_Cancel_SignOut_Dialog);
        Button btn_Confirm_SignOut_Dialog   = dialogLogOut.findViewById(R.id.btn_Confirm_SignOut_Dialog);

        dialogLogOut.show();

        /*
         * 1. Khi chọn vào Cancel Button: đóng dialog
         * 2. Khi chọn vào Confirm Button: Chuyển đến login activity và set currentUser = null
         * */

        // 1. Cancel Button
        btn_Cancel_SignOut_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLogOut.dismiss();
            }
        });

        // 2. Confirm Button
        btn_Confirm_SignOut_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),LoginActivity.class));
                Common.currentUser = null;
                requireActivity().finishAffinity();
            }
        });
    }

    // Dialog đổi mật khẩu
    private void showDialogChangePassword() {

        // Khởi tạo dialog
        Dialog dialogPassword = new Dialog(requireActivity());
        dialogPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPassword.setContentView(R.layout.dialog_change_password);
        dialogPassword.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogPassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo, ánh xạ view trong dialog

        EditText edt_Password_Old_Dialog            = dialogPassword.findViewById(R.id.edt_Password_Old_Dialog);
        EditText edt_Password_New_Dialog            = dialogPassword.findViewById(R.id.edt_Password_New_Dialog);
        EditText edt_Password_NewConfirm_Dialog     = dialogPassword.findViewById(R.id.edt_Password_NewConfirm_Dialog);
        txt_Error_ChangePassword_Dialog             = dialogPassword.findViewById(R.id.txt_Error_ChangePassword_Dialog);
        Button btn_Cancel_ChangePassword_Dialog     = dialogPassword.findViewById(R.id.btn_Cancel_ChangePassword_Dialog);
        Button btn_Confirm_ChangePassword_Dialog    = dialogPassword.findViewById(R.id.btn_Confirm_ChangePassword_Dialog);

        // Hiển thị dialog
        dialogPassword.show();

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
                dialogPassword.dismiss();
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
                    dialogPassword.dismiss();
                }

            }
        });
    }

    @Override
    public void listManagerAccount(List<User> managerAccounts) {

    }

    @Override
    public void exception(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successMessage(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedMessage(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inCorrectPassOld() {
        txt_Error_ChangePassword_Dialog.setVisibility(View.VISIBLE);
        txt_Error_ChangePassword_Dialog.setText(R.string.inCorrectPassOld);


    }

    @Override
    public void inCorrectPassLength() {
        txt_Error_ChangePassword_Dialog.setVisibility(View.VISIBLE);
        txt_Error_ChangePassword_Dialog.setText(R.string.inCorrectPassLength);
    }

    @Override
    public void inCorrectPassConfirm() {
        txt_Error_ChangePassword_Dialog.setVisibility(View.VISIBLE);
        txt_Error_ChangePassword_Dialog.setText(R.string.inCorrectPassConfirm);
    }
}