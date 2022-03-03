package com.example.apptxng.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.apptxng.R;


public class Setting_Admin_Fragment extends Fragment {


    private View viewSetting;
    private LinearLayout layout_Scale_Setting_Admin, layout_Banner_Setting_Admin, layout_changePassword_Setting_Admin, layout_LogOut_Setting_Admin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewSetting =  inflater.inflate(R.layout.fragment_setting__admin_, container, false);

        // init view
        initView(viewSetting);
        return viewSetting;
    }

    // Init view: Ánh xạ view của setting fragment
    private void initView(View viewSetting) {
        layout_Scale_Setting_Admin          = viewSetting.findViewById(R.id.layout_Scale_Setting_Admin);
        layout_Banner_Setting_Admin         = viewSetting.findViewById(R.id.layout_Banner_Setting_Admin);
        layout_changePassword_Setting_Admin = viewSetting.findViewById(R.id.layout_changePassword_Setting_Admin);
        layout_LogOut_Setting_Admin         = viewSetting.findViewById(R.id.layout_LogOut_Setting_Admin);
    }


    // Event: Chọn các option tại setting fragment
    @Override
    public void onResume() {
        super.onResume();

        // Scale: Click layout Scale - Chuyển qua activity quản lý các đơn vị tính của ứng dụng
        layout_Scale_Setting_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(viewSetting.getContext(), Balance_Admin_Activity.class));
            }
        });


    }
}