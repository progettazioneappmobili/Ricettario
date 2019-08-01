package com.developer.luca.foodbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Insert2Fragment extends Fragment {

    private Activity mainActivity;
    private View view;
    private Recipe recipe;

    private LinearLayout ingredients_linearLayout;
    private ImageButton addIngredient_imageButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_insert2, container, false);

        return view;
    }


    private static boolean firstTime = true;

    @Override
    public void onStart() {
        super.onStart();

        mainActivity = getActivity();
        recipe = ((InsertActivity) getActivity()).recipe;

        ingredients_linearLayout = view.findViewById(R.id.ingredients_linearLayout);

        addIngredient_imageButton = view.findViewById(R.id.addIngredient_imageButton);
        addIngredient_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientFragment();
            }
        });

        // Inserisci primo ingrediente
        if (firstTime){
            addIngredientFragment();
            firstTime = false;
        }
    }

    public void addIngredientFragment(){
        getFragmentManager().beginTransaction().add(ingredients_linearLayout.getId(), NewIngredientFragment.newInstance(), "INGREDIENT_FRAGMENT_" + ingredients_linearLayout.getChildCount()).commit();
    }

    public void setIngredients(){
        recipe.clearIngredients();
        int t = ingredients_linearLayout.getChildCount();
        for (int i = 0; i < t; i++) {
            NewIngredientFragment nif = (NewIngredientFragment) getFragmentManager().findFragmentByTag("INGREDIENT_FRAGMENT_"+i);
            Ingredient ing = nif.getIngredient();

            if (!ing.getIngredient().equals("")){
                Log.d("SET INGREDIENTS", "setIngredients: "+ing.getIngredient());
                recipe.addIngredient(ing);
            }
        }
    }
}
