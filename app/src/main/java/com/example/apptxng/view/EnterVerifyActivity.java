package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.ResponsePOST;
import com.example.apptxng.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterVerifyActivity extends AppCompatActivity {
    private EditText edt_Code1, edt_Code2, edt_Code3, edt_Code4, edt_Code5, edt_Code6;
    private LinearLayout layout_ResendCode;
    private Button btn_Verify;
    private User userTemp;
    private int idTypeFactory;
    private String nameFactory, phone, verifyID, strOTP;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken forceResending;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_verify);
    
        // Nhận User
        userTemp = (User) getIntent().getExtras().getSerializable("user");

        // Nhận idTypeFactory
        idTypeFactory = getIntent().getIntExtra("idTypeFactory", 0);

        // Nhận nameFactory
        nameFactory = getIntent().getStringExtra("nameFactory");

        // Nhận phone
        phone = getIntent().getStringExtra("phone");

        // Nhận verifyID
        verifyID = getIntent().getStringExtra("verifyID");

        // Init view
        initView();
        
    }

    private void initView() {
        layout_ResendCode       = findViewById(R.id.layout_ResendCode);
        btn_Verify              = findViewById(R.id.btn_Verify);
        edt_Code1               = findViewById(R.id.edt_Code1);
        edt_Code2               = findViewById(R.id.edt_Code2);
        edt_Code3               = findViewById(R.id.edt_Code3);
        edt_Code4               = findViewById(R.id.edt_Code4);
        edt_Code5               = findViewById(R.id.edt_Code5);
        edt_Code6               = findViewById(R.id.edt_Code6);
        mAuth                   = FirebaseAuth.getInstance();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Chuyển sang edittext tiếp theo khi nhập
        setupInputOTP();
        // Check mã OTP
        btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCodeVerify();
            }
        });

        // Gửi lại
        layout_ResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendCode();
            }
        });
    }

    private void setupInputOTP() {
        nextEditText(edt_Code1, edt_Code2);
        nextEditText(edt_Code2, edt_Code3);
        nextEditText(edt_Code3, edt_Code4);
        nextEditText(edt_Code4, edt_Code5);
        nextEditText(edt_Code5, edt_Code6);
    }
    private void nextEditText(EditText edt1, EditText edt2)
    {
        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty())
                {
                    edt2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void resendCode() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(Common.codeCounty + phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(EnterVerifyActivity.this)
                        .setForceResendingToken(forceResending)// Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(EnterVerifyActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    // The SMS quota for the project has been exceeded
                                    Toast.makeText(EnterVerifyActivity.this, " The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verifyID = s;
                                forceResending = forceResendingToken;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void checkCodeVerify() {
        strOTP = edt_Code1.getText().toString().trim() +
                edt_Code2.getText().toString().trim() +
                edt_Code3.getText().toString().trim() +
                edt_Code4.getText().toString().trim() +
                edt_Code5.getText().toString().trim() +
                edt_Code6.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyID, strOTP);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        ProgressDialog progressDialog = Common.createProgress(EnterVerifyActivity.this);
        progressDialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signUpUser();
                        }
                        else
                        {
                            Toast.makeText(EnterVerifyActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void signUpUser() {
        // Call API - Insert User
        Common.api.signUpUser(phone, userTemp.getIdUser(), userTemp.getName(), userTemp.getPassWord(), userTemp.isAccept(), userTemp.getIdRole(), idTypeFactory,nameFactory, userTemp.getIdOwner())
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();
                        assert  responsePOST != null;
                        if (responsePOST.getStatus() == 1)
                        {
                            startActivity(new Intent(EnterVerifyActivity.this,LoginActivity.class));
                            finishAffinity();
                        }
                        Toast.makeText(EnterVerifyActivity.this, responsePOST.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        Toast.makeText(EnterVerifyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}