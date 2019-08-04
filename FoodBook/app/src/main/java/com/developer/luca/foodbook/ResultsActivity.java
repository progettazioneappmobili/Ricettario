package com.developer.luca.foodbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe che utilizzo per mostrare una gallery con l'elenco delle ricette, ogni ricetta
 * avra un'immagine e un campo di testo con il suo nome. Le ricette sono divise per tipo
 * di portata, quindi avro un elemento di tipo ExpandableListView per ogni tipo di portata
 * (antipasto, primo, secondo..).
 */

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred);

        // Assegno il titolo alla toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.found_recipes);

        // Configuro la ListView
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        HashMap<String, List<String>> item = new HashMap<>(); // conterra titolo e contenuti della list view
        ArrayList<String> antipastiGroup = new ArrayList<>();

        antipastiGroup.add("Antipasto 1§Antipasto 2"); // Unisco le stringhe con un carattere speciale
        antipastiGroup.add("Antipasto 3§Antipasto 4");
        item.put(getString(R.string.antipasti), antipastiGroup);

        ArrayList<String> primiGroup = new ArrayList<>();

        primiGroup.add("Spaghetti§Pasta al pesto"); // Unisco le stringhe con un carattere speciale
        primiGroup.add("Lasagne§Zuppa");
        item.put(getString(R.string.primi), primiGroup);

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(item, "Results");
        expandableListView.setAdapter(adapter);
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Assegno il titolo alla toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.found_recipes);

        // Configuro la ListView
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        HashMap<String, List<String>> item = new HashMap<>(); // conterra titolo e contenuti della list view
        ArrayList<String> antipastiGroup = new ArrayList<>();

        antipastiGroup.add("Antipasto 1§Antipasto 2"); // Unisco le stringhe con un carattere speciale
        antipastiGroup.add("Antipasto 3§Antipasto 4");
        item.put(getString(R.string.antipasti), antipastiGroup);

        ArrayList<String> primiGroup = new ArrayList<>();

        primiGroup.add("Spaghetti§Pasta al pesto"); // Unisco le stringhe con un carattere speciale
        primiGroup.add("Lasagne§Zuppa");
        item.put(getString(R.string.primi), primiGroup);

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(item, "Results");
        expandableListView.setAdapter(adapter);
    }

}
