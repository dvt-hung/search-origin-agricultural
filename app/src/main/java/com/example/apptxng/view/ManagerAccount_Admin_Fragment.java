package com.example.apptxng.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Manager_Account_Admin_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.Account_Presenter;
import com.example.apptxng.presenter.IAccount;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerAccount_Admin_Fragment extends Fragment implements Manager_Account_Admin_Adapter.IManagerListener, IAccount {

    private View viewAccountManager;
    private RecyclerView recycler_AccountManager_Admin;
    private Manager_Account_Admin_Adapter managerAdapter;
    private Account_Presenter accountPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewAccountManager =  inflater.inflate(R.layout.fragment_manager_account__admin_, container, false);


        // init view: Ánh xạ view
        initView();
        return viewAccountManager;
    }

    private void initView() {
        recycler_AccountManager_Admin = viewAccountManager.findViewById(R.id.recycler_AccountManager_Admin);

        // Presenter
        accountPresenter            = new Account_Presenter(this,requireActivity());

        managerAdapter = new Manager_Account_Admin_Adapter(this);
        recycler_AccountManager_Admin.setAdapter(managerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(),RecyclerView.VERTICAL,false);
        recycler_AccountManager_Admin.setLayoutManager(layoutManager);

        // Load list manager account
        accountPresenter.getListManagerAccount();
    }

    @Override
    public void onClickSwitch(User manager, int status) {
        String messageDialog = "Cho phép tài khoản này truy cập";
        if (status == 0)
        {
            messageDialog = "Không cho phép tài khoản này truy cập";
        }
        showDialogAccept(manager,messageDialog,status);
    }

    @Override
    public void onClickManger(User manager) {
        getInfoFactory(manager);
    }

    private synchronized void getInfoFactory(User manager) {

            ProgressDialog progressDialog = Common.createProgress(requireActivity());
            progressDialog.show();

            Common.api.getFactoryByID(manager.getIdUser())
                    .enqueue(new Callback<Factory>() {
                        @Override
                        public void onResponse(@NonNull Call<Factory> call, @NonNull Response<Factory> response) {
                            Factory factory = response.body();
                            assert factory != null;

                            showDetail(manager,factory);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Call<Factory> call, @NonNull Throwable t) {
                            progressDialog.dismiss();
                        }
                    });
    }


    private void showDialogAccept(User user, String messageDialog, int status)
    {
        // Create dialog
        Dialog dialogAccept = new Dialog(requireActivity());
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
                dialogAccept.dismiss();
            }
        });
    }

    @Override
    public void listManagerAccount(List<User> managerAccounts) {
            managerAdapter.setListManager(managerAccounts);
    }

    @Override
    public void exception(String message) {
        Toast.makeText(requireActivity()    , message, Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void inCorrectPassLength() {

    }

    @Override
    public void inCorrectPassConfirm() {

    }


    private void showDetail(User manager, Factory factory) {
        // Config dialog
        Dialog dialogInfo = new Dialog(requireActivity());
        dialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInfo.setContentView(R.layout.dialog_info);
        dialogInfo.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Init view dialog: Ánh xạ view dialog
        TextView txt_Email                  = dialogInfo.findViewById(R.id.txt_Email);
        TextView txt_Name                   = dialogInfo.findViewById(R.id.txt_Name);
        TextView txt_Phone                  = dialogInfo.findViewById(R.id.txt_Phone);
        TextView txt_Address                = dialogInfo.findViewById(R.id.txt_Address);
        TextView txt_Name_Factory           = dialogInfo.findViewById(R.id.txt_Name_Factory);
        TextView txt_Name_TypeFactory       = dialogInfo.findViewById(R.id.txt_Name_TypeFactory);
        TextView txt_Phone_Factory          = dialogInfo.findViewById(R.id.txt_Phone_Factory);
        TextView txt_Owner_Factory          = dialogInfo.findViewById(R.id.txt_Owner_Factory);
        TextView txt_Web_Factory            = dialogInfo.findViewById(R.id.txt_Web_Factory);
        TextView txt_Address_Factory        = dialogInfo.findViewById(R.id.txt_Address_Factory);

        // Set value: Gán giá trị cho text view

        // Email
        Common.displayValueTextView(txt_Email,manager.getEmail());
        // Name
        Common.displayValueTextView(txt_Name,manager.getName());
        // Email
        Common.displayValueTextView(txt_Phone,manager.getPhone());
        // Email
        Common.displayValueTextView(txt_Address,manager.getAddress());

        // Name TypeFactory
        Common.displayValueTextView(txt_Name_TypeFactory,factory.getType_factory().getNameTypeFactory());

        // Name Factory
        Common.displayValueTextView(txt_Name_Factory,factory.getNameFactory());

        // Phone Factory
        Common.displayValueTextView(txt_Phone_Factory,factory.getPhoneFactory());

        // Owner Factory
        Common.displayValueTextView(txt_Owner_Factory,factory.getOwnerFactory());

        // txt_Web_Factory Factory
        Common.displayValueTextView(txt_Web_Factory,factory.getWebFactory());

        // txt_Address_Factory Factory
        Common.displayValueTextView(txt_Address_Factory,factory.getAddressFactory());

        // Show dialog
        dialogInfo.show();
    }
}