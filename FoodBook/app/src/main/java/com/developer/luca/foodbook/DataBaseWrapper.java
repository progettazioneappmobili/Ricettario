package com.developer.luca.foodbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseWrapper {

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    // Database fields
    private static final String DATABASE_TABLE = "recipe";

    public static final String KEY_RECIPEID = "_id";
    public static final String KEY_NAME = "name"; // nome della ricetta
    public static final String KEY_PREPARATION = "preparation"; // passi della preparazione separati da '§'
    public static final String KEY_DISHTYPE = "dishtype"; // tipo di portata: "Antipasto","Primo","Secondo","Dessert"
    public static final String KEY_PREPARATIONTIME = "preparationtime"; // tempo di preparazione in minuti
    public static final String KEY_TIMETYPE = "timetype"; // tipo di tempo di preparazione: "Veloce", "Medio", "Lungo"
    public static final String KEY_FILENAME = "filename"; // nome del file, foto del piatto
    public static final String KEY_INGREDIENTS = "ingredients"; // lista di ingredienti separati da ";"
    public static final String KEY_ISPREFERRED = "ispreferred"; // flag 1 = true o 0 = false

    public DataBaseWrapper(Context context){
        this.context = context;
    }

    public DataBaseWrapper open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    // Metodo per creare un nuovo record
    public long createRecipe (String name, String preparation, String dishType, String filename, int preparationTime, String timeType, String ingredients, int ispreferred){
        ContentValues initialValues = createContentValues(name, preparation, dishType, filename, preparationTime, timeType, ingredients, ispreferred);
        return database.insertOrThrow(DATABASE_TABLE, null, initialValues);
    }

    // Metodo ausiliario per aggiungere un nuovo record (riga del db)
    private ContentValues createContentValues(String name, String preparation, String dishType, String filename, int preparationTime, String timeType, String ingredients, int ispreferred){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_DISHTYPE, dishType);
        values.put(KEY_FILENAME, filename);
        values.put(KEY_PREPARATION,preparation);
        values.put(KEY_PREPARATIONTIME, preparationTime);
        values.put(KEY_TIMETYPE, timeType);
        values.put(KEY_INGREDIENTS, ingredients);
        values.put(KEY_ISPREFERRED, ispreferred);

        return values;
    }

    // Metodo per aggiornare un record
    public boolean updateRecipe(long recipeId, String name, String preparation, String dishType, String filename, int preparationTime, String timeType, String ingredients, int ispreferred){
        ContentValues updateValues = createContentValues(name, preparation, dishType, filename, preparationTime, timeType, ingredients, ispreferred);
        return database.update(DATABASE_TABLE, updateValues, KEY_RECIPEID + "=" + recipeId,null) > 0;
    }

    // Metodo per cancellare un record
    public boolean deleteRecipe(long recipeId){
        return database.delete(DATABASE_TABLE, KEY_RECIPEID + "=" + recipeId,null) > 0;
    }

    // Restituisce tutte le ricette
    public Cursor fetchAllRecipes(){
        return database.query(DATABASE_TABLE, null, null, null, null, null,null);
    }

    // Restituisce una ricetta in base all'id
    public Cursor fetchRecipe(long recipeId){
        return database.query(true, DATABASE_TABLE, null, KEY_RECIPEID + "=" + recipeId, null, null, null, null,null);
    }

    // Cerco ricetta per nome
    public Cursor fetchRecipeByName(String name){
        return database.query(true, DATABASE_TABLE, null, KEY_NAME + " like '%" + name + "%'" ,null, null, null, null,null);
    }

    // Cerco ricetta per tipo
    public Cursor fetchRecipeByType(String portata){
        return database.query(true, DATABASE_TABLE, null, KEY_DISHTYPE + " like '%" + portata + "%'",null, null, null, null,null);
    }

    // Cerco ricetta per tipo e deve essere fra le preferite
    public Cursor fetchPrefRecipeByType(String portata){
        return database.query(true, DATABASE_TABLE, null, KEY_DISHTYPE + " like '%" + portata + "%'" + " AND " + KEY_ISPREFERRED + " = 1",null, null, null, null,null);
    }


    // Ricette con id nella lista di id e tipo di portata specificato
    public Cursor fetchRecipesByIdAndType(String [] ids, String portata){
        if(ids.length > 0){
            String query = "SELECT * FROM table"
                    + " WHERE id IN (" + makePlaceholders(ids.length) + ")"
                    + " AND dishtype LIKE '%" + portata + "%'";
            return database.rawQuery(query, ids);
        }else{
            return null;
        }
    }

    // Funzione aux per generare i "?" da mettere nella raw query
    private String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    // Cerco ricetta che contenga name, sia di uno tra i tipi di dishType, sia di uno tra i tipi di timeType e che contenga gli ingredients
    public Cursor fetchSearchedRecipes(String name, String[] dishType, String[] timeType, String[] ingredients){
        return database.query(true, DATABASE_TABLE, new String[]{KEY_RECIPEID, KEY_NAME, KEY_DISHTYPE, KEY_FILENAME, KEY_PREPARATION, KEY_PREPARATIONTIME, KEY_INGREDIENTS, KEY_ISPREFERRED },
                KEY_NAME + " like '%" + name + "%'" + " AND " +
                        KEY_DISHTYPE + " in " + stringArrayToInList(dishType) + " AND " +
                        KEY_TIMETYPE + " in " + stringArrayToInList(timeType) +
                        queryForIngredients(ingredients),
                null, null, null, null, null);
    }

    private String queryForIngredients(String[] ingredients) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String i: ingredients) {
            stringBuilder.append(" AND ").append(KEY_INGREDIENTS).append(" like '%").append(i).append("%'");
        }

        return stringBuilder.toString();
    }

    private String stringArrayToInList(String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("(");

        String prefix = "";
        for (String s: strings) {
            stringBuilder.append(prefix).append("'").append(s).append("'");
            prefix = ",";
        }

        stringBuilder.append(")");
        return stringBuilder.toString();

    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "mydatabase.db";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE = "create table "
                + DATABASE_TABLE + " ("
                + KEY_RECIPEID + " integer primary key autoincrement, "
                + KEY_NAME + " text not null, "
                + KEY_DISHTYPE + " text not null, "
                + KEY_FILENAME + " text not null, "
                + KEY_PREPARATION + " text not null, "
                + KEY_INGREDIENTS + " text not null, "
                + KEY_ISPREFERRED + " integer not null,"
                + KEY_PREPARATIONTIME + " integer, "
                + KEY_TIMETYPE + " text not null);";
        // Statement di creazione del db

        // Costruttore
        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Questo metodo viene chiamato (automaticamente da Android) durante la creazione del db
        @Override
        public void onCreate(SQLiteDatabase database){
            database.execSQL(DATABASE_CREATE);
        }

        // Metodo chiamato (automaticamente da Android) durante l'upgrade del db ad esempio quando viene incrementato il numero di versione
        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
            database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(database);
        }

    } // classe DatabaseHelper

}
