package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.ResponsePOST;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductFarmerActivity extends AppCompatActivity {

    private ImageView img_Option_Detail_Product,img_Close_Detail_Product,img_Detail_Product;
    private TextView txt_Balance_Detail_Product,txt_Name_Detail_Product,txt_Des_Detail_Product,txt_Price_Detail_Product,txt_Quantity_Detail_Product,txt_QuantitySold_Detail_Product;
    private Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product_farmer);


        // Init view: Ánh xạ view
        initView();
    }

    // Ánh xạ view
    private void initView() {
        img_Option_Detail_Product       = findViewById(R.id.img_Option_Detail_Product);
        img_Detail_Product              = findViewById(R.id.img_Detail_Product);
        txt_Name_Detail_Product         = findViewById(R.id.txt_Name_Detail_Product);
        txt_Des_Detail_Product          = findViewById(R.id.txt_Des_Detail_Product);
        txt_Price_Detail_Product        = findViewById(R.id.txt_Price_Detail_Product);
        txt_Quantity_Detail_Product     = findViewById(R.id.txt_Quantity_Detail_Product);
        txt_QuantitySold_Detail_Product = findViewById(R.id.txt_QuantitySold_Detail_Product);
        txt_Balance_Detail_Product      = findViewById(R.id.txt_Balance_Detail_Product);
        img_Close_Detail_Product        = findViewById(R.id.img_Close_Detail_Product);

        // Get Bundle: Nhận đối tượng product đã truyền qua
        Bundle bundleDetailsProduct = getIntent().getBundleExtra("b_product");
        product = (Product) bundleDetailsProduct.getSerializable("product");
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

    @Override
    protected void onResume() {
        super.onResume();

        // 1. Hiển thị giá trị của sản phẩm lên text view
        displayValueProduct();

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
    // Show dialog option
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
                startActivity(new Intent(DetailProductFarmerActivity.this,InsertHistoryProductActivity.class));
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
                dialogOptions.cancel();
                finish();
            }
        });

        // 3. Delete Button: Mở dialog xác nhận xóa sản phẩm
        btn_Delete_OptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDeleteProduct();
                dialogOptions.cancel();

            }
        });
    }

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

}