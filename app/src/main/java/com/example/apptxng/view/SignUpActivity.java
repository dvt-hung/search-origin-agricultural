package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.bottom_dialog.BottomDialogTypeFactory;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.ResponsePOST;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.ITypeFactory;
import com.example.apptxng.presenter.TypeFactory_Presenter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements ChoiceType_Adapter.IListenerChoiceType, ITypeFactory {

    private LinearLayout layout_TypeFactory,layout_NameFactory;
    private EditText edt_Phone_SU,edt_Name_SU, edt_Password_SU, edt_Password_Confirm_SU,edt_NameFactory_SU ;
    private TextView txt_Type_SU,txt_Error_SU, txt_TypeFactory_SU;
    private ImageView img_Back_SU;
    private Button  btn_SignUp;
    private String name;
    private String passWord;
    private String phone;
    private String nameFactory;
    private int idTypeFactory;
    private int idRole;
    private final User user = new User();
    private List<TypeFactory> typeFactoryList;
    private TypeFactory_Presenter typeFactoryPresenter;
    private BottomDialogTypeFactory dialogTypeFactory;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Init view
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Click Sign Up
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToVerifyActivity();
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

        // Show dialog choice type factory
        txt_TypeFactory_SU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTypeFactory();
            }
        });
    }

    private void goToVerifyActivity() {
        txt_Error_SU.setVisibility(View.VISIBLE);
        // Lấy dữ liệu từ các EDT
        name                = edt_Name_SU.getText().toString().trim();
        phone               = edt_Phone_SU.getText().toString().trim();
        passWord            = edt_Password_SU.getText().toString().trim();
        String passWordConfirm = edt_Password_Confirm_SU.getText().toString().trim();
        String idUser = "U" + Calendar.getInstance().getTime().getTime();
        nameFactory  = edt_NameFactory_SU.getText().toString().trim();
        // Chuyển dữ liệu qua signUpPresenter
        user.setName(name);
        user.setPhone(phone);
        user.setPassWord(passWord);
        user.setIdRole(idRole);
        user.setIdUser(idUser);
        user.setIdOwner(idUser);

        if (name.isEmpty() || phone.isEmpty() || passWord.isEmpty() || passWordConfirm.isEmpty() || idRole == 0 )
        {
            txt_Error_SU.setText(R.string.title_error_empty);
        }
        else if (user.getIdRole() == 3 && nameFactory.isEmpty())
        {
            txt_Error_SU.setText(R.string.title_error_empty);
        }
        else if (user.getIdRole() == 2 && idTypeFactory == 0 && nameFactory.isEmpty() )
        {
            txt_Error_SU.setText(R.string.title_error_empty);
        }
        else if (!passWord.equals(passWordConfirm))
        {
            txt_Error_SU.setText(R.string.title_error_incorrect_pass);
        }
        else if (passWord.length() < 6)
        {
            txt_Error_SU.setText(R.string.title_error_length_pass);
        }
        else
        {
            sendOTP(phone);
        }
    }


    private void sendOTP(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(Common.codeCounty + phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(SignUpActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(SignUpActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    // The SMS quota for the project has been exceeded
                                    Toast.makeText(SignUpActivity.this, " The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCodeSent(@NonNull String verifyID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verifyID, forceResendingToken);
                                // Chuyển đối tượng User sang activity Verify Code
                                Bundle bundleUser = new Bundle();
                                bundleUser.putSerializable("user",user); // Obj USER
                                // Chuyển idTypeFactory sang Verify Code
                                Intent intentUser = new Intent(SignUpActivity.this, EnterVerifyActivity.class);
                                intentUser.putExtra("idTypeFactory", idTypeFactory);
                                intentUser.putExtra("nameFactory", nameFactory);
                                intentUser.putExtra("phone", phone);
                                intentUser.putExtra("verifyID", verifyID);
                                intentUser.putExtras(bundleUser);
                                startActivity(intentUser);
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            signUpUser();
                        }
                    }
                });
    }

    private void signUpUser() {
        // Call API - Insert User
        Common.api.signUpUser(phone, user.getIdUser(), name, passWord, user.isAccept(),idRole,idTypeFactory,nameFactory, user.getIdOwner())
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                            ResponsePOST responsePOST = response.body();
                            assert  responsePOST != null;
                            if (responsePOST.getStatus() == 1)
                            {
                                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                finishAffinity();
                            }
                        Toast.makeText(SignUpActivity.this, responsePOST.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Show dialog choice type factory: Mở bottom dialog lựa chọn loại cơ sở của người quản lý
    private void showDialogTypeFactory() {
        dialogTypeFactory = new BottomDialogTypeFactory(typeFactoryList,this);
        dialogTypeFactory.show(getSupportFragmentManager() , dialogTypeFactory.getTag());
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
        RadioButton radio_manager  = dialogTypeUser.findViewById(R.id.radio_manager);
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
                        // Hiển thị loại cơ sở và nhập tên cơ sở
                        layout_TypeFactory.setVisibility(View.GONE);
                        txt_TypeFactory_SU.setVisibility(View.GONE);

                        layout_NameFactory.setVisibility(View.VISIBLE);
                        edt_NameFactory_SU.setVisibility(View.VISIBLE);

                        idTypeFactory = 1; // Loại cơ sở của nông dân là 1
                        user.setAccept(0);
                        break;
                    }
                    case  R.id.radio_customer:
                    {
                        txt_Type_SU.setText(radio_customer.getText());
                        idRole = 4;
                        layout_TypeFactory.setVisibility(View.GONE);
                        txt_TypeFactory_SU.setVisibility(View.GONE);
                        layout_NameFactory.setVisibility(View.GONE);
                        edt_NameFactory_SU.setVisibility(View.GONE);
                        idTypeFactory = 0;
                        user.setAccept(1);
                        break;
                    }
                    case  R.id.radio_manager:
                    {
                        typeFactoryPresenter.getTypeFactory();
                        txt_Type_SU.setText(radio_manager.getText());
                        idRole = 2;

                        user.setAccept(0);

                        // Hiển thị loại cơ sở và nhập tên cơ sở
                        layout_TypeFactory.setVisibility(View.VISIBLE);
                        txt_TypeFactory_SU.setVisibility(View.VISIBLE);
                        layout_NameFactory.setVisibility(View.VISIBLE);
                        edt_NameFactory_SU.setVisibility(View.VISIBLE);
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
        edt_Phone_SU                = findViewById(R.id.edt_Phone_SU);
        edt_Name_SU                 = findViewById(R.id.edt_Name_SU);
        edt_Password_SU             = findViewById(R.id.edt_Password_SU);
        edt_Password_Confirm_SU     = findViewById(R.id.edt_Password_Confirm_SU);
        btn_SignUp                  = findViewById(R.id.btn_SignUp);
        txt_Type_SU                 = findViewById(R.id.txt_Type_SU);
        txt_Error_SU                = findViewById(R.id.txt_Error_SU);
        img_Back_SU                 = findViewById(R.id.img_Back_SU);
        layout_TypeFactory          = findViewById(R.id.layout_TypeFactory);
        txt_TypeFactory_SU          = findViewById(R.id.txt_TypeFactory_SU);
        edt_NameFactory_SU          = findViewById(R.id.edt_NameFactory_SU);
        layout_NameFactory          = findViewById(R.id.layout_NameFactory);
        mAuth                       = FirebaseAuth.getInstance();
        typeFactoryPresenter        = new TypeFactory_Presenter(this);
        typeFactoryList             = new ArrayList<>();
    }

    @Override
    public void onClickChoiceType(Object obj) {
        TypeFactory type = (TypeFactory) obj;
        txt_TypeFactory_SU.setText(type.getNameTypeFactory());
        idTypeFactory = type.getIdTypeFactory();
        dialogTypeFactory.dismiss();
    }

    @Override
    public void getTypeFactory(List<TypeFactory> list) {
        typeFactoryList = list;
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successMessage(String message) {

    }

    @Override
    public void failedMessage(String message) {

    }
}