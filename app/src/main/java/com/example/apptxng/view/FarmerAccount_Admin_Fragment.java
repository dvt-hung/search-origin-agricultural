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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Farmer_Account_Admin_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.IFarmerAccountAdmin;
import com.example.apptxng.presenter.Farmer_Account_Admin_Presenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

        // Set item decoration cho recycler view
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(viewFarmerAccount.getContext(),DividerItemDecoration.VERTICAL);
        recycler_AccountFarmer_Admin.addItemDecoration(itemDecoration);

        // Load list farmer
        loadListFarmer();
        return viewFarmerAccount;
    }

    private void initView() {
        recycler_AccountFarmer_Admin = viewFarmerAccount.findViewById(R.id.recycler_AccountFarmer_Admin);
        farmerAccountAdminPresenter = new Farmer_Account_Admin_Presenter(this);

        // Khởi tạo Adapter
        farmerAdapter = new Farmer_Account_Admin_Adapter(new Farmer_Account_Admin_Adapter.IFarmerAccountListener() {
            @Override
            public void onClickFarmer(User user) {
                getInfoFactory(user);
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

    private synchronized void getInfoFactory(User user) {
        ProgressDialog progressDialog = Common.createProgress(requireActivity());
        progressDialog.show();

        Common.api.getFactoryByID(user.getIdUser())
                .enqueue(new Callback<Factory>() {
                    @Override
                    public void onResponse(@NonNull Call<Factory> call, @NonNull Response<Factory> response) {
                        Factory factory = response.body();
                        assert factory != null;

                        showDetail(user,factory);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Factory> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void showDetail(User user, Factory factory) {
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
        Common.displayValueTextView(txt_Email,user.getEmail());
        // Name
        Common.displayValueTextView(txt_Name,user.getName());
        // Email
        Common.displayValueTextView(txt_Phone,user.getPhone());
        // Email
        Common.displayValueTextView(txt_Address,user.getAddress());

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
                farmerAccountAdminPresenter.updateAcceptUser(user.getIdUser(),status);
                dialogAccept.dismiss();
            }
        });
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