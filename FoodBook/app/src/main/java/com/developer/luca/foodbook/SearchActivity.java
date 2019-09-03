package com.developer.luca.foodbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che permette di cercare una ricetta tramite un form che consente di inserire
 * dei filtri per i valori di alcuni parametri (es: tempo di preparazione, nome della ricetta,..).
 */

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Activity mainActivity;

    private LinearLayout ingredients_linearLayout;
    private ImageButton addIngredient_imageButton;
    private FloatingActionButton search_floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mainActivity = this;

        // Assegno il titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.search_toolbar);
    }

    private boolean firstTime = true;

    @Override
    protected void onStart(){
        super.onStart();

        // Assegno il titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.search_toolbar);

        ingredients_linearLayout = findViewById(R.id.ingredientsSearch_linearLayout);

        addIngredient_imageButton = findViewById(R.id.addIngredientSearch_imageButton);
        addIngredient_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientFragment();
            }
        });

        hideKeyBoardOnClickAllToggleButtons();

        search_floatingActionButton = findViewById(R.id.search_floatingActionButton);
        search_floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("name", getRecipeName());
                returnIntent.putExtra("dishType", getDishTypeList());
                returnIntent.putExtra("timeType", getTimeTypeList());
                returnIntent.putExtra("ingredients", getIngredientsList());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


        // Inserisci primo ingrediente
        // utilizzo firstTime perchè altrimenti aggiungerebbe ingredienti anche quando nascondo e riapro l'app
        if (firstTime){
            addIngredientFragment();
            firstTime = false;
        }
    }

    private void hideKeyBoardOnClickAllToggleButtons() {
        final View.OnClickListener hideKeyBoardOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(mainActivity, v);
            }
        };

        findViewById(R.id.toggleButton1Search).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.toggleButton2Search).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.toggleButton3Search).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.toggleButton4Search).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.fastSearch_toggleButton).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.mediumSearch_toggleButton).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.longSearch_toggleButton).setOnClickListener(hideKeyBoardOnClick);
    }

    // Aggiunge un frammento relativo ad un ingrediente alla lista
    public void addIngredientFragment(){
        getSupportFragmentManager().beginTransaction().add(ingredients_linearLayout.getId(), SearchIngredientFragment.newInstance(), "INGREDIENT_FRAGMENT_" + ingredients_linearLayout.getChildCount()).commit();
    }


    private String getRecipeName(){
        EditText recipeNameSearch_editText = findViewById(R.id.recipeNameSearch_editText);

        return recipeNameSearch_editText.getText().toString().trim();
    }

    private ArrayList<String> getDishTypeList(){
        ToggleButton toggleButton1Search = findViewById(R.id.toggleButton1Search);
        ToggleButton toggleButton2Search = findViewById(R.id.toggleButton2Search);
        ToggleButton toggleButton3Search = findViewById(R.id.toggleButton3Search);
        ToggleButton toggleButton4Search = findViewById(R.id.toggleButton4Search);

        ArrayList<String> returnList = new ArrayList<>();

        if(toggleButton1Search.isChecked())
            returnList.add(Recipe.DishType.FIRST.getDishTypeString());

        if(toggleButton2Search.isChecked())
            returnList.add(Recipe.DishType.SECOND.getDishTypeString());

        if(toggleButton3Search.isChecked())
            returnList.add(Recipe.DishType.APPETIZER.getDishTypeString());

        if(toggleButton4Search.isChecked())
            returnList.add(Recipe.DishType.DESSERT.getDishTypeString());

        // Nessuno selezionato == tutti selezionati
        if(returnList.isEmpty()){
            returnList.add(Recipe.DishType.FIRST.getDishTypeString());
            returnList.add(Recipe.DishType.SECOND.getDishTypeString());
            returnList.add(Recipe.DishType.APPETIZER.getDishTypeString());
            returnList.add(Recipe.DishType.DESSERT.getDishTypeString());
        }

        return returnList;
    }

    private ArrayList<String> getTimeTypeList(){
        ToggleButton fastSearch_toggleButton = findViewById(R.id.fastSearch_toggleButton);
        ToggleButton mediumSearch_toggleButton = findViewById(R.id.mediumSearch_toggleButton);
        ToggleButton longSearch_toggleButton = findViewById(R.id.longSearch_toggleButton);

        ArrayList<String> returnList = new ArrayList<>();

        if(fastSearch_toggleButton.isChecked())
            returnList.add(Recipe.TimeType.FAST.getTimeTypeString());

        if(mediumSearch_toggleButton.isChecked())
            returnList.add(Recipe.TimeType.MEDIUM.getTimeTypeString());

        if(longSearch_toggleButton.isChecked())
            returnList.add(Recipe.TimeType.LONG.getTimeTypeString());

        // Nessuno selezionato == tutti selezionati
        if(returnList.isEmpty()){
            returnList.add(Recipe.TimeType.FAST.getTimeTypeString());
            returnList.add(Recipe.TimeType.MEDIUM.getTimeTypeString());
            returnList.add(Recipe.TimeType.LONG.getTimeTypeString());
        }

        return returnList;
    }

    private ArrayList<String> getIngredientsList(){

        ArrayList<String> list = new ArrayList<>();

        int t = ingredients_linearLayout.getChildCount();
        for (int i = 0; i < t; i++) {
            // Recupera il frammento del i-esimo ingrediente
            SearchIngredientFragment nif = (SearchIngredientFragment) getSupportFragmentManager().findFragmentByTag("INGREDIENT_FRAGMENT_"+i);

            if (nif != null && !nif.getText().trim().equals(""))
                list.add(nif.getText());
        }

        return list;
    }

    // Per avere un flow di inserimento più fluido nascondo la tastiera quando viene selezionato un pulsante
    public static void hideSoftKeyboard (Activity activity, View view){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}
