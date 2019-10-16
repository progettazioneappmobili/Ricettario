package com.developer.luca.foodbook;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Classe che utilizzo per mostrare una gallery con l'elenco delle ricette, ogni ricetta
 * avra un'immagine e un campo di testo con il suo nome. Le ricette sono divise per tipo
 * di portata, quindi avro un elemento di tipo ExpandableListView per ogni tipo di portata
 * (antipasto, primo, secondo, dessert).
 */

public class ResultsActivity extends AppCompatActivity {

    private DataBaseWrapper dbWrapper; // comunicazione db
    private Cursor cursor; // ausiliario per scorrere i record trovati con la query

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred);

        // Assegno il titolo alla toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.found_recipes);

        // Inizializzo questa classe per la comunicazione con il db
        dbWrapper = new DataBaseWrapper(this);

        Bundle b = getIntent().getExtras();
        if(b != null) { // ricevo id dei piatti dalla search, mostro i risultati
            configGallery2(b.getStringArrayList("ids"));
        }else { // non ricevo niente, mostro tutte le ricette preferite
            configGallery();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Bundle b = getIntent().getExtras();
        if(b != null) { // ricevo id dei piatti dalla search, mostro i risultati
            configGallery2(b.getStringArrayList("ids"));
        }else { // non ricevo niente, mostro tutte le ricette preferite
            configGallery();
        }
    }

    /**
     * Inizializzo la Gallery passando le varie ricette raggruppate per tipo alla ListView
     */
    public void configGallery() {
        // Configuro la ListView
        ExpandableListView expandableListView = findViewById(R.id.expandableListViewPreferred);
        LinkedHashMap<String, ArrayList<String>> item = new LinkedHashMap<>(); // conterra titolo e contenuti della list view

        // Primi
        ArrayList<String> primiGroup = new ArrayList<>();
        primiGroup = getPrefRecipesByType(getString(R.string.primo), primiGroup);
        item.put(getString(R.string.primi), primiGroup);

        // Secondi
        ArrayList<String> secondiGroup = new ArrayList<>();
        secondiGroup = getPrefRecipesByType(getString(R.string.secondo), secondiGroup);
        item.put(getString(R.string.secondi), secondiGroup);

        // Antipasti
        ArrayList<String> antipastiGroup = new ArrayList<>();
        antipastiGroup = getPrefRecipesByType(getString(R.string.antipasto), antipastiGroup);
        item.put(getString(R.string.antipasti), antipastiGroup);

        // Dolci
        ArrayList<String> dolciGroup = new ArrayList<>();
        dolciGroup = getPrefRecipesByType(getString(R.string.dessert), dolciGroup);
        item.put(getString(R.string.dessert), dolciGroup);

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(item, this);
        expandableListView.setAdapter(adapter);
        expandableListView.setDividerHeight(0);
        expandableListView.expandGroup(0);
    }

    /**
     * Dato il tipo di portata ed un ArrayList<String> su cui scrivere il risultato inserisco nell'array
     * tutti i piatti di quel tipo
     * @param tipo String ad esempio "Antipasto", "Primo",...
     * @param result ArrayList su cui andro a scrivere il risultato
     * @return lista di stringhe contenenti coppie di piatti e i loro id
     */
    public ArrayList<String> getPrefRecipesByType(String tipo, ArrayList<String> result){
        dbWrapper.open();
        cursor = dbWrapper.fetchPrefRecipeByType(tipo); // prendo tutte le ricette con il flag preferita e del tipo che cerco
        int count = 0;
        String nome1 = "";
        String id1 = "";
        while (cursor.moveToNext()) {
            String recipeId = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_RECIPEID));
            String name = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_NAME));
            if (count == 0) {
                nome1 = name;
                id1 = recipeId;
                count = 1;
            } else {
                result.add(nome1 + "£" + id1 + "§" + name + "£" + recipeId);
                nome1 = "";
                id1 = "";
                count = 0;
            }
        }
        if (count == 1) { // aggiungo l'ultimo piatto, se presente
            result.add(nome1 + "£" + id1 + "§");
        }
        // Chiudo la connessione al db
        cursor.close();
        dbWrapper.close();

        return result;
    }

    /**
     * Inizializzo la Gallery passando le varie ricette raggruppate per tipo alla ListView
     */
    public void configGallery2(ArrayList<String> idList) {

        String[] ids = idList.toArray(new String[idList.size()]);

        // Configuro la ListView
        ExpandableListView expandableListView = findViewById(R.id.expandableListViewPreferred);
        LinkedHashMap<String, ArrayList<String>> item = new LinkedHashMap<>(); // conterra titolo e contenuti della list view

        // Primi
        ArrayList<String> primiGroup = new ArrayList<>();
        primiGroup = getRecipesByTypeAndId(getString(R.string.primo), primiGroup, ids);
        item.put(getString(R.string.primi), primiGroup);

        // Secondi
        ArrayList<String> secondiGroup = new ArrayList<>();
        secondiGroup = getRecipesByTypeAndId(getString(R.string.secondo), secondiGroup, ids);
        item.put(getString(R.string.secondi), secondiGroup);

        // Antipasti
        ArrayList<String> antipastiGroup = new ArrayList<>();
        antipastiGroup = getRecipesByTypeAndId(getString(R.string.antipasto), antipastiGroup, ids);
        item.put(getString(R.string.antipasti), antipastiGroup);

        // Dolci
        ArrayList<String> dolciGroup = new ArrayList<>();
        dolciGroup = getRecipesByTypeAndId(getString(R.string.dessert), dolciGroup, ids);
        item.put(getString(R.string.dessert), dolciGroup);

        int groupToExpand = 0; // scelgo quale gruppo del menu a fisarmonica (list view) espandere
        int totalRecipesFound = primiGroup.size() + secondiGroup.size() + antipastiGroup.size() + dolciGroup.size(); // numero di ricette trovate

        if (totalRecipesFound == 0){

            // Dico all'utente che non ho trovato nessuna ricetta
            Toast.makeText(this, getString(R.string.message_no_recipe_found), Toast.LENGTH_LONG).show();

        }else{

            // Espando il primo gruppo con almeno una ricetta
            if (primiGroup.size() > 0){
                groupToExpand = 0;
            }else if (secondiGroup.size() > 0){
                groupToExpand = 1;
            }else if (antipastiGroup.size() > 0){
                groupToExpand = 2;
            }else if (dolciGroup.size() > 0){
                groupToExpand = 3;
            }

            // Mostro un messaggio con il numero di ricette trovate
            if (totalRecipesFound > 1){ // + di 1 ricetta
                Toast.makeText(this, "" + totalRecipesFound + " ricette trovate" , Toast.LENGTH_SHORT).show();
            }else{ // solo una ricetta
                Toast.makeText(this, "" + totalRecipesFound + " ricetta trovata" , Toast.LENGTH_SHORT).show();
            }

        }

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(item, this);
        expandableListView.setAdapter(adapter);
        expandableListView.setDividerHeight(0);
        expandableListView.expandGroup(groupToExpand);
    }

    /**
     * Dato il tipo di portata ed un ArrayList<String> su cui scrivere il risultato inserisco nell'array
     * tutti i piatti di quel tipo
     * @param tipo String ad esempio "Antipasto", "Primo",...
     * @param result ArrayList su cui andro a scrivere il risultato
     * @param ids array di stringhe con gli id delle ricette
     * @return lista di stringhe contenenti coppie di piatti e i loro id
     */
    public ArrayList<String> getRecipesByTypeAndId(String tipo, ArrayList<String> result, String [] ids){
        dbWrapper.open();
        cursor = dbWrapper.fetchRecipesByIdAndType(ids, tipo); // prendo tutte le ricette con il flag preferita e del tipo che cerco
        if (cursor == null) // se non ho trovato nessuna ricetta mi fermo
                return result; // array vuoto
        int count = 0;
        String nome1 = "";
        String id1 = "";
        while (cursor.moveToNext()) {
            String recipeId = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_RECIPEID));
            String name = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_NAME));
            if (count == 0) {
                nome1 = name;
                id1 = recipeId;
                count = 1;
            } else {
                result.add(nome1 + "£" + id1 + "§" + name + "£" + recipeId);
                nome1 = "";
                id1 = "";
                count = 0;
            }
        }
        if (count == 1) { // aggiungo l'ultimo piatto, se presente
            result.add(nome1 + "£" + id1 + "§");
        }
        // Chiudo la connessione al db
        cursor.close();
        dbWrapper.close();

        return result;
    }

}
