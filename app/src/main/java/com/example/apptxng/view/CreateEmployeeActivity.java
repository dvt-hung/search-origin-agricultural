package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.Account_Presenter;
import com.example.apptxng.presenter.IAccount;

import java.util.Calendar;
import java.util.List;

public class CreateEmployeeActivity extends AppCompatActivity implements IAccount {

    private EditText edt_Name_Employee, edt_Phone_Employee, edt_Password_Employee, edt_PasswordConfirm_Employee;
    private Button btn_SignUp;
    private ImageView img_Back;
    private TextView txt_Error_SU;
    private final int ACCEPT_EMPLOYEE = 1;
    private final int ID_ROLE_EMPLOYEE = 5;
    private final int ID_TYPE_FACTORY_EMPLOYEE = 0;
    private final String EMPTY_STRING = "EMPTY_STRING";
    private Account_Presenter accountPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);

        initView();
    }

    private void initView() {
        edt_Name_Employee                       = findViewById(R.id.edt_Name_Employee);
        edt_Phone_Employee                      = findViewById(R.id.edt_Phone_Employee);
        edt_Password_Employee                   = findViewById(R.id.edt_Password_Employee);
        edt_PasswordConfirm_Employee            = findViewById(R.id.edt_PasswordConfirm_Employee);
        btn_SignUp                              = findViewById(R.id.btn_SignUp);
        img_Back                                = findViewById(R.id.img_Back);
        txt_Error_SU                            = findViewById(R.id.txt_Error_SU);
        accountPresenter                        = new Account_Presenter(this,this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Back button: Đóng activity thêm tài khoản nhân viên
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Sign up button: Chuyển dữ liệu sang presenter xử lý
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create object - Employee
                User employee = new User();

                // Get value: Nhận giá trị từ edit text
                String name                     = edt_Name_Employee.getText().toString().trim();
                String phone                    = edt_Phone_Employee.getText().toString().trim();
                String password                 = edt_Password_Employee.getText().toString().trim();
                String password_confirm         = edt_PasswordConfirm_Employee.getText().toString().trim();

                // Gán giá trị cho employee
                String idUser = "U" + Calendar.getInstance().getTime().getTime();
                employee.setIdUser(idUser); // Id user
                employee.setName(name); // Name
                employee.setPhone(phone); // Phone
                employee.setPassWord(password); // Password
                employee.setAccept(ACCEPT_EMPLOYEE); // Accept
                employee.setIdRole(ID_ROLE_EMPLOYEE); // Id Role
                employee.setIdOwner(Common.currentUser.getIdUser()); // Set Id Owner

                // Call Presenter: Chuyển dữ liệu sang cho presenter xử lý
                txt_Error_SU.setVisibility(View.VISIBLE);



                accountPresenter.signUpUser(employee,password_confirm,ID_TYPE_FACTORY_EMPLOYEE,EMPTY_STRING);
            }
        });
    }


    // ====================== OVERRIDE METHOD - I.ACCOUNT =========================
    @Override
    public void listAccount(List<User> managerAccounts) {

    }

    @Override
    public void exception(String message) {

    }

    @Override
    public void successMessage(String message) {
        finish();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedMessage(String message) {
        txt_Error_SU.setText(message);
    }

    @Override
    public void inCorrectPassOld() {

    }

    @Override
    public void inCorrectPassLength() {
        txt_Error_SU.setText(R.string.title_error_length_pass);
    }

    @Override
    public void inCorrectPassConfirm() {
        txt_Error_SU.setText(R.string.title_error_incorrect_pass);
    }

    @Override
    public void emptyValue() {
        txt_Error_SU.setText(R.string.title_error_empty);

    }
}