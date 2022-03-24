package com.example.apptxng.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.presenter.IUpdateProduct;
import com.example.apptxng.presenter.Update_Product_Presenter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateProductFarmerActivity extends AppCompatActivity implements ChoiceType_Adapter.IListenerChoiceType, IUpdateProduct {

    private ImageView img_UpdateProduct,img_Back_UpdateProduct;
    private EditText edt_Name_UpdateProduct, edt_Price_UpdateProduct,edt_Des_UpdateProduct,edt_Quantity_UpdateProduct;
    private TextView txt_ChoiceCategory_UpdateProduct, txt_ResultCategory_UpdateProduct, txt_ChoiceBalance_UpdateProduct, txt_ResultBalance_UpdateProduct;
    private Button btn_UpdateProduct;
    private Product product;
    private Dialog dialogChoiceCategory;
    private ChoiceType_Adapter choiceTypeAdapter;
    private List<Category> typeCategoryList = new ArrayList<>() ;
    private List<Balance> typeBalanceList = new ArrayList<>() ;
    private Uri uri_ImageProductTemp;
    private Update_Product_Presenter updateProductPresenter;
    private ProgressDialog progressUpdate;

    private final ActivityResultLauncher<Intent> resultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData() != null)
            {
                uri_ImageProductTemp = result.getData().getData(); // Gán Uri của ảnh đã chọn cho biến
                img_UpdateProduct.setImageURI(uri_ImageProductTemp);
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product_farmer);

        // Khởi tạo, ánh xạ view
        initView();

    }


    // Ánh xạ view
    private void initView() {
        img_UpdateProduct                   = findViewById(R.id.img_UpdateProduct);
        img_Back_UpdateProduct              = findViewById(R.id.img_Back_UpdateProduct);
        edt_Name_UpdateProduct              = findViewById(R.id.edt_Name_UpdateProduct);
        edt_Price_UpdateProduct             = findViewById(R.id.edt_Price_UpdateProduct);
        edt_Des_UpdateProduct               = findViewById(R.id.edt_Des_UpdateProduct);
        edt_Quantity_UpdateProduct          = findViewById(R.id.edt_Quantity_UpdateProduct);
        txt_ChoiceCategory_UpdateProduct    = findViewById(R.id.txt_ChoiceCategory_UpdateProduct);
        txt_ResultCategory_UpdateProduct    = findViewById(R.id.txt_ResultCategory_UpdateProduct);
        txt_ChoiceBalance_UpdateProduct     = findViewById(R.id.txt_ChoiceBalance_UpdateProduct);
        txt_ResultBalance_UpdateProduct     = findViewById(R.id.txt_ResultBalance_UpdateProduct);
        btn_UpdateProduct                   = findViewById(R.id.btn_UpdateProduct);
        choiceTypeAdapter                   = new ChoiceType_Adapter(this);
        updateProductPresenter              = new Update_Product_Presenter(this,this);
        progressUpdate                      = new ProgressDialog(this);
        progressUpdate.setMessage("Vui lòng chờ...");
        // Nhận Product từ Detail chuyển sang
        Bundle bundleUpdate  = getIntent().getBundleExtra("bundle_product");
        product = (Product) bundleUpdate.getSerializable("product_detail");

        // Hiển thị dữ liệu ban đầu
        displayValue();
    }

    // Hiển thị dữ liệu ban đầu của Product
    private void displayValue() {
        edt_Name_UpdateProduct.setText(product.getNameProduct());

        edt_Price_UpdateProduct.setText(String.valueOf(product.getPriceProduct()));

        edt_Des_UpdateProduct.setText(product.getDescriptionProduct());

        edt_Quantity_UpdateProduct.setText(String.valueOf(product.getQuantityProduct()));

        Glide.with(this).load(product.getImageProduct()).into(img_UpdateProduct);

        txt_ResultCategory_UpdateProduct.setText(product.getCategory().getNameCategory());

        txt_ResultBalance_UpdateProduct.setText(product.getBalance().getNameBalance());
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadListChoiceType();

        // Khi ấn sẽ tắt activity đi
        img_Back_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Chọn danh mục sản phẩm mới
        txt_ChoiceCategory_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceCategory();
            }
        });

        // 3. Chọn đơn vị tính mới cho sản phẩm
        txt_ChoiceBalance_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceBalance();
            }
        });

        // 4. Chọn ảnh mới cho sản phẩm
        img_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOpenGallery();
            }
        });

        // 5. Confirm button: Tiến hành thêm sản phẩm
        btn_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name         = edt_Name_UpdateProduct.getText().toString().trim();
                String price        = edt_Price_UpdateProduct.getText().toString().trim();
                String des          = edt_Des_UpdateProduct.getText().toString().trim();
                String quantity     = edt_Quantity_UpdateProduct.getText().toString().trim();

                // Gán giá trị đã thay đổi
                product.setNameProduct(name);
                product.setPriceProduct(Integer.parseInt(price));
                product.setDescriptionProduct(des);
                product.setQuantityProduct(Integer.parseInt(quantity));

                updateProductPresenter.updateProduct(product,uri_ImageProductTemp);
                progressUpdate.show();
            }
        });
    }
    // LOAD LIST CHOICE TYPE: Tải dữ liệu danh sách các đơn vị tính và danh mục
    private void loadListChoiceType() {
        updateProductPresenter.getCategory();
        updateProductPresenter.getBalance();
    }
    // Dialog: Chọn danh mục cho sản phẩm
    private void showDialogChoiceCategory() {
        dialogChoiceCategory = new Dialog(this);
        dialogChoiceCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChoiceCategory.setContentView(R.layout.dialog_bottom_choice_type);
        Window window = dialogChoiceCategory.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        // Khởi tạo ảnh xạ view cho dialog + Khởi tạo adapter
        RecyclerView recycler_Choice_Type   = dialogChoiceCategory.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogChoiceCategory.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_category_product);
        // gán adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // tạo layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        choiceTypeAdapter.setList(typeCategoryList);
        dialogChoiceCategory.show();
    }

    // Dialog: Chọn đơn vị tính cho sản phẩm
    private void showDialogChoiceBalance() {
        dialogChoiceCategory = new Dialog(this);
        dialogChoiceCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChoiceCategory.setContentView(R.layout.dialog_bottom_choice_type);
        Window window = dialogChoiceCategory.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        // Khởi tạo ảnh xạ view cho dialog + Khởi tạo adapter
        RecyclerView recycler_Choice_Type = dialogChoiceCategory.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogChoiceCategory.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_balance_product);

        // gán adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // tạo layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        dialogChoiceCategory.show();

        choiceTypeAdapter.setList(typeBalanceList);
    }

    // Check permission: Kiểm tra quyền truy cập vào thư viện ảnh
    private void checkPermissionOpenGallery() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        openGallery();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    // Open gallery: nếu đã cho phép thì mở thư viện ảnh
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        resultLauncherGallery.launch(intent);
    }


    @Override
    public void onClickChoiceType(Object obj) {
        if (obj.getClass() == Category.class)
        {
            product.setCategory((Category) obj);
            txt_ResultCategory_UpdateProduct.setText(product.getCategory().getNameCategory());
        }
        else if (obj.getClass() == Balance.class)
        {
            product.setBalance((Balance) obj);
            txt_ResultBalance_UpdateProduct.setText(product.getBalance().getNameBalance());

        }
        dialogChoiceCategory.cancel();
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressUpdate.cancel();
    }

    @Override
    public void getCategory(List<Category> List) {
        typeCategoryList = List;
    }

    @Override
    public void getBalance(List<Balance> list) {
        typeBalanceList = list; // gán danh sách đơn vị tính đã tải về
    }

    @Override
    public void emptyValue() {
        Toast.makeText(this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(String message) {
        finish();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressUpdate.cancel();

    }

    @Override
    public void failed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressUpdate.cancel();
    }
}