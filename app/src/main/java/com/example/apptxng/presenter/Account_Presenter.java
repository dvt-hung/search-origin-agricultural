package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.ResponsePOST;
import com.example.apptxng.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Account_Presenter {
    private final IAccount iAccount;
    private final Context context;


    public Account_Presenter(IAccount iAccount, Context context) {
        this.iAccount = iAccount;
        this.context = context;
    }

    public synchronized void getListManagerAccount()
    {
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();
        Common.api.getListManager()
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                            iAccount.listManagerAccount(response.body());
                            dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                            iAccount.exception(t.getMessage());
                            dialog.dismiss();
                    }
                });
    }

    public synchronized void updateAcceptUser(String idUser, int accept){

        // Create progress dialog
        ProgressDialog progressDialog = Common.createProgress(context);
        progressDialog.show();
        Common.api.updateAcceptUser(idUser,accept)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();
                        assert responsePOST != null;
                        if (responsePOST.getStatus() == 1)
                        {
                            iAccount.successMessage(responsePOST.getMessage());
                        }
                        else
                        {
                            iAccount.failedMessage(responsePOST.getMessage());
                        }
                        getListManagerAccount();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iAccount.exception(t.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }

    public synchronized void change_Password(String passOld, String passNew,String passNewConfirm) {
        /*
         * 1. Kiểm tra password cũ có đúng hay chưa
         * 2. Kiểm tra 2 mật khậu có trùng nhau không
         * 3. Kiểm tra độ dài mật khẩu
         * */

        if (!Common.currentUser.checkPasswordOld(passOld)) {
            iAccount.inCorrectPassOld();
        } else if (passNew.length() < 6) {
            iAccount.inCorrectPassLength();
        } else if (!passNew.equals(passNewConfirm)) {
            iAccount.inCorrectPassConfirm();
        } else {
            ProgressDialog dialog = Common.createProgress(context);
            dialog.show();
            Common.api.change_password(Common.currentUser.getEmail(), passNew, Common.currentUser.getIdUser())
                    .enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                            ResponsePOST responsePOST = response.body();
                            assert responsePOST != null;
                            if (responsePOST.getStatus() == 1) {
                                iAccount.successMessage(responsePOST.getMessage());
                                Common.currentUser.setPassWord(passNew);
                            } else {
                                iAccount.failedMessage(responsePOST.getMessage());
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                            iAccount.exception(t.getMessage());
                            dialog.dismiss();
                        }
                    });
        }
    }
}
