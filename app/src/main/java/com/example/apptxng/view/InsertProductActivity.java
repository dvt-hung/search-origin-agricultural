package com.example.apptxng.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Factory;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.User;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertProductActivity extends AppCompatActivity implements ChoiceType_Adapter.IListenerChoiceType, IInsertProduct {


    private ImageView img_Back_InsertProduct, img_InsertProduct;
    private EditText edt_Name_InsertProduct,edt_Price_InsertProduct,edt_Des_InsertProduct,edt_Quantity_InsertProduct;

    private TextView txt_ChoiceCategory_InsertProduct,txt_ResultCategory_InsertProduct, txt_ChoiceBalance_InsertProduct,txt_ResultBalance_InsertProduct
    ,txt_IngredientProduct, txt_UseProduct,  txt_GuideProduct, txt_ConditionProduct, txt_ChoiceEmployee_InsertProduct, txt_ResultEmployee_InsertProduct;

    @SuppressLint("StaticFieldLeak")
    public static TextView txt_Result_IngredientProduct,txt_Result_UseProduct,txt_Result_GuideProduct,txt_Result_ConditionProduct;
    private Button btn_InsertProduct;
    private ChoiceType_Adapter choiceTypeAdapter;
    private Dialog dialogInsertProduct;
    private Insert_Product_Presenter insertProductPresenter;
    private List<Category> typeCategoryList = new ArrayList<>() ;
    private List<Balance> typeBalanceList = new ArrayList<>() ;
    private List<User> listEmployee = new ArrayList<>() ;
    private Product product;
    private String idProduct,idHistory;
    private Factory factoryTemp;
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

        // 0. Load danh sách danh mục + đơn vị tính
        loadListChoiceType();
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
        txt_ChoiceEmployee_InsertProduct    = findViewById(R.id.txt_ChoiceEmployee_InsertProduct);
        txt_ResultEmployee_InsertProduct    = findViewById(R.id.txt_ResultEmployee_InsertProduct);

        // Adapter + Presenter
        product                             = new Product();
        choiceTypeAdapter                   = new ChoiceType_Adapter(this);
        insertProductPresenter              = new Insert_Product_Presenter(this, this);

    }

    @Override
    protected void onResume() {
        super.onResume();

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
                        insertProduct();
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

        /*
        * 10. txt_ChoiceEmployee_InsertProduct: Mở dialog lựa chọn nhân viên phụ trách
        *
        * */
        txt_ChoiceEmployee_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceEmployee();
            }
        });
    }

    // FUN THÊM SẢN PHẨM
    private void insertProduct() {
        String nameProduct, priceProduct, desProduct, quantityProduct,ingredient,use,guide,condition, desHistory;
        desHistory = "Khởi tạo sản phẩm";

        // Nhận các giá trị được người dùng nhập vào
        nameProduct         = edt_Name_InsertProduct.getText().toString().trim();
        priceProduct        = edt_Price_InsertProduct.getText().toString().trim();
        desProduct          = edt_Des_InsertProduct.getText().toString().trim();
        quantityProduct     = edt_Quantity_InsertProduct.getText().toString().trim();
        ingredient          = txt_Result_IngredientProduct.getText().toString().trim();
        use                 = txt_Result_UseProduct.getText().toString().trim();
        guide               = txt_Result_GuideProduct.getText().toString().trim();
        condition           = txt_Result_ConditionProduct.getText().toString().trim();


        // Kiểm tra dữ liễu quan trọng có rỗng hay không
        if (nameProduct.isEmpty() || priceProduct.isEmpty() || desProduct.isEmpty() || quantityProduct.isEmpty())
        {
            Toast.makeText(InsertProductActivity.this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
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

            // Set ngày cho product
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = Calendar.getInstance().getTime();


            String dateProduct = formatter.format(date);
            product.setDateProduct(dateProduct); // Set ngày cho Product

            idProduct = "Product" + date.getTime(); // ID Product
            product.setIdProduct(idProduct);

            idHistory = "History" + date.getTime(); // ID Product

            Log.e("abc", "insertProduct: " + nameProduct + " - " + priceProduct + " - " + idProduct + " - " + idHistory );
            // Gọi đến presenter
            insertProductPresenter.insertProduct(product,idHistory,desHistory,factoryTemp.getIdFactory());


        }

    }


    // LOAD LIST CHOICE TYPE: Tải dữ liệu danh sách các đơn vị tính và danh mục
    private void loadListChoiceType() {
        insertProductPresenter.getCategory();
        insertProductPresenter.getBalance();
        insertProductPresenter.getInfoFactory();
        insertProductPresenter.getListEmployeeAccount(Common.currentUser.getIdUser());
    }

    // Dialog: Chọn danh mục cho sản phẩm
    private void showDialogChoiceCategory() {
        dialogInsertProduct = new Dialog(this);
        dialogInsertProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInsertProduct.setContentView(R.layout.dialog_bottom_choice_type);
        Window window = dialogInsertProduct.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        // Khởi tạo ảnh xạ view cho dialog + Khởi tạo adapter
        RecyclerView recycler_Choice_Type   = dialogInsertProduct.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogInsertProduct.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_category_product);
        // gán adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // tạo layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        choiceTypeAdapter.setList(typeCategoryList);
        dialogInsertProduct.show();
    }

    // Dialog: Chọn đơn vị tính cho sản phẩm
    private void showDialogChoiceBalance() {
        dialogInsertProduct = new Dialog(this);
        dialogInsertProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInsertProduct.setContentView(R.layout.dialog_bottom_choice_type);
        Window window = dialogInsertProduct.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        // Khởi tạo ảnh xạ view cho dialog + Khởi tạo adapter
        RecyclerView recycler_Choice_Type   = dialogInsertProduct.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogInsertProduct.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_balance_product);

        // gán adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // tạo layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        dialogInsertProduct.show();

        choiceTypeAdapter.setList(typeBalanceList);
    }

    // Dialog: Chọn nhân viên phụ trách
    private void showDialogChoiceEmployee() {
        dialogInsertProduct = new Dialog(this);
        dialogInsertProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInsertProduct.setContentView(R.layout.dialog_bottom_choice_type);
        Window window = dialogInsertProduct.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        // Khởi tạo ảnh xạ view cho dialog + Khởi tạo adapter
        RecyclerView recycler_Choice_Type   = dialogInsertProduct.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogInsertProduct.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_balance_product);

        // gán adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // tạo layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        dialogInsertProduct.show();

        choiceTypeAdapter.setList(listEmployee);
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
        else if (obj.getClass() == User.class)
        {
            User employee = (User) obj;
            product.setIdEmployee(employee.getIdUser());
            txt_ResultEmployee_InsertProduct.setVisibility(View.VISIBLE);
            txt_ResultEmployee_InsertProduct.setText(employee.getName());
        }

        dialogInsertProduct.dismiss();
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
    public void infoFactory(Factory factory) {
        factoryTemp = factory;
    }

    @Override
    public void addProductSuccess(String message) {
        // Chuyển String idProduct sang activity QR Code
        Intent intentQR = new Intent(InsertProductActivity.this,CreateQRCodeActivity.class);
        intentQR.putExtra("idProduct",idProduct);
        startActivity(intentQR);

        // Sau khi chuyển sang activity QR Code thì đóng activity lại
        finish();
    }

    @Override
    public void addProductFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void listEmployee(List<User> list) {
        listEmployee = list;
    }

    public synchronized void getListEmployeeAccount(String idOwner)
    {
        ProgressDialog dialog = Common.createProgress(InsertProductActivity.this);
        dialog.show();
        Common.api.getListEmployee(idOwner)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                        listEmployee = response.body();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                        Toast.makeText(InsertProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        typeBalanceList = null;
        typeCategoryList = null;
        idProduct = null;
        idHistory = null;
    }
}