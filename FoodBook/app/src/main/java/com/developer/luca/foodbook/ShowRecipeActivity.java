package com.developer.luca.foodbook;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

    private FloatingActionButton fab; // bottone stella per aggiungere la ricetta ai preferiti
    private DataBaseWrapper dbWrapper; // per recuperare dal db i dettagli della ricetta
    private Cursor cursor; // ausiliario per la query al db
    private int dishId = 0; // id del piatto di cui dovro mostrare i dettagli

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        dbWrapper = new DataBaseWrapper(this);

        Bundle b = getIntent().getExtras(); // ricevo l'id del piatto da un'altra activity
        if(b != null)
            dishId = b.getInt("key");

        // Cliccando sul bottone stella aggiungo la ricetta corrente ai preferiti
        fab = findViewById(R.id.floatingPrefButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreferred(dishId);
            }
        });

        // Configuro la ListView
        ExpandableListView expandableListView2 = findViewById(R.id.expandableListView2);
        HashMap<String, List<String>> item_info = new HashMap<>();
        ArrayList<String> informazioniGroup = new ArrayList<>();

        // Estraggo dal db i dettagli della ricetta
        ArrayList<String> recipeInfos = getRecipeDetails(dishId);
        String name = recipeInfos.get(0);

        // Assegno il titolo alla toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle(name);

        String dishType = recipeInfos.get(1);
        String preparation = recipeInfos.get(2);
        String prepTime = recipeInfos.get(3);
        String ingred = recipeInfos.get(4);
        String minutes = "minuti";
        int time = Integer.parseInt(prepTime);
        if (time < 2)
                minutes = "minuto";

        informazioniGroup.add(name + "\n" + dishType + "\n" + prepTime + " " + minutes + "\n" + dishId);
        item_info.put(getString(R.string.recipe_info), informazioniGroup);

        // Configuro la ListView
        ExpandableListView expandableListView3 = findViewById(R.id.expandableListView3);
        HashMap<String, List<String>> item_ingred = new HashMap<>();
        ArrayList<String> ingredientiGroup = new ArrayList<>();

        ingredientiGroup.add(ingred);
        item_ingred.put(getString(R.string.ingredients), ingredientiGroup);

        // Configuro la ListView
        ExpandableListView expandableListView4 = findViewById(R.id.expandableListView4);
        HashMap<String, List<String>> item_preparaz = new HashMap<>();
        ArrayList<String> preparazioneGroup = new ArrayList<>();

        preparazioneGroup.add(preparation);
        item_preparaz.put(getString(R.string.preparation), preparazioneGroup);

        // Passo l'activityName all'Adapter in modo che sappia quale layout mostrare
        ShowExpandableListAdapter adapter2 = new ShowExpandableListAdapter(item_info, "ShowRecipe1", this);
        expandableListView2.setAdapter(adapter2);
        expandableListView2.expandGroup(0);

        ShowExpandableListAdapter adapter3 = new ShowExpandableListAdapter(item_ingred, "ShowRecipe2", this);
        expandableListView3.setAdapter(adapter3);

        ShowExpandableListAdapter adapter4 = new ShowExpandableListAdapter(item_preparaz, "ShowRecipe3", this);
        expandableListView4.setAdapter(adapter4);
    }

    /**
     * Funzione che dato l'id di una ricetta effettua una query al db ed estrae tutti i dettagli della
     * ricetta salvandoli in un ArrayList<String>
     * @param recipeIdent: id della ricetta
     * @return lista di stringhe, ognuna rappresenta uno dei campi del db (ingredienti, preparazione,..)
     */
    protected ArrayList<String> getRecipeDetails(long recipeIdent){
        ArrayList<String> result = new ArrayList<>();
        dbWrapper.open();
        cursor = dbWrapper.fetchRecipe(recipeIdent);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_NAME));
            String preparation = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_PREPARATION));
            String prepTime = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_PREPARATIONTIME));
            String dishType = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_DISHTYPE));
            String ingred = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_INGREDIENTS));
            result.add(name);
            result.add(dishType);
            result.add(preparation);
            result.add(prepTime);
            result.add(ingred);
        }
        cursor.close();
        dbWrapper.close();
        return result;
    }

    /**
     * Dato l'id di una ricetta faccio una query di update per quella ricetta e setto a 1 il
     * campo KEY_ISPREFERRED.
     * @param id: id della ricetta di cui voglio fare l'update
     */
    public void setPreferred(long id){
        dbWrapper.open();
        cursor = dbWrapper.fetchRecipe(id);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_NAME));
            String preparation = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_PREPARATION));
            int prepTime = cursor.getInt(cursor.getColumnIndex(DataBaseWrapper.KEY_PREPARATIONTIME));
            String timeType = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_TIMETYPE));
            String dishType = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_DISHTYPE));
            String ingred = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_INGREDIENTS));
            String filename = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_FILENAME));
            dbWrapper.updateRecipe(id, name, preparation, dishType, filename, prepTime, timeType, ingred, 1);
        }
        cursor.close();
        dbWrapper.close();
    }
}

