package com.example.apptxng.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import androidx.annotation.NonNull;

import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.ResponsePOST;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Category_Admin_Presenter {

    private final ICategoryAdmin iCategoryAdmin;
    private final Context context;
    private List<Category> categoryList;

    public Category_Admin_Presenter(Context context, ICategoryAdmin iCategoryAdmin) {
        this.iCategoryAdmin = iCategoryAdmin;
        this.context = context;
    }

    // Add new category
    public void addCategory(Category category){
        Uri uriImageCategory = Uri.parse(category.getImageCategory());
        File file = new File(getRealPathFromURI(uriImageCategory));

        String filePath = file.getAbsolutePath();
        String[] arraySplitPath = filePath.split("\\.");

        String nameCategoryNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];

        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"),category.getNameCategory());
        RequestBody requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part requestPartImage = MultipartBody.Part.createFormData("imageCategory", nameCategoryNew,requestBodyImage);

        Common.api.addCategory(requestBodyName, requestPartImage)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST result = response.body();
                        assert result != null;

                        if (result.getStatus() == 1)
                        {
                            getAllCategory();
                            iCategoryAdmin.addSuccess(result.getMessage());
                        }
                        else
                        {
                            iCategoryAdmin.addFailed(result.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iCategoryAdmin.Exception(t.getMessage());
                    }
                });
    }

    // Delete category : Xóa ảnh khi xóa category và xóa các sản phẩm có trong category (Chưa làm)
    public void deleteCategory(int idCategory, String imageCategory){
        Common.api.deleteCategory(idCategory,imageCategory)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();
                        assert responsePOST != null;
                        if (responsePOST.getStatus() == 1)
                        {
                            iCategoryAdmin.deleteSuccess(responsePOST.getMessage());
                            getAllCategory();
                        }
                        else
                        {
                            iCategoryAdmin.deleteFailed(responsePOST.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iCategoryAdmin.Exception(t.getMessage());
                    }
                });
    }

    // Update category
    public void updateCategory(Category category, Uri imageNewCategory){
        // Khởi tạo ban đầu
        RequestBody requestBodyImage,requestBodyImageOld = null;
        MultipartBody.Part requestPartImage = null;
        String nameCategoryNew;

        // Nếu có thay đổi ảnh mới hay không, nếu không thì request Image = null
        if (imageNewCategory != null)
        {
            File file = new File(getRealPathFromURI(imageNewCategory));
            String filePath = file.getAbsolutePath();
            String[] arraySplitPath = filePath.split("\\.");
            nameCategoryNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];
            requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            requestPartImage = MultipartBody.Part.createFormData("imageCategory", nameCategoryNew,requestBodyImage);
            requestBodyImageOld = RequestBody.create(MediaType.parse("multipart/form-data"),category.getImageCategory());

        }
        // tạo các Request body: idCategory, nameCategory, imgOld_Category
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"),category.getNameCategory());
        RequestBody requestBodyID = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(category.getIdCategory()));

        Common.api.updateCategory(requestBodyID, requestBodyImageOld, requestBodyName, requestPartImage)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                            ResponsePOST responsePOST = response.body();
                            assert responsePOST != null;
                            if (responsePOST.getStatus() == 1)
                            {
                                getAllCategory();
                                iCategoryAdmin.updateSuccess(responsePOST.getMessage());
                            }
                            else
                            {
                                iCategoryAdmin.updateFailed(responsePOST.getMessage());
                            }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                            iCategoryAdmin.Exception(t.getMessage());
                    }
                });
    }

    // Get all category
    public void getAllCategory(){
        categoryList = new ArrayList<>();
        Common.api.getAllCategory()
                .enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                        categoryList = response.body();
                        iCategoryAdmin.getAllCategorySuccess(categoryList);

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                        iCategoryAdmin.Exception(t.getMessage());
                    }
                });
    }

    // Lấy đường dẫn thực của URI
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


}
