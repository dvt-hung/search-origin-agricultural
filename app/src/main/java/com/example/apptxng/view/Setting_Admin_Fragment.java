package com.example.apptxng.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.Account_Presenter;
import com.example.apptxng.presenter.IAccount;

import java.util.List;


public class Setting_Admin_Fragment extends Fragment implements  IAccount {

    private Dialog dialogSettingAdmin;
    private Account_Presenter accountPresenter;
    private View viewSetting;
    private TextView txt_Error_ChangePassword_Dialog;
    private LinearLayout layout_Scale_Setting_Admin,layout_Linked_Setting_Admin ,layout_Banner_Setting_Admin, layout_ChangePassword_Setting_Admin, layout_LogOut_Setting_Admin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewSetting =  inflater.inflate(R.layout.fragment_setting__admin_, container, false);

        // init view
        initView(viewSetting);

        return viewSetting;
    }

    // Init view: Ánh xạ view của setting fragment
    private void initView(View viewSetting) {
        layout_Scale_Setting_Admin          = viewSetting.findViewById(R.id.layout_Scale_Setting_Admin);
        layout_Banner_Setting_Admin         = viewSetting.findViewById(R.id.layout_Banner_Setting_Admin);
        layout_ChangePassword_Setting_Admin = viewSetting.findViewById(R.id.layout_changePassword_Setting_Admin);
        layout_LogOut_Setting_Admin         = viewSetting.findViewById(R.id.layout_LogOut_Setting_Admin);
        layout_Linked_Setting_Admin         = viewSetting.findViewById(R.id.layout_Linked_Setting_Admin);
        accountPresenter                    = new Account_Presenter(this,requireActivity());
    }


    // Event: Chọn các option tại setting fragment
    @Override
    public void onResume() {
        super.onResume();

        // 1. Scale: Click layout Scale - Chuyển qua activity quản lý các đơn vị tính của ứng dụng
        layout_Scale_Setting_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(viewSetting.getContext(), Balance_Admin_Activity.class));
            }
        });

        //2. Chang Password: Mở dialog: 1 EditText nhập mật khẩu cũ, 2 EditText nhập và nhập lại mật khẩu.
        layout_ChangePassword_Setting_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangePassword();
            }
        });

        // 3. Sign Out: Mở dialog xác nhận đăng xuất
        layout_LogOut_Setting_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSignOut();
            }
        });

        // 4. Linked: Mở activity mới hiển thị các chuỗi liên kết đang quản lý
        layout_Linked_Setting_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(viewSetting.getContext(), TypeFactory__Activity.class));
            }
        });

        //5 Banner: Mở activity quản lý banner
        layout_Banner_Setting_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),BannerActivity.class));
            }
        });
    }

    // Dialog đăng xuất
    private void showDialogSignOut() {
        dialogSettingAdmin = new Dialog(viewSetting.getContext());
        dialogSettingAdmin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSettingAdmin.setContentView(R.layout.dialog_sign_out);
        dialogSettingAdmin.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogSettingAdmin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // Khai báo, ánh xạ view trong dialog sign out
        Button btn_Cancel_SignOut_Dialog    = dialogSettingAdmin.findViewById(R.id.btn_Cancel_SignOut_Dialog);
        Button btn_Confirm_SignOut_Dialog   = dialogSettingAdmin.findViewById(R.id.btn_Confirm_SignOut_Dialog);

        dialogSettingAdmin.show();

        /*
        * 1. Khi chọn vào Cancel Button: đóng dialog
        * 2. Khi chọn vào Confirm Button: Chuyển đến login activity và set currentUser = null
        * */

        // 1. Cancel Button
        btn_Cancel_SignOut_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSettingAdmin.cancel();
                dialogSettingAdmin = null;
            }
        });

        // 2. Confirm Button
        btn_Confirm_SignOut_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),LoginActivity.class));
                requireActivity().finishAffinity();
                Common.currentUser = null;
            }
        });
    }

    // Dialog đổi mật khẩu
    private void showDialogChangePassword() {

        // Khởi tạo dialog
        dialogSettingAdmin = new Dialog(viewSetting.getContext());
        dialogSettingAdmin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSettingAdmin.setContentView(R.layout.dialog_change_password);
        dialogSettingAdmin.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogSettingAdmin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo, ánh xạ view trong dialog

        EditText edt_Password_Old_Dialog            = dialogSettingAdmin.findViewById(R.id.edt_Password_Old_Dialog);
        EditText edt_Password_New_Dialog            = dialogSettingAdmin.findViewById(R.id.edt_Password_New_Dialog);
        EditText edt_Password_NewConfirm_Dialog     = dialogSettingAdmin.findViewById(R.id.edt_Password_NewConfirm_Dialog);
        txt_Error_ChangePassword_Dialog             = dialogSettingAdmin.findViewById(R.id.txt_Error_ChangePassword_Dialog);
        Button btn_Cancel_ChangePassword_Dialog     = dialogSettingAdmin.findViewById(R.id.btn_Cancel_ChangePassword_Dialog);
        Button btn_Confirm_ChangePassword_Dialog    = dialogSettingAdmin.findViewById(R.id.btn_Confirm_ChangePassword_Dialog);

        // Hiển thị dialog
        dialogSettingAdmin.show();

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
                dialogSettingAdmin.cancel();
                dialogSettingAdmin = null;
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
        dialogSettingAdmin.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void failedMessage(String message) {
        dialogSettingAdmin.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    // Override Method: interface ISettingAdmin
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