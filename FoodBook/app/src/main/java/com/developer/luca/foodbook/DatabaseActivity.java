package com.developer.luca.foodbook;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Classe temporanea per testare il funzionamento del database
 */

public class DatabaseActivity extends Activity {

    private DataBaseWrapper dbWrapper;
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_activity);

        dbWrapper = new DataBaseWrapper(this);
    }

    // Funzione usata per i test: creo un certo numero di record con i loro dettagli
    public void addRecord(View v){
        dbWrapper.open();
        dbWrapper.createRecipe("Spaghetti", "Passo1\nButta l'acqua\nPasso2\nMetti il sale", "Primo","lasagne.jpg",18,"Spaghetti\nIngred2",1);
        dbWrapper.createRecipe("Pasta", "Passo1\nButta un po d'acqua\nPasso2\nAggiungi il sale", "Primo","lasagne.jpg",21,"Pasta\nIngred2",1);
        dbWrapper.createRecipe("Patatine", "Passo1\nApri il sacchetto\nPasso2\nBla bla bla", "Antipasto","lasagne.jpg",5,"Pringles\nIngred2",1);
        dbWrapper.createRecipe("Antipasto di pesce", "Passo1\nTira fuori il pesce\nPasso2\nBla bla bla", "Antipasto","lasagne.jpg",20,"Calamari\nSeppie\nGamberi",1);
        dbWrapper.close();
    }

    public void updateRecords(View v){
        dbWrapper.open();
        dbWrapper.updateRecipe(1,"Spaghetti", "Passo1\nButta l'acqua\nPasso2\nMetti il sale", "Primo","lasagne.jpg",18,"Spaghetti\nIngred2",1);
        dbWrapper.updateRecipe(2,"Pasta", "Passo1\nButta un po d'acqua\nPasso2\nAggiungi il sale", "Primo","lasagne.jpg",21,"Pasta\nIngred2",1);
        dbWrapper.updateRecipe(3,"Patatine", "Passo1\nApri il sacchetto\nPasso2\nBla bla bla", "Antipasto","lasagne.jpg",5,"Pringles\nIngred2",1);
        dbWrapper.updateRecipe(4,"Antipasto di pesce", "Passo1\nTira fuori il pesce\nPasso2\nBla bla bla", "Antipasto","lasagne.jpg",20,"Calamari\nSeppie\nGamberi",1);
        dbWrapper.close();
    }

    // Mostro tramite Toast tutti i record attualmente salvati nel db
    public void showRecords(View v){
        dbWrapper.open();
        cursor = dbWrapper.fetchAllRecipes();
        while(cursor.moveToNext()){
            String recipeId = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_RECIPEID));
            String name = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_NAME));
            Toast.makeText(getApplicationContext(),"recipe id = " + recipeId + ";" + " recipe name = " + name,Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        dbWrapper.close();
    }


}
