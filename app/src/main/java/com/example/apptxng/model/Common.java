package com.example.apptxng.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.apptxng.api.API;
import com.example.apptxng.api.Retrofit_Client;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Common {
    public static User currentUser;
    public static final String URL = "http://192.168.3.117/txng/";
    public static final API api = Retrofit_Client.getRetrofit(Common.URL).create(API.class);

    public static final Locale locale = new Locale("vi","VN");
    public static NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void closeKeyboard(View view){
        if (view != null)
        {
            InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    // Lấy đường dẫn thực của URI
    public static String getRealPathFromURI (Uri contentUri,Context context) {
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

    // Cập nhật thông tin currentUser
    public static void reloadCurrentUser()
    {
        Common.api.reloadInfo(currentUser.getIdUser())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        currentUser = response.body();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("reload", "onFailure: " + t.getMessage() );
                    }
                });
    }

    // Chuyển ảnh từ bitmap sang uri: Khi sử dụng camero chụp và hiền thị
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
