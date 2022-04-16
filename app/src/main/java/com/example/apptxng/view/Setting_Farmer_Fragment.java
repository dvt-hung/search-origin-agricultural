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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.Account_Presenter;
import com.example.apptxng.presenter.IAccount;

import java.util.List;


public class Setting_Farmer_Fragment extends Fragment implements IAccount {

    private Account_Presenter accountPresenter;
    private View viewSetting;
    private ImageView img_Farmer_Setting;
    private TextView  txt_Error_ChangePassword_Dialog;
    private LinearLayout layout_Info_Setting_Farmer,layout_Password_Setting_Farmer, layout_Factory_Setting_Farmer, layout_LogOut_Setting_Farmer;
    private Dialog dialogSettingFarmer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewSetting =  inflater.inflate(R.layout.fragment_setting__farmer_, container, false);

        // initView: Ánh xạ view Setting Fragment
        initView();

        return viewSetting;
    }

    private void initView() {
        img_Farmer_Setting                  = viewSetting.findViewById(R.id.img_Farmer_Setting);
        layout_Info_Setting_Farmer          = viewSetting.findViewById(R.id.layout_Info_Setting_Farmer);
        layout_Password_Setting_Farmer      = viewSetting.findViewById(R.id.layout_Password_Setting_Farmer);
        layout_Factory_Setting_Farmer       = viewSetting.findViewById(R.id.layout_Factory_Setting_Farmer);
        layout_LogOut_Setting_Farmer        = viewSetting.findViewById(R.id.layout_LogOut_Setting_Farmer);
        accountPresenter                    = new Account_Presenter(this, requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        // Gán giá trị ban đầu cho ảnh và tên vườn
        displayValue();

        // initEvent: Các sự kiện trong Setting Fragment
        initEvents();
    }

    // Hiển thị dữ liệu của thông tin cá nhân
    private void displayValue() {
        // Gán giá trị cho Image của Farmer
        Glide.with(this).load(Common.currentUser.getImage()).error(R.drawable.logo).into(img_Farmer_Setting);

    }

    // Khai báo các sự kiện
    private void initEvents() {
        /*
        * 1. layout_Info_Setting_Farmer: Chuyển sang activity thay đổi thông tin
        * 2. layout_Password_Setting_Farmer: Mở dialog đổi password
        * 3. layout_Factory_Setting_Farmer: Mở sang activity Factory
        * 4. layout_LogOut_Setting_Farmer: Mở dialog xác nhận
        * */

        // 1. Info Setting: Thay đổi thông tin
        layout_Info_Setting_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(viewSetting.getContext(), InfoFarmerActivity.class));
            }
        });

        // 2. Change Password: Đổi mật khẩu
        layout_Password_Setting_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangePassword();
            }
        });

        // 3. Factory Setting: Activity quản lí thông tin cơ sở
        layout_Factory_Setting_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),FactoryActivity.class));
            }
        });

        // 4. Log out: Đăng xuất
        layout_LogOut_Setting_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSignOut();
            }
        });
    }

    // Dialog đăng xuất
    private void showDialogSignOut() {
        dialogSettingFarmer = new Dialog(viewSetting.getContext());
        dialogSettingFarmer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSettingFarmer.setContentView(R.layout.dialog_sign_out);
        dialogSettingFarmer.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogSettingFarmer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // Khai báo, ánh xạ view trong dialog sign out
        Button btn_Cancel_SignOut_Dialog    = dialogSettingFarmer.findViewById(R.id.btn_Cancel_SignOut_Dialog);
        Button btn_Confirm_SignOut_Dialog   = dialogSettingFarmer.findViewById(R.id.btn_Confirm_SignOut_Dialog);

        dialogSettingFarmer.show();

        /*
         * 1. Khi chọn vào Cancel Button: đóng dialog
         * 2. Khi chọn vào Confirm Button: Chuyển đến login activity và set currentUser = null
         * */

        // 1. Cancel Button
        btn_Cancel_SignOut_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSettingFarmer.cancel();
                dialogSettingFarmer = null;
            }
        });

        // 2. Confirm Button
        btn_Confirm_SignOut_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),LoginActivity.class));
                requireActivity().finishAffinity();
                Common.currentUser = null;
                dialogSettingFarmer = null;
            }
        });
    }

    // Dialog đổi mật khẩu
    private void showDialogChangePassword() {

        // Khởi tạo dialog
        dialogSettingFarmer = new Dialog(viewSetting.getContext());
        dialogSettingFarmer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSettingFarmer.setContentView(R.layout.dialog_change_password);
        dialogSettingFarmer.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogSettingFarmer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo, ánh xạ view trong dialog

        EditText edt_Password_Old_Dialog            = dialogSettingFarmer.findViewById(R.id.edt_Password_Old_Dialog);
        EditText edt_Password_New_Dialog            = dialogSettingFarmer.findViewById(R.id.edt_Password_New_Dialog);
        EditText edt_Password_NewConfirm_Dialog     = dialogSettingFarmer.findViewById(R.id.edt_Password_NewConfirm_Dialog);
        txt_Error_ChangePassword_Dialog             = dialogSettingFarmer.findViewById(R.id.txt_Error_ChangePassword_Dialog);
        Button btn_Cancel_ChangePassword_Dialog     = dialogSettingFarmer.findViewById(R.id.btn_Cancel_ChangePassword_Dialog);
        Button btn_Confirm_ChangePassword_Dialog    = dialogSettingFarmer.findViewById(R.id.btn_Confirm_ChangePassword_Dialog);

        // Hiển thị dialog
        dialogSettingFarmer.show();

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
                dialogSettingFarmer.cancel();
                dialogSettingFarmer = null;
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
    public void listManagerAccount(List<User> managerAccounts) {

    }

    @Override
    public void exception(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successMessage(String message) {
        dialogSettingFarmer.cancel();
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
    public void inCorrectPassLength() {
        txt_Error_ChangePassword_Dialog.setVisibility(View.VISIBLE);
        txt_Error_ChangePassword_Dialog.setText(R.string.inCorrectPassLength);
    }



}