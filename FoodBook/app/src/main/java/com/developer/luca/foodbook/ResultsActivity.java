package com.developer.luca.foodbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        HashMap<String, List<String>> item = new HashMap<>();

        ArrayList<String> antipastiGroup = new ArrayList<>();
        // Unisco le stringhe con un carattere speciale
        antipastiGroup.add("Antipasto 1§Antipasto 2");
        antipastiGroup.add("Antipasto 3§Antipasto 4");

        item.put(getString(R.string.antipasti), antipastiGroup);

        ArrayList<String> primiGroup = new ArrayList<>();
        // Unisco le stringhe con un carattere speciale
        primiGroup.add("Spaghetti§Pasta al pesto");
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

        HashMap<String, List<String>> item = new HashMap<>();

        ArrayList<String> antipastiGroup = new ArrayList<>();
        // Unisco le stringhe con un carattere speciale
        antipastiGroup.add("Antipasto 1§Antipasto 2");
        antipastiGroup.add("Antipasto 3§Antipasto 4");

        item.put(getString(R.string.antipasti), antipastiGroup);

        ArrayList<String> primiGroup = new ArrayList<>();
        // Unisco le stringhe con un carattere speciale
        primiGroup.add("Spaghetti§Pasta al pesto");
        primiGroup.add("Lasagne§Zuppa");

        item.put(getString(R.string.primi), primiGroup);

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(item, "Results");
        expandableListView.setAdapter(adapter);
    }

}
