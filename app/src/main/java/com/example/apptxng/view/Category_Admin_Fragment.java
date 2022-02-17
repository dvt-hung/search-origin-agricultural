package com.example.apptxng.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptxng.R;


public class Category_Admin_Fragment extends Fragment {

    private View view;
    private RecyclerView recycler_Category_Admin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_category__admin_, container, false);

        // Init view
        recycler_Category_Admin = view.findViewById(R.id.recycler_Category_Admin);

        return view;
    }
}