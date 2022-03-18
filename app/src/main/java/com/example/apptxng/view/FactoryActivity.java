package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.apptxng.R;
import com.example.apptxng.adapter.Factory_Adapter;
import com.example.apptxng.model.Factory;
import com.example.apptxng.presenter.Factory_Presenter;
import com.example.apptxng.presenter.IFactory;

import java.util.ArrayList;
import java.util.List;

public class FactoryActivity extends AppCompatActivity implements Factory_Adapter.IListenerFactory, IFactory {

    private ImageView img_Close_Factory,img_Add_Factory,img_Filter,img_Clear_Filter;
    private RecyclerView recycler_Factory;
    private Factory_Adapter factoryAdapter;
    private Factory_Presenter presenter;
    private List<Factory> factories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory);

        // Ánh xạ view
        initView();
    }

    private void initView() {
        img_Close_Factory           = findViewById(R.id.img_Close_Factory);
        img_Add_Factory             = findViewById(R.id.img_Add_Factory);
        recycler_Factory            = findViewById(R.id.recycler_Factory);
        img_Filter                  = findViewById(R.id.img_Filter);
        img_Clear_Filter            = findViewById(R.id.img_Clear_Filter);
        factories                   = new ArrayList<>();
        // Presenter
        presenter                   = new Factory_Presenter(this,this);

        // Adapter của recycler view
        factoryAdapter              = new Factory_Adapter(this);
        recycler_Factory.setAdapter(factoryAdapter);

        // Layout manager của recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Factory.setLayoutManager(layoutManager);


    }


    @Override
    protected void onResume() {
        super.onResume();

        // Get factory: Lấy ra danh sách factory
        presenter.getFactory();

        // init events: Khai báo các event trong activity
        initEvents();
    }

    private void initEvents() {

        // 1. Close Button: Đóng activity
        img_Close_Factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });

        // 2. Add Button: Mở dialog thêm liên kết
        img_Add_Factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 3. Filter Button: Mở dialog lựa chọn các loại cơ sở sau đó lọc
        img_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_Clear_Filter.setVisibility(View.VISIBLE);
                List<Factory> listTemp = new ArrayList<>();
                for (Factory f : factories)
                {
                    if (f.getType_factory().getIdTypeFactory() == 2)
                    {
                        listTemp.add(f);
                    }
                }
                factoryAdapter.setFactoryList(listTemp);
            }
        });

        // 4. Clear Filter Button: Xóa trạng thái lọc danh sách
        img_Clear_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                factoryAdapter.setFactoryList(factories);
            }
        });
    }


    // OVERRIDE METHOD: interface IListenerFactory
    @Override
    public void onClickItemFactory(Factory factory) {

    }

    @Override
    public void getFactory(List<Factory> list) {
            factories = list;
            factoryAdapter.setFactoryList(factories);
    }

    @Override
    public void Exception(String message) {

    }
}