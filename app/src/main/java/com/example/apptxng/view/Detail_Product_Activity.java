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

public class Detail_Product_Activity extends AppCompatActivity implements History_Adapter.IListenerHistory, IHistory {
    private ImageView img_Option_Detail_Product,img_Close_Detail_Product,img_Detail_Product, img_QR_Product;
    private TextView txt_Balance_Detail_Product,txt_Name_Detail_Product,txt_Des_Detail_Product,txt_Price_Detail_Product,txt_Quantity_Detail_Product,txt_QuantitySold_Detail_Product;
    private Product product;
    private History_Adapter historyAdapter;
    private History_Presenter historyPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        // Get Bundle: Nhận đối tượng product đã truyền qua
        product = (Product) getIntent().getExtras().getSerializable("product");
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
        img_QR_Product                          = findViewById(R.id.img_QR_Product);
        RecyclerView recycler_History_Detail_Product = findViewById(R.id.recycler_History_Detail_Product);
        historyPresenter                        = new History_Presenter(this,this);

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

        // 3. Close Button: Khi ấn thì sẽ đóng activity lại
        img_Close_Detail_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 4. Option Button: Mở dialog bottom có 3 chức năng: Thêm nhật ký, Chỉnh sửa sản phẩm, Xóa sản phẩm
        img_Option_Detail_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOption();
            }
        });

        // 5. Display Image: Hiển thị QR Code
        img_QR_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayQRCode();
            }
        });
    }

    private void displayQRCode() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.display_image);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Image view
        ImageView imgQR = dialog.findViewById(R.id.img_Display);

        // Load image
        Glide.with(Detail_Product_Activity.this).load(product.getQrProduct()).into(imgQR);

        dialog.show();

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

        // Ảnh  QR sản phẩm
        Glide.with(this).load(product.getQrProduct()).error(R.drawable.ic_photo).into(img_QR_Product);
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
                Intent intentInsertHistory = new Intent(Detail_Product_Activity.this, InsertHistoryActivity.class);
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
                bundleUpdate.putSerializable("product",product); // Đẩy product vào bundle
                Intent intentUpdate = new Intent(Detail_Product_Activity.this,UpdateProductActivity.class);
                intentUpdate.putExtras(bundleUpdate);
                startActivity(intentUpdate);
                dialogOptions.dismiss();
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
                                    Toast.makeText(Detail_Product_Activity.this, responsePOST.getMessage(), Toast.LENGTH_SHORT).show();
                                    dialogDelete.cancel();
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(Detail_Product_Activity.this, responsePOST.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                                Toast.makeText(Detail_Product_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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
        Intent intent = new Intent(Detail_Product_Activity.this, DetailHistoryActivity.class);
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