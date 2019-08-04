package com.developer.luca.foodbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NewIngredientFragment extends Fragment {

    private Activity mainActivity;
    private View view;

    private EditText ingredientName_editText;
    private EditText ingredientQuantity_editText;
    private Button unit_button;

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
        unit_button = view.findViewById(R.id.unit_button);
        unit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (unit_button.getText().toString()){
                    case "UNIT":
                        unit_button.setText("GR");
                        ingredient.setUnit(Ingredient.Unit.GR);
                        break;
                    case "GR":
                        unit_button.setText("ML");
                        ingredient.setUnit(Ingredient.Unit.ML);
                        break;
                    case "ML":
                        unit_button.setText("UNIT");
                        ingredient.setUnit(Ingredient.Unit.UNIT);
                }
            }
        });
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