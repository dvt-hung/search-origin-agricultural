package com.example.apptxng.model;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.apptxng.R;
import com.example.apptxng.api.API;
import com.example.apptxng.api.Retrofit_Client;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Common {

    /*
    * Admin: 0123456 - 123456
    * ND: 0123123 - 123456
    * KH: 0923088 - 123456
    * QL (Vận chuyển): 0923089 - 123456
    * NV - Nhà vườn: 0456456 - 123456
    * NV - QL: 0789789 - 123456
    * Chế biến: 0933230
    * Kho: 0988776
    * Cửa hàng: 0988666
    * */


    public static User currentUser;

    public static final String codeCounty = "+84";

    public static final String URL = "https://truyxuatnongsan.000webhostapp.com/api/";

    public static final API api = Retrofit_Client.getRetrofit(Common.URL).create(API.class);

    public static Calendar calendar = Calendar.getInstance();

    public static final Locale locale = new Locale("vi","VN");
    public static NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);


    /*
    * ID ROLE
    * 1: ADMIN
    * 2: QUẢN LÝ
    * 3: NÔNG DÂN
    * 4: KHÁCH HÀNG
    * 5: NHÂN VIÊN
    * */
    public static int ID_ROLE_ADMIN = 1; // Id Role của nông dân
    public static int ID_ROLE_MANAGER = 2; // Id Role của nông dân
    public static int ID_ROLE_FARMER = 3; // Id Role của nông dân
    public static int ID_ROLE_CUSTOMER = 4; // Id Role của khách hàng
    public static int ID_ROLE_EMPLOYEE = 5; // Id Role của nhân viên
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

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



    // Chuyển ảnh từ bitmap sang uri: Khi sử dụng camero chụp và hiền thị
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // Sắp xếp mảng theo ngày tăng dần
    public static void sortDates(List<History> historyList)
    {
        Collections.sort(historyList, new Comparator<History>() {
            @Override
            public int compare(History history, History t1) {
                try {
                    Date d1 = Common.dateFormat.parse(history.getDateHistory());
                    Date d2 = Common.dateFormat.parse(t1.getDateHistory());

                    assert d1 != null;
                    return d1.compareTo(d2);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });
    }

    public static ProgressDialog createProgress(Context context)
    {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Vui lòng đợi ...");
        return dialog;
    }


    // Check và hiển thị giá trị của text view: Null or No Null
    public static void displayValueTextView(TextView textView, String value)
    {
        if (value == null || value.equals(" ") || value.isEmpty())
        {
            textView.setText(R.string.title_error_empty_user);
        }
        else
        {
            textView.setText(value);
        }
    }

    // Kiểm tra dữ liệu STRING vào để truyền lên API có null hay không
    public static String checkStringValue(String value)
    {
        if (value == null || value.isEmpty() || value.equals(" ") )
        {
            return " ";
        }
        else
        {
            return value;
        }
    }
}
