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

    private LinearLayout layout_Statistic_Setting_Manager,layout_Info_Setting_Manager, layout_Password_Setting_Manager, layout_LogOut_Setting_Manager,layout_Employee_Setting_Manager;
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
        layout_Employee_Setting_Manager     = view.findViewById(R.id.layout_Employee_Setting_Manager);
        layout_Statistic_Setting_Manager    = view.findViewById(R.id.layout_Statistic_Setting_Manager);
        accountPresenter                    = new Account_Presenter(this,requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        * 1. Info: Chuy???n s??ng activity qu???n l?? th??ng tin c???a c?? s???
        * 2. Password: M??? dialog thay ?????i m???t kh???u
        * 3. LogOut: M??? dialog x??c nh???n
        *
        * */

        // 1. Info Layout
        layout_Info_Setting_Manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),FactoryActivity.class));
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


        // 4. Employee
        layout_Employee_Setting_Manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(),AccountEmployeeActivity.class));
            }
        });

        // 5. Chuy???n activity th???ng k??
        layout_Statistic_Setting_Manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), StatisticActivity.class));
            }
        });

    }

    // Dialog ????ng xu???t
    private void showDialogSignOut() {

        Dialog dialogLogOut = new Dialog(requireActivity());
        dialogLogOut.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogOut.setContentView(R.layout.dialog_sign_out);
        dialogLogOut.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogLogOut.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        // Khai b??o, ??nh x??? view trong dialog sign out
        Button btn_Cancel_SignOut_Dialog    = dialogLogOut.findViewById(R.id.btn_Cancel_SignOut_Dialog);
        Button btn_Confirm_SignOut_Dialog   = dialogLogOut.findViewById(R.id.btn_Confirm_SignOut_Dialog);

        dialogLogOut.show();

        /*
         * 1. Khi ch???n v??o Cancel Button: ????ng dialog
         * 2. Khi ch???n v??o Confirm Button: Chuy???n ?????n login activity v?? set currentUser = null
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

    // Dialog ?????i m???t kh???u
    private void showDialogChangePassword() {

        // Kh???i t???o dialog
        Dialog dialogPassword = new Dialog(requireActivity());
        dialogPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPassword.setContentView(R.layout.dialog_change_password);
        dialogPassword.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogPassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai b??o, ??nh x??? view trong dialog

        EditText edt_Password_Old_Dialog            = dialogPassword.findViewById(R.id.edt_Password_Old_Dialog);
        EditText edt_Password_New_Dialog            = dialogPassword.findViewById(R.id.edt_Password_New_Dialog);
        EditText edt_Password_NewConfirm_Dialog     = dialogPassword.findViewById(R.id.edt_Password_NewConfirm_Dialog);
        txt_Error_ChangePassword_Dialog             = dialogPassword.findViewById(R.id.txt_Error_ChangePassword_Dialog);
        Button btn_Cancel_ChangePassword_Dialog     = dialogPassword.findViewById(R.id.btn_Cancel_ChangePassword_Dialog);
        Button btn_Confirm_ChangePassword_Dialog    = dialogPassword.findViewById(R.id.btn_Confirm_ChangePassword_Dialog);

        // Hi???n th??? dialog
        dialogPassword.show();

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
                dialogPassword.dismiss();
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
                    dialogPassword.dismiss();
                }

            }
        });
    }

    @Override
    public void listAccount(List<User> managerAccounts) {

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

    @Override
    public void emptyValue() {

    }
}