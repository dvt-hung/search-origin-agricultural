package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Images_Adapter;
import com.example.apptxng.bottom_dialog.BottomDialogOption;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.History;
import com.example.apptxng.model.ImageHistory;
import com.example.apptxng.model.ResponsePOST;
import com.example.apptxng.presenter.IImageHistory;
import com.example.apptxng.presenter.ImageHistory_Presenter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailHistoryActivity extends AppCompatActivity implements Images_Adapter.IListenerImages,IImageHistory {

    private History historyTemp;
    private Images_Adapter adapter;
    private ImageHistory_Presenter imageHistoryPresenter;
    private TextView txt_Date_History_Detail, txt_Des_History_Detail, txt_TypeFactory_History_Detail,
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
        Bundle receiveHistory = getIntent().getExtras();
        historyTemp = (History) receiveHistory.getSerializable("history");

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
        imageHistories                                      = new ArrayList<>();
        // Presenter
        imageHistoryPresenter = new ImageHistory_Presenter(this, this);

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
        img_Back_Detail_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_Option_Detail_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOption();
            }
        });
    }

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
        });
        dialogOption.show(getSupportFragmentManager(),dialogOption.getTag());
    }

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



    private void displayValue() {
        txt_Date_History_Detail.setText(historyTemp.getDateHistory());
        txt_Des_History_Detail.setText(historyTemp.getDescriptionHistory());
        txt_TypeFactory_History_Detail.setText(historyTemp.getType_factory().getNameTypeFactory());
        txt_NameFactory_History_Detail.setText(historyTemp.getFactory().getNameFactory());
        txt_AddressFactory_History_Detail.setText(historyTemp.getFactory().getAddressFactory());
        txt_PhoneFactory_History_Detail.setText(historyTemp.getFactory().getPhoneFactory());

        // Nếu người dùng hiện tại là người đã viết nhật ký thì có thể update
        if (!historyTemp.getFactory().getIdUser().equals(Common.currentUser.getIdUser()))
        {
            img_Option_Detail_History.setVisibility(View.GONE);
        }
    }

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
        finish();
    }

    @Override
    public void failed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickImage(ImageHistory imageHistory) {

    }
}