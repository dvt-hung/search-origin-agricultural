package com.example.apptxng.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.adapter.Category_Admin_Adapter;
import com.example.apptxng.model.Category;
import com.example.apptxng.presenter.ICategoryAdmin;
import com.example.apptxng.presenter.Category_Admin_Presenter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;


public class Category_Admin_Fragment extends Fragment implements ICategoryAdmin {

    private View viewFragment;
    private ImageView img_Add_Category;                     // Nút thêm category mới
    private Category_Admin_Adapter category_Admin_Adapter;  // Biến adapter recycler view
    private Uri uri_ImageCategory = null;                          // Uri của ảnh category
    private ImageView img_Category_Dialog;               // Ảnh của category
    private RecyclerView recycler_Category_Admin;           // Recycler view category
    private Category_Admin_Presenter categoryPresenter;     // Tạo biến Category Presenter
    private ProgressDialog progressDialogCategoryAdmin;     // Tạo progress dialog
    private Dialog dialogCategoryAdmin;                     // Tạo dialog dùng chung cho Fragment


    // Tạo biến Result Launcher để lấy URI ảnh chọn từ người dùng
    private final ActivityResultLauncher<Intent> intentGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        uri_ImageCategory = result.getData().getData();
                        img_Category_Dialog.setImageURI(uri_ImageCategory);
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("A", "onCreateView" );
        // Inflate the layout for this fragment
        viewFragment =  inflater.inflate(R.layout.fragment_category__admin_, container, false);

        // Init view
        initView(viewFragment);

        // Khởi tạo adapter Recycler Category
        category_Admin_Adapter = new Category_Admin_Adapter(viewFragment.getContext(), new Category_Admin_Adapter.IListenerCategoryAdmin() {
            @Override
            public void onClickItemCategoryAdmin(Category category) {
                showDialogOptionCategory(category);

            }
        });

        // Tạo layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(viewFragment.getContext(),2,RecyclerView.VERTICAL,false);
        recycler_Category_Admin.setLayoutManager(layoutManager);

        // Get all category
        categoryPresenter.getAllCategory();
        // Set adapter Recycler
        recycler_Category_Admin.setAdapter(category_Admin_Adapter);

        return viewFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        img_Add_Category.setOnClickListener(view -> showDialogAddCategory());

    }



    // Ánh xạ view
    private void initView(View view) {
        recycler_Category_Admin         = view.findViewById(R.id.recycler_Category_Admin);
        img_Add_Category                = view.findViewById(R.id.img_Add_Category_Admin);
        categoryPresenter               = new Category_Admin_Presenter(viewFragment.getContext(),this);
        progressDialogCategoryAdmin     = new ProgressDialog(viewFragment.getContext());
        progressDialogCategoryAdmin.setMessage("Chờ trong giây lát...");
    }

    // Hiện dialog lựa chọn
    private void showDialogOptionCategory(Category category) {
        // Tạo và cài đặt layout cho dialog
        Dialog dialogOptions = new Dialog(viewFragment.getContext());
        dialogOptions.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOptions.setContentView(R.layout.dialog_bottom_option);
        dialogOptions.getWindow().setGravity(Gravity.BOTTOM);
        dialogOptions.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialogOptions.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khởi tạo và ảnh xạ view trong dialog Option
        Button btn_Update_DialogOption      = dialogOptions.findViewById(R.id.btn_Update_DialogOption);
        Button btn_Delete_DialogOption      = dialogOptions.findViewById(R.id.btn_Delete_DialogOption);
        Button btn_Cancel_DialogOption      = dialogOptions.findViewById(R.id.btn_Cancel_DialogOption);

        // Xự kiện khi chọn Update
        btn_Update_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdateCategory(category);
            }
        });

        // Xự kiện khi chọn Delete
        btn_Delete_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDeleteCategory(category);
            }
        });

        // Xự kiện khi chọn Cancel
        btn_Cancel_DialogOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOptions.cancel();
            }
        });

        // Hiện dialog
        dialogOptions.show();
    }


    // Dialog add Category
    private void showDialogAddCategory() {
        dialogCategoryAdmin = new Dialog(viewFragment.getContext());
        dialogCategoryAdmin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCategoryAdmin.setCanceledOnTouchOutside(true);
        dialogCategoryAdmin.setContentView(R.layout.dialog_add_category_admin);

        Window window = dialogCategoryAdmin.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
        }

        // Ánh xạ view của dialog
        img_Category_Dialog                      = dialogCategoryAdmin.findViewById(R.id.img_AddCategory_Dialog);
        EditText edt_Name_AddCategory_Dialog        = dialogCategoryAdmin.findViewById(R.id.edt_Name_AddCategory_Dialog);
        Button btn_Cancel_AddCategory_Dialog        = dialogCategoryAdmin.findViewById(R.id.btn_Cancel_AddCategory_Dialog);
        Button btn_Confirm_AddCategory_Dialog       = dialogCategoryAdmin.findViewById(R.id.btn_Confirm_AddCategory_Dialog);


        // Chọn ảnh cho category
        img_Category_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOpenGallery();
            }
        });

        // Hủy dialog thêm category
        btn_Cancel_AddCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCategoryAdmin.cancel();
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
                    progressDialogCategoryAdmin.show();
                    Category category = new Category(nameCategory,uri_ImageCategory.toString());
                    categoryPresenter.addCategory(category);
                    closeKeyboard();
                }
            }
        });

        // Show dialog
        dialogCategoryAdmin.show();
    }

    // Dialog update Category
    private void showDialogUpdateCategory(Category category) {
        dialogCategoryAdmin = new Dialog(viewFragment.getContext());
        dialogCategoryAdmin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCategoryAdmin.setContentView(R.layout.dialog_update_category_admin);

        Window window = dialogCategoryAdmin.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
        }

        // Ánh xạ view của dialog
        img_Category_Dialog                            = dialogCategoryAdmin.findViewById(R.id.img_UpdateCategory_Dialog);
        EditText edt_Name_UpdateCategory_Dialog        = dialogCategoryAdmin.findViewById(R.id.edt_Name_UpdateCategory_Dialog);
        Button btn_Cancel_UpdateCategory_Dialog        = dialogCategoryAdmin.findViewById(R.id.btn_Cancel_UpdateCategory_Dialog);
        Button btn_Confirm_UpdateCategory_Dialog       = dialogCategoryAdmin.findViewById(R.id.btn_Confirm_UpdateCategory_Dialog);

        // Hiển thị dữ liệu hiện tại
        edt_Name_UpdateCategory_Dialog.setText(category.getNameCategory());
        Glide.with(viewFragment.getContext()).load(category.getImageCategory()).into(img_Category_Dialog);

        // Chọn ảnh cho category
        img_Category_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOpenGallery();
            }
        });

        // Hủy dialog cập nhật category
        btn_Cancel_UpdateCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCategoryAdmin.cancel();
            }
        });

        // Xác nhận cập nhật category
        btn_Confirm_UpdateCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra dữ liệu có rỗng hay không
                progressDialogCategoryAdmin.show();
                String nameCategory = edt_Name_UpdateCategory_Dialog.getText().toString().trim();

                // Trường hợp xóa tên thì không được
                if (nameCategory.isEmpty())
                {
                    Toast.makeText(view.getContext(), "Bạn đang bỏ trống dữ liệu nào đó.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    category.setNameCategory(nameCategory);
                    categoryPresenter.updateCategory(category,uri_ImageCategory);
                }
            }
        });

        // Show dialog
        dialogCategoryAdmin.show();
    }

    // Dialog delete Category
    private void showDialogDeleteCategory(Category category) {

        dialogCategoryAdmin = new Dialog(viewFragment.getContext());
        dialogCategoryAdmin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCategoryAdmin.setContentView(R.layout.dialog_delete_admin);

        Window window = dialogCategoryAdmin.getWindow();

        if (window != null)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
        }

        // Khai báo và ánh xạ view của dialog update
        Button btn_Cancel_DeleteCategory_Dialog     = dialogCategoryAdmin.findViewById(R.id.btn_Cancel_DeleteCategory_Dialog);
        Button btn_Confirm_DeleteCategory_Dialog    = dialogCategoryAdmin.findViewById(R.id.btn_Confirm_DeleteCategory_Dialog);
        TextView txt_Title_Delete_Dialog            = dialogCategoryAdmin.findViewById(R.id.txt_Title_Delete_Dialog);
        TextView txt_Message_Delete_Dialog          = dialogCategoryAdmin.findViewById(R.id.txt_Message_Delete_Dialog);

        // Gán Title, Message cho dialog
        txt_Title_Delete_Dialog.setText(R.string.title_delete_category);
        txt_Message_Delete_Dialog.setText(R.string.title_question_delete_category);

        // Hiển thị dialog
        dialogCategoryAdmin.show();

        // Button cancel
        btn_Cancel_DeleteCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCategoryAdmin.cancel();
            }
        });

        // Button confirm
        btn_Confirm_DeleteCategory_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialogCategoryAdmin.show();
                categoryPresenter.deleteCategory(category.getIdCategory(), category.getImageCategory());
            }
        });

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

    // Đóng bàn phím
    private void closeKeyboard(){
        View view = viewFragment;
        if (view != null)
        {
            InputMethodManager manager = (InputMethodManager) viewFragment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    // ============================================
    // Override method
    @Override
    public void addSuccess(String message) {
        Toast.makeText(viewFragment.getContext(), message, Toast.LENGTH_LONG).show();
        dialogCategoryAdmin.dismiss();
        progressDialogCategoryAdmin.dismiss();
        uri_ImageCategory = null;
    }

    @Override
    public void addFailed(String message) {
        Toast.makeText(viewFragment.getContext(), message, Toast.LENGTH_LONG).show();
        progressDialogCategoryAdmin.dismiss();
    }
    
    @Override
    public void deleteSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialogCategoryAdmin.cancel();
        progressDialogCategoryAdmin.dismiss();
    }

    @Override
    public void deleteFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        progressDialogCategoryAdmin.dismiss();
    }

    @Override
    public void updateFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        progressDialogCategoryAdmin.dismiss();

    }

    @Override
    public void updateSuccess(String message) {
        dialogCategoryAdmin.dismiss();
        progressDialogCategoryAdmin.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        uri_ImageCategory = null;
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(viewFragment.getContext(), message, Toast.LENGTH_LONG).show();
        progressDialogCategoryAdmin.dismiss();
    }

    @Override
    public void getAllCategorySuccess(List<Category> categoryList) {
        category_Admin_Adapter.setListCategory(categoryList);
    }
}