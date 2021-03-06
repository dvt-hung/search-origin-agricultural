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


public class Setting_Employee_Fragment extends Fragment implements IAccount {

    private LinearLayout layout_Info_Setting_Employee,layout_Password_Setting_Employee, layout_LogOut_Setting_Employee;
    private Account_Presenter accountPresenter;
    private Dialog dialogSettingEmployee;
    private TextView txt_Error_ChangePassword_Dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewSetting =  inflater.inflate(R.layout.fragment_setting__employee_, container, false);

        initView(viewSetting);

        return viewSetting;
    }

    private void initView(View viewSetting) {
        layout_Info_Setting_Employee            = viewSetting.findViewById(R.id.layout_Info_Setting_Employee);
        layout_Password_Setting_Employee        = viewSetting.findViewById(R.id.layout_Password_Setting_Employee);
        layout_LogOut_Setting_Employee          = viewSetting.findViewById(R.id.layout_LogOut_Setting_Employee);
        accountPresenter                        = new Account_Presenter(this,requireActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
        
        initEvents();
    }

    private void initEvents() {

        // 1. Info Setting: Chuy???n sang activity qu???n l?? th??ng tin c?? nh??n
        layout_Info_Setting_Employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), InformationActivity.class));

            }
        });

        // 2. Change password: M??? dialog thay ?????i m???t kh???u
        layout_Password_Setting_Employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangePassword();
            }
        });

        // 3. Log out: M??? dialog x??c nh???n ????ng xu???t t??i kho???n
        layout_LogOut_Setting_Employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSignOut();
            }
        });
    }

    // Dialog ????ng xu???t
    private void showDialogSignOut() {
        dialogSettingEmployee = new Dialog(requireActivity());
        dialogSettingEmployee.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSettingEmployee.setContentView(R.layout.dialog_sign_out);
        dialogSettingEmployee.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogSettingEmployee.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // Khai b??o, ??nh x??? view trong dialog sign out
        Button btn_Cancel_SignOut_Dialog    = dialogSettingEmployee.findViewById(R.id.btn_Cancel_SignOut_Dialog);
        Button btn_Confirm_SignOut_Dialog   = dialogSettingEmployee.findViewById(R.id.btn_Confirm_SignOut_Dialog);

        dialogSettingEmployee.show();

        /*
         * 1. Khi ch???n v??o Cancel Button: ????ng dialog
         * 2. Khi ch???n v??o Confirm Button: Chuy???n ?????n login activity v?? set currentUser = null
         * */

        // 1. Cancel Button
        btn_Cancel_SignOut_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSettingEmployee.dismiss();

            }
        });

        // 2. Confirm Button
        btn_Confirm_SignOut_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),LoginActivity.class));
                requireActivity().finishAffinity();
                Common.currentUser = null;
                dialogSettingEmployee.dismiss();

            }
        });
    }


    // Dialog ?????i m???t kh???u
    private void showDialogChangePassword() {

        // Kh???i t???o dialog
        dialogSettingEmployee = new Dialog(requireActivity());
        dialogSettingEmployee.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSettingEmployee.setContentView(R.layout.dialog_change_password);
        dialogSettingEmployee.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogSettingEmployee.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai b??o, ??nh x??? view trong dialog

        EditText edt_Password_Old_Dialog            = dialogSettingEmployee.findViewById(R.id.edt_Password_Old_Dialog);
        EditText edt_Password_New_Dialog            = dialogSettingEmployee.findViewById(R.id.edt_Password_New_Dialog);
        EditText edt_Password_NewConfirm_Dialog     = dialogSettingEmployee.findViewById(R.id.edt_Password_NewConfirm_Dialog);
        txt_Error_ChangePassword_Dialog             = dialogSettingEmployee.findViewById(R.id.txt_Error_ChangePassword_Dialog);
        Button btn_Cancel_ChangePassword_Dialog     = dialogSettingEmployee.findViewById(R.id.btn_Cancel_ChangePassword_Dialog);
        Button btn_Confirm_ChangePassword_Dialog    = dialogSettingEmployee.findViewById(R.id.btn_Confirm_ChangePassword_Dialog);

        // Hi???n th??? dialog
        dialogSettingEmployee.show();

        /*
         * 1. Khi ng?????i d??ng click Cancel Button: T???t dialog
         * 2. Khi ng?????i d??ng click Confirm Button: ?????i m???t kh???u
         *   - Ki???m tra m???t kh???u hi???n t???i ????ng ch??a.
         *   - Ki???m tra 2 m???t kh???u m???i c?? kh???p nhau ch??a
         *   - Ki???m tra m???t kh???u m???i c?? r???ng hay kh??ng, ????? d??i ph???i > 6 k?? t???
         *   - Th???c hi???n thay ?????i m???t kh???u
         * */

        // 1. Cancel Button
        btn_Cancel_ChangePassword_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSettingEmployee.dismiss();
            }
        });

        // 2. Confirm Button
        btn_Confirm_ChangePassword_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // L???y d??? li???u ng?????i d??ng nh???p
                String passWordOld          = edt_Password_Old_Dialog.getText().toString().trim();
                String passWordNew          = edt_Password_New_Dialog.getText().toString().trim();
                String passWordNewConfirm   = edt_Password_NewConfirm_Dialog.getText().toString().trim();
                // Ki???m tra d??? li???u c?? r???ng hay kh??ng
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

    // *********** OVERRIDE METHOD - I.ACCOUNT ***************
    @Override
    public void listAccount(List<User> managerAccounts) {

    }

    @Override
    public void exception(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void successMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialogSettingEmployee.dismiss();
    }

    @Override
    public void failedMessage(String message) {
        txt_Error_ChangePassword_Dialog.setVisibility(View.VISIBLE);
        txt_Error_ChangePassword_Dialog.setText(message);
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

    @Override
    public void emptyValue() {

    }
}