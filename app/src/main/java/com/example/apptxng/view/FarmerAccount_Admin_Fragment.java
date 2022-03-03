package com.example.apptxng.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Farmer_Account_Admin_Adapter;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.IFarmerAccountAdmin;
import com.example.apptxng.presenter.Farmer_Account_Admin_Presenter;

import java.util.List;


public class FarmerAccount_Admin_Fragment extends Fragment implements IFarmerAccountAdmin {

    private View viewFarmerAccount;
    private Farmer_Account_Admin_Adapter farmerAdapter;
    private Farmer_Account_Admin_Presenter farmerAccountAdminPresenter;
    private RecyclerView recycler_AccountFarmer_Admin;
    private ProgressDialog progressDialogAccept;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewFarmerAccount = inflater.inflate(R.layout.fragment_account_farmer__admin_, container, false);

        // init view
        initView();

        // Set adapter for recyclerview
        recycler_AccountFarmer_Admin.setAdapter(farmerAdapter);

        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(viewFarmerAccount.getContext(),RecyclerView.VERTICAL,true);
        recycler_AccountFarmer_Admin.setLayoutManager(layoutManager);

        // Load list farmer
        loadListFarmer();
        return viewFarmerAccount;
    }

    private void initView() {
        recycler_AccountFarmer_Admin = viewFarmerAccount.findViewById(R.id.recycler_AccountFarmer_Admin);
        farmerAccountAdminPresenter = new Farmer_Account_Admin_Presenter(this);

        // Adapter
        farmerAdapter = new Farmer_Account_Admin_Adapter(new Farmer_Account_Admin_Adapter.IFarmerAccountListener() {
            @Override
            public void onClickItem(User user) {

            }

            @Override
            public void onClickSwitch(User user, int status) {
                String messageDialog = "Cho phép tài khoản này truy cập";
                if (status == 0)
                {
                    messageDialog = "Không cho phép tài khoản này truy cập";
                }
                showDialogAccept(user,messageDialog,status);
            }
        });
    }

    // Show dialog change accept for user
    private void showDialogAccept(User user, String messageDialog, int status) {
        // Create dialog
        Dialog dialogAccept = new Dialog(viewFarmerAccount.getContext());
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
        progressDialogAccept = new ProgressDialog(viewFarmerAccount.getContext());
        progressDialogAccept.setMessage("Chờ trong giây lát...");

        // Set message for dialog
        txt_Message_Accept_Dialog.setText(messageDialog);

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
                progressDialogAccept.show();
                farmerAccountAdminPresenter.updateAcceptUser(user.getIdUser(),status);
                dialogAccept.dismiss();
            }
        });
        dialogAccept.show();
    }

    // Load list account farmer
    private void loadListFarmer() {
        farmerAccountAdminPresenter.getListFarmer();
    }

    @Override
    public void updateSuccess(String message) {
        progressDialogAccept.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFailed(String message) {
        progressDialogAccept.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Override method
    @Override
    public void listFarmer(List<User> list) {
        farmerAdapter.setListAccountFarmer(list);
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}