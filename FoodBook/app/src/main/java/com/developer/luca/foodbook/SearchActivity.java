package com.developer.luca.foodbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che permette di cercare una ricetta tramite un form che consente di inserire
 * dei filtri per i valori di alcuni parametri (es: tempo di preparazione, nome della ricetta,..).
 */

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private LinearLayout ingredients_linearLayout;
    private ImageButton addIngredient_imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        // Inserisci primo ingrediente
        // utilizzo firstTime perchè altrimenti aggiungerebbe ingredienti anche quando nascondo e riapro l'app
        if (firstTime){
            addIngredientFragment();
            firstTime = false;
        }
    }

    // Aggiunge un frammento relativo ad un ingrediente alla lista
    public void addIngredientFragment(){
        getSupportFragmentManager().beginTransaction().add(ingredients_linearLayout.getId(), SearchIngredientFragment.newInstance(), "INGREDIENT_FRAGMENT_" + ingredients_linearLayout.getChildCount()).commit();
    }


    public ArrayList<String> getIngredientsList(){

        ArrayList<String> list = new ArrayList<>();

        int t = ingredients_linearLayout.getChildCount();
        for (int i = 0; i < t; i++) {
            // Recupera il frammento del i-esimo ingrediente
            SearchIngredientFragment nif = (SearchIngredientFragment) getSupportFragmentManager().findFragmentByTag("INGREDIENT_FRAGMENT_"+i);

            if (!nif.getText().equals("")){ // L'ingrediente è già trimmato
                Log.d("SEARCH", "getIngredientsList: "+nif.getText());
                list.add(nif.getText());
            }
        }

        return list;
    }
}
