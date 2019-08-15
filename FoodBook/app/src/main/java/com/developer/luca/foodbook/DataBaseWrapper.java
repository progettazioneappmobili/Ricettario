package com.developer.luca.foodbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final String KEY_FILENAME = "filename"; // nome del file, foto del piatto
    public static final String KEY_INGREDIENTS = "ingredients"; // lista di ingredienti separati da ";"

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
    public long createRecipe (String name, String preparation, String dishType, String filename, int preparationTime, String ingredients){
        ContentValues initialValues = createContentValues(name, preparation,dishType,filename,preparationTime,ingredients);
        return database.insertOrThrow(DATABASE_TABLE, null, initialValues);
    }

    // Metodo ausiliario per aggiungere un nuovo record (riga del db)
    private ContentValues createContentValues(String name, String preparation, String dishType, String filename, int preparationTime, String ingredients){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_DISHTYPE, dishType);
        values.put(KEY_FILENAME, filename);
        values.put(KEY_PREPARATION,preparation);
        values.put(KEY_PREPARATIONTIME, preparationTime);
        values.put(KEY_INGREDIENTS, ingredients);

        return values;
    }

    // Metodo per aggiornare un record
    public boolean updateRecipe(long recipeId, String name, String preparation, String dishType, String filename, int preparationTime, String ingredients){
        ContentValues updateValues = createContentValues(name, preparation,dishType,filename,preparationTime, ingredients);
        return database.update(DATABASE_TABLE, updateValues, KEY_RECIPEID + "=" + recipeId,null) > 0;
    }

    // Metodo per cancellare un record
    public boolean deleteRecipe(long recipeId){
        return database.delete(DATABASE_TABLE, KEY_RECIPEID + "=" + recipeId,null) > 0;
    }

    // Restituisce tutte le ricette
    public Cursor fetchAllRecipes(){
        return database.query(DATABASE_TABLE, new String[] {KEY_RECIPEID, KEY_NAME, KEY_DISHTYPE, KEY_FILENAME, KEY_PREPARATION, KEY_PREPARATIONTIME, KEY_INGREDIENTS }, null, null, null, null,null);
    }

    // Restituisce una ricetta in base all'id
    public Cursor fetchRecipe(long recipeId){
        return database.query(true, DATABASE_TABLE, new String[] {KEY_RECIPEID, KEY_NAME, KEY_DISHTYPE, KEY_FILENAME, KEY_PREPARATION, KEY_PREPARATIONTIME, KEY_INGREDIENTS }, KEY_RECIPEID + "=" + recipeId, null, null, null, null,null);
    }

    // Cerco ricetta per nome
    public Cursor fetchRecipeByName(String name){
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[]{KEY_RECIPEID, KEY_NAME, KEY_DISHTYPE, KEY_FILENAME, KEY_PREPARATION, KEY_PREPARATIONTIME, KEY_INGREDIENTS }, KEY_NAME + " like '%" + name + "%'" ,null, null, null, null,null);
        return mCursor;
    }

    // Cerco ricetta per tipo
    public Cursor fetchRecipeByType(String portata){
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[]{KEY_RECIPEID, KEY_NAME, KEY_DISHTYPE, KEY_FILENAME, KEY_PREPARATION, KEY_PREPARATIONTIME, KEY_INGREDIENTS }, KEY_DISHTYPE + " like '%" + portata + "%'" ,null, null, null, null,null);
        return mCursor;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "mydatabase.db";
        private static final int DATABASE_VERSION = 1;

        // Statement di creazione del db
        private static final String DATABASE_CREATE = "create table "
                + DATABASE_TABLE + " ("
                + KEY_RECIPEID + " integer primary key autoincrement, "
                + KEY_NAME + " text not null, "
                + KEY_DISHTYPE + " text not null, "
                + KEY_FILENAME + " text not null, "
                + KEY_PREPARATION + " text not null, "
                + KEY_INGREDIENTS + " text not null, "
                + KEY_PREPARATIONTIME + " integer);";

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
