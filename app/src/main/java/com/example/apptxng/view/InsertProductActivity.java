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
                    Uri uri_ImageProduct = result.getData().getData(); // G??n Uri c???a ???nh ???? ch???n cho bi???n
                    img_InsertProduct.setImageURI(uri_ImageProduct);
                    product.setImageProduct(uri_ImageProduct.toString());
                }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_prodcut_farmer);

        // initView: ??nh x??? view
        initView();

        // 0. Load danh s??ch danh m???c + ????n v??? t??nh
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
        * 1. Ch???n ???nh cho s???n ph???m
        * - Xin quy???n ????? truy c???p th?? vi???n ???nh
        * - Ch???n v?? hi???n th??? ???nh l??n
        * */
        img_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOpenGallery();
            }
        });

        /*
        * 2. N??t quay v???
        * - Khi ???n v??o th?? s??? ????ng activity ??i
        * */
        img_Back_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*
        * 3. Ch???n danh m???c cho s???n ph???m
        * - Hi???n th??? bottom dialog cho ng?????i d??ng ch???n
        * - Danh s??ch ????n v??? t??nh s??? t???i t??? csdl
        * - Danh s??ch danh m???c s??? hi???n th??? tr??n recycler view
        * - Sau khi ch???n xong s??? thay ?????i text view k???t qu??? ???? ch???n
        * */
        txt_ChoiceCategory_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceCategory();
            }
        });


        /*
        * 4. Ch???n ????n v??? t??nh cho s???n ph???m
        * - Hi???n th??? bottom dialog cho ng?????i d??ng ch???n
        * - Danh s??ch ????n v??? t??nh s??? t???i t??? csdl
        * - Hi???n th??? danh s??ch t???i recycler view
        * - Sau khi ???? ch???n xong s??? thay ?????i text view k???t qu??? ???? ch???n
        * */
        txt_ChoiceBalance_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceBalance();
            }
        });


        /*
        * 5. N??t th??m s???n ph???m
        * - G??n c??c d??? li???u nh?? t??n, gi??, m?? t???, s??? l?????ng cho bi???n product
        * - Sau ???? chuy???n qua cho Present s??? l??
        * */

        btn_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        insertProduct();
            }
        });


        /*
        6. txt_IngredientProduct: Chuy???n sang activiy nh???p th??nh ph???n
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
        7. txt_IngredientProduct: Chuy???n sang activiy nh???p th??nh ph???n
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
        8. txt_IngredientProduct: Chuy???n sang activiy nh???p th??nh ph???n
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
        9. txt_IngredientProduct: Chuy???n sang activiy nh???p th??nh ph???n
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
        * 10. txt_ChoiceEmployee_InsertProduct: M??? dialog l???a ch???n nh??n vi??n ph??? tr??ch
        *
        * */
        txt_ChoiceEmployee_InsertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceEmployee();
            }
        });
    }

    // FUN TH??M S???N PH???M
    private void insertProduct() {
        String nameProduct, priceProduct, desProduct, quantityProduct,ingredient,use,guide,condition, desHistory;
        desHistory = "Kh???i t???o s???n ph???m";

        // Nh???n c??c gi?? tr??? ???????c ng?????i d??ng nh???p v??o
        nameProduct         = edt_Name_InsertProduct.getText().toString().trim();
        priceProduct        = edt_Price_InsertProduct.getText().toString().trim();
        desProduct          = edt_Des_InsertProduct.getText().toString().trim();
        quantityProduct     = edt_Quantity_InsertProduct.getText().toString().trim();
        ingredient          = txt_Result_IngredientProduct.getText().toString().trim();
        use                 = txt_Result_UseProduct.getText().toString().trim();
        guide               = txt_Result_GuideProduct.getText().toString().trim();
        condition           = txt_Result_ConditionProduct.getText().toString().trim();


        // Ki???m tra d??? li???u quan tr???ng c?? r???ng hay kh??ng
        if (nameProduct.isEmpty() || priceProduct.isEmpty() || desProduct.isEmpty() || quantityProduct.isEmpty())
        {
            Toast.makeText(InsertProductActivity.this, R.string.title_error_empty, Toast.LENGTH_SHORT).show();
        }
        else
        {
            product.setNameProduct(nameProduct); // Set t??n cho product
            product.setPriceProduct(Integer.parseInt(priceProduct)); // Set gi?? cho product
            product.setDescriptionProduct(desProduct); // Set des cho product
            product.setQuantityProduct(Integer.parseInt(quantityProduct)); // Set s??? l?????ng cho product
            product.setIdUser(Common.currentUser.getIdUser());
            product.setIngredientProduct(ingredient);
            product.setUseProduct(use);
            product.setGuideProduct(guide);
            product.setConditionProduct(condition);

            // Set ng??y cho product
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = Calendar.getInstance().getTime();


            String dateProduct = formatter.format(date);
            product.setDateProduct(dateProduct); // Set ng??y cho Product

            idProduct = "Product" + date.getTime(); // ID Product
            product.setIdProduct(idProduct);

            idHistory = "History" + date.getTime(); // ID Product

            Log.e("abc", "insertProduct: " + nameProduct + " - " + priceProduct + " - " + idProduct + " - " + idHistory );
            // G???i ?????n presenter
            insertProductPresenter.insertProduct(product,idHistory,desHistory,factoryTemp.getIdFactory());


        }

    }


    // LOAD LIST CHOICE TYPE: T???i d??? li???u danh s??ch c??c ????n v??? t??nh v?? danh m???c
    private void loadListChoiceType() {
        insertProductPresenter.getCategory();
        insertProductPresenter.getBalance();
        insertProductPresenter.getInfoFactory();
        insertProductPresenter.getListEmployeeAccount(Common.currentUser.getIdUser());
    }

    // Dialog: Ch???n danh m???c cho s???n ph???m
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

        // Kh???i t???o ???nh x??? view cho dialog + Kh???i t???o adapter
        RecyclerView recycler_Choice_Type   = dialogInsertProduct.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogInsertProduct.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_category_product);
        // g??n adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // t???o layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        choiceTypeAdapter.setList(typeCategoryList);
        dialogInsertProduct.show();
    }

    // Dialog: Ch???n ????n v??? t??nh cho s???n ph???m
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

        // Kh???i t???o ???nh x??? view cho dialog + Kh???i t???o adapter
        RecyclerView recycler_Choice_Type   = dialogInsertProduct.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogInsertProduct.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_balance_product);

        // g??n adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // t???o layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        dialogInsertProduct.show();

        choiceTypeAdapter.setList(typeBalanceList);
    }

    // Dialog: Ch???n nh??n vi??n ph??? tr??ch
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

        // Kh???i t???o ???nh x??? view cho dialog + Kh???i t???o adapter
        RecyclerView recycler_Choice_Type   = dialogInsertProduct.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogInsertProduct.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_balance_product);

        // g??n adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // t???o layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        dialogInsertProduct.show();

        choiceTypeAdapter.setList(listEmployee);
    }


    // Check permission: Ki???m tra quy???n truy c???p v??o th?? vi???n ???nh
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

    // Open gallery: n???u ???? cho ph??p th?? m??? th?? vi???n ???nh
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
        typeCategoryList = List; // g??n danh s??ch category ???? t???i v???
    }

    @Override
    public void getBalance(List<Balance> list) {
        typeBalanceList = list; // g??n danh s??ch ????n v??? t??nh ???? t???i v???
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
        // Chuy???n String idProduct sang activity QR Code
        Intent intentQR = new Intent(InsertProductActivity.this,CreateQRCodeActivity.class);
        intentQR.putExtra("idProduct",idProduct);
        startActivity(intentQR);

        // Sau khi chuy???n sang activity QR Code th?? ????ng activity l???i
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