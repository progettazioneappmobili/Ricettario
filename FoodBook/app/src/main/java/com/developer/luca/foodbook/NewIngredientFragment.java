package com.developer.luca.foodbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NewIngredientFragment extends Fragment {

    private Activity mainActivity;
    private View view;

    private EditText ingredientName_editText;
    private EditText ingredientQuantity_editText;

    private Ingredient ingredient;

    public static NewIngredientFragment newInstance(){
        return new NewIngredientFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_new_ingredient, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mainActivity = getActivity();
        ingredient = new Ingredient();

        ingredientName_editText = view.findViewById(R.id.ingredientName_editText);
        ingredientQuantity_editText = view.findViewById(R.id.ingredientQuantity_editText);
    }

    public Ingredient getIngredient(){
        ingredient.setIngredient(ingredientName_editText.getText().toString().trim());

        try{
            ingredient.setQuantity(Integer.parseInt(ingredientQuantity_editText.getText().toString()));
        } catch (Exception e){
            ingredient.setQuantity(0);
        }


        return ingredient;
    }
}
