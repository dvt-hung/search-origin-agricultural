package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apptxng.R;

public class InputValueProductActivity extends AppCompatActivity {

    private ImageView img_Back_Input,img_Confirm_Input;
    private EditText edt_Input;

    private final String TITLE_INGREDIENT_I   = "TITLE_INGREDIENT_I";
    private final String TITLE_USE_I          = "TITLE_USE_I";
    private final String TITLE_GUIDE_I        = "TITLE_GUIDE_I";
    private final String TITLE_CONDITION_I    = "TITLE_CONDITION_I";

    private final String TITLE_INGREDIENT_U   = "TITLE_INGREDIENT_U";
    private final String TITLE_USE_U          = "TITLE_USE_U";
    private final String TITLE_GUIDE_U        = "TITLE_GUIDE_U";
    private final String TITLE_CONDITION_U    = "TITLE_CONDITION_U";


    private String receiveCode, value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_value_product);

        // Ánh xạ view
        img_Back_Input      = findViewById(R.id.img_Back_Input);
        img_Confirm_Input   = findViewById(R.id.img_Confirm_Input);
        edt_Input           = findViewById(R.id.edt_Input);

        // Nhận code
        receiveCode = getIntent().getStringExtra("code");
        value       = getIntent().getStringExtra("value");
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Gán giá trị ban đầu cho edt
        edt_Input.setText(value);


        // 1. Close button
        img_Back_Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 2. Confirm button
        img_Confirm_Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnResult();
            }
        });
    }

    private void returnResult() {
        String value = edt_Input.getText().toString().trim();
        // Check code
        switch (receiveCode)
        {
            case TITLE_INGREDIENT_I:
            {
                returnValue(InsertProductActivity.txt_Result_IngredientProduct, value);
                break;
            }
            case TITLE_USE_I:
            {
                returnValue(InsertProductActivity.txt_Result_UseProduct, value);
                break;

            }
            case TITLE_GUIDE_I:
            {
                returnValue(InsertProductActivity.txt_Result_GuideProduct, value);
                break;

            }
            case TITLE_CONDITION_I:
            {
                returnValue(InsertProductActivity.txt_Result_ConditionProduct, value);
                break;
            }

            case TITLE_INGREDIENT_U:
            {
                returnValue(UpdateProductActivity.txt_Result_IngredientProduct, value);
                break;
            }
            case TITLE_USE_U:
            {
                returnValue(UpdateProductActivity.txt_Result_UseProduct, value);
                break;

            }
            case TITLE_GUIDE_U:
            {
                returnValue(UpdateProductActivity.txt_Result_GuideProduct, value);
                break;

            }
            case TITLE_CONDITION_U:
            {
                returnValue(UpdateProductActivity.txt_Result_ConditionProduct, value);
                break;
            }
        }
        finish();
    }
    private void returnValue(TextView view, String value)
    {
        view.setVisibility(View.VISIBLE);
        view.setText(value);
    }

}