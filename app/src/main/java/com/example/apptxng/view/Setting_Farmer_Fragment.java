package com.example.apptxng.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apptxng.R;
import com.example.apptxng.model.Common;


public class Setting_Farmer_Fragment extends Fragment {

    private View viewSetting;
    private ImageView img_Farmer_Setting;
    private TextView txt_NameFarm_Setting;
    private LinearLayout layout_Info_Setting_Farmer,layout_Password_Setting_Farmer, layout_Factory_Setting_Farmer, layout_LogOut_Setting_Farmer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewSetting =  inflater.inflate(R.layout.fragment_setting__farmer_, container, false);

        // initView: Ánh xạ view Setting Fragment
        initView();

        return viewSetting;
    }

    private void initView() {
        img_Farmer_Setting                  = viewSetting.findViewById(R.id.img_Farmer_Setting);
        txt_NameFarm_Setting                = viewSetting.findViewById(R.id.txt_NameFarm_Setting);
        layout_Info_Setting_Farmer          = viewSetting.findViewById(R.id.layout_Info_Setting_Farmer);
        layout_Password_Setting_Farmer      = viewSetting.findViewById(R.id.layout_Password_Setting_Farmer);
        layout_Factory_Setting_Farmer       = viewSetting.findViewById(R.id.layout_Factory_Setting_Farmer);
        layout_LogOut_Setting_Farmer        = viewSetting.findViewById(R.id.layout_LogOut_Setting_Farmer);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Gán giá trị ban đầu cho ảnh và tên vườn
        displayValue();

        // initEvent: Các sự kiện trong Setting Fragment
        initEvents();
    }

    private void displayValue() {
        // Gán giá trị cho Image của Farmer
        Glide.with(this).load(Common.currentUser.getImage()).error(R.drawable.logo).into(img_Farmer_Setting);

        // Gán giá trị cho tên của vườn
        if (Common.currentUser.getNameFarm() == null)
        {
            txt_NameFarm_Setting.setText(R.string.title_error_empty_user);
        }
        else
        {
            txt_NameFarm_Setting.setText(Common.currentUser.getNameFarm());
        }
    }

    private void initEvents() {
        /*
        * 1. layout_Info_Setting_Farmer: Chuyển sang activity thay đổi thông tin
        * 2. layout_Password_Setting_Farmer: Mở dialog đổi password
        * 3. layout_Factory_Setting_Farmer: Mở sang activity Factory
        * 4. layout_LogOut_Setting_Farmer: Mở dialog xác nhận
        * */

        // 1. Info Setting: Thay đổi thông tin
        layout_Info_Setting_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(viewSetting.getContext(),ChangeInfoFarmerActivity.class));
            }
        });

        // 2. Change Password: Đổi mật khẩu
        layout_Password_Setting_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 3. Factory Setting: Danh sách các liên kết với vườn
        layout_Factory_Setting_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 4. Log out: Đăng xuất
        layout_LogOut_Setting_Farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}