package com.developer.luca.foodbook;

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

        // Per ottenere i dettagli della ricetta a partire dall'id
        dbWrapper = new DataBaseWrapper(this);

        // Ricevo l'id del piatto da un'altra activity
        Bundle b = getIntent().getExtras();
        if(b != null)
            dishId = b.getInt("key");

        // Cliccando sul bottone stella aggiungo la ricetta corrente ai preferiti
        fab = findViewById(R.id.floatingPrefButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFabIcon(setPreferred(dishId));
            }
        });

        // Estraggo dal db i dettagli della ricetta
        ArrayList<String> recipeInfos = getRecipeDetails(dishId);
        String name = recipeInfos.get(0);

        // Assegno il titolo alla toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle(name);

        // Dettagli estratti precedentemente, da passare alla list view
        String dishType = recipeInfos.get(1);
        String preparation = recipeInfos.get(2);
        String prepTime = recipeInfos.get(3);
        String ingred = recipeInfos.get(4);
        String minutes = "minuti";
        int time = Integer.parseInt(prepTime);
        if (time < 2)
                minutes = "minuto";
        String infos = name + "\n" + dishType + "\n" + prepTime + " " + minutes + "\n" + dishId;

        configShowListView(infos, ingred, preparation);

        setFabIcon(recipeInfos.get(5).equals("1"));
    }

    private void setFabIcon(boolean isPref) {
        if(isPref){
            fab.setImageResource(R.drawable.ic_icon_fullstar);
        }else {
            fab.setImageResource(R.drawable.ic_icon_emptystar);
        }
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
            int preferred = cursor.getInt(cursor.getColumnIndex(DataBaseWrapper.KEY_ISPREFERRED));
            result.add(name);
            result.add(dishType);
            result.add(preparation);
            result.add(prepTime);
            result.add(ingred);
            result.add(String.valueOf(preferred));
        }
        cursor.close();
        dbWrapper.close();
        return result;
    }

    /**
     * Dato l'id di una ricetta faccio una query di update per quella ricetta e modifico il
     * campo KEY_ISPREFERRED, se era fra le preferite la rimuovo, se nonera la aggiungo.
     * @param id: id della ricetta di cui voglio fare l'update
     * @return isPref: true se la ricetta è ora tra i preferiti, false altrimenti
     */
    public Boolean setPreferred(long id){
        Boolean isPref = false;

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
            int pref = cursor.getInt(cursor.getColumnIndex(DataBaseWrapper.KEY_ISPREFERRED));
            if (pref == 0){ // non fra le preferite => la aggiungo
                dbWrapper.updateRecipe(id, name, preparation, dishType, filename, prepTime, timeType, ingred, 1);
                isPref = true;
            }else{ // fra le preferite => la rimuovo
                dbWrapper.updateRecipe(id, name, preparation, dishType, filename, prepTime, timeType, ingred, 0);
                isPref = false;
            }
        }
        cursor.close();
        dbWrapper.close();

        return isPref;
    }

    /**
     * Dati i dettagli da mostrare nell show di una ricetta configuro la list view che mostrera
     * le tre sezioni: Informazioni, Ingredienti e Preparazione
     * @param infos: nome ricetta, tipo di portata, tempo di preparazione e id
     * @param ingred: lista di ingredienti e quantita
     * @param preparation: passi della preparazione
     */
    private void configShowListView(String infos, String ingred, String preparation){
        // Informazioni
        ExpandableListView expandableListView2 = findViewById(R.id.expandableListView2);
        HashMap<String, List<String>> item_info = new HashMap<>();
        ArrayList<String> informazioniGroup = new ArrayList<>();
        informazioniGroup.add(infos);
        item_info.put(getString(R.string.recipe_info), informazioniGroup);

        // Ingredienti
        ExpandableListView expandableListView3 = findViewById(R.id.expandableListView3);
        HashMap<String, List<String>> item_ingred = new HashMap<>();
        ArrayList<String> ingredientiGroup = new ArrayList<>();
        ingredientiGroup.add(ingred);
        item_ingred.put(getString(R.string.ingredients), ingredientiGroup);

        // Preparazione
        ExpandableListView expandableListView4 = findViewById(R.id.expandableListView4);
        HashMap<String, List<String>> item_preparaz = new HashMap<>();
        ArrayList<String> preparazioneGroup = new ArrayList<>();
        preparazioneGroup.add(preparation);
        item_preparaz.put(getString(R.string.preparation), preparazioneGroup);

        // Configuro gli adapter
        ShowExpandableListAdapter adapter2 = new ShowExpandableListAdapter(item_info, "ShowRecipe1", this);
        expandableListView2.setDividerHeight(0);
        expandableListView2.setAdapter(adapter2);
        expandableListView2.expandGroup(0);

        ShowExpandableListAdapter adapter3 = new ShowExpandableListAdapter(item_ingred, "ShowRecipe2", this);
        expandableListView3.setAdapter(adapter3);

        ShowExpandableListAdapter adapter4 = new ShowExpandableListAdapter(item_preparaz, "ShowRecipe3", this);
        expandableListView4.setAdapter(adapter4);
    }
}

