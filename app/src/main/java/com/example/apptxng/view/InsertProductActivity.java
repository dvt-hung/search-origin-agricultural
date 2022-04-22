package com.example.apptxng.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.presenter.IInsertProduct;
import com.example.apptxng.presenter.Insert_Product_Presenter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InsertProductActivity extends AppCompatActivity implements ChoiceType_Adapter.IListenerChoiceType, IInsertProduct {


    private ImageView img_Back_InsertProduct, img_InsertProduct;
    private EditText edt_Name_InsertProduct,edt_Price_InsertProduct,edt_Des_InsertProduct,edt_Quantity_InsertProduct;

    private TextView txt_ChoiceCategory_InsertProduct,txt_ResultCategory_InsertProduct, txt_ChoiceBalance_InsertProduct,txt_ResultBalance_InsertProduct
    ,txt_IngredientProduct, txt_UseProduct,  txt_GuideProduct, txt_ConditionProduct;

    @SuppressLint("StaticFieldLeak")
    public static TextView txt_Result_IngredientProduct,txt_Result_UseProduct,txt_Result_GuideProduct,txt_Result_ConditionProduct;
    private Button btn_InsertProduct;
    private ChoiceType_Adapter choiceTypeAdapter;
    private Dialog dialogChoiceCategory;
    private Insert_Product_Presenter insertProductPresenter;
    private List<Category> typeCategoryList = new ArrayList<>() ;
    private List<Balance> typeBalanceList = new ArrayList<>() ;
    private Product product;
    private ProgressDialog progressAddProduct;
    private String idProduct;
    private final String TITLE_INGREDIENT_I   = "TITLE_INGREDIENT_I";
    private final String TITLE_USE_I          = "TITLE_USE_I";
    private final String TITLE_GUIDE_I        = "TITLE_GUIDE_I";
    private final String TITLE_CONDITION_I    = "TITLE_CONDITION_I";

    private final ActivityResultLauncher<Intent> resultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null)
                {
                    Uri uri_ImageProduct = result.getData().getData(); // Gán Uri của ảnh đã chọn cho biến
                    img_InsertProduct.setImageURI(uri_ImageProduct);
                    product.setImageProduct(uri_ImageProduct.toString());
                }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_prodcut_farmer);

        // initView: Ánh xạ view
        initView();

    }
    // INIT VIEW
    private void initView() {
        img_Back_InsertProduct              = findViewById(R.id.img_Back_InsertProduct);
        img_InsertProduct                   = findViewById(R.id.img_InsertProduct);
        edt_Name_InsertProduct              = findViewById(R.id.edt_Name_InsertProduct);
        edt_Price_InsertProduct             = findViewById(R.id.edt_Price_InsertProduct);
        edt_Des_InsertProduct               = findViewById(R.id.edt_Des_InsertProduct);
        edt_Quantity_InsertProduct          = findViewById(R.id.edt_Quantity_InsertProduct);
        txt_ChoiceCategory_InsertProduct    = findViewById(R.id.txt_ChoiceCategory_InsertProduct);
        txt_ResultCategory_InsertProduct    = findViewById(R.id.txt_ResultCategory_InsertProduct);
        txt_ChoiceBalance_InsertProduct     = findViewById(R.id.txt_ChoiceBalance_InsertProduct);
        txt_ResultBalance_InsertProduct     = findViewById(R.id.txt_ResultBalance_InsertProduct);
        btn_InsertProduct                   = findViewById(R.id.btn_InsertProduct);
        txt_IngredientProduct               = findViewById(R.id.txt_IngredientProduct);
        txt_Result_IngredientProduct        = findViewById(R.id.txt_Result_IngredientProduct);
        txt_UseProduct                      = findViewById(R.id.txt_UseProduct);
        txt_Result_UseProduct               = findViewById(R.id.txt_Result_UseProduct);
        txt_GuideProduct                    = findViewById(R.id.txt_GuideProduct);
        txt_Result_GuideProduct             = findViewById(R.id.txt_Result_GuideProduct);
        txt_ConditionProduct                = findViewById(R.id.txt_ConditionProduct);
        txt_Result_ConditionProduct         = findViewById(R.id.txt_Result_ConditionProduct);

        // Adapter + Presenter
        product                             = new Product();
        choiceTypeAdapter                   = new ChoiceType_Adapter(this);
        insertProductPresenter              = new Insert_Product_Presenter(this, this);
        progressAddProduct                  = new ProgressDialog(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 0. Load danh sách danh mục + đơn vị tính
        loadListChoiceType();

        /*
        * 1. Chọn ảnh cho sản phẩm
        * - Xin quyển để truy cập thư viện ảnh
        * - Chọn và hiển thị ảnh lên
        * */
        img_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOpenGallery();
            }
        });

        /*
        * 2. Nút quay về
        * - Khi ấn vào thì sẽ đóng activity đi
        * */
        img_Back_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*
        * 3. Chọn danh mục cho sản phẩm
        * - Hiển thị bottom dialog cho người dùng chọn
        * - Danh sách đơn vị tính sẽ tại từ csdl
        * - Danh sách danh mục sẽ hiển thị trên recycler view
        * - Sau khi chọn xong sẽ thay đổi text view kết quả đã chọn
        * */
        txt_ChoiceCategory_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceCategory();
            }
        });


        /*
        * 4. Chọn đơn vị tính cho sản phẩm
        * - Hiển thị bottom dialog cho người dùng chọn
        * - Danh sách đơn vị tính sẽ tại từ csdl
        * - Hiển thị danh sách tại recycler view
        * - Sau khi đã chọn xong sẽ thay đổi text view kết quả đã chọn
        * */
        txt_ChoiceBalance_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceBalance();
            }
        });


        /*
        * 5. Nút thêm sản phẩm
        * - Gán các dữ liệu như tên, giá, mô tả, số lượng cho biến product
        * - Sau đó chuyển qua cho Present sử lý
        * */

        btn_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameProduct, priceProduct, desProduct, quantityProduct;
                nameProduct = edt_Name_InsertProduct.getText().toString().trim();
                priceProduct = edt_Price_InsertProduct.getText().toString().trim();
                desProduct = edt_Des_InsertProduct.getText().toString().trim();
                quantityProduct = edt_Quantity_InsertProduct.getText().toString().trim();
                String ingredient   = txt_Result_IngredientProduct.getText().toString().trim();
                String use          = txt_Result_UseProduct.getText().toString().trim();
                String guide        = txt_Result_GuideProduct.getText().toString().trim();
                String condition    = txt_ConditionProduct.getText().toString().trim();



                // Set nội dung cho progress dialog
                progressAddProduct.setMessage("Vui lòng chờ...");
                progressAddProduct.show();
                if (nameProduct.isEmpty() || priceProduct.isEmpty() || desProduct.isEmpty() || quantityProduct.isEmpty())
                {
                    Toast.makeText(InsertProductActivity.this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
                    progressAddProduct.cancel();
                }
                else
                {
                    product.setNameProduct(nameProduct); // Set tên cho product
                    product.setPriceProduct(Integer.parseInt(priceProduct)); // Set giá cho product
                    product.setDescriptionProduct(desProduct); // Set des cho product
                    product.setQuantityProduct(Integer.parseInt(quantityProduct)); // Set số lượng cho product
                    product.setIdUser(Common.currentUser.getIdUser());
                    product.setIngredientProduct(ingredient);
                    product.setUseProduct(use);
                    product.setGuideProduct(guide);
                    product.setConditionProduct(condition);

                    idProduct = "Product" +Common.calendar.getTime().getTime();
                    product.setIdProduct(idProduct);
                    // Set ngày cho product
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = Calendar.getInstance().getTime();
                    String dateProduct = formatter.format(date);
                    product.setDateProduct(dateProduct); // Set ngày cho Product
                    insertProductPresenter.insertProduct(product);
                }

            }
        });


        /*
        6. txt_IngredientProduct: Chuyển sang activiy nhập thành phần
         */
        txt_IngredientProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = txt_Result_IngredientProduct.getText().toString().trim();
                Intent intentIngredient = new Intent(InsertProductActivity.this, InputValueProductActivity.class);
                intentIngredient.putExtra("code", TITLE_INGREDIENT_I);
                intentIngredient.putExtra("value", value);
                startActivity(intentIngredient);
            }
        });

         /*
        7. txt_IngredientProduct: Chuyển sang activiy nhập thành phần
         */
        txt_UseProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = txt_Result_UseProduct.getText().toString().trim();
                Intent intentUse = new Intent(InsertProductActivity.this, InputValueProductActivity.class);
                intentUse.putExtra("code", TITLE_USE_I);
                intentUse.putExtra("value", value);
                startActivity(intentUse);
            }
        });

         /*
        8. txt_IngredientProduct: Chuyển sang activiy nhập thành phần
         */
        txt_GuideProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = txt_Result_GuideProduct.getText().toString().trim();
                Intent intentGuide = new Intent(InsertProductActivity.this, InputValueProductActivity.class);
                intentGuide.putExtra("code", TITLE_GUIDE_I);
                intentGuide.putExtra("value", value);
                startActivity(intentGuide);
            }
        });

         /*
        9. txt_IngredientProduct: Chuyển sang activiy nhập thành phần
         */
        txt_ConditionProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = txt_Result_ConditionProduct.getText().toString().trim();
                Intent intentCondition = new Intent(InsertProductActivity.this, InputValueProductActivity.class);
                intentCondition.putExtra("code", TITLE_CONDITION_I);
                intentCondition.putExtra("value", value);
                startActivity(intentCondition);
            }
        });
    }


    // LOAD LIST CHOICE TYPE: Tải dữ liệu danh sách các đơn vị tính và danh mục
    private void loadListChoiceType() {
        insertProductPresenter.getCategory();
        insertProductPresenter.getBalance();
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


    // OVERRIDE METHOD CHOICE TYPE ADAPTER
    @Override
    public void onClickChoiceType(Object obj) {
        if (obj.getClass() == Category.class)
        {
            product.setCategory((Category) obj);
            txt_ResultCategory_InsertProduct.setVisibility(View.VISIBLE);
            txt_ResultCategory_InsertProduct.setText(product.getCategory().getNameCategory());

        }
        else if (obj.getClass() == Balance.class)
        {
            product.setBalance((Balance) obj);
            txt_ResultBalance_InsertProduct.setVisibility(View.VISIBLE);
            txt_ResultBalance_InsertProduct.setText(product.getBalance().getNameBalance());
        }

        dialogChoiceCategory.cancel();
    }

    // OVERRIDE METHOD INTERFACE: INSERT PRODUCT
    @Override
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getCategory(List<Category> List) {
        typeCategoryList = List; // gán danh sách category đã tải về
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
    public void addProductSuccess(String message) {
        // Chuyển String idProduct sang activity QR Code
        Intent intentQR = new Intent(InsertProductActivity.this,CreateQRCodeActivity.class);
        intentQR.putExtra("idProduct",idProduct);
        startActivity(intentQR);

        // Sau khi chuyển sang activity QR Code thì đóng activity lại
        finish();
        progressAddProduct.cancel();
    }

    @Override
    public void addProductFailed(String message) {
        progressAddProduct.cancel();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}