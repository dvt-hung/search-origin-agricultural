package com.example.apptxng.bottom_dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.model.TypeFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomDialogTypeFactory extends BottomSheetDialogFragment  {

    private final ChoiceType_Adapter.IListenerChoiceType iListenerChoiceType;
    private final List<?> list;

    public BottomDialogTypeFactory(List<?> list, ChoiceType_Adapter.IListenerChoiceType iListener) {
        this.iListenerChoiceType = iListener;
        this.list = list;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog =  (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        @SuppressLint("InflateParams")
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom_choice_type,null);
        dialog.setContentView(viewDialog);

        TextView txt_Title_Choice_Type      = viewDialog.findViewById(R.id.txt_Title_Choice_Type);
        RecyclerView recycler_Choice_Type   = viewDialog.findViewById(R.id.recycler_Choice_Type);

        // Set title dialog
        txt_Title_Choice_Type.setText(R.string.filter_type_factory);

        // Layout manager của recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recycler_Choice_Type.setLayoutManager(layoutManager);

        // Adapter cho recycler view
        ChoiceType_Adapter adapter = new ChoiceType_Adapter(iListenerChoiceType);
        recycler_Choice_Type.setAdapter(adapter);

        adapter.setList(list);

        return  dialog;
    }
}
