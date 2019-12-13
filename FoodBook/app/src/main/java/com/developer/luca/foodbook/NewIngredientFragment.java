package com.developer.luca.foodbook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Classe che gestisce il frammento di un singolo ingrediente
 * nel processo di inseriemento degli ingredienti di una nuova ricetta.
 */
public class NewIngredientFragment extends Fragment {

    private View view;
    private Activity mainActivity;

    private EditText ingredientName_editText;
    private EditText ingredientQuantity_editText;
    private Spinner unit_spinner;

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

        unit_spinner = view.findViewById(R.id.unit_spinner);

        String[] choices = {getString(R.string.gr), getString(R.string.ml), getString(R.string.unit) };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainActivity, R.layout.support_simple_spinner_dropdown_item, choices);

        unit_spinner.setAdapter(adapter);

        unit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            // Imposta l'unità di misura del ingrediente in base al elemento selezionato
            // Viene chiamato anche appena viene creato
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: ingredient.setUnit(Ingredient.Unit.GR);
                        break;
                    case 1: ingredient.setUnit(Ingredient.Unit.ML);
                        break;
                    case 2: ingredient.setUnit(Ingredient.Unit.UNIT);
                        break;
                }
                hideSoftKeyboard(mainActivity, view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * @return l'ingrediente rappresentato dal frammento.
     */
    public Ingredient getIngredient(){
        ingredient.setIngredient(ingredientName_editText.getText().toString().trim());

        try{
            ingredient.setQuantity(Integer.parseInt(ingredientQuantity_editText.getText().toString()));
        } catch (Exception e){
            ingredient.setQuantity(0);
        }

        return ingredient;
    }


    /**
     * Per avere un flow di inserimento più fluido nascondo la tastiera quando viene selezionato un pulsante
     */
    public static void hideSoftKeyboard (Activity activity, View view){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}
