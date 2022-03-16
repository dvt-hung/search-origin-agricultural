package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.History;
import com.example.apptxng.model.ResponsePOST;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History_Presenter {

    private final IHistory iHistory;
    private final Context context;

    public History_Presenter(IHistory iHistory, Context context) {
        this.iHistory = iHistory;
        this.context = context;
    }


    // Thêm nhật ký của sản phẩm
    public void InsertHistory(History history, Uri image)
    {
        // Kiểm tra dữ liệu
        if (image == null || history.getFactory().getIdFactory() == 0 || history.getDescriptionHistory().isEmpty())
        {
            iHistory.emptyValue();
        }
        else
        {
            // Tạo request body + multipart của Ảnh History
//            Uri uriHistory = Uri.parse(history.getImageHistory());
            File file = new File(Common.getRealPathFromURI(image,context));

            String filePath = file.getAbsolutePath();
            String[] arraySplitPath = filePath.split("\\.");

            // Tạo tên mới cho ảnh
            String nameImageNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];

            // Request: idProduct
            RequestBody idProduct           = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(history.getIdProduct()));

            // Request: idFactory
            RequestBody idFactory           = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(history.getFactory().getIdFactory()));

            // Request: descriptionHistory
            RequestBody descriptionHistory  = RequestBody.create(MediaType.parse("multipart/form-data"), history.getDescriptionHistory());

            // Request: dateHistory
            RequestBody dateHistory         = RequestBody.create(MediaType.parse("multipart/form-data"), history.getDateHistory());

            // MultipartBody: imageHistory
            RequestBody requestBodyImage    = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            MultipartBody.Part imageHistory = MultipartBody.Part.createFormData("imageHistory", nameImageNew,requestBodyImage);


            //Tạo progress dialog
            ProgressDialog progress = new ProgressDialog(context);
            progress.setMessage("Vui lòng đợi...");
            progress.show();
            // Gọi đến API
            Common.api.insertHistory(idProduct,idFactory,descriptionHistory,dateHistory,imageHistory)
                    .enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                            ResponsePOST result = response.body();

                            assert result != null;
                            if (result.getStatus() == 1)
                            {
                                iHistory.successMessage(result.getMessage());
                            }
                            else
                            {
                                iHistory.failedMessage(result.getMessage());
                            }
                            progress.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                            iHistory.exceptionMessage(t.getMessage());
                            progress.dismiss();
                        }
                    });

        }

    }


    // Tải danh sách sản phẩm
    public void loadHistory(int idProduct)
    {
        //Tạo progress dialog
        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Vui lòng đợi...");
        progress.show();

        // Gọi api
        Common.api.getHistory(idProduct)
                .enqueue(new Callback<List<History>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<History>> call, @NonNull Response<List<History>> response) {
                        iHistory.getHistory(response.body());
                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<History>> call, @NonNull Throwable t) {
                        iHistory.exceptionMessage(t.getMessage());
                        progress.dismiss();
                    }
                });
    }
}
