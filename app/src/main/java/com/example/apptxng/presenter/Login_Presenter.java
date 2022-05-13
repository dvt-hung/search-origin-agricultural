package com.example.apptxng.presenter;

import android.util.Log;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Presenter {

    private final ILogin iLogin;

    public Login_Presenter(ILogin iLogin) {
        this.iLogin = iLogin;
    }

    // Login

    public void Login(String email, String passWord)
    {

        if (email.isEmpty() || passWord.isEmpty())
        {
            iLogin.emptyValueLogin();
        }
        else
        {
            Common.api.login(email,passWord)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            assert user != null;
                            if (user.getResponse().getStatus() == 1)
                            {
                                iLogin.loginSuccess(user);
                                Common.currentUser = user;
                            }
                            else if (user.getResponse().getStatus() == 0)
                            {
                                iLogin.loginFailed(user.getResponse().getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            iLogin.exception(t.getMessage());
                        }
                    });
        }
    }
}
