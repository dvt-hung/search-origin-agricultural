package com.example.apptxng.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.ResponsePOST;
import com.example.apptxng.model.User;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeInfo_Farmer_Presenter {

    private final IChangeInfo_Farmer iChangeInfo_farmer;
    private final Context context;


    public ChangeInfo_Farmer_Presenter(IChangeInfo_Farmer iChangeInfo_farmer, Context context) {
        this.iChangeInfo_farmer = iChangeInfo_farmer;
        this.context = context;
    }

    public void changeInfo_Farmer(User user, Uri uriImageNew)
    {

        RequestBody imgOld = null; // request body ảnh cũ
        RequestBody requestBodyImage = null; // request body của ảnh mới user
        MultipartBody.Part requestPartImage = null; // multipart của ảnh mới user

        if (user.checkValueString(user.getName()) ||
                user.checkValueString(user.getPhone()) || user.checkValueString(user.getAddress()))
        {
            iChangeInfo_farmer.emptyValue();
        }
        else if (uriImageNew != null)
        {
            File file = new File(Common.getRealPathFromURI(uriImageNew,context));

            String filePath = file.getAbsolutePath();
            String[] arraySplitPath = filePath.split("\\.");

            String nameProductNew = arraySplitPath[0] + System.currentTimeMillis() + "." + arraySplitPath[1];

            requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"),file);
            requestPartImage = MultipartBody.Part.createFormData("image", nameProductNew,requestBodyImage);

            if (user.getImage() != null)
            {
                imgOld = RequestBody.create(MediaType.parse("multipart/form-data"),user.getImage());
            }
        }
            RequestBody name                = RequestBody.create(MediaType.parse("multipart/form-data"),Common.checkStringValue(user.getName()));
            RequestBody address             = RequestBody.create(MediaType.parse("multipart/form-data"),Common.checkStringValue(user.getAddress()));
            RequestBody email             = RequestBody.create(MediaType.parse("multipart/form-data"),Common.checkStringValue(user.getEmail()));
            RequestBody idUser              = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(user.getIdUser()));

            // Progress dialog
            ProgressDialog progressDialog = Common.createProgress(context);
            progressDialog.show();

            Common.api.changeInfo(idUser,name,address,email,imgOld,requestPartImage)
                    .enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponsePOST> call, @NonNull Response<ResponsePOST> response) {
                            ResponsePOST result = response.body();

                            assert result != null;
                            if (result.getStatus() == 1)
                            {
                                iChangeInfo_farmer.success(result.getMessage());
                            }
                            else
                            {
                                iChangeInfo_farmer.failed(result.getMessage());
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponsePOST> call, @NonNull Throwable t) {
                            iChangeInfo_farmer.Exception(t.getMessage());
                            progressDialog.dismiss();

                        }
                    });
    }
}
