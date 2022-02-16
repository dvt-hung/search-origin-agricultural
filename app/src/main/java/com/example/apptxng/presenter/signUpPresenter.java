package com.example.apptxng.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.apptxng.R;
import com.example.apptxng.api.API;
import com.example.apptxng.api.Retrofit_Client;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.model.responsePOST;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class signUpPresenter {

    private final ISignUp iSignUp;
    public  long rd = 1000 + (int)(Math.random() * 9999);
    public String message;
    public signUpPresenter(ISignUp iSignUp) {
        this.iSignUp = iSignUp;
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
    public void signUpUser(User user, String codeEmail, String passWordConfirm)
    {

        if (!user.checkLengthPassword()) {
            iSignUp.errorLengthPassword();
        }
        else if (!user.checkConfirmPassword(passWordConfirm)) {
            iSignUp.incorrectPassword();
        }
        else if (rd != Long.parseLong(codeEmail))
        {
            iSignUp.incorrectCode();
        }
        else
        {
            Common.api.signUpUser(user)
                    .enqueue(new Callback<responsePOST>() {
                        @Override
                        public void onResponse(@NonNull Call<responsePOST> call, @NonNull Response<responsePOST> response) {
                            responsePOST responsePOST = response.body();

                            assert responsePOST != null;
                            if (responsePOST.getStatus() == 0)
                            {
                                iSignUp.isFailed(responsePOST.getMessage());
                            }
                            else
                            {
                                iSignUp.isSuccess();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<responsePOST> call, @NonNull Throwable t) {

                        }
                    });
        }


    }

}
