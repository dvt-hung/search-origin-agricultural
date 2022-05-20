package com.example.apptxng.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.ResponsePOST;
import com.example.apptxng.model.User;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_Product_Activity extends AppCompatActivity  {
    private ImageView img_Option_Detail_Product,img_Close_Detail_Product,img_Detail_Product, img_QR_Product;
    private TextView txt_Balance_Detail_Product,txt_Name_Detail_Product,txt_Des_Detail_Product,txt_Price_Detail_Product, txt_Supply_Chain
            ,txt_Ingredient_Detail_Product,txt_Use_Detail_Product, txt_Info_Employee,txt_Guide_Detail_Product, txt_Condition_Detail_Product,txt_Seen_History;
    private Product product;
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
        txt_Ingredient_Detail_Product           = findViewById(R.id.txt_Ingredient_Detail_Product);
        txt_Use_Detail_Product                  = findViewById(R.id.txt_Use_Detail_Product);
        txt_Balance_Detail_Product              = findViewById(R.id.txt_Balance_Detail_Product);
        img_Close_Detail_Product                = findViewById(R.id.img_Close_Detail_Product);
        img_QR_Product                          = findViewById(R.id.img_QR_Product);
        txt_Guide_Detail_Product                = findViewById(R.id.txt_Guide_Detail_Product);
        txt_Condition_Detail_Product            = findViewById(R.id.txt_Condition_Detail_Product);
        txt_Seen_History                        = findViewById(R.id.txt_Seen_History);
        txt_Info_Employee                       = findViewById(R.id.txt_Info_Employee);
        txt_Supply_Chain                        = findViewById(R.id.txt_Supply_Chain);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 1. Hiển thị giá trị của sản phẩm lên text view
        displayValueProduct();

        // 2. Xem danh sách lịch sử
        txt_Seen_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundleProduct = new Bundle();
                bundleProduct.putSerializable("product",product);
                Intent intentHistory = new Intent(Detail_Product_Activity.this, HistoriesActivity.class);
                intentHistory.putExtras(bundleProduct);
                startActivity(intentHistory);
            }
        });

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


        // 6 Info Employee: Hiển dialog thông tin nhân viên đang quản lí sản phẩm
        txt_Info_Employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInfoEmployee();
            }
        });

        // Supply Chain: Chuyển sang activity hiển thị chuỗi cung ứng theo sản phẩm
        txt_Supply_Chain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSupplyChain = new Intent(Detail_Product_Activity.this, SupplyChainActivity.class);
                intentSupplyChain.putExtra("idProduct", product.getIdProduct()); // Truyền idProduct sang để lấy thông tin
                startActivity(intentSupplyChain);
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


    // Gán giá trị của product, hiển thị lên text view
    @SuppressLint("SetTextI18n")
    private void displayValueProduct() {
        // Kiểm tra quyền chỉnh sửa product
        if (!product.getIdUser().equals(Common.currentUser.getIdUser()) )
        {
            img_Option_Detail_Product.setVisibility(View.GONE);
        }

        // Nếu chưa chỉ định nhân viên quản lý sản phẩm thì sẽ ẩn đi
        if (product.getIdEmployee() == null || product.getIdEmployee().isEmpty() || product.getIdEmployee().equals(" "))
        {
            txt_Info_Employee.setVisibility(View.GONE);
        }

        // Tên sản phẩm
        txt_Name_Detail_Product.setText(product.getNameProduct());

        // Giá sản phẩm
        txt_Price_Detail_Product.setText(Common.numberFormat.format(product.getPriceProduct()) + " VNĐ");

        // Thành phần sản phẩm
        checkValue(txt_Ingredient_Detail_Product,product.getIngredientProduct());

        // Công dụng bán của sản phẩm
        checkValue(txt_Use_Detail_Product,product.getUseProduct());

        // Mô tả của sản phẩm
        txt_Des_Detail_Product.setText(product.getDescriptionProduct());

        // Đơn vị tính
        txt_Balance_Detail_Product.setText(product.getBalance().getNameBalance());

        // Cách sử dụng
        checkValue(txt_Guide_Detail_Product, product.getGuideProduct());

        // Bảo quản
        checkValue(txt_Condition_Detail_Product,product.getConditionProduct());

        // Ảnh sản phẩm
        Glide.with(this).load(product.getImageProduct()).error(R.drawable.logo).into(img_Detail_Product);

        // Ảnh  QR sản phẩm
        Glide.with(this).load(product.getQrProduct()).error(R.drawable.ic_photo).into(img_QR_Product);
    }
    // Kiểm tra dữ liệu có null không
    private void checkValue(TextView view, String val)
    {
        if (val == null || val.equals(" ") || val.isEmpty())
        {
            view.setText(R.string.title_error_empty_user);
        }
        else
        {
            view.setText(val);
        }
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


        // 1. Insert Ẩn
        btn_InsertHistory_OptionProduct.setVisibility(View.GONE);

        // Nếu người dùng đang đăng sử dụng là loại Nhân viên thì ẩn đi chức năng xóa
        if (product.getIdEmployee().equals(Common.currentUser.getIdUser()))
        {
            btn_Delete_OptionProduct.setVisibility(View.GONE);
        }

        // 2. Update Button
        btn_Update_OptionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundleUpdate = new Bundle();
                bundleUpdate.putSerializable("product",product); // Đẩy product vào bundle
                Intent intentUpdate = new Intent(Detail_Product_Activity.this,UpdateProductActivity.class);
                intentUpdate.putExtras(bundleUpdate);
                startActivity(intentUpdate);
                finish();
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

    // Lấy thông tin nhân viên quản lí sản phẩm
    private synchronized void getInfoEmployee() {
        // Tạo progress dialog
        ProgressDialog progressDialog = Common.createProgress(this);
        progressDialog.show();
        // Gọi đến API - lấy info user
        Common.api.getInfoUser(product.getIdEmployee())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                        User employee = response.body();
                        if (employee != null)
                        {
                            showInfoEmployee(employee);
                        }
                        else
                        {
                            Toast.makeText(Detail_Product_Activity.this, "Chưa chỉ định nhân viên quản lí", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Toast.makeText(Detail_Product_Activity.this, "Đã có lỗi. Vui lòng thử lại! " , Toast.LENGTH_SHORT).show();
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

}