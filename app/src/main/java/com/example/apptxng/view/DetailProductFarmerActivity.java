package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.adapter.History_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.History;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.ResponsePOST;
import com.example.apptxng.presenter.History_Presenter;
import com.example.apptxng.presenter.IHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductFarmerActivity extends AppCompatActivity implements History_Adapter.IListenerHistory, IHistory {

    private ImageView img_Option_Detail_Product,img_Close_Detail_Product,img_Detail_Product;
    private TextView txt_Balance_Detail_Product,txt_Name_Detail_Product,txt_Des_Detail_Product,txt_Price_Detail_Product,txt_Quantity_Detail_Product,txt_QuantitySold_Detail_Product;
    private Product product;
    private RecyclerView recycler_History_Detail_Product;
    private History_Adapter historyAdapter;
    private History_Presenter historyPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product_farmer);


        // Init view: Ánh xạ view
        initView();
    }

    // Ánh xạ view
    private void initView() {
        img_Option_Detail_Product               = findViewById(R.id.img_Option_Detail_Product);
        img_Detail_Product                      = findViewById(R.id.img_Detail_Product);
        txt_Name_Detail_Product                 = findViewById(R.id.txt_Name_Detail_Product);
        txt_Des_Detail_Product                  = findViewById(R.id.txt_Des_Detail_Product);
        txt_Price_Detail_Product                = findViewById(R.id.txt_Price_Detail_Product);
        txt_Quantity_Detail_Product             = findViewById(R.id.txt_Quantity_Detail_Product);
        txt_QuantitySold_Detail_Product         = findViewById(R.id.txt_QuantitySold_Detail_Product);
        txt_Balance_Detail_Product              = findViewById(R.id.txt_Balance_Detail_Product);
        img_Close_Detail_Product                = findViewById(R.id.img_Close_Detail_Product);
        recycler_History_Detail_Product         = findViewById(R.id.recycler_History_Detail_Product);
        historyPresenter                        = new History_Presenter(this,this);

        // Get Bundle: Nhận đối tượng product đã truyền qua
        Bundle bundleDetailsProduct = getIntent().getBundleExtra("b_product");
        product = (Product) bundleDetailsProduct.getSerializable("product");

        // Khởi tạo adapter cho recycler view
        historyAdapter = new History_Adapter(this);

        // Gán adapter cho recycler view
        recycler_History_Detail_Product.setAdapter(historyAdapter);

        // Tạo layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_History_Detail_Product.setLayoutManager(layoutManager);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // 1. Hiển thị giá trị của sản phẩm lên text view
        displayValueProduct();

        // 2. Tải danh sách nhật ký của sản phẩm
        loadHistory();

        // 2. Close Button: Khi ấn thì sẽ đóng activity lại
        img_Close_Detail_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 3. Option Button: Mở dialog bottom có 3 chức năng: Thêm nhật ký, Chỉnh sửa sản phẩm, Xóa sản phẩm
        img_Option_Detail_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOption();
            }
        });
    }

    private void loadHistory() {
        historyPresenter.loadHistory(product.getIdProduct());
    }

    // Gán giá trị của product, hiển thị lên text view
    private void displayValueProduct() {
        // Tên sản phẩm
        txt_Name_Detail_Product.setText(product.getNameProduct());

        // Giá sản phẩm
        txt_Price_Detail_Product.setText(Common.numberFormat.format(product.getPriceProduct()));

        // Số lượng sản phẩm
        txt_Quantity_Detail_Product.setText(String.valueOf(product.getQuantityProduct()));

        // Số lượng đã bán của sản phẩm
        txt_QuantitySold_Detail_Product.setText(String.valueOf(product.getQuantitySold()));

        // Mô tả của sản phẩm
        txt_Des_Detail_Product.setText(product.getDescriptionProduct());

        // Đơn vị tính
        txt_Balance_Detail_Product.setText(product.getBalance().getNameBalance());

        // Ảnh sản phẩm
        Glide.with(this).load(product.getImageProduct()).error(R.drawable.logo).into(img_Detail_Product);
    }


    // Show dialog option: Hiển thị các lựa chọn của
    private void showDialogOption() {
        Dialog dialogOptions = new Dialog(this);
        dialogOptions.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOptions.setContentView(R.layout.dialog_bottom_product_farmer);
        dialogOptions.getWindow().setGravity(Gravity.BOTTOM);
        dialogOptions.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogOptions.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khai báo ảnh xạ view cho dialog
        Button btn_InsertHistory_OptionProduct      = dialogOptions.findViewById(R.id.btn_InsertHistory_OptionProduct);
        Button btn_Update_OptionProduct             = dialogOptions.findViewById(R.id.btn_Update_OptionProduct);
        Button btn_Delete_OptionProduct             = dialogOptions.findViewById(R.id.btn_Delete_OptionProduct);

        // Hiện dialog
        dialogOptions.show();

        /*
         * 2. Insert History Button: Mở sang activity History
         * 3. Update Button: Mở sang activity Update
         * 4. Delete Button: Mở dialog xác nhận xóa
         * */


        // 1. Insert History Button
        btn_InsertHistory_OptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentInsertHistory = new Intent(DetailProductFarmerActivity.this, InsertHistoryActivity.class);
                intentInsertHistory.putExtra("idProduct",product.getIdProduct());
                startActivity(intentInsertHistory);
                dialogOptions.dismiss();
            }
        });

        // 2. Update Button
        btn_Update_OptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleUpdate = new Bundle();
                bundleUpdate.putSerializable("product_detail",product); // Đẩy product vào bundle

                Intent intentUpdate = new Intent(DetailProductFarmerActivity.this,UpdateProductFarmerActivity.class);
                intentUpdate.putExtra("bundle_product",bundleUpdate);

                startActivity(intentUpdate);
                dialogOptions.dismiss();
                finish();
            }
        });

        // 3. Delete Button: Mở dialog xác nhận xóa sản phẩm
        btn_Delete_OptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDeleteProduct();
                dialogOptions.dismiss();
            }
        });
    }

    // Show dialog delete: Xác nhận có muốn xóa hay không
    private void showDialogDeleteProduct() {
        Dialog dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setContentView(R.layout.dialog_delete);
        dialogDelete.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Ánh xạ view
        TextView txt_Title_Delete_Dialog        = dialogDelete.findViewById(R.id.txt_Title_Delete_Dialog);
        TextView txt_Message_Delete_Dialog      = dialogDelete.findViewById(R.id.txt_Message_Delete_Dialog);
        Button btn_Cancel_Delete_Dialog         = dialogDelete.findViewById(R.id.btn_Cancel_Delete_Dialog);
        Button btn_Confirm_Delete_Dialog        = dialogDelete.findViewById(R.id.btn_Confirm_Delete_Dialog);

        txt_Title_Delete_Dialog.setText(R.string.delete_product);
        txt_Message_Delete_Dialog.setText(R.string.question_delete_product);

        dialogDelete.show();

        /*
        * 1. Cancel Button: Tắt dialog xóa đi
        * 2. Confirm Button: Tiển hành xóa sản phẩm. Nếu thành công thì Toast lên và đóng activity
        * */

        // 1. Cancel Button
        btn_Cancel_Delete_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDelete.cancel();

            }
        });


        // 2. Confirm Button
        btn_Confirm_Delete_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.api.deleteProduct(product.getIdProduct(),product.getImageProduct())
                        .enqueue(new Callback<ResponsePOST>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                                ResponsePOST responsePOST = response.body();

                                assert responsePOST != null;
                                if (responsePOST.getStatus() == 1)
                                {
                                    Toast.makeText(DetailProductFarmerActivity.this, responsePOST.getMessage(), Toast.LENGTH_SHORT).show();
                                    dialogDelete.cancel();
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(DetailProductFarmerActivity.this, responsePOST.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                                Toast.makeText(DetailProductFarmerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


    // Dialog lựa chọn chỉnh sửa hoặc xóa cái lịch sử sản phẩm
    private void showDialogOptionHistory(History history) {
        // Tạo và cài đặt layout cho dialog
        Dialog dialogOptions = new Dialog(this);
        dialogOptions.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOptions.setContentView(R.layout.dialog_bottom_option);
        dialogOptions.getWindow().setGravity(Gravity.BOTTOM);
        dialogOptions.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogOptions.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khởi tạo và ảnh xạ view trong dialog Option
        Button btn_Update_DialogOption      = dialogOptions.findViewById(R.id.btn_Update_DialogOption);
        Button btn_Delete_DialogOption      = dialogOptions.findViewById(R.id.btn_Delete_DialogOption);
        Button btn_Cancel_DialogOption      = dialogOptions.findViewById(R.id.btn_Cancel_DialogOption);

        /*
         * 1. Chọn vào option Update: Chuyển sang activity cập nhật
         * 2. Chọn vào option Delete: Hiện thị dialog yêu cầu xác nhận lần cuối
         * 3. Chọn vào option Cancel: Đóng dialog
         * */

        // Hiện dialog
        dialogOptions.show();

        // 1. Update Button: Chuyển sang activity update
        btn_Update_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Chuyển đối tượng lịch sự sang activity cập nhật
                Bundle bundleUpdate = new Bundle();
                bundleUpdate.putSerializable("history", history);

                Intent intentUpdate = new Intent(DetailProductFarmerActivity.this, UpdateHistoryActivity.class);
                intentUpdate.putExtras(bundleUpdate);

                startActivity(intentUpdate);

                dialogOptions.dismiss();
            }
        });

        // 2. Delete Button: Mở dialog xác nhận
        btn_Delete_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDelete(history);
                dialogOptions.dismiss();
            }
        });

        // 3. Cancel Button: Đóng dialog
        btn_Cancel_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOptions.dismiss();
            }
        });
    }

    // Dialog xác nhận có thật sự muốn xóa không
    private void showDialogDelete(History history) {
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
                historyPresenter.DeleteHistory(history, product.getIdProduct());
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


    // OVERRIDE METHOD: interface IListenerHistory
    @Override
    public void onClickHistoryItem(History history) {
        //showDialogOptionHistory(history);

        // Chuyển đối tượng history sang activity detail
        Bundle bundleHistory = new Bundle();
        bundleHistory.putSerializable("history", history);
        Intent intent = new Intent(DetailProductFarmerActivity.this, DetailHistoryActivity.class);
        intent.putExtras(bundleHistory);
        startActivity(intent);
    }


    // OVERRIDE METHOD: interface IHistory
    @Override
    public void successMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void getHistory(List<History> histories) {
        historyAdapter.setHistoryList(histories);
    }
}