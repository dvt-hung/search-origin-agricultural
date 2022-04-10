package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.FileUtils;
import com.example.apptxng.model.ImageHistory;
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

public class ImageHistory_Presenter {

    private final IImageHistory iImageHistory;
    private final Context context;


    public ImageHistory_Presenter(IImageHistory iImageHistory, Context context) {
        this.iImageHistory = iImageHistory;
        this.context = context;
    }

    // Lấy hình ảnh của history
    public synchronized void getImageHistory(String idHistory)
    {
        // Progress dialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Vui lòng chờ...");
        progressDialog.show();
       new Thread(new Runnable() {
           @Override
           public void run() {
                Common.api.getImageHistory(idHistory)
                        .enqueue(new Callback<List<ImageHistory>>() {
                            @Override
                            public void onResponse(@NonNull Call<List<ImageHistory>> call, @NonNull Response<List<ImageHistory>> response) {
                                iImageHistory.getImages(response.body());
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<ImageHistory>> call, @NonNull Throwable t) {
                                iImageHistory.exception(t.getMessage());
                                progressDialog.dismiss();
                            }
                        });
           }
       }).start();
    }

    // Xoá ảnh của history
    public  synchronized void deleteImageHistory(ImageHistory image)
    {
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();
        Common.api.deleteImageHistory(image.getIdImageHistory(), image.getImageHistory())
            .enqueue(new Callback<ResponsePOST>() {
                @Override
                public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                    ResponsePOST result = response.body();
                    assert result != null;
                    if (result.getStatus() == 1)
                    {
                        iImageHistory.success(result.getMessage());
                        getImageHistory(image.getIdHistory());
                    }
                    else
                    {
                        iImageHistory.failed(result.getMessage());
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iImageHistory.exception(t.getMessage());
                        dialog.dismiss();
                }
            });
    }


    // Xóa history
    public synchronized void deleteHistory(List<ImageHistory> list)
    {
        Common.api.deleteHistory(list)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();
                        assert responsePOST != null;
                        if (responsePOST.getStatus() == 1)
                        {
                            iImageHistory.success(responsePOST.getMessage());
                        }
                        else
                        {
                            iImageHistory.failed(responsePOST.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                            iImageHistory.exception(t.getMessage());
                    }
                });
    }

    // Thêm ảnh cho history
    public synchronized void insertImageHistory(List<Uri> images, String idHis)
    {
        // Request: idHistory
        RequestBody idHistory           = RequestBody.create(MediaType.parse("multipart/form-data"), idHis);

        // Create list multipart body
        List<MultipartBody.Part> listMultipartImage = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {

            File file = new File(FileUtils.getPath(context,images.get(i)));

            String filePath = file.getAbsolutePath();
            String[] arraySplitPath = filePath.split("\\.");

            // Tạo tên mới cho ảnh
            String nameImageNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];

            // MultipartBody: imageHistory
            RequestBody requestBodyImage    = RequestBody.create(MediaType.parse("multipart/form-data"),FileUtils.getFile(context,images.get(i)));

            MultipartBody.Part imageHistory = MultipartBody.Part.createFormData("imageHistory[]", nameImageNew,requestBodyImage);

            listMultipartImage.add(imageHistory);
        }

        // Call API
        ProgressDialog dialog = Common.createProgress(context);
        dialog.show();
        Common.api.insertImageHistory(idHistory,listMultipartImage)
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST result = response.body();
                        assert result != null;
                        if (result.getStatus() == 1)
                        {
                            iImageHistory.success(result.getMessage());
                        }
                        else
                        {
                            iImageHistory.failed(result.getMessage());
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iImageHistory.exception(t.getMessage());
                        dialog.dismiss();

                    }
                });
    }
}
