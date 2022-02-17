package com.example.apptxng.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.apptxng.model.Category;
import com.example.apptxng.model.Common;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class categoryAdminPresenter {

    private final ICategoryAdmin iCategoryAdmin;
    private Context context;

    public categoryAdminPresenter(Context context, ICategoryAdmin iCategoryAdmin) {
        this.iCategoryAdmin = iCategoryAdmin;
        this.context = context;
    }

    public void addCategory(Category category)
    {
        File file = new File(getRealPathFromURI(category.getImageCategory()));

        String filePath = file.getAbsolutePath();
        String[] arraySplitPath = filePath.split("\\.");
        String nameCategoryNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];
        Log.e("A", "addCategory: " + nameCategoryNew);

        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"),category.getNameCategory());
        RequestBody requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part requestPartImage = MultipartBody.Part.createFormData("image_category", nameCategoryNew,requestBodyImage);

        Common.api.addCategory(requestBodyName, requestPartImage)
                .enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(Call<Category> call, Response<Category> response) {

                    }

                    @Override
                    public void onFailure(Call<Category> call, Throwable t) {

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
