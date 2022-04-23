package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Banner;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.FileUtils;
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

public class Banner_Presenter {
    private final IBanner iBanner;
    private final Context context;


    public Banner_Presenter(IBanner iBanner, Context context) {
        this.iBanner = iBanner;
        this.context = context;
    }

    public synchronized void getBanner()
    {
        //Tạo progress dialog
        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Vui lòng đợi...");
        progress.show();

        Common.api.getBanner()
                .enqueue(new Callback<List<Banner>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Banner>> call, @NonNull Response<List<Banner>> response) {
                        iBanner.getBanner(response.body());
                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Banner>> call, @NonNull Throwable t) {
                        iBanner.exception(t.getMessage());
                        progress.dismiss();
                    }
                });
    }


    // Thêm banner
    public synchronized void insertBanner(List<Uri> image)
    {
        // Kiểm tra dữ liệu
        if (image == null || image.isEmpty())
        {
            iBanner.emptyValue();
        }
        else
        {
            // Create list multipart body
            List<MultipartBody.Part> listMultipartImage = new ArrayList<>();
            for (int i = 0; i < image.size(); i++) {

                File file = new File(FileUtils.getPath(context,image.get(i)));

                String filePath = file.getAbsolutePath();
                String[] arraySplitPath = filePath.split("\\.");

                // Tạo tên mới cho ảnh
                String nameImageNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];

               // MultipartBody: imageHistory
                RequestBody requestBodyImage    = RequestBody.create(MediaType.parse("multipart/form-data"),FileUtils.getFile(context,image.get(i)));

                MultipartBody.Part imageHistory = MultipartBody.Part.createFormData("image_Banner[]", nameImageNew,requestBodyImage);

                listMultipartImage.add(imageHistory);
            }

            //Tạo progress dialog
            ProgressDialog progress = new ProgressDialog(context);
            progress.setMessage("Vui lòng đợi...");
            progress.show();

            // Call API
            Common.api.insertBanner(listMultipartImage)
                    .enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                            ResponsePOST result = response.body();
                            assert result != null;

                            if (result.getStatus() == 1)
                            {
                                iBanner.successMessage(result.getMessage());
                            }
                            else
                            {
                                iBanner.failedMessage(result.getMessage());
                            }
                            progress.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                                iBanner.exception(t.getMessage());
                                progress.dismiss();
                        }
                    });
        }
    }


    // Xóa banner
    public synchronized void deleteBanner(Banner banner)
    {
        //Tạo progress dialog
        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Vui lòng đợi...");
        progress.show();

        Common.api.deleteBanner(banner.getIdBanner(),banner.getImage_Banner())
                .enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                        ResponsePOST result = response.body();
                        assert result != null;

                        if (result.getStatus() == 1)
                        {
                            iBanner.successMessage(result.getMessage());
                            getBanner();
                        }
                        else
                        {
                            iBanner.failedMessage(result.getMessage());
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                        iBanner.exception(t.getMessage());
                        progress.dismiss();
                    }
                });
    }

}
