package com.example.apptxng.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.responsePOST;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class categoryAdminPresenter {

    private final ICategoryAdmin iCategoryAdmin;
    private Context context;
    private List<Category> categoryList;

    public categoryAdminPresenter(Context context, ICategoryAdmin iCategoryAdmin) {
        this.iCategoryAdmin = iCategoryAdmin;
        this.context = context;
    }

    // Add new category
    public void addCategory(Category category)
    {
        Uri uriImageCategory = Uri.parse(category.getImageCategory());
        File file = new File(getRealPathFromURI(uriImageCategory));

        String filePath = file.getAbsolutePath();
        String[] arraySplitPath = filePath.split("\\.");
        String nameCategoryNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];

        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"),category.getNameCategory());
        RequestBody requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part requestPartImage = MultipartBody.Part.createFormData("imageCategory", nameCategoryNew,requestBodyImage);

        Common.api.addCategory(requestBodyName, requestPartImage)
                .enqueue(new Callback<responsePOST>() {
                    @Override
                    public void onResponse(Call<responsePOST> call, Response<responsePOST> response) {
                        responsePOST result = response.body();
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
                    public void onFailure(Call<responsePOST> call, Throwable t) {
                        iCategoryAdmin.addException(t.getMessage());
                    }
                });
    }


    // Get all category
    public void getAllCategory()
    {
        categoryList = new ArrayList<>();
        Common.api.getAllCategory()
                .enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                        categoryList = response.body();
                        iCategoryAdmin.getAllCategorySuccess(categoryList);
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {

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
