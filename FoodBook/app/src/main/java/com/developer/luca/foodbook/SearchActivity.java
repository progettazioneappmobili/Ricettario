package com.developer.luca.foodbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Classe che permette di cercare una ricetta tramite un form che consente di inserire
 * dei filtri per i valori di alcuni parametri (es: tempo di preparazione, nome della ricetta,..).
 */

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Assegno il titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.search_toolbar);
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Assegno il titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.search_toolbar);
    }
}
