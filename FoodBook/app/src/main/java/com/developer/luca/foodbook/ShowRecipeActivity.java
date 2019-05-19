package com.developer.luca.foodbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowRecipeActivity extends AppCompatActivity {

    private int dishId = 0; // id del piatto di cui dovro mostrare i dettagli

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Bundle b = getIntent().getExtras();
        if(b != null)
            dishId = b.getInt("key");

        // Assegno il titolo alla toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.recipe_name);

        // Configuro la ListView
        ExpandableListView expandableListView2 = findViewById(R.id.expandableListView2);

        HashMap<String, List<String>> item2 = new HashMap<>();

        ArrayList<String> informazioniGroup = new ArrayList<>();
        // Unisco le stringhe con un carattere speciale
        informazioniGroup.add("Antipasto§Veloce 6 minuti" + "\n" + dishId);

        item2.put(getString(R.string.recipe_info), informazioniGroup);

        // Configuro la ListView
        ExpandableListView expandableListView3 = findViewById(R.id.expandableListView3);

        HashMap<String, List<String>> item3 = new HashMap<>();

        ArrayList<String> ingredientiGroup = new ArrayList<>();
        // Unisco le stringhe con un carattere speciale
        ingredientiGroup.add("50 grammi di burro\n100 grammi di farina\n2 uova");

        item3.put(getString(R.string.ingredients), ingredientiGroup);

        // Configuro la ListView
        ExpandableListView expandableListView4 = findViewById(R.id.expandableListView4);

        HashMap<String, List<String>> item4 = new HashMap<>();

        ArrayList<String> preparazioneGroup = new ArrayList<>();
        // Unisco le stringhe con un carattere speciale
        preparazioneGroup.add("Passo1\nDescrizione del primo passo...\n\nPasso2\nDescrizione del...");

        item4.put(getString(R.string.preparation), preparazioneGroup);

        MyExpandableListAdapter adapter2 = new MyExpandableListAdapter(item2, "ShowRecipe1");
        expandableListView2.setAdapter(adapter2);

        MyExpandableListAdapter adapter3 = new MyExpandableListAdapter(item3, "ShowRecipe2");
        expandableListView3.setAdapter(adapter3);

        MyExpandableListAdapter adapter4 = new MyExpandableListAdapter(item4, "ShowRecipe3");
        expandableListView4.setAdapter(adapter4);
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Assegno il titolo alla toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.recipe_name);

        // Configuro la ListView
        ExpandableListView expandableListView2 = findViewById(R.id.expandableListView2);

        HashMap<String, List<String>> item2 = new HashMap<>();

        ArrayList<String> informazioniGroup = new ArrayList<>();
        // Unisco le stringhe con un carattere speciale
        informazioniGroup.add("Antipasto§Veloce 6 minuti" + "\n" + dishId);

        item2.put(getString(R.string.recipe_info), informazioniGroup);

        // Configuro la ListView
        ExpandableListView expandableListView3 = findViewById(R.id.expandableListView3);

        HashMap<String, List<String>> item3 = new HashMap<>();

        ArrayList<String> ingredientiGroup = new ArrayList<>();
        // Unisco le stringhe con un carattere speciale
        ingredientiGroup.add("50 grammi di burro\n100 grammi di farina\n2 uova");

        item3.put(getString(R.string.ingredients), ingredientiGroup);

        // Configuro la ListView
        ExpandableListView expandableListView4 = findViewById(R.id.expandableListView4);

        HashMap<String, List<String>> item4 = new HashMap<>();

        ArrayList<String> preparazioneGroup = new ArrayList<>();
        // Unisco le stringhe con un carattere speciale
        preparazioneGroup.add("Passo1\nDescrizione del primo passo...\n\nPasso2\nDescrizione del...");

        item4.put(getString(R.string.preparation), preparazioneGroup);

        MyExpandableListAdapter adapter2 = new MyExpandableListAdapter(item2, "ShowRecipe1");
        expandableListView2.setAdapter(adapter2);

        MyExpandableListAdapter adapter3 = new MyExpandableListAdapter(item3, "ShowRecipe2");
        expandableListView3.setAdapter(adapter3);

        MyExpandableListAdapter adapter4 = new MyExpandableListAdapter(item4, "ShowRecipe3");
        expandableListView4.setAdapter(adapter4);
    }
}

