package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.FileUtils;
import com.example.apptxng.model.History;
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

public class History_Presenter {

    private final IHistory iHistory;
    private final Context context;

    public History_Presenter(IHistory iHistory, Context context) {
        this.iHistory = iHistory;
        this.context = context;
    }


    // Thêm nhật ký của sản phẩm
    public synchronized void InsertHistory(History history, List<Uri> image, int change)
    {
        // Kiểm tra dữ liệu
        if (image == null || history.getFactory() == null || history.getDescriptionHistory().isEmpty())
        {
            iHistory.emptyValue();
        }
        else
        {
            // Request: idHistory
            RequestBody idHistory           = RequestBody.create(MediaType.parse("multipart/form-data"), history.getIdHistory());

            // Request: idProduct
            RequestBody idProduct           = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(history.getIdProduct()));

            // Request: idFactory
            RequestBody idFactory           = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(history.getFactory().getIdFactory()));

            // Request: idCurrent
            RequestBody idCurrent           = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(history.getFactoryReceive().getIdUser()));

            // Request: idAuthor
            RequestBody idAuthor           = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(history.getIdAuthor()));

            // Request: idAuthor
            RequestBody changeFactory           = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(change));

            // Request: descriptionHistory
            RequestBody descriptionHistory  = RequestBody.create(MediaType.parse("multipart/form-data"), history.getDescriptionHistory());

            // Request: dateHistory
            RequestBody dateHistory         = RequestBody.create(MediaType.parse("multipart/form-data"), history.getDateHistory());

            // Request: idFactoryReceive
            RequestBody idFactoryReceive    = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(history.getFactoryReceive().getIdFactory()));

            // Request: idFactoryReceive
            RequestBody idTypeFactory    = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(history.getFactoryReceive().getType_factory().getIdTypeFactory()));


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

                MultipartBody.Part imageHistory = MultipartBody.Part.createFormData("imageHistory[]", nameImageNew,requestBodyImage);

                listMultipartImage.add(imageHistory);
            }

            //Tạo progress dialog
            ProgressDialog progress = new ProgressDialog(context);
            progress.setMessage("Vui lòng đợi...");
            progress.show();

            // Gọi đến API
            Common.api.insertHistory(idHistory,idProduct,idFactory,idCurrent,idAuthor,changeFactory ,descriptionHistory,dateHistory,idFactoryReceive,idTypeFactory,listMultipartImage)
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
    public synchronized void loadHistory(String idProduct)
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


    // Cập nhật mô tả của nhất ký
    public synchronized void UpdateDesHistory(String idHistory, String descriptionHistory)
    {
        if (descriptionHistory.isEmpty())
        {
            iHistory.emptyValue();
        }
        else
        {
            // Tạo progress dialog
            ProgressDialog dialog = Common.createProgress(context);
            dialog.show();
            Common.api.updateDesHistory(idHistory,descriptionHistory)
                    .enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                            ResponsePOST result = response.body();
                            assert  result != null;

                            if (result.getStatus() == 1)
                            {
                                iHistory.successMessage(result.getMessage());
                            }
                            else
                            {
                                iHistory.failedMessage(result.getMessage());
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                                iHistory.exceptionMessage(t.getMessage());
                                dialog.dismiss();

                        }
                    });
        }
    }


}
