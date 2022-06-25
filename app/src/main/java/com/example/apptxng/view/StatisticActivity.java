package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Employee_Adapter;
import com.example.apptxng.adapter.Product_Adapter;
import com.example.apptxng.bottom_dialog.BottomDialogEmployee;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.Account_Presenter;
import com.example.apptxng.presenter.IAccount;
import com.example.apptxng.presenter.IProduct;
import com.example.apptxng.presenter.IStatistic;
import com.example.apptxng.presenter.Product_Presenter;
import com.example.apptxng.presenter.Statistic_Activity_Presenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatisticActivity extends AppCompatActivity implements Product_Adapter.IProductAdapterListener, IStatistic, Employee_Adapter.IEmployeeListener {

    private final String TEXT_QUANTITY = "Số lượng: ";
    private ImageView img_Menu_Statistic,img_Back_Statistic;
    private TextView txt_Quantity;
    private Product_Adapter productAdapter;
    private BottomDialogEmployee bottomDialogEmployee;
    private Statistic_Activity_Presenter statisticPresenter;
    private Factory factoryTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        // Init view: Ánh xạ view
        initView();
    }

    private void initView() {
        img_Menu_Statistic                  = findViewById(R.id.img_Menu_Statistic);
        img_Back_Statistic                  = findViewById(R.id.img_Back_Statistic);
        txt_Quantity                        = findViewById(R.id.txt_Quantity);
        RecyclerView recycler_Statistic     = findViewById(R.id.recycler_Statistic);

        // Adapter recycler view
        productAdapter                      = new Product_Adapter(this,this);
        recycler_Statistic.setAdapter(productAdapter);


        // Presenter
        statisticPresenter                  = new Statistic_Activity_Presenter(this,this);

        // Get Info Factory: Lấy thông tin của Factory
        statisticPresenter.getInfoFactory();
    }


    @Override
    protected void onResume() {
        super.onResume();

//        // Menu Bottom: Mở popup menu
        img_Menu_Statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuStatistic();
            }
        });

        // Back Button: Đóng activity
        img_Back_Statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void showMenuStatistic() {
        PopupMenu menuStatistic = new PopupMenu(this,img_Menu_Statistic);
        menuStatistic.getMenuInflater().inflate(R.menu.menu_statistic_farmer,menuStatistic.getMenu());
        menuStatistic.show();

        // Event Click Menu
        menuStatistic.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.menu_by_day:
                        showDialogPicker();
                        break;

                    case R.id.menu_changed:
                        statisticPresenter.getProductChanged(Common.currentUser.getIdUser());
                        break;

                    case R.id.menu_by_employee:
                        getListEmployee();
                        break;
                }

                return true;
            }
        });

    }

    // HIỂN THỊ DIALOG PICKER
    private void showDialogPicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String strDatePicker = makeDateToString(day,insertZeroOfMonth(month),year);

                getProductByDate(strDatePicker);
            }
        };


        // Set Ngày hiện tại
        int year = Common.calendar.get(Calendar.YEAR);
        int month = Common.calendar.get(Calendar.MONTH);
        int day = Common.calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                R.style.ThemeOverlay_Material3_MaterialTimePicker,dateSetListener,
                year,month,day);
        datePickerDialog.show();
    }

    // Chuyển đổi các số tháng dưới 10 sẽ thêm số không ở đầu
    private String insertZeroOfMonth(int month) {
        if( month < 10)
        {
            return "0" + month;
        }
        else
        {
            return String.valueOf(month);
        }
    }

    private void getProductByDate(String strDatePicker) {
        // Kiểm tra người dùng hiện tại: Nông dân, Quản lý cơ sở
        if (Common.currentUser.getIdRole() == Common.ID_ROLE_FARMER)
        {
            statisticPresenter.getProductByDateFarmer(factoryTemp.getIdFactory(),strDatePicker);
        }
        else if (Common.currentUser.getIdRole() == Common.ID_ROLE_MANAGER)
        {
            statisticPresenter.getProductByDateManager(Common.currentUser.getIdUser(),strDatePicker);
        }
    }

    // Chuyển đổi ngày đã chọn sang String
    private String makeDateToString(int day, String month, int year) {
        return day + "/" + month + "/" + year;
    }


    // LẤY DANH SÁCH NHÂN VIÊN
    private synchronized void getListEmployee() {
        statisticPresenter.getListEmployeeAccount(Common.currentUser.getIdUser());
    }

    // ****************** OVERRIDE METHOD: I PRODUCT ADAPTER ****************
    @Override
    public void onClickProduct(Product product) {
        // Khi click vào sản phẩm sẽ chuyển sang activity chi tiết sản phẩm
        Bundle bundleProduct = new Bundle();
        bundleProduct.putSerializable("product",product);

        Intent intentDetailProduct = new Intent(StatisticActivity.this, Detail_Product_Activity.class);
        intentDetailProduct.putExtras(bundleProduct);
        startActivity(intentDetailProduct);
    }

    // ****************** OVERRIDE METHOD: I STATISTIC ****************
    @Override
    public void listAccount(List<User> managerAccounts) {
        bottomDialogEmployee = new BottomDialogEmployee(managerAccounts,this);
        bottomDialogEmployee.show(getSupportFragmentManager(), bottomDialogEmployee.getTag());
    }

    @Override
    public void onClickEmployee(User employee) {
        statisticPresenter.getProductEmployee(employee.getIdUser());
        bottomDialogEmployee.dismiss();
    }

    @Override
    public void listProducts(List<Product> products) {

        showProductList(products);

    }

    @SuppressLint("SetTextI18n")
    private void showProductList(List<Product> products) {
        for (int second = 1; second < products.size(); second++) {
            int first = second -1;
            Product pFirst = products.get(first);
            Product pSecond = products.get(second);

            while (first >= 0 && pFirst.getIdProduct().equals(pSecond.getIdProduct()))
            {
                products.remove(pFirst);
                first--;
            }
        }
        txt_Quantity.setText(TEXT_QUANTITY + products.size());
        productAdapter.setProductList(products);
    }

    @Override
    public void exception(String message) {

    }

    @Override
    public void infoFactory(Factory factory) {
        factoryTemp = factory;
    }
}