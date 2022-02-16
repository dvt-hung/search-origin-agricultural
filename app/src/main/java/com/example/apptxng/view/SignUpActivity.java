package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.api.API;
import com.example.apptxng.api.Retrofit_Client;
import com.example.apptxng.model.User;
import com.example.apptxng.model.responsePOST;
import com.example.apptxng.presenter.ISignUp;
import com.example.apptxng.presenter.signUpPresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements ISignUp {

    private EditText edt_Email_SU,edt_Name_SU, edt_Password_SU, edt_Password_Confirm_SU, edt_Code_SU ;
    private TextView txt_Type_SU,txt_Error_SU;
    private ImageView img_Back_SU;
    private Button btn_SendOTP, btn_SignUp;
    private signUpPresenter signUpPresenter;
    private String name, passWord, passWordConfirm, email,codeEmail;
    private int idRole;
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Init view
        initView();

        // Check Email
        edt_Email_SU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(editable.toString());
                if (Patterns.EMAIL_ADDRESS.matcher(stringBuffer).matches())
                {
                    btn_SendOTP.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Send OTP
        btn_SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_Email_SU.getText().toString().trim();
                if (email.isEmpty())
                {
                    txt_Error_SU.setText(R.string.title_error_empty);
                    txt_Error_SU.setVisibility(View.VISIBLE);
                }
                else
                {
                    signUpPresenter.sendOTP(email);
                }
            }
        });

        // Click Sign Up
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_Error_SU.setVisibility(View.VISIBLE);

                // Lấy dữ liệu từ các EDT
                name            = edt_Name_SU.getText().toString().trim();
                email           = edt_Email_SU.getText().toString().trim();
                passWord        = edt_Password_SU.getText().toString().trim();
                passWordConfirm = edt_Password_Confirm_SU.getText().toString().trim();
                codeEmail       = edt_Code_SU.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || passWord.isEmpty() || passWordConfirm.isEmpty() || idRole == 0 || codeEmail.isEmpty())
                {
                    txt_Error_SU.setText(R.string.title_error_empty);
                }
                else
                {
                    // Chuyển dữ liệu qua signUpPresenter
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassWord(passWord);
                    user.setAccept(false);
                    user.setIdRole(idRole);
                    signUpPresenter.signUpUser(user,codeEmail,passWordConfirm);
                }
            }
        });

        // Show dialog type
        txt_Type_SU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở dialog người dùng chọn kiểu người dùng
                showDialogTypeUser();
            }
        });

        // Back
        img_Back_SU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Dialog choice type user
    private void showDialogTypeUser() {
        Dialog dialogTypeUser = new Dialog(this);
        dialogTypeUser.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTypeUser.setContentView(R.layout.dialog_type_user);
        Window window = dialogTypeUser.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        // Init view in dialog
        RadioButton radio_farmer    = dialogTypeUser.findViewById(R.id.radio_farmer);
        RadioButton radio_customer  = dialogTypeUser.findViewById(R.id.radio_customer);
        RadioGroup  radio_group     = dialogTypeUser.findViewById(R.id.radio_group);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i)
                {
                    case R.id.radio_farmer:
                    {
                        txt_Type_SU.setText(radio_farmer.getText());
                        idRole = 3;
                        break;
                    }
                    case  R.id.radio_customer:
                    {
                        txt_Type_SU.setText(radio_customer.getText());
                        idRole = 4;
                        break;
                    }
                }
                dialogTypeUser.dismiss();

            }
        });
        dialogTypeUser.show();

    }


    // Init view
    private void initView() {
        edt_Email_SU                = findViewById(R.id.edt_Email_SU);
        edt_Name_SU                 = findViewById(R.id.edt_Name_SU);
        edt_Password_SU             = findViewById(R.id.edt_Password_SU);
        edt_Password_Confirm_SU     = findViewById(R.id.edt_Password_Confirm_SU);
        edt_Code_SU                 = findViewById(R.id.edt_Code_SU);
        btn_SendOTP                 = findViewById(R.id.btn_SendOTP);
        btn_SignUp                  = findViewById(R.id.btn_SignUp);
        txt_Type_SU                 = findViewById(R.id.txt_Type_SU);
        txt_Error_SU                = findViewById(R.id.txt_Error_SU);
        img_Back_SU                 = findViewById(R.id.img_Back_SU);
        signUpPresenter             = new signUpPresenter(this);
    }

    // OVERRIDE INTERFACE
    @Override
    public void emailError() {
        txt_Error_SU.setText(R.string.title_error_email);

    }

    @Override
    public void errorLengthPassword() {
        txt_Error_SU.setText(R.string.title_error_length_pass);
    }

    @Override
    public void incorrectPassword() {
        txt_Error_SU.setText(R.string.title_error_incorrect_pass);
    }

    @Override
    public void incorrectCode() {
        txt_Error_SU.setText(R.string.title_error_incorrect_code);
    }

    @Override
    public void isSuccess() {
        this.finish();
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
    }

    @Override
    public void isFailed(String message) {
        txt_Error_SU.setVisibility(View.VISIBLE);
        txt_Error_SU.setText(message);
    }

    @Override
    public void sendOTPSuccess() {
        txt_Error_SU.setVisibility(View.VISIBLE);
        txt_Error_SU.setText(R.string.title_success_sendOTP);
    }

    @Override
    public void sendOTPFailed() {
        txt_Error_SU.setVisibility(View.VISIBLE);
        txt_Error_SU.setText(R.string.title_error_sendOTP);
    }


}