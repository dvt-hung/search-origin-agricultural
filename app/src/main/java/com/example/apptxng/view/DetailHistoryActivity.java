package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.apptxng.R;
import com.example.apptxng.model.History;

public class DetailHistoryActivity extends AppCompatActivity {

    private History historyTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        // Nhận history
//        Bundle receiveHistory = getIntent().getExtras();
//        historyTemp = (History) receiveHistory.getSerializable("history");
    }
}