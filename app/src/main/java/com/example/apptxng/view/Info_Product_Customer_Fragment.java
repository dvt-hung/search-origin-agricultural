package com.example.apptxng.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apptxng.R;
import com.example.apptxng.model.Product;

public class Info_Product_Customer_Fragment extends Fragment {

    private final Product productTemp;
    private TextView txt_DesProduct, txt_IngredientProduct,txt_UseProduct, txt_GuideProduct, txt_ConditionProduct;

    public Info_Product_Customer_Fragment(Product productTemp) {
        this.productTemp = productTemp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_info__product__customer_, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        txt_DesProduct              = view.findViewById(R.id.txt_DesProduct);
        txt_IngredientProduct       = view.findViewById(R.id.txt_IngredientProduct);
        txt_UseProduct              = view.findViewById(R.id.txt_UseProduct);
        txt_GuideProduct            = view.findViewById(R.id.txt_GuideProduct);
        txt_ConditionProduct        = view.findViewById(R.id.txt_ConditionProduct);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (productTemp != null)
        {
            displayValue();
        }
    }

    private void displayValue() {
        // Mô tả
        checkValueEmpty(txt_DesProduct,productTemp.getDescriptionProduct());

        // Thành phần
        checkValueEmpty(txt_IngredientProduct, productTemp.getIngredientProduct());

        // Công dụng
        checkValueEmpty(txt_UseProduct, productTemp.getUseProduct());

        // Hướng dẫn sử dụng
        checkValueEmpty(txt_GuideProduct, productTemp.getGuideProduct());

        // Điều kiện bảo quản
        checkValueEmpty(txt_ConditionProduct, productTemp.getConditionProduct());

    }

    private void checkValueEmpty(TextView textView, String val)
    {
        if (val == null ||val.equals(" ") || val.isEmpty())
        {
            textView.setText(R.string.title_error_empty_user);
        }
        else
        {
            textView.setText(val);
        }
    }


}