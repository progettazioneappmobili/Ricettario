package com.developer.luca.foodbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che permette di cercare una ricetta tramite un form che consente di inserire
 * dei filtri per i valori di alcuni parametri (es: tempo di preparazione, nome della ricetta,..).
 */

public class SearchActivity extends AppCompatActivity {

    private DataBaseWrapper dbWrapper;

    private Toolbar toolbar;
    private Activity mainActivity;

    private LinearLayout ingredients_linearLayout;
    private ImageButton addIngredient_imageButton;
    private FloatingActionButton search_floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mainActivity = this;

        // Assegno il titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.search_toolbar);

        dbWrapper = new DataBaseWrapper(this); // per la query di ricerca ricette

    }

    private boolean firstTime = true;

    @Override
    protected void onStart(){
        super.onStart();

        // Assegno il titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.search_toolbar);

        ingredients_linearLayout = findViewById(R.id.ingredientsSearch_linearLayout);

        addIngredient_imageButton = findViewById(R.id.addIngredientSearch_imageButton);
        addIngredient_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientFragment();
            }
        });

        hideKeyBoardOnClickAllToggleButtons();

        search_floatingActionButton = findViewById(R.id.search_floatingActionButton);
        search_floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultsIntent = new Intent(v.getContext(), ResultsActivity.class);
                // Get recipe info from search form
                ArrayList<String> dishtypes = getDishTypeList();
                String [] dishTypes =  dishtypes.toArray(new String[dishtypes.size()]);
                ArrayList<String> timetypes = getTimeTypeList();
                String [] timeTypes =  timetypes.toArray(new String[timetypes.size()]);
                ArrayList<String> ingred = getIngredientsList();
                String [] ingredients =  ingred.toArray(new String[ingred.size()]);
                ArrayList<String> ids = getSearchResults(getRecipeName(), dishTypes, timeTypes, ingredients);
                resultsIntent.putStringArrayListExtra("ids", ids);
//                resultsIntent.putExtra("name", getRecipeName());
//                resultsIntent.putExtra("dishType", getDishTypeList());
//                resultsIntent.putExtra("timeType", getTimeTypeList());
//                resultsIntent.putExtra("ingredients", getIngredientsList());
//                setResult(Activity.RESULT_OK, resultsIntent);
                startActivity(resultsIntent);
            }
        });


        // Inserisci primo ingrediente
        // utilizzo firstTime perchè altrimenti aggiungerebbe ingredienti anche quando nascondo e riapro l'app
        if (firstTime){
            addIngredientFragment();
            firstTime = false;
        }
    }

    private void hideKeyBoardOnClickAllToggleButtons() {
        final View.OnClickListener hideKeyBoardOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(mainActivity, v);
            }
        };

        findViewById(R.id.toggleButton1Search).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.toggleButton2Search).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.toggleButton3Search).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.toggleButton4Search).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.fastSearch_toggleButton).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.mediumSearch_toggleButton).setOnClickListener(hideKeyBoardOnClick);
        findViewById(R.id.longSearch_toggleButton).setOnClickListener(hideKeyBoardOnClick);
    }

    // Aggiunge un frammento relativo ad un ingrediente alla lista
    public void addIngredientFragment(){
        getSupportFragmentManager().beginTransaction().add(ingredients_linearLayout.getId(), SearchIngredientFragment.newInstance(), "INGREDIENT_FRAGMENT_" + ingredients_linearLayout.getChildCount()).commit();
    }


    private String getRecipeName(){
        EditText recipeNameSearch_editText = findViewById(R.id.recipeNameSearch_editText);

        return recipeNameSearch_editText.getText().toString().trim();
    }

    private ArrayList<String> getDishTypeList(){
        ToggleButton toggleButton1Search = findViewById(R.id.toggleButton1Search);
        ToggleButton toggleButton2Search = findViewById(R.id.toggleButton2Search);
        ToggleButton toggleButton3Search = findViewById(R.id.toggleButton3Search);
        ToggleButton toggleButton4Search = findViewById(R.id.toggleButton4Search);

        ArrayList<String> returnList = new ArrayList<>();

        if(toggleButton1Search.isChecked())
            returnList.add(Recipe.DishType.FIRST.getDishTypeString());

        if(toggleButton2Search.isChecked())
            returnList.add(Recipe.DishType.SECOND.getDishTypeString());

        if(toggleButton3Search.isChecked())
            returnList.add(Recipe.DishType.APPETIZER.getDishTypeString());

        if(toggleButton4Search.isChecked())
            returnList.add(Recipe.DishType.DESSERT.getDishTypeString());

        // Nessuno selezionato == tutti selezionati
        if(returnList.isEmpty()){
            returnList.add(Recipe.DishType.FIRST.getDishTypeString());
            returnList.add(Recipe.DishType.SECOND.getDishTypeString());
            returnList.add(Recipe.DishType.APPETIZER.getDishTypeString());
            returnList.add(Recipe.DishType.DESSERT.getDishTypeString());
        }

        return returnList;
    }

    private ArrayList<String> getTimeTypeList(){
        ToggleButton fastSearch_toggleButton = findViewById(R.id.fastSearch_toggleButton);
        ToggleButton mediumSearch_toggleButton = findViewById(R.id.mediumSearch_toggleButton);
        ToggleButton longSearch_toggleButton = findViewById(R.id.longSearch_toggleButton);

        ArrayList<String> returnList = new ArrayList<>();

        if(fastSearch_toggleButton.isChecked())
            returnList.add(Recipe.TimeType.FAST.getTimeTypeString());

        if(mediumSearch_toggleButton.isChecked())
            returnList.add(Recipe.TimeType.MEDIUM.getTimeTypeString());

        if(longSearch_toggleButton.isChecked())
            returnList.add(Recipe.TimeType.LONG.getTimeTypeString());

        // Nessuno selezionato == tutti selezionati
        if(returnList.isEmpty()){
            returnList.add(Recipe.TimeType.FAST.getTimeTypeString());
            returnList.add(Recipe.TimeType.MEDIUM.getTimeTypeString());
            returnList.add(Recipe.TimeType.LONG.getTimeTypeString());
        }

        return returnList;
    }

    private ArrayList<String> getIngredientsList(){

        ArrayList<String> list = new ArrayList<>();

        int t = ingredients_linearLayout.getChildCount();
        for (int i = 0; i < t; i++) {
            // Recupera il frammento del i-esimo ingrediente
            SearchIngredientFragment nif = (SearchIngredientFragment) getSupportFragmentManager().findFragmentByTag("INGREDIENT_FRAGMENT_"+i);

            if (nif != null && !nif.getText().trim().equals(""))
                list.add(nif.getText());
        }

        return list;
    }

    // Per avere un flow di inserimento più fluido nascondo la tastiera quando viene selezionato un pulsante
    public static void hideSoftKeyboard (Activity activity, View view){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    /**
     * Funzione che dati i parametri della ricerca inseriti dall'utente tramite form effettua la query al
     * db per ottenere i risultati e inserisce gli id delle ricette trovate in un ArrayList<String>
     * @param name: nome della ricetta
     * @param dishTypes: possibili tipi di portata
     * @param timeTypes: possibili tempi di preperazione
     * @param ingredients: ingredienti della ricetta
     * @return recipeIds: lista di id delle ricette compatibili con i parametri della ricerca
     */
    public ArrayList<String> getSearchResults(String name, String [] dishTypes, String [] timeTypes, String [] ingredients){
        dbWrapper.open();
        Cursor cursor = dbWrapper.fetchSearchedRecipes(name, dishTypes, timeTypes, ingredients); // prendo tutte le ricette con il flag preferita e del tipo che cerco
        ArrayList<String> recipeIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            String recipeId = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_RECIPEID));
            recipeIds.add(recipeId);
        }

        // Chiudo la connessione al db
        cursor.close();
        dbWrapper.close();

        return recipeIds;
    }

}
