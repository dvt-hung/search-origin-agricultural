package com.example.apptxng.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptxng.R;
import com.example.apptxng.adapter.History_Adapter;
import com.example.apptxng.model.Common;
import com.example.apptxng.model.History;
import com.example.apptxng.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class History_Product_Customer_Fragment extends Fragment implements History_Adapter.IListenerHistory {

    private static final String KEY = "product";
    private final Product productTemp;
    private History_Adapter historyAdapter;
    private List<History> historyList;
    public static boolean reload;


    public History_Product_Customer_Fragment(Product product) {
        this.productTemp = product;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        View view =  inflater.inflate(R.layout.fragment_history__product__customer_, container, false);
        initView(view);

        getListHistory();
        return view;
    }

    private void initView(View view) {
        RecyclerView  recycler_History_Product = view.findViewById(R.id.recycler_History_Product);

        // Adapter của recycler
        historyAdapter = new History_Adapter(this);
        recycler_History_Product.setAdapter(historyAdapter);

        // Layout manager của recycler
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(),RecyclerView.VERTICAL,false);
        recycler_History_Product.setLayoutManager(layoutManager);


    }

    private void getListHistory() {
        // Progress dialog
        ProgressDialog dialog = Common.createProgress(requireActivity());
        dialog.show();

        Common.api.getHistory(productTemp.getIdProduct())
                .enqueue(new Callback<List<History>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<History>> call, @NonNull Response<List<History>> response) {
                        historyList = response.body();
                        historyAdapter.setHistoryList(historyList);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<History>> call, @NonNull Throwable t) {
                        dialog.dismiss();
                    }
                });
    }


    @Override
    public void onClickHistoryItem(History history) {
        // Chuyển đối tượng history sang activity detail
        Bundle bundleHistory = new Bundle();
        bundleHistory.putSerializable("history", history);
        Intent intent = new Intent(requireActivity(), DetailHistoryActivity.class);
        intent.putExtras(bundleHistory);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (reload)
        {
            requireActivity().finish();
        }
    }
}