package com.example.apptxng.presenter;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.ResponsePOST;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setting_Farmer_Presenter {

    private final ISettingFarmer iSettingFarmer;

    public Setting_Farmer_Presenter(ISettingFarmer iSettingFarmer) {
        this.iSettingFarmer = iSettingFarmer;
    }

    public void change_Password(String passOld, String passNew, String passNewConfirm){
        /*
         * 1. Kiểm tra password cũ có đúng hay chưa
         * 2. Kiểm tra 2 mật khậu có trùng nhau không
         * 3. Kiểm tra độ dài mật khẩu
         * */

        if (!Common.currentUser.checkPasswordOld(passOld))
        {
            iSettingFarmer.inCorrectPassOld();
        }else if (passNew.length() < 6)
        {
            iSettingFarmer.inCorrectPassLength();
        }else if (!passNew.equals(passNewConfirm))
        {
            iSettingFarmer.inCorrectPassConfirm();
        } else
        {
            Common.api.change_password(Common.currentUser.getEmail(),passNew,Common.currentUser.getIdUser())
                    .enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                            ResponsePOST responsePOST = response.body();
                            assert responsePOST != null;
                            if (responsePOST.getStatus() == 1)
                            {
                                Common.currentUser.setPassWord(passNew);
                            }
                            iSettingFarmer.resultChangePass(responsePOST.getMessage());
                        }

                        @Override
                        public void onFailure(Call<ResponsePOST> call, Throwable t) {
                            iSettingFarmer.Exception(t.getMessage());
                        }
                    });
        }
    }

}
