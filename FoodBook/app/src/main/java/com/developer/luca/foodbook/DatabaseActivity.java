package com.developer.luca.foodbook;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DatabaseActivity extends Activity {

    private DataBaseWrapper dbWrapper;
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_activity);

        dbWrapper = new DataBaseWrapper(this);
    }

    public void addRecord(View v){
        dbWrapper.open();
        dbWrapper.createRecipe("Spaghetti", "Passo1\nBla bla\nPasso2\nBla bla bla", "Antipasto","lasagne.jpg",15);
        Log.v("DB_ACTIVITY", "record aggiunti");
        dbWrapper.close();
    }

    public void showRecords(View v){
        dbWrapper.open();
        cursor = dbWrapper.fetchAllRecipes();
        while(cursor.moveToNext()){
            String recipeId = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_RECIPEID));
            String name = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_NAME));
            Log.v("DB_ACTIVITY","recipe id = " + recipeId + ";" + " recipe name = " + name);
        }
        cursor.close();
        dbWrapper.close();
    }


}
