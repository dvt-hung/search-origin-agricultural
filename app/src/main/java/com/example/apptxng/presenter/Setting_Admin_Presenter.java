package com.example.apptxng.presenter;

import com.example.apptxng.model.Common;
import com.example.apptxng.model.ResponsePOST;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setting_Admin_Presenter {

    private final ISettingAdmin iSettingAdmin;

    public Setting_Admin_Presenter(ISettingAdmin iSettingAdmin) {
        this.iSettingAdmin = iSettingAdmin;
    }

    public void change_Password(String passOld, String passNew,String passNewConfirm){
        /*
        * 1. Kiểm tra password cũ có đúng hay chưa
        * 2. Kiểm tra 2 mật khậu có trùng nhau không
        * 3. Kiểm tra độ dài mật khẩu
        * */

        if (!Common.currentUser.checkPasswordOld(passOld))
        {
            iSettingAdmin.inCorrectPassOld();
        }else if (passNew.length() < 6)
        {
            iSettingAdmin.inCorrectPassLength();
        }else if (!passNew.equals(passNewConfirm))
        {
            iSettingAdmin.inCorrectPassConfirm();
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
                            iSettingAdmin.resultChangePass(responsePOST.getMessage());
                        }

                        @Override
                        public void onFailure(Call<ResponsePOST> call, Throwable t) {
                            iSettingAdmin.Exception(t.getMessage());
                        }
                    });
        }
    }
}
