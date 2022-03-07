package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.TypeFactory_Admin_Adapter;
import com.example.apptxng.model.TypeFactory;
import com.example.apptxng.presenter.ITypeFactoryAdmin;
import com.example.apptxng.presenter.TypeFactory_Admin_Presenter;

import java.util.List;

public class TypeFactory_Admin_Activity extends AppCompatActivity implements TypeFactory_Admin_Adapter.IListenerLinked, ITypeFactoryAdmin {

    private ImageView img_Back_Linked_Admin, img_Add_Linked_Admin;
    private RecyclerView recycler_Linked_Admin;
    private TypeFactory_Admin_Adapter typeFactoryAdminAdapter;
    private TypeFactory_Admin_Presenter typeFactoryPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typefactory_admin);

        // Init view: Ảnh xạ view
        initView();

        // Tạo layout manager cho recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recycler_Linked_Admin.setLayoutManager(layoutManager);

        // Tạo item decoration cho recycler view
        RecyclerView.ItemDecoration itemDecoration  = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recycler_Linked_Admin.addItemDecoration(itemDecoration);

        // Gán adapter cho recycler
        recycler_Linked_Admin.setAdapter(typeFactoryAdminAdapter);

        // Load danh sách liên kết
        loadLinked();
    }

    // Hiển thị danh sách liên kết
    private void loadLinked() {
        typeFactoryPresenter.loadLinked();
    }


    @Override
    protected void onResume() {
        super.onResume();

        /*
        * 1. Back Image: tắt activity Linked_Admin_Activity
        * 2. Add Image: Mở dialog thêm liên kết
        * */

        // 1. Back Image
        img_Back_Linked_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Add Image: Mở dialog
        img_Add_Linked_Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    // initView: Ảnh xạ view
    private void initView() {
        img_Back_Linked_Admin       = findViewById(R.id.img_Back_TypeFactory_Admin);
        img_Add_Linked_Admin        = findViewById(R.id.img_Add_TypeFactory_Admin);
        recycler_Linked_Admin       = findViewById(R.id.recycler_TypeFactory_Admin);
        typeFactoryAdminAdapter     = new TypeFactory_Admin_Adapter(this);
        typeFactoryPresenter        = new TypeFactory_Admin_Presenter(this);
    }


    // Override method: viết lại phương thức của IListenerLinked
    @Override
    public void onClickLinked(TypeFactory typeFactory) {

    }

    // Override method: viết lại phương thức của ILinkedAdmin
    @Override
    public void getLinked(List<TypeFactory> list) {
        typeFactoryAdminAdapter.setLinkList(list);
    }

    @Override
    public void Exception(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}