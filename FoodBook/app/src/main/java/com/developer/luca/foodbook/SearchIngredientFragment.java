package com.developer.luca.foodbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class SearchIngredientFragment extends Fragment {

    private View view;

    private EditText ingredientName_editText;

    public static SearchIngredientFragment newInstance(){
        return new SearchIngredientFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search_ingredient, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ingredientName_editText = view.findViewById(R.id.ingredientName_editText);
    }

    public String getText(){
        return ingredientName_editText.getText().toString().trim();
    }
}
