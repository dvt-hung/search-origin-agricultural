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
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.Product;
import com.example.apptxng.model.User;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProductActivity extends AppCompatActivity implements ChoiceType_Adapter.IListenerChoiceType, IUpdateProduct {

    private ImageView img_UpdateProduct,img_Back_UpdateProduct;
    private EditText edt_Name_UpdateProduct, edt_Price_UpdateProduct,edt_Des_UpdateProduct,edt_Quantity_UpdateProduct;
    private TextView txt_ChoiceCategory_UpdateProduct, txt_ResultCategory_UpdateProduct, txt_ChoiceBalance_UpdateProduct, txt_ResultBalance_UpdateProduct,
            txt_IngredientProduct, txt_UseProduct,  txt_GuideProduct, txt_ConditionProduct,txt_ChoiceEmployee_UpdateProduct,txt_ResultEmployee_UpdateProduct;

    @SuppressLint("StaticFieldLeak")
    public static TextView txt_Result_IngredientProduct,txt_Result_UseProduct,txt_Result_GuideProduct,txt_Result_ConditionProduct;
    private Button btn_UpdateProduct;
    private Product product;
    private Dialog dialogUpdateProduct;
    private ChoiceType_Adapter choiceTypeAdapter;
    private List<Category> typeCategoryList = new ArrayList<>() ;
    private List<Balance> typeBalanceList = new ArrayList<>() ;
    private List<User> listEmployee = new ArrayList<>() ;
    private Uri uri_ImageProductTemp;
    private Update_Product_Presenter updateProductPresenter;
    private ProgressDialog progressUpdate;

    private final String TITLE_INGREDIENT_U   = "TITLE_INGREDIENT_U";
    private final String TITLE_USE_U          = "TITLE_USE_U";
    private final String TITLE_GUIDE_U        = "TITLE_GUIDE_U";
    private final String TITLE_CONDITION_U    = "TITLE_CONDITION_U";
    private final String NON_EMPLOYEE    = "Ch??a ch??? ?????nh nh??n vi??n qu???n l?? s???n ph???m";

    private final ActivityResultLauncher<Intent> resultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData() != null)
            {
                uri_ImageProductTemp = result.getData().getData(); // G??n Uri c???a ???nh ???? ch???n cho bi???n
                img_UpdateProduct.setImageURI(uri_ImageProductTemp);
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product_farmer);

        // Kh???i t???o, ??nh x??? view
        initView();

    }


    // ??nh x??? view
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
        txt_IngredientProduct               = findViewById(R.id.txt_IngredientProduct);
        txt_Result_IngredientProduct        = findViewById(R.id.txt_Result_IngredientProduct);
        txt_UseProduct                      = findViewById(R.id.txt_UseProduct);
        txt_Result_UseProduct               = findViewById(R.id.txt_Result_UseProduct);
        txt_GuideProduct                    = findViewById(R.id.txt_GuideProduct);
        txt_Result_GuideProduct             = findViewById(R.id.txt_Result_GuideProduct);
        txt_ConditionProduct                = findViewById(R.id.txt_ConditionProduct);
        txt_Result_ConditionProduct         = findViewById(R.id.txt_Result_ConditionProduct);
        txt_ChoiceEmployee_UpdateProduct    = findViewById(R.id.txt_ChoiceEmployee_UpdateProduct);
        txt_ResultEmployee_UpdateProduct    = findViewById(R.id.txt_ResultEmployee_UpdateProduct);

        choiceTypeAdapter                   = new ChoiceType_Adapter(this);
        updateProductPresenter              = new Update_Product_Presenter(this,this);
        progressUpdate                      = new ProgressDialog(this);
        progressUpdate.setMessage("Vui l??ng ch???...");
        // Nh???n Product t??? Detail chuy???n sang
        product = (Product) getIntent().getExtras().getSerializable("product");

        // L???y th??ng tin c???a employee


        // Hi???n th??? d??? li???u ban ?????u
        displayValue();
    }



    // Hi???n th??? d??? li???u ban ?????u c???a Product
    private void displayValue() {
        edt_Name_UpdateProduct.setText(product.getNameProduct());

        edt_Price_UpdateProduct.setText(String.valueOf(product.getPriceProduct()));

        edt_Des_UpdateProduct.setText(product.getDescriptionProduct());

        edt_Quantity_UpdateProduct.setText(String.valueOf(product.getQuantityProduct()));

        Glide.with(this).load(product.getImageProduct()).into(img_UpdateProduct);

        txt_ResultCategory_UpdateProduct.setText(product.getCategory().getNameCategory());

        txt_ResultBalance_UpdateProduct.setText(product.getBalance().getNameBalance());

        checkValueTextView(txt_Result_IngredientProduct,product.getIngredientProduct());

        checkValueTextView(txt_Result_UseProduct,product.getUseProduct());

        checkValueTextView(txt_Result_GuideProduct,product.getGuideProduct());

        checkValueTextView(txt_Result_ConditionProduct,product.getConditionProduct());

        // N???u ch??a ch??? ?????nh nh??n vi??n qu???n l?? s???n ph???m th?? s??? ???n ??i
        if (product.getIdEmployee() == null || product.getIdEmployee().isEmpty() || product.getIdEmployee().equals(" "))
        {
            txt_ResultEmployee_UpdateProduct.setText(R.string.title_non_employee);
        }
        else
        {
            getInfoEmployee();
        }

    }

    // L???y th??ng tin nh??n vi??n qu???n l?? s???n ph???m
    private synchronized void getInfoEmployee() {
        // G???i ?????n API - l???y info user
        Common.api.getInfoUser(product.getIdEmployee())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        User employee = response.body();
                        assert employee != null;
                        txt_ResultEmployee_UpdateProduct.setText(employee.getName());

                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Toast.makeText(UpdateProductActivity.this, "???? c?? l???i. Vui l??ng th??? l???i! " , Toast.LENGTH_SHORT).show();

                    }
                });
    }

    // Check value text view
    private void checkValueTextView(TextView view, String value) {
        if (value == null || value.equals(" ") || value.isEmpty()) {
            view.setVisibility(View.GONE);

        } else
        {
            view.setVisibility(View.VISIBLE);
            view.setText(value);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadListChoiceType();

        // Khi ???n s??? t???t activity ??i
        img_Back_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Ch???n danh m???c s???n ph???m m???i
        txt_ChoiceCategory_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceCategory();
            }
        });

        // 3. Ch???n ????n v??? t??nh m???i cho s???n ph???m
        txt_ChoiceBalance_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceBalance();
            }
        });

        // 4. Ch???n ???nh m???i cho s???n ph???m
        img_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOpenGallery();
            }
        });

        // 5. Confirm button: Ti???n h??nh c???p nh???t s???n ph???m
        btn_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name         = edt_Name_UpdateProduct.getText().toString().trim();
                String price        = edt_Price_UpdateProduct.getText().toString().trim();
                String des          = edt_Des_UpdateProduct.getText().toString().trim();
                String quantity     = edt_Quantity_UpdateProduct.getText().toString().trim();
                String ingredient   = txt_Result_IngredientProduct.getText().toString().trim();
                String use          = txt_Result_UseProduct.getText().toString().trim();
                String guide        = txt_Result_GuideProduct.getText().toString().trim();
                String condition    = txt_Result_ConditionProduct.getText().toString().trim();

                // G??n gi?? tr??? ???? thay ?????i
                product.setNameProduct(name);
                product.setPriceProduct(Integer.parseInt(price));
                product.setDescriptionProduct(des);
                product.setQuantityProduct(Integer.parseInt(quantity));
                product.setIngredientProduct(ingredient);
                product.setUseProduct(use);
                product.setGuideProduct(guide);
                product.setConditionProduct(condition);

                updateProductPresenter.updateProduct(product,uri_ImageProductTemp);
                progressUpdate.show();
            }
        });

        /*
        6. txt_IngredientProduct: Chuy???n sang activiy nh???p th??nh ph???n
         */
        txt_IngredientProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = txt_Result_IngredientProduct.getText().toString().trim();
                Intent intentIngredient = new Intent(UpdateProductActivity.this, InputValueProductActivity.class);
                intentIngredient.putExtra("code", TITLE_INGREDIENT_U);
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
                Intent intentUse = new Intent(UpdateProductActivity.this, InputValueProductActivity.class);
                intentUse.putExtra("code", TITLE_USE_U);
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
                Intent intentGuide = new Intent(UpdateProductActivity.this, InputValueProductActivity.class);
                intentGuide.putExtra("code", TITLE_GUIDE_U);
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
                Intent intentCondition = new Intent(UpdateProductActivity.this, InputValueProductActivity.class);
                intentCondition.putExtra("code", TITLE_CONDITION_U);
                intentCondition.putExtra("value", value);
                startActivity(intentCondition);
            }
        });

        /*
        * 10. Hi???n th??? dialog l???a ch???n nh??n vi??n qu???n l??
        *
        * */

        txt_ChoiceEmployee_UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChoiceEmployee();
            }
        });
    }
    // LOAD LIST CHOICE TYPE: T???i d??? li???u danh s??ch c??c ????n v??? t??nh v?? danh m???c
    private void loadListChoiceType() {
        updateProductPresenter.getCategory();
        updateProductPresenter.getBalance();
        getListEmployeeAccount(Common.currentUser.getIdUser());
    }
    // Dialog: Ch???n danh m???c cho s???n ph???m
    private void showDialogChoiceCategory() {
        dialogUpdateProduct = new Dialog(this);
        dialogUpdateProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdateProduct.setContentView(R.layout.dialog_bottom_choice_type);
        Window window = dialogUpdateProduct.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        // Kh???i t???o ???nh x??? view cho dialog + Kh???i t???o adapter
        RecyclerView recycler_Choice_Type   = dialogUpdateProduct.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogUpdateProduct.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_category_product);
        // g??n adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // t???o layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        choiceTypeAdapter.setList(typeCategoryList);
        dialogUpdateProduct.show();
    }

    // Dialog: Ch???n ????n v??? t??nh cho s???n ph???m
    private void showDialogChoiceBalance() {
        dialogUpdateProduct = new Dialog(this);
        dialogUpdateProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdateProduct.setContentView(R.layout.dialog_bottom_choice_type);
        Window window = dialogUpdateProduct.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        // Kh???i t???o ???nh x??? view cho dialog + Kh???i t???o adapter
        RecyclerView recycler_Choice_Type   = dialogUpdateProduct.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogUpdateProduct.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_balance_product);

        // g??n adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // t???o layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        dialogUpdateProduct.show();

        choiceTypeAdapter.setList(typeBalanceList);
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


    // Dialog: Ch???n nh??n vi??n ph??? tr??ch
    private void showDialogChoiceEmployee() {
        dialogUpdateProduct = new Dialog(this);
        dialogUpdateProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdateProduct.setContentView(R.layout.dialog_bottom_choice_type);
        Window window = dialogUpdateProduct.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }

        // Kh???i t???o ???nh x??? view cho dialog + Kh???i t???o adapter
        RecyclerView recycler_Choice_Type   = dialogUpdateProduct.findViewById(R.id.recycler_Choice_Type);
        TextView txt_Title_Choice_Type      = dialogUpdateProduct.findViewById(R.id.txt_Title_Choice_Type);

        txt_Title_Choice_Type.setText(R.string.choice_balance_product);

        // g??n adapter cho recycler view
        recycler_Choice_Type.setAdapter(choiceTypeAdapter);

        // t???o layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        dialogUpdateProduct.show();

        choiceTypeAdapter.setList(listEmployee);
    }
    public synchronized void getListEmployeeAccount(String idOwner)
    {
        ProgressDialog dialog = Common.createProgress(UpdateProductActivity.this);
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
                        Toast.makeText(UpdateProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }

    // ************* OVERRIDE METHOD ******************
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
        else if (obj.getClass() == User.class)
        {
            User employee = (User) obj;
            product.setIdEmployee(employee.getIdUser());
            txt_ResultEmployee_UpdateProduct.setText(employee.getName());
        }
        dialogUpdateProduct.cancel();
    }


    @Override
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressUpdate.dismiss();
    }

    @Override
    public void getCategory(List<Category> List) {
        typeCategoryList = List;
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
    public void success(String message) {
        if (Common.currentUser.getIdRole() == Common.ID_ROLE_MANAGER)
        {
            startActivity(new Intent(UpdateProductActivity.this, ManagerActivity.class));
        }
        else if (Common.currentUser.getIdRole() == Common.ID_ROLE_FARMER)
        {
            startActivity(new Intent(UpdateProductActivity.this, FarmerActivity.class));
        }
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