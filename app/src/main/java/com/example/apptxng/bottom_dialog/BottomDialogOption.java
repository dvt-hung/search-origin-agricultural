package com.example.apptxng.bottom_dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.apptxng.R;
import com.example.apptxng.model.History;
import com.example.apptxng.model.ImageHistory;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomDialogOption extends BottomSheetDialogFragment {

    private final   IDialogOptionListener listener;

    public interface IDialogOptionListener{
        void deleteHistory();
        void imageHistory();
    }

    public BottomDialogOption(IDialogOptionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_detail_history,null);
        dialog.setContentView(viewDialog);

        // init view
        TextView txt_Image_Detail       = viewDialog.findViewById(R.id.txt_Image_Detail);
        TextView txt_Info_Detail        = viewDialog.findViewById(R.id.txt_Info_Detail);
        TextView txt_Delete_Detail      = viewDialog.findViewById(R.id.txt_Delete_Detail);
        Button btn_Cancel_Detail        = viewDialog.findViewById(R.id.btn_Cancel_Detail);


        // 1. txt_Image_Detail: Chuyển sang activity quản lý danh sách hình ảnh
        txt_Image_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.imageHistory();
                dialog.dismiss();
            }
        });

        // 2. txt_Info_Detail: Chuyển sang activity quản lý thông tin khác
        txt_Info_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 3.txt_Delete_Detail:  Mở dialog xác nhận xóa
        txt_Delete_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.deleteHistory();
                dialog.dismiss();
            }
        });

        // 4. btn_Cancel_Detail: Tắt dialog
        btn_Cancel_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
