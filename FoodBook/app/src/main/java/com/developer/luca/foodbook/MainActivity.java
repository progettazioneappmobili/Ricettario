package com.developer.luca.foodbook;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Home page dell'applicazione: in alto abbiamo due bottoni (icona di ricerca e stella dei preferiti),
 * poi una gallery che mostra tutte le ricette, divise per tipo di portata e infine un bottone +
 * da cui e' possibile accedere all'inserimento di una nuova ricetta.
 */

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab; // bottone + sulla home
    private DataBaseWrapper dbWrapper; // comunicazione db
    private Cursor cursor; // ausiliario per scorrere i record trovati con la query

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cliccando sul bottone + dalla home page passo alla schermata di inserimento nuova ricetta
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert_intent = new Intent(v.getContext(), InsertActivity.class);
                startActivity(insert_intent);
            }
        });

        // Inizializzo questa classe per la comunicazione con il db
        dbWrapper = new DataBaseWrapper(this);

        // Aggiungo alcuni record se db vuoto
        if (checkDb()) // restituisce true se db vuoto => aggiungo i record
            addRecords();

        // Configuro la ListView
        ExpandableListView expandableListView = findViewById(R.id.expandableListView5);
        HashMap<String, ArrayList<String>> item = new HashMap<>(); // conterra titolo e contenuti della list view

        // Antipasti
        ArrayList<String> antipastiGroup = new ArrayList<>();
        antipastiGroup = getRecipesByType("Antipasto", antipastiGroup);
        item.put(getString(R.string.antipasti), antipastiGroup);

        // Primi
        ArrayList<String> primiGroup = new ArrayList<>();
        primiGroup = getRecipesByType("Primo", primiGroup);
        item.put(getString(R.string.primi), primiGroup);

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(item);
        expandableListView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case (R.id.action_preferred):
                Intent preferred_intent = new Intent(this, ResultsActivity.class);
                startActivity(preferred_intent);
                break;

            case (R.id.action_search):
                Intent search_intent = new Intent(this, DatabaseActivity.class); // TODO temp per i test (sarebbe SearchActivity)
                startActivity(search_intent);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Dato il tipo di portata ed un ArrayList<String> su cui scrivere il risultato inserisco nell'array
     * tutti i piatti di quel tipo
     * @param tipo String ad esempio "Antipasto", "Primo",...
     * @param result ArrayList su cui andro a scrivere il risultato
     * @return lista di stringhe contenenti coppie di piatti e i loro id
     */
    // TODO gestire caso singola ricetta (TableLayout)
    public ArrayList<String> getRecipesByType(String tipo, ArrayList<String> result){
        dbWrapper.open();
        cursor = dbWrapper.fetchRecipeByType(tipo);
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

    // Funzione usata per i test: creo un certo numero di ricette con i loro dettagli
    public void addRecords(){
        dbWrapper.open();
        dbWrapper.createRecipe("Spaghetti", "Passo1\nButta l'acqua\nPasso2\nMetti il sale", "Primo","lasagne.jpg",18,"Spaghetti\nIngred2",1);
        dbWrapper.createRecipe("Pasta", "Passo1\nButta un po d'acqua\nPasso2\nAggiungi il sale", "Primo","lasagne.jpg",21,"Pasta\nIngred2",1);
        dbWrapper.createRecipe("Patatine", "Passo1\nApri il sacchetto\nPasso2\nBla bla bla", "Antipasto","lasagne.jpg",5,"Pringles\nIngred2",1);
        dbWrapper.createRecipe("Antipasto di pesce", "Passo1\nTira fuori il pesce\nPasso2\nBla bla bla", "Antipasto","lasagne.jpg",20,"Calamari\nSeppie\nGamberi",1);
        dbWrapper.createRecipe("Formaggi misti", "Passo1\nTira fuori il formaggio\nPasso2\nBla bla bla", "Antipasto","lasagne.jpg",10,"Gorgonzola\nMontasio\nEmmenthal",0);
        dbWrapper.close();
    }

    /**
     * Funzione usata per controllare se il db ha almeno un record
     * @return true se il db non ha record, false altrimenti
     */
    public Boolean checkDb(){
        dbWrapper.open();
        cursor = dbWrapper.fetchAllRecipes();
        while (cursor.moveToNext()) {
            String recipeId = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_RECIPEID));
            if (recipeId.length() > 0) { // esiste almeno un record
                cursor.close();
                dbWrapper.close();
                return false;
            }
        }
        // Chiudo la connessione al db
        cursor.close();
        dbWrapper.close();
        return true; // se arrivo qui => db vuoto
    }
}
