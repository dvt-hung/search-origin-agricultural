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

        // init view: ??nh x??? view
        initView();

        // Nh???n history
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

        // G??n adapter cho recycler view
        adapter   = new Images_Adapter(this,this);
        recycler_Images_History.setAdapter(adapter);

        // T???o layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recycler_Images_History.setLayoutManager(layoutManager);


    }


    @Override
    protected void onResume() {
        super.onResume();

        // Hi???n th??? d??? li???u
        displayValue();
        // Load images: T???i h??nh ???nh c???a history
        imageHistoryPresenter.getImageHistory(historyTemp.getIdHistory());
        
        // init events
        initEvents();
    }

    private void initEvents() {

        // Back Button: ????ng activity ??i
        img_Back_Detail_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Option Button: Hi???n th??? dialog c?? c??c l???a ch???n cho ng?????i d??ng
        img_Option_Detail_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOption();
            }
        });

        // Text view Author: Hi???n th??? dialog th??ng tin c???a t??c gi??
        txt_Info_Author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInfoEmployee();
            }
        });
    }

    // L???y th??ng tin nh??n vi??n qu???n l?? s???n ph???m
    private synchronized void getInfoEmployee() {
        // T???o progress dialog
        ProgressDialog progressDialog = Common.createProgress(this);
        progressDialog.show();
        // G???i ?????n API - l???y info user
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
                        Toast.makeText(DetailHistoryActivity.this, "???? c?? l???i. Vui l??ng th??? l???i! " , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });
    }


    // Hi???n thi th??ng tin c???a nh??n vi??n ??ang qu???n l??
    private void showInfoEmployee(User user) {
        // Config dialog
        Dialog dialogInfo = new Dialog(this);
        dialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInfo.setContentView(R.layout.dialog_info_customer);
        dialogInfo.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Init view dialog: ??nh x??? view dialog
        TextView txt_Email          = dialogInfo.findViewById(R.id.txt_Email);
        TextView txt_Name           = dialogInfo.findViewById(R.id.txt_Name);
        TextView txt_Phone          = dialogInfo.findViewById(R.id.txt_Phone);
        TextView txt_Address        = dialogInfo.findViewById(R.id.txt_Address);

        // Set value: G??n gi?? tr??? cho text view

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

    // HI???N TH??? DIALOG OPTION
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

    // Hi???n dialog c???p nh???t m?? t???
    private void showDialogUpdateDes() {
        // T???o v?? c??i ?????t layout cho dialog
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

        // Hi???n th??? d??? li???u ban ?????u c???a m?? ta
        edt_Content_Dialog.setText(historyTemp.getDescriptionHistory());

        // Hi???n th??? dialog
        dialogUpdate.show();

        /*
         * 1. Khi ch???n "X??c nh???n": Th??m ????n v??? t??nh m???i v??o csdl
         * 2. Khi ch???n "H???y": ????ng dialog
         * */

        // 1. X??c nh???n th??m
        btn_Confirm_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String des = edt_Content_Dialog.getText().toString().trim();

                // G???i ?????n presenter
                historyPresenter.UpdateDesHistory(historyTemp.getIdHistory(),des);
                dialogUpdate.dismiss();
            }
        });

        // 2. H???y th??m
        btn_Cancel_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdate.dismiss();
            }
        });
    }

    // Hi???n th??? dialog x??a nh???t k??
    private void showDialogDeleteHistory() {
        // Kh???i t???o dialog
        Dialog dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setContentView(R.layout.dialog_delete);
        dialogDelete.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai b??o v?? ??nh x??? view c???a dialog update
        Button btn_Cancel_DeleteCategory_Dialog     = dialogDelete.findViewById(R.id.btn_Cancel_Delete_Dialog);
        Button btn_Confirm_DeleteCategory_Dialog    = dialogDelete.findViewById(R.id.btn_Confirm_Delete_Dialog);
        TextView txt_Title_Delete_Dialog            = dialogDelete.findViewById(R.id.txt_Title_Delete_Dialog);
        TextView txt_Message_Delete_Dialog          = dialogDelete.findViewById(R.id.txt_Message_Delete_Dialog);

        // G??n Title, Message cho dialog
        txt_Title_Delete_Dialog.setText(R.string.title_delete_history);
        txt_Message_Delete_Dialog.setText(R.string.title_question_delete_history);

        // Hi???n th??? dialog
        dialogDelete.show();

        /*
         * 1. Khi ch???n Confirm Button: S??? x??a ??i ????n v??? t??nh n??y
         * 2. Khu ch???n Cancel Button: S??? t???t ??i dialog
         * */

        //1. Confirm Button
        btn_Confirm_DeleteCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // X??a history
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

    // Hi???n th??? d??? li???u ban ?????u
    private void displayValue() {
        txt_Date_History_Detail.setText(historyTemp.getDateHistory());
        txt_Des_History_Detail.setText(historyTemp.getDescriptionHistory());
        txt_TypeFactory_History_Detail.setText(historyTemp.getType_factory().getNameTypeFactory());
        txt_NameFactory_History_Detail.setText(historyTemp.getFactory().getNameFactory());

        checkValueNullForTextView(txt_AddressFactory_History_Detail,historyTemp.getFactory().getAddressFactory());
        checkValueNullForTextView(txt_PhoneFactory_History_Detail,historyTemp.getFactory().getPhoneFactory());
        checkValueNullForTextView(txt_WebFactory_History_Detail,historyTemp.getFactory().getWebFactory());

        // KI???M TRA NG?????I T???O C?? HO???C NG?????I S??? H???U HI???N T???I C?? ????NG KH??NG
        /*
        * C??C ??I???U KI???N KH??NG ???????C HI???N TH??? BUTTON OPTION
        * 1. N???u ng?????i ??ang xem chi ti???t l???ch s??? l?? Kh??ch h??ng
        * 2. N???u t??c gi??? kh??ng ????ng v???i ng?????i d??ng hi???n t???i
        * 3. N???u kh??ng ph???i l?? ch??? c???a c?? s??? s???n ph???m ??ang ???????c l??u gi???
        * 4. N???u kh??ng ph???i c??ng 1 ng??y v???i l???ch s??? ???????c t???o
        * */
        int ID_ROLE_EMPLOYEE = 5; // Id Role c???a nh??n vi??n
        if (Common.currentUser.getIdRole() == Common.ID_ROLE_CUSTOMER
                || Common.currentUser.getIdRole() == Common.ID_ROLE_EMPLOYEE && !historyTemp.getIdAuthor().equals(Common.currentUser.getIdUser())
                || Common.currentUser.getIdRole() == Common.ID_ROLE_FARMER && !historyTemp.getFactory().getIdUser().equals(Common.currentUser.getIdUser())
                ||!compareDate())
        {
            img_Option_Detail_History.setVisibility(View.GONE);
        }

    }

    // Ki???m tra quy???n ch???nh s???a l???i nh???t k??: Ch??? trong c??ng 1 m???t ng??y th?? c?? th??? ch???nh s???a
    private boolean compareDate() {

        // KI???M TRA NG??Y HI???N T???I C?? C??NG NG??Y V???I T???O NH???T K?? KH??NG
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