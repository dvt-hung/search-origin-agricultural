package com.example.apptxng.bottom_dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.ChoiceType_Adapter;
import com.example.apptxng.adapter.Employee_Adapter;
import com.example.apptxng.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BottomDialogEmployee extends BottomSheetDialogFragment {

    private final List<User> employeeList;
    private final Employee_Adapter.IEmployeeListener iEmployeeListener;

    public BottomDialogEmployee(List<User> userList, Employee_Adapter.IEmployeeListener iEmployeeListener) {
        this.employeeList = userList;
        this.iEmployeeListener = iEmployeeListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog =  (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        @SuppressLint("InflateParams")
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom_choice_type,null);
        dialog.setContentView(viewDialog);

        RecyclerView recycler_Choice_Employee   = viewDialog.findViewById(R.id.recycler_Choice_Type);

        // Layout manager của recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recycler_Choice_Employee.setLayoutManager(layoutManager);

        // Adapter cho recycler view
        Employee_Adapter employeeAdapter = new Employee_Adapter(iEmployeeListener);
        recycler_Choice_Employee.setAdapter(employeeAdapter);


        employeeAdapter.setEmployees(employeeList);
        return  dialog;
    }
}
