package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apptxng.R;
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
                            iAccount.listAccount(response.body());
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
         * 1. Ki???m tra password c?? c?? ????ng hay ch??a
         * 2. Ki???m tra 2 m???t kh???u c?? tr??ng nhau kh??ng
         * 3. Ki???m tra ????? d??i m???t kh???u
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
            Common.api.change_password(passNew, Common.currentUser.getIdUser())
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

    public synchronized void getListEmployeeAccount(String idOwner)
    {
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();
        Common.api.getListEmployee(idOwner)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                        iAccount.listAccount(response.body());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                        iAccount.exception(t.getMessage());
                        dialog.dismiss();
                    }
                });
    }

    // Sign Up
    public synchronized void signUpUser(User user, String passWordConfirm, int idTypeFactory, String nameFactory)
    {
        if (user.getName().isEmpty() || user.getPhone().isEmpty() || user.getPassWord().isEmpty()
                || user.getIdRole() == 0 || user.getIdUser().isEmpty() || passWordConfirm.isEmpty())
        {
            iAccount.emptyValue();
        }
        else if (user.checkLengthPassword()) {
            iAccount.inCorrectPassLength();

        }
        else if (!user.checkConfirmPassword(passWordConfirm)) {
            iAccount.inCorrectPassConfirm();

        }
        else if (user.getIdRole() == 3 && nameFactory.isEmpty())
        {
            iAccount.emptyValue();
        }
        else if (user.getIdRole() == 2 && idTypeFactory == 0 && nameFactory.isEmpty() )
        {
            iAccount.emptyValue();

        }
        else {
            // T???o progress dialog
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Vui l??ng ?????i...");
            progressDialog.show();

            Common.api.signUpUser(user.getPhone(), user.getIdUser(), user.getName(),user.getPassWord(),user.isAccept(),user.getIdRole(),idTypeFactory,nameFactory, user.getIdOwner())
                    .enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                            ResponsePOST responsePOST = response.body();
                            assert responsePOST != null;
                            if (responsePOST.getStatus() == 0)
                            {
                                iAccount.failedMessage(responsePOST.getMessage());
                            }
                            else
                            {
                                iAccount.successMessage(responsePOST.getMessage());
                            }
                            progressDialog.dismiss();

                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                            iAccount.exception(t.getMessage());
                            progressDialog.dismiss();
                        }
                    });
        }


    }
}
