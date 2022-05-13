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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Customer_Account_Admin_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.ICustomerAccountAdmin;
import com.example.apptxng.presenter.Customer_Account_Admin_Presenter;

import java.util.List;


public class CustomerAccount_Admin_Fragment extends Fragment implements ICustomerAccountAdmin {

    private View viewAccountCustomer;
    private Customer_Account_Admin_Adapter accountCustomerAdminAdapter;
    private Customer_Account_Admin_Presenter customerPresenter;
    private RecyclerView recycler_AccountCustomer_Admin;
    private ProgressDialog progressDialogAccept;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewAccountCustomer = inflater.inflate(R.layout.fragment_account_customer__admin_, container, false);

        // Init view
        initView();

        // Set adapter
        recycler_AccountCustomer_Admin.setAdapter(accountCustomerAdminAdapter);

        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(viewAccountCustomer.getContext(),RecyclerView.VERTICAL,false);
        recycler_AccountCustomer_Admin.setLayoutManager(layoutManager);

        // Set item decoration cho recycler view
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(viewAccountCustomer.getContext(),DividerItemDecoration.VERTICAL);
        recycler_AccountCustomer_Admin.addItemDecoration(itemDecoration);

        // Load list customer
        loadCustomer();
        return viewAccountCustomer;
    }

    private void initView() {
        customerPresenter = new Customer_Account_Admin_Presenter(this);
        recycler_AccountCustomer_Admin = viewAccountCustomer.findViewById(R.id.recycler_AccountCustomer_Admin);

        accountCustomerAdminAdapter = new Customer_Account_Admin_Adapter(new Customer_Account_Admin_Adapter.ICustomerAccountListener() {
            @Override
            public void onClickItem(User user) {
                showDetailCustomer(user);
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


    // * Dialog Accept
    private void showDialogAccept(User user, String messageDialog, int status) {
        // Create dialog
        Dialog dialogAccept = new Dialog(viewAccountCustomer.getContext());
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
        progressDialogAccept = new ProgressDialog(viewAccountCustomer.getContext());
        progressDialogAccept.setMessage("Chờ trong giây lát...");

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
                progressDialogAccept.show();
                customerPresenter.updateAcceptUser(user.getIdUser(),status);
                dialogAccept.cancel();
            }
        });
    }

    private void loadCustomer() {
        customerPresenter.getListCustomer();
    }

    @Override
    public void updateSuccess(String message) {
        progressDialogAccept.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void listCustomer(List<User> list) {
        accountCustomerAdminAdapter.setListAccountCustomer(list);
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(viewAccountCustomer.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showDetailCustomer(User user) {
            // Config dialog
            Dialog dialogInfo = new Dialog(requireActivity());
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
}