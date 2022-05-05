package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.bottom_dialog.BottomDialogTypeFactory;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.ISignUp;
import com.example.apptxng.presenter.ITypeFactory;
import com.example.apptxng.presenter.SignUp_Presenter;
import com.example.apptxng.presenter.TypeFactory_Presenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements ChoiceType_Adapter.IListenerChoiceType, ITypeFactory {

    private LinearLayout layout_TypeFactory,layout_NameFactory;
    private EditText edt_Phone_SU,edt_Name_SU, edt_Password_SU, edt_Password_Confirm_SU,edt_NameFactory_SU ;
    private TextView txt_Type_SU,txt_Error_SU, txt_TypeFactory_SU;
    private ImageView img_Back_SU;
    private Button  btn_SignUp;
    private String name, passWord, passWordConfirm, phone;
    private int idTypeFactory;
    private int idRole;
    private final User user = new User();
    private List<TypeFactory> typeFactoryList;
    private TypeFactory_Presenter typeFactoryPresenter;
    private BottomDialogTypeFactory dialogTypeFactory;
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
                txt_Error_SU.setVisibility(View.VISIBLE);
                // Lấy dữ liệu từ các EDT
                name                = edt_Name_SU.getText().toString().trim();
                phone               = edt_Phone_SU.getText().toString().trim();
                passWord            = edt_Password_SU.getText().toString().trim();
                passWordConfirm     = edt_Password_Confirm_SU.getText().toString().trim();
                String idUser       = "U" + Calendar.getInstance().getTime().getTime();
                String nameFactory  = edt_NameFactory_SU.getText().toString().trim();
                // Chuyển dữ liệu qua signUpPresenter
                user.setName(name);
                user.setPhone(phone);
                user.setPassWord(passWord);
                user.setIdRole(idRole);
                user.setIdUser(idUser);

                if (name.isEmpty() || phone.isEmpty() || passWord.isEmpty() || passWordConfirm.isEmpty() || idRole == 0 )
                {
                    txt_Error_SU.setVisibility(View.VISIBLE);
                    txt_Error_SU.setText(R.string.title_error_empty);
                }
                else if (user.getIdRole() == 3 && nameFactory.isEmpty())
                {
                    txt_Error_SU.setVisibility(View.VISIBLE);
                    txt_Error_SU.setText(R.string.title_error_empty);
                }
                else if (user.getIdRole() == 2 && idTypeFactory == 0 && nameFactory.isEmpty() )
                {
                    txt_Error_SU.setVisibility(View.VISIBLE);
                    txt_Error_SU.setText(R.string.title_error_empty);
                }
                else if (!passWord.equals(passWordConfirm))
                {
                    txt_Error_SU.setVisibility(View.VISIBLE);
                    txt_Error_SU.setText(R.string.title_error_incorrect_pass);
                }
                else if (passWord.length() < 6)
                {
                    txt_Error_SU.setVisibility(View.VISIBLE);
                    txt_Error_SU.setText(R.string.title_error_length_pass);
                }
                else
                {
                    // Chuyển đối tượng User sang activity Verify Code
                    Bundle bundleUser = new Bundle();
                    bundleUser.putSerializable("user",user);
                    // Chuyển idTypeFactory sang Verify Code
                    Intent intentUser = new Intent(SignUpActivity.this, EnterVerifyActivity.class);
                    intentUser.putExtra("idTypeFactory", idTypeFactory);
                    intentUser.putExtra("nameFactory", nameFactory);
                    startActivity(intentUser);
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

        // Show dialog choice type factory
        txt_TypeFactory_SU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTypeFactory();
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
}