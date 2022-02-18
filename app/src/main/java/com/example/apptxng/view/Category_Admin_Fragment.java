package com.example.apptxng.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.category_Admin_Adapter;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.presenter.ICategoryAdmin;
import com.example.apptxng.presenter.categoryAdminPresenter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Category_Admin_Fragment extends Fragment implements ICategoryAdmin {

    private View viewFragment;
    private ImageView img_Add_Category;                     // Nút thêm category mới
    private category_Admin_Adapter category_Admin_Adapter;  // Biến adapter recycler view
    private Uri uri_ImageCategory;                          // Uri của ảnh category
    private ImageView img_AddCategory_Dialog;               // Ảnh của category
    private RecyclerView recycler_Category_Admin;           // Recycler view category
    private com.example.apptxng.presenter.categoryAdminPresenter categoryPresenter;     // Tạo biến Category Presenter
    private ProgressDialog progressDialogCategoryAdmin;     // Tạo progress dialog
    private List<Category> list = new ArrayList<>();
    // Tạo biến Result Launcher để lấy URI ảnh chọn từ người dùng
    private final ActivityResultLauncher<Intent> intentGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        uri_ImageCategory = result.getData().getData();
                        img_AddCategory_Dialog.setImageURI(uri_ImageCategory);
                    }
                }
            });



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewFragment =  inflater.inflate(R.layout.fragment_category__admin_, container, false);

        // Init view
        initView(viewFragment);

        // Khởi tạo adapter Recycler Category
        category_Admin_Adapter = new category_Admin_Adapter(viewFragment.getContext(), new category_Admin_Adapter.IListenerCategoryAdmin() {
            @Override
            public void onClickItemCategoryAdmin(Category category) {

            }
        });

        // Tạo layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(viewFragment.getContext(),RecyclerView.VERTICAL,false);
        recycler_Category_Admin.setLayoutManager(layoutManager);

        // Get all category
        categoryPresenter.getAllCategory();
        // Set adapter Recycler
        recycler_Category_Admin.setAdapter(category_Admin_Adapter);


        return viewFragment;
    }



    private void initView(View view) {
        recycler_Category_Admin         = view.findViewById(R.id.recycler_Category_Admin);
        img_Add_Category                = view.findViewById(R.id.img_Add_Category_Admin);
        categoryPresenter               = new categoryAdminPresenter(viewFragment.getContext(),this);
        progressDialogCategoryAdmin     = new ProgressDialog(viewFragment.getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        img_Add_Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddCategory();
            }   
        });

    }

    // Dialog add Category
    private void showDialogAddCategory() {
        Dialog dialogAddCategory = new Dialog(viewFragment.getContext());
        dialogAddCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddCategory.setCanceledOnTouchOutside(true);
        dialogAddCategory.setContentView(R.layout.dialog_add_category_admin);

        Window window = dialogAddCategory.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
        }

        // Ánh xạ view của dialog
        img_AddCategory_Dialog                      = dialogAddCategory.findViewById(R.id.img_AddCategory_Dialog);
        EditText edt_Name_AddCategory_Dialog        = dialogAddCategory.findViewById(R.id.edt_Name_AddCategory_Dialog);
        Button btn_Cancel_AddCategory_Dialog        = dialogAddCategory.findViewById(R.id.btn_Cancel_AddCategory_Dialog);
        Button btn_Confirm_AddCategory_Dialog       = dialogAddCategory.findViewById(R.id.btn_Confirm_AddCategory_Dialog);


        // Chọn ảnh cho category
        img_AddCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOpenGallery();
            }
        });

        // Hủy dialog thêm category
        btn_Cancel_AddCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddCategory.cancel();
            }
        });

        // Xác nhận thêm category
        btn_Confirm_AddCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra dữ liệu có rỗng hay không
                String nameCategory = edt_Name_AddCategory_Dialog.getText().toString().trim();

                if (nameCategory.isEmpty() || uri_ImageCategory == null)
                {
                    Toast.makeText(view.getContext(), "Bạn đang bỏ trống dữ liệu nào đó.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialogCategoryAdmin.setMessage("Chờ trong giây lát...");
                    progressDialogCategoryAdmin.show();
                    Category category = new Category(nameCategory,uri_ImageCategory.toString());
                    categoryPresenter.addCategory(category);
                    closeKeyboard();
                }
            }
        });

        // Show dialog
        dialogAddCategory.show();
    }

    // Xin quyền truy cập thư viện
    private void checkPermissionOpenGallery() {
        Dexter.withContext(viewFragment.getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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

    // Mở thư viện ảnh
    private void openGallery() {
        Intent intentGallery = new Intent();
        intentGallery.setType("image/*");
        intentGallery.setAction(Intent.ACTION_PICK);
        intentGalleryLauncher.launch(intentGallery);
    }
    //
    private void closeKeyboard()
    {
        View view = viewFragment;
        if (view != null)
        {
            InputMethodManager manager = (InputMethodManager) viewFragment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }


    // Override method
    @Override
    public void addSuccess(String message) {
        Toast.makeText(viewFragment.getContext(), message, Toast.LENGTH_LONG).show();
        progressDialogCategoryAdmin.dismiss();
    }

    @Override
    public void addFailed(String message) {
        Toast.makeText(viewFragment.getContext(), message, Toast.LENGTH_LONG).show();
        progressDialogCategoryAdmin.dismiss();
    }

    @Override
    public void addException(String message) {
        Toast.makeText(viewFragment.getContext(), message, Toast.LENGTH_LONG).show();
        progressDialogCategoryAdmin.dismiss();
    }

    @Override
    public void getAllCategorySuccess(List<Category> categoryList) {
        for (Category c : categoryList)
        {
            Log.e("I", "getAllCategorySuccess: " + c.getImageCategory());
        }
        category_Admin_Adapter.setListCategory(categoryList);

    }
}