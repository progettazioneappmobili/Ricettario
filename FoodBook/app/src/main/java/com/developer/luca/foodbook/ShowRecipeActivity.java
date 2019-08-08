package com.developer.luca.foodbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe che utilizzo per mostrare i dettagli relativi ad un piatto;
 * ricevo un dishId da altre activity e poi mostro i dettagli relativi al piatto
 * con quell'id tramite tre expandableListView: informazioni, ingredienti e preparazione.
 */

public class ShowRecipeActivity extends AppCompatActivity {

    private int dishId = 0; // id del piatto di cui dovro mostrare i dettagli

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Bundle b = getIntent().getExtras(); // ricevo l'id del piatto da un'altra activity
        if(b != null)
            dishId = b.getInt("key");

        // Assegno il titolo alla toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.recipe_name);

        // Configuro la ListView
        ExpandableListView expandableListView2 = findViewById(R.id.expandableListView2);
        HashMap<String, List<String>> item_info = new HashMap<>();
        ArrayList<String> informazioniGroup = new ArrayList<>();

        informazioniGroup.add("Antipasto§Veloce 6 minuti" + "\n" + dishId);
        item_info.put(getString(R.string.recipe_info), informazioniGroup);

        // Configuro la ListView
        ExpandableListView expandableListView3 = findViewById(R.id.expandableListView3);
        HashMap<String, List<String>> item_ingred = new HashMap<>();
        ArrayList<String> ingredientiGroup = new ArrayList<>();

        ingredientiGroup.add("50 grammi di burro\n100 grammi di farina\n2 uova");
        item_ingred.put(getString(R.string.ingredients), ingredientiGroup);

        // Configuro la ListView
        ExpandableListView expandableListView4 = findViewById(R.id.expandableListView4);
        HashMap<String, List<String>> item_preparaz = new HashMap<>();
        ArrayList<String> preparazioneGroup = new ArrayList<>();

        preparazioneGroup.add("Passo1\nDescrizione del primo passo...\n\nPasso2\nDescrizione del...");
        item_preparaz.put(getString(R.string.preparation), preparazioneGroup);

        // Passo l'activityName all'Adapter in modo che sappia quale layout mostrare
        ShowExpandableListAdapter adapter2 = new ShowExpandableListAdapter(item_info, "ShowRecipe1");
        expandableListView2.setAdapter(adapter2);

        ShowExpandableListAdapter adapter3 = new ShowExpandableListAdapter(item_ingred, "ShowRecipe2");
        expandableListView3.setAdapter(adapter3);

        ShowExpandableListAdapter adapter4 = new ShowExpandableListAdapter(item_preparaz, "ShowRecipe3");
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
        HashMap<String, List<String>> item_info = new HashMap<>(); // salvo qui le informazioni che poi passero all'Adapter
        ArrayList<String> informazioniGroup = new ArrayList<>();

        informazioniGroup.add("Antipasto§Veloce 6 minuti" + "\n" + dishId); // Contenuto della expandableListView
        item_info.put(getString(R.string.recipe_info), informazioniGroup); // Titolo e contenuto della expandableListView

        // Configuro la ListView, come sopra
        ExpandableListView expandableListView3 = findViewById(R.id.expandableListView3);
        HashMap<String, List<String>> item_ingred = new HashMap<>();
        ArrayList<String> ingredientiGroup = new ArrayList<>();

        ingredientiGroup.add("50 grammi di burro\n100 grammi di farina\n2 uova");
        item_ingred.put(getString(R.string.ingredients), ingredientiGroup);

        // Configuro la ListView
        ExpandableListView expandableListView4 = findViewById(R.id.expandableListView4);
        HashMap<String, List<String>> item_preparaz = new HashMap<>();
        ArrayList<String> preparazioneGroup = new ArrayList<>();

        preparazioneGroup.add("Passo1\nDescrizione del primo passo...\n\nPasso2\nDescrizione del...");
        item_preparaz.put(getString(R.string.preparation), preparazioneGroup);

        // Passo l'activityName all'Adapter in modo che sappia quale layout mostrare
        ShowExpandableListAdapter adapter2 = new ShowExpandableListAdapter(item_info, "ShowRecipe1");
        expandableListView2.setAdapter(adapter2);

        ShowExpandableListAdapter adapter3 = new ShowExpandableListAdapter(item_ingred, "ShowRecipe2");
        expandableListView3.setAdapter(adapter3);

        ShowExpandableListAdapter adapter4 = new ShowExpandableListAdapter(item_preparaz, "ShowRecipe3");
        expandableListView4.setAdapter(adapter4);
    }
}

