package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.User;

import java.util.List;

public class Employee_Adapter extends RecyclerView.Adapter<Employee_Adapter.EmployeeViewHolder> {

    private List<User> employees;

    private final IEmployeeListener iEmployeeListener;
    public interface IEmployeeListener{
        void onClickEmployee (User employee);
    }

    public Employee_Adapter(IEmployeeListener iEmployeeListener) {
        this.iEmployeeListener = iEmployeeListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEmployees(List<User> list)
    {
        this.employees = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee,parent,false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        User employee = employees.get(position);

        if (employee != null)
        {
            // Name Employee
            Common.displayValueTextView(holder.txt_Name_Employee,employee.getName());

            // Email Employee
            Common.displayValueTextView(holder.txt_Email_Employee,employee.getEmail());

            // Phone Employee
            Common.displayValueTextView(holder.txt_Phone_Employee,employee.getPhone());

            // Click Layout
            holder.layout_Employee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iEmployeeListener.onClickEmployee(employee);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(employees != null)
        {
            return employees.size();
        }
        return 0;
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private final TextView txt_Name_Employee;
        private final TextView txt_Email_Employee;
        private final TextView txt_Phone_Employee;
        private final RelativeLayout layout_Employee;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_Name_Employee           = itemView.findViewById(R.id.txt_Name_Employee);
            txt_Email_Employee          = itemView.findViewById(R.id.txt_Email_Employee);
            txt_Phone_Employee          = itemView.findViewById(R.id.txt_Phone_Employee);
            layout_Employee             = itemView.findViewById(R.id.layout_Employee);
        }
    }
}
