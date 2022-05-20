package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.adapter.Images_Adapter;
import com.example.apptxng.bottom_dialog.BottomDialogOption;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.History;
import com.example.apptxng.model.ImageHistory;
import com.example.apptxng.model.ResponsePOST;
import com.example.apptxng.model.User;
import com.example.apptxng.presenter.History_Presenter;
import com.example.apptxng.presenter.IHistory;
import com.example.apptxng.presenter.IImageHistory;
import com.example.apptxng.presenter.ImageHistory_Presenter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailHistoryActivity extends AppCompatActivity implements Images_Adapter.IListenerImages,IImageHistory, IHistory {

    private History historyTemp;
    private Images_Adapter adapter;
    private ImageHistory_Presenter imageHistoryPresenter;
    private History_Presenter historyPresenter;
    private TextView txt_Info_Author,txt_Date_History_Detail, txt_Des_History_Detail, txt_TypeFactory_History_Detail,
            txt_NameFactory_History_Detail, txt_AddressFactory_History_Detail,txt_PhoneFactory_History_Detail, txt_WebFactory_History_Detail;
    private ImageView img_Back_Detail_History,img_Option_Detail_History;
    private List<ImageHistory> imageHistories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        // init view: Ánh xạ view
        initView();

        // Nhận history
        historyTemp = (History) getIntent().getExtras().getSerializable("history");


    }
    private void initView() {
        RecyclerView recycler_Images_History                = findViewById(R.id.recycler_Images_History);
        txt_Date_History_Detail                             = findViewById(R.id.txt_Date_History_Detail);
        txt_Des_History_Detail                              = findViewById(R.id.txt_Des_History_Detail);
        txt_TypeFactory_History_Detail                      = findViewById(R.id.txt_TypeFactory_History_Detail);
        txt_NameFactory_History_Detail                      = findViewById(R.id.txt_NameFactory_History_Detail);
        txt_AddressFactory_History_Detail                   = findViewById(R.id.txt_AddressFactory_History_Detail);
        txt_PhoneFactory_History_Detail                     = findViewById(R.id.txt_PhoneFactory_History_Detail);
        txt_WebFactory_History_Detail                       = findViewById(R.id.txt_WebFactory_History_Detail);
        img_Back_Detail_History                             = findViewById(R.id.img_Back_Detail_History);
        img_Option_Detail_History                           = findViewById(R.id.img_Option_Detail_History);
        txt_Info_Author                                     = findViewById(R.id.txt_Info_Author);
        imageHistories                                      = new ArrayList<>();
        historyPresenter                                    = new History_Presenter(this,this);
        // Presenter
        imageHistoryPresenter                               = new ImageHistory_Presenter(this, this);

        // Gán adapter cho recycler view
        adapter   = new Images_Adapter(this,this);
        recycler_Images_History.setAdapter(adapter);

        // Tạo layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recycler_Images_History.setLayoutManager(layoutManager);


    }


    @Override
    protected void onResume() {
        super.onResume();

        // Hiển thị dữ liệu
        displayValue();
        // Load images: Tải hình ảnh của history
        imageHistoryPresenter.getImageHistory(historyTemp.getIdHistory());
        
        // init events
        initEvents();
    }

    private void initEvents() {

        // Back Button: Đóng activity đi
        img_Back_Detail_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Option Button: Hiển thị dialog có các lựa chọn cho người dùng
        img_Option_Detail_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOption();
            }
        });

        // Text view Author: Hiển thị dialog thông tin của tác giá
        txt_Info_Author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInfoEmployee();
            }
        });
    }

    // Lấy thông tin nhân viên quản lí sản phẩm
    private synchronized void getInfoEmployee() {
        // Tạo progress dialog
        ProgressDialog progressDialog = Common.createProgress(this);
        progressDialog.show();
        // Gọi đến API - lấy info user
        Common.api.getInfoUser(historyTemp.getIdAuthor())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        User employee = response.body();
                        if (employee != null)
                        {
                            showInfoEmployee(employee);
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Toast.makeText(DetailHistoryActivity.this, "Đã có lỗi. Vui lòng thử lại! " , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });
    }


    // Hiển thi thông tin của nhân viên đang quản lí
    private void showInfoEmployee(User user) {
        // Config dialog
        Dialog dialogInfo = new Dialog(this);
        dialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInfo.setContentView(R.layout.dialog_info_customer);
        dialogInfo.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Init view dialog: Ánh xạ view dialog
        TextView txt_Email          = dialogInfo.findViewById(R.id.txt_Email);
        TextView txt_Name           = dialogInfo.findViewById(R.id.txt_Name);
        TextView txt_Phone          = dialogInfo.findViewById(R.id.txt_Phone);
        TextView txt_Address        = dialogInfo.findViewById(R.id.txt_Address);

        // Set value: Gán giá trị cho text view

        // Email
        Common.displayValueTextView(txt_Email,user.getEmail());
        // Name
        Common.displayValueTextView(txt_Name,user.getName());
        // Email
        Common.displayValueTextView(txt_Phone,user.getPhone());
        // Email
        Common.displayValueTextView(txt_Address,user.getAddress());

        // Show dialog
        dialogInfo.show();
    }

    // HIỂN THỊ DIALOG OPTION
    private void showDialogOption() {
        BottomDialogOption dialogOption = new BottomDialogOption(new BottomDialogOption.IDialogOptionListener() {
            @Override
            public void deleteHistory() {
                showDialogDeleteHistory();
            }

            @Override
            public void imageHistory() {
                Intent intentImage = new Intent(DetailHistoryActivity.this, ImageHistoryActivity.class);
                intentImage.putExtra("idHistory", historyTemp.getIdHistory());
                startActivity(intentImage);
            }

            @Override
            public void desHistory() {
                showDialogUpdateDes();
            }
        });
        dialogOption.show(getSupportFragmentManager(),dialogOption.getTag());
    }

    // Hiện dialog cập nhật mô tả
    private void showDialogUpdateDes() {
        // Tạo và cài đặt layout cho dialog
        Dialog dialogUpdate = new Dialog(this);
        dialogUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdate.setContentView(R.layout.dialog_one_edittext);
        dialogUpdate.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogUpdate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txt_Title_Dialog               = dialogUpdate.findViewById(R.id.txt_Title_Dialog);
        EditText edt_Content_Dialog             = dialogUpdate.findViewById(R.id.edt_Content_Dialog);
        Button btn_Cancel_Dialog                = dialogUpdate.findViewById(R.id.btn_Cancel_Dialog);
        Button btn_Confirm_Dialog               = dialogUpdate.findViewById(R.id.btn_Confirm_Dialog);

        // Set title dialog
        txt_Title_Dialog.setText(R.string.update_des);
        edt_Content_Dialog.setHint(R.string.description);

        // Hiển thị dữ liệu ban đầu của mô ta
        edt_Content_Dialog.setText(historyTemp.getDescriptionHistory());

        // Hiển thị dialog
        dialogUpdate.show();

        /*
         * 1. Khi chọn "Xác nhận": Thêm đơn vị tính mới vào csdl
         * 2. Khi chọn "Hủy": Đóng dialog
         * */

        // 1. Xác nhận thêm
        btn_Confirm_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String des = edt_Content_Dialog.getText().toString().trim();

                // Gọi đến presenter
                historyPresenter.UpdateDesHistory(historyTemp.getIdHistory(),des);
                dialogUpdate.dismiss();
            }
        });

        // 2. Hủy thêm
        btn_Cancel_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdate.dismiss();
            }
        });
    }

    // Hiển thị dialog xóa nhật ký
    private void showDialogDeleteHistory() {
        // Khởi tạo dialog
        Dialog dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setContentView(R.layout.dialog_delete);
        dialogDelete.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo và ánh xạ view của dialog update
        Button btn_Cancel_DeleteCategory_Dialog     = dialogDelete.findViewById(R.id.btn_Cancel_Delete_Dialog);
        Button btn_Confirm_DeleteCategory_Dialog    = dialogDelete.findViewById(R.id.btn_Confirm_Delete_Dialog);
        TextView txt_Title_Delete_Dialog            = dialogDelete.findViewById(R.id.txt_Title_Delete_Dialog);
        TextView txt_Message_Delete_Dialog          = dialogDelete.findViewById(R.id.txt_Message_Delete_Dialog);

        // Gán Title, Message cho dialog
        txt_Title_Delete_Dialog.setText(R.string.title_delete_history);
        txt_Message_Delete_Dialog.setText(R.string.title_question_delete_history);

        // Hiển thị dialog
        dialogDelete.show();

        /*
         * 1. Khi chọn Confirm Button: Sẽ xóa đi đơn vị tính này
         * 2. Khu chọn Cancel Button: Sẽ tắt đi dialog
         * */

        //1. Confirm Button
        btn_Confirm_DeleteCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xóa history
                imageHistoryPresenter.deleteHistory(imageHistories);
                dialogDelete.cancel();
            }
        });

        // 2. Cancel Button
        btn_Cancel_DeleteCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDelete.cancel();
            }
        });

    }

    // Hiển thị dữ liệu ban đầu
    private void displayValue() {
        txt_Date_History_Detail.setText(historyTemp.getDateHistory());
        txt_Des_History_Detail.setText(historyTemp.getDescriptionHistory());
        txt_TypeFactory_History_Detail.setText(historyTemp.getType_factory().getNameTypeFactory());
        txt_NameFactory_History_Detail.setText(historyTemp.getFactory().getNameFactory());

        checkValueNullForTextView(txt_AddressFactory_History_Detail,historyTemp.getFactory().getAddressFactory());
        checkValueNullForTextView(txt_PhoneFactory_History_Detail,historyTemp.getFactory().getPhoneFactory());
        checkValueNullForTextView(txt_WebFactory_History_Detail,historyTemp.getFactory().getWebFactory());

        // KIỂM TRA NGƯỜI TẠO CÓ HOẶC NGƯỜI SỬ HỮU HIỆN TẠI CÓ ĐÚNG KHÔNG
        /*
        * CÁC ĐIỀU KIỆN KHÔNG ĐƯỢC HIỂN THỊ BUTTON OPTION
        * 1. Nếu người đang xem chi tiết lịch sử là Khách hàng
        * 2. Nếu tác giả không đúng với người dùng hiện tại
        * 3. Nếu không phải là chủ của cơ sở sản phẩm đang được lưu giữ
        * 4. Nếu không phải cùng 1 ngày với lịch sử được tạo
        * */
        int ID_ROLE_EMPLOYEE = 5; // Id Role của nhân viên
        if (Common.currentUser.getIdRole() == Common.ID_ROLE_CUSTOMER
                || Common.currentUser.getIdRole() == Common.ID_ROLE_EMPLOYEE && !historyTemp.getIdAuthor().equals(Common.currentUser.getIdUser())
                || Common.currentUser.getIdRole() == Common.ID_ROLE_FARMER && !historyTemp.getFactory().getIdUser().equals(Common.currentUser.getIdUser())
                ||!compareDate())
        {
            img_Option_Detail_History.setVisibility(View.GONE);
        }

    }

    // Kiểm tra quyền chỉnh sửa lại nhật ký: Chỉ trong cùng 1 một ngày thì có thể chỉnh sửa
    private boolean compareDate() {

        // KIỂM TRA NGÀY HIỆN TẠI CÓ CÙNG NGÀY VỚI TẠO NHẬT KÝ KHÔNG
        String strDateCurrent = Common.dateFormat.format(Calendar.getInstance().getTime());
        try {
            Date dateCurrent = Common.dateFormat.parse(strDateCurrent);
            Date dateProduct = Common.dateFormat.parse(historyTemp.getDateHistory());

            assert dateCurrent != null;
            if (dateCurrent.compareTo(dateProduct) != 0)
            {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    // Check value null text view
    private void checkValueNullForTextView(TextView view, String value)
    {
        if (value == null || value.isEmpty() || value.equals(" "))
        {
            view.setText(R.string.title_error_empty_user);
        }
        else
        {
            view.setText(value);
        }
    }




    // ********* IMAGE HISTORY PRESENTER **********
    @Override
    public void getImages(List<ImageHistory> images) {
        imageHistories = images;
        adapter.setUriList(imageHistories);
    }

    @Override
    public void exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        History_Product_Customer_Fragment.reload = true;
        finish();
    }

    @Override
    public void failed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // ********* IMAGE ADAPTER  **********
    @Override
    public void onClickImage(ImageHistory imageHistory) {
        displayImageHistory(imageHistory);
    }

    private void displayImageHistory(ImageHistory image) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.display_image);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Image view
        ImageView img = dialog.findViewById(R.id.img_Display);

        // Load image
        Glide.with(DetailHistoryActivity.this).load(image.getImageHistory()).into(img);

        dialog.show();

    }
    // ********* HISTORY PRESENTER **********
    @Override
    public void successMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        History_Product_Customer_Fragment.reload = true;
        finish();
    }

    @Override
    public void failedMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void exceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void emptyValue() {
        Toast.makeText(this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void getHistory(List<History> histories) {

    }
}