package com.example.apptxng.bottom_dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Factory_Adapter;
import com.example.apptxng.model.Factory;
import com.example.apptxng.view.FactoryActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomDialogChoiceFactory extends BottomSheetDialogFragment {
    private final List<Factory> factoryList;
    private final Factory_Adapter.IListenerFactory iListenerFactory;

    public BottomDialogChoiceFactory(List<Factory> factoryList,Factory_Adapter.IListenerFactory iListenerFactory) {
        this.iListenerFactory = iListenerFactory;
        this.factoryList = factoryList;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choice_factory,null);
        dialog.setContentView(viewDialog);

        RecyclerView recycler_Choice_Factory = viewDialog.findViewById(R.id.recycler_Choice_Factory);

        // Layout manager của recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recycler_Choice_Factory.setLayoutManager(layoutManager);

        // Tạo adapter cho recycler view
        Factory_Adapter adapter = new Factory_Adapter(iListenerFactory);
        recycler_Choice_Factory.setAdapter(adapter);

        adapter.setFactoryList(factoryList);

        return dialog;
    }
}
