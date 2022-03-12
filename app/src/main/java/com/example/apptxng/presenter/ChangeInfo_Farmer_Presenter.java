package com.example.apptxng.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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

        if (user.checkValueString(user.getName()) || user.checkValueString(user.getNameFarm()) ||
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
            RequestBody name                = RequestBody.create(MediaType.parse("multipart/form-data"),user.getName());
            RequestBody nameFarm            = RequestBody.create(MediaType.parse("multipart/form-data"),user.getNameFarm());
            RequestBody phone               = RequestBody.create(MediaType.parse("multipart/form-data"),user.getPhone());
            RequestBody address             = RequestBody.create(MediaType.parse("multipart/form-data"),user.getAddress());
            RequestBody idUser              = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(user.getIdUser()));

            Common.api.changeInfo(idUser,name,phone,nameFarm,address,imgOld,requestPartImage)
                    .enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                            ResponsePOST result = response.body();

                            assert result != null;
                            if (result.getStatus() == 1)
                            {
                                iChangeInfo_farmer.success(result.getMessage());
                                Common.reloadCurrentUser();
                            }
                            else
                            {
                                iChangeInfo_farmer.failed(result.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponsePOST> call, Throwable t) {
                            iChangeInfo_farmer.Exception(t.getMessage());
                        }
                    });
    }
}
