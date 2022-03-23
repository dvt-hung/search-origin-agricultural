package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.model.Balance;
import com.example.apptxng.model.Category;
import com.example.apptxng.model.TypeFactory;

import java.util.List;

public class ChoiceType_Adapter extends RecyclerView.Adapter<ChoiceType_Adapter.ChoiceTypeViewHolder>{

    private List<?> list;
    private final IListenerChoiceType iListenerChoiceType;

    public interface IListenerChoiceType{
        void onClickChoiceType(Object obj);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<?> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    public ChoiceType_Adapter(IListenerChoiceType iListenerChoiceType) {
        this.iListenerChoiceType = iListenerChoiceType;
    }

    @NonNull
    @Override
    public ChoiceTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_choice_type,parent,false);
        return new ChoiceTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceTypeViewHolder holder, int position) {
        if (list.get(position).getClass() == Category.class)
        {
            Category category = (Category) list.get(position);

            holder.radioChoiceType.setText(category.getNameCategory());

            holder.radioChoiceType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListenerChoiceType.onClickChoiceType(category);
                }
            });
        }
        else if (list.get(position).getClass() == Balance.class)
        {
            Balance balance = (Balance) list.get(position);
            holder.radioChoiceType.setText(balance.getNameBalance());

            holder.radioChoiceType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListenerChoiceType.onClickChoiceType(balance);
                }
            });
        }
        else if (list.get(position).getClass() == TypeFactory.class)
        {
            TypeFactory typeFactory = (TypeFactory) list.get(position);
            holder.radioChoiceType.setText(typeFactory.getNameTypeFactory());

            holder.radioChoiceType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListenerChoiceType.onClickChoiceType(typeFactory);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list != null)
        {
            return list.size();
        }
        return 0;
    }

    public static class ChoiceTypeViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioChoiceType;
        public ChoiceTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            radioChoiceType = itemView.findViewById(R.id.radio_choice_type);
        }
    }
}
