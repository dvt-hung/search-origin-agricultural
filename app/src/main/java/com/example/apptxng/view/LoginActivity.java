package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.ILogin;
import com.example.apptxng.presenter.loginPresenter;

public class LoginActivity extends AppCompatActivity implements ILogin {

    private LinearLayout layout_Create_Account;
    private EditText edt_UserName_Login,edt_Password_Login;
    private Button btn_Login;
    private loginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init view
        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_UserName_Login.getText().toString().trim();
                String passWord = edt_Password_Login.getText().toString().trim();
                loginPresenter.Login(email,passWord);
            }
        });


        layout_Create_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
    }

    private void initView() {
        layout_Create_Account   = findViewById(R.id.layout_Create_Account);
        edt_UserName_Login      = findViewById(R.id.edt_UserName_Login);
        edt_Password_Login      = findViewById(R.id.edt_Password_Login);
        btn_Login               = findViewById(R.id.btn_Login);
        loginPresenter          = new loginPresenter(this);
    }

    @Override
    public void emptyValueLogin() {
        Toast.makeText(getApplicationContext(), "Vui lòng không bỏ trống!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccess(User user) {

        if (user.getIdRole() == 1)
        {
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
        } else if (user.getIdRole() == 3)
        {
            startActivity(new Intent(LoginActivity.this, FarmerActivity.class));
        } else if (user.getIdRole() == 4)
        {
            startActivity(new Intent(LoginActivity.this, CustomerActivity.class));
        }
        finishAffinity();
    }

    @Override
    public void loginFailed(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }
}