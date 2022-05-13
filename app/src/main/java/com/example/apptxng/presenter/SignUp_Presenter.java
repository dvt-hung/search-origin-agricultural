package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.model.ResponsePOST;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp_Presenter {

    private final ISignUp iSignUp;
    private final Context context;
    public  long rd = 1000 + (int)(Math.random() * 9999);
    public String message;

    public SignUp_Presenter(ISignUp iSignUp, Context context) {
        this.iSignUp = iSignUp;
        this.context = context;
    }

    // Send otp email
    public void sendOTP(String email)
    {
        // Check email
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && !TextUtils.isEmpty(email))
        {
            // Call API
            Common.api.sendOTP(rd,email).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    iSignUp.sendOTPSuccess();
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    iSignUp.sendOTPFailed();
                }
            });
        }
        else
        {
            iSignUp.emailError();
        }

    }

    // Sign Up
    public void signUpUser(User user, long codeEmail, String passWordConfirm, int idTypeFactory, String nameFactory)
    {
        if (!user.checkLengthPassword()) {
            iSignUp.errorLengthPassword();
        }
        else if (!user.checkConfirmPassword(passWordConfirm)) {
            iSignUp.incorrectPassword();
        }
        else if (rd != codeEmail)
        {
            iSignUp.incorrectCode();
        } else if (idTypeFactory != 0 && nameFactory.isEmpty())
        {
            iSignUp.emptyValue();
        }
        else {
            // Tạo progress dialog
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Vui lòng đợi...");
            progressDialog.show();

            Common.api.signUpUser(user.getEmail(), user.getIdUser(), user.getName(),user.getPassWord(),user.isAccept(),user.getIdRole(),idTypeFactory,nameFactory)
                    .enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                            ResponsePOST responsePOST = response.body();
                            assert responsePOST != null;
                            if (responsePOST.getStatus() == 0)
                            {
                                iSignUp.isFailed(responsePOST.getMessage());
                            }
                            else
                            {
                                iSignUp.isSuccess();
                            }
                            progressDialog.dismiss();

                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                            iSignUp.isFailed(t.getMessage());
                            progressDialog.dismiss();
                        }
                    });
        }


    }

}
