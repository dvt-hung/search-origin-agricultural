package com.example.apptxng.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptxng.R;
import com.example.apptxng.model.Factory;

import java.util.List;

public class Factory_Adapter extends RecyclerView.Adapter<Factory_Adapter.FactoryViewHolder> {

    private List<Factory> factoryList;
    private final IListenerFactory iListenerFactory;

    public Factory_Adapter(IListenerFactory iListenerFactory) {
        this.iListenerFactory = iListenerFactory;
    }

    public interface IListenerFactory{
        void onClickItemFactory(Factory factory);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setFactoryList(List<Factory> list)
    {
        this.factoryList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FactoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_factory,parent,false);
        return new FactoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FactoryViewHolder holder, int position) {
        Factory factory = factoryList.get(position);

        if (factory != null)
        {
            holder.txt_Name_Factory.setText(factory.getNameFactory());
            holder.txt_Address_Factory.setText(factory.getAddressFactory());
            holder.txt_Phone_Factory.setText(factory.getPhoneFactory());
            holder.txt_Name_TypeFactory.setText(factory.getType_factory().getNameTypeFactory());
            holder.layout_Factory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iListenerFactory.onClickItemFactory(factory);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (factoryList != null)
        {
            return factoryList.size();
        }
        return 0;
    }

    public static class FactoryViewHolder extends RecyclerView.ViewHolder {
        private final CardView layout_Factory;
        private final TextView txt_Name_Factory, txt_Address_Factory, txt_Phone_Factory, txt_Name_TypeFactory;
        public FactoryViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_Factory              = itemView.findViewById(R.id.layout_Factory);
            txt_Name_Factory            = itemView.findViewById(R.id.txt_Name_Factory);
            txt_Address_Factory         = itemView.findViewById(R.id.txt_Address_Factory);
            txt_Phone_Factory           = itemView.findViewById(R.id.txt_Phone_Factory);
            txt_Name_TypeFactory        = itemView.findViewById(R.id.txt_Name_TypeFactory);
        }
    }
}
