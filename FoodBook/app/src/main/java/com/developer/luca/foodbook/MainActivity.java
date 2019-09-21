package com.developer.luca.foodbook;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Home page dell'applicazione: in alto abbiamo due bottoni (icona di ricerca e stella dei preferiti),
 * poi una gallery che mostra tutte le ricette, divise per tipo di portata e infine un bottone +
 * da cui e' possibile accedere all'inserimento di una nuova ricetta.
 */

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab; // bottone + sulla home
    private DataBaseWrapper dbWrapper; // comunicazione db
    private Cursor cursor; // ausiliario per scorrere i record trovati con la query

    static final int INSERT_RECIPE_REQUEST = 1;
    static final int SEARCH_RECIPE_REQUEST = 2;

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
                startActivityForResult(insert_intent, INSERT_RECIPE_REQUEST);
            }
        });

        // Inizializzo questa classe per la comunicazione con il db
        dbWrapper = new DataBaseWrapper(this);

        // Aggiungo alcuni record se db vuoto
        if (checkDb()) // restituisce true se db vuoto => aggiungo i record
            addRecords();

        configGallery();

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
                Intent search_intent = new Intent(this, SearchActivity.class);
                startActivityForResult(search_intent, SEARCH_RECIPE_REQUEST);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Dato il tipo di portata ed un ArrayList<String> su cui scrivere il risultato inserisco nell'array
     * tutti i piatti di quel tipo
     * @param tipo String con tipo di portata: "Antipasto", "Primo", "Secondo", "Dessert"
     * @param result ArrayList su cui andro a scrivere il risultato
     * @return lista di stringhe contenenti coppie di piatti e i loro id
     */
    public ArrayList<String> getRecipesByType(String tipo, ArrayList<String> result){
        dbWrapper.open();
        cursor = dbWrapper.fetchRecipeByType(tipo); // tutte le ricette di un certo tipo
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
     * Funzione usata come seed iniziale per il database: creo un certo numero di ricette con diverso tipo di portata
     * e con i loro dettagli, alcune le metto come preferite, altre no.
      */
    public void addRecords(){
        dbWrapper.open();
        dbWrapper.createRecipe("Spaghetti alla carbonara", "\u2022 Passo 1\nMettere sul fuoco una pentola con l'acqua salata per cuocere la pasta. Nel frattempo eliminate la cotenna dal guanciale e tagliatelo prima a fette e poi a striscioline spesse circa 1 cm.\n\n\u2022 Passo 2\nVersate i pezzetti in una padella antiaderente e rosolate per circa 15 minuti a fiamma media, fate attenzione a non bruciarlo altrimenti rilascerà un aroma troppo forte.", "Primo","spaghetti_carbonara",18, "Veloce", "\u2022 100 gr,  Spaghetti\n\u2022 150 gr,  Guanciale\n\u2022 6 , Tuorli di uova medie",1);
        dbWrapper.createRecipe("Bavette al pesto", "\u2022 Passo 1\nPulire le foglie di basilico con un panno morbido oppure sciacquarle delicatamente in una ciotola d'acqua fredda.\n\n\u2022 Passo 2\nPonete l'aglio sbucciato nel mortaio insieme a qualche grano di sale grosso. Cominciate a pestare e, quando l'aglio sarà ridotto in crema aggiungete le foglie di basilico contro le pareti del mortaio.", "Primo","bavette_pesto",21,"Veloce", "\u2022 200 gr,  Pasta\n\u2022 qb,  Sale\n\u2022 15 gr, Pinoli\n\u2022 50 gr, Basilico",1);
        dbWrapper.createRecipe("Focaccia alla genovese", "\u2022 Passo 1\nVersate la farina nella ciotola di una planetaria e sbriciolate dentro il lievito fresco.\n\n\u2022 Passo 2\nAggiungete metà dell'acqua e azionate la planetaria.\n\n\u2022 Passo 3\nNon appena sarà assorbita, ci vorranno circa 3 minuti, aggiungete a poco a poco la restante acqua mentre la planetaria è in movimento.\n\n\u2022 Passo 4\nQuando sarà completamente assorbita aggiungete il malto e il sale.", "Antipasto","focaccia",15,"Veloce", "\u2022 500 gr,  Farina Manitoba\n\u2022 qb,  Sale fino\n\u2022 15 gr, Olio\n\u2022 50 gr, Acqua\n\u2022 qb , Malto",1);
        dbWrapper.createRecipe("Crocchette di patate", "\u2022 Passo 1\nLavate le patate sotto l'acqua corrente per togliere residui di terra, ponetele a lessare in un tegame capiente versando acqua fino a coprirle e senza sbuciarle.\n\n\u2022 Passo 2\nUtilizzate patate il più possibile della stessa dimensione così da uniformare la cottura. Ci vorranno circa 40 minuti se le bollite oppure la metà del tempo circa con una pentola a pressione.", "Antipasto","crocchette",20,"Veloce", "\u2022 1000 gr,  Patate rosse\n\u2022 30 gr,  Tuorli\n\u2022 qb ,  Pepe nero",1);
        dbWrapper.createRecipe("Polpette", "\u2022 Passo 1\nTagliate la mollica in pezzi e versatela in un mixer dotato di lame e tritate il tutto fin quando la mollica non sarà ridotta in briciole.\n\n\u2022 Passo 2\nTenete da parte la mollica e eliminate lo spago dalle salisccie, incidetele delicatamente nel senso della lunghezza e infine privatele del budello.\n\n\u2022 Passo 3\nCon la lama di un coltello o con una forchetta schiacciate la carne.", "Secondo","polpette",31,"Media", "\u2022 220 gr,  Manzo\n\u2022 165 gr,  Salsiccia\n\u2022 25 gr,  Parmigiano\n\u2022 30 gr, Mollica di pane",0);
        dbWrapper.createRecipe("Cheescake","\u2022 Passo 1\nFondete il burro e lasciatelo intiepidire; nel frattempo ponete i biscotti  in un mixer e frullateli fino a ridurli in polvere.\n\n\u2022 Passo 2\nTrasferite i biscotti in una ciotola e versate il burro. Mescolate con un cucchiaio fino ad uniformare il composto; successivamente prendete uno stampo a cerniera e foderate la base con la carta da forno.\n\n\u2022 Passo 3\nPonete metà dei biscotti all'interno e schiacciateli con il dorso del cucchiaio per compattarli.","Dessert","cheesecake",35,"Media", "\u2022 50 gr,  Formaggio\n\u2022 3 ,  Zucchero\n\u2022 50 gr,  Burro\n\u2022 240 gr, Biscotti Digestive",0);
        dbWrapper.createRecipe("Trota al cartoccio","\u2022 Passo 1\nTagliate le verdure che serviranno per farcire la trota; lavate e pelate le carote, poi tagliatele a metà e dividete ogni metà per il lungo.\n\n\u2022 Passo 2\nOra lavate il cipollotto, eliminate la base con le radici e dividetelo a metà, poi tagliate ogni metà a bastoncini sottili.\n\n\u2022 Passo 3\nLavate e pelate le patate, e tagliate anch'esse prima a fettine e poi a bastoncini dello stesso spessore delle carote.","Secondo","trota",20,"Veloce", "\u2022 900 gr,  Trota\n\u2022 qb,  Sale\n\u2022 50 gr, Carote\n\u2022 80 gr, Patate\n\u2022 20 gr, Cipollotto fresco",1);
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

    /**
     * Inizializzo la Gallery passando le varie ricette raggruppate per tipo alla ListView
     */
    public void configGallery() {
        // Configuro la ListView
        ExpandableListView expandableListView = findViewById(R.id.expandableListViewHome);
        LinkedHashMap<String, ArrayList<String>> item = new LinkedHashMap<>(); // conterra titolo e contenuti della list view

        // Primi
        ArrayList<String> primiGroup = new ArrayList<>();
        primiGroup = getRecipesByType("Primo", primiGroup);
        item.put(getString(R.string.primi), primiGroup);

        // Secondi
        ArrayList<String> secondiGroup = new ArrayList<>();
        secondiGroup = getRecipesByType("Secondo", secondiGroup);
        item.put(getString(R.string.secondi), secondiGroup);

        // Antipasti
        ArrayList<String> antipastiGroup = new ArrayList<>();
        antipastiGroup = getRecipesByType("Antipasto", antipastiGroup);
        item.put(getString(R.string.antipasti), antipastiGroup);

        // Dolci
        ArrayList<String> dolciGroup = new ArrayList<>();
        dolciGroup = getRecipesByType("Dessert", dolciGroup);
        item.put(getString(R.string.dessert), dolciGroup);

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(item, this);
        expandableListView.setAdapter(adapter);
        expandableListView.setDividerHeight(0); // Rimuove linee tra le immagini
        expandableListView.expandGroup(0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        configGallery();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        configGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_OK)
            return;

        switch (requestCode){
            case INSERT_RECIPE_REQUEST:
                if (data != null) {
                    // TODO: inserire la ricetta nel database
                    dbWrapper.open();
                    dbWrapper.createRecipe(
                    data.getStringExtra( "name"),
                    data.getStringExtra( "phases"),
                    data.getStringExtra( "dishType"),
                    data.getStringExtra( "imageUri"),
                    data.getIntExtra( "minutes", 0),
                    data.getStringExtra( "timeType"), // Manca nel db
                    data.getStringExtra( "ingredients"),
                    data.getIntExtra( "isPreferred", 0)
                    );
                    dbWrapper.close();
                }
                break;

            case SEARCH_RECIPE_REQUEST:
                if (data != null) {
                    // TODO: cercare la ricetta
                    dbWrapper.open();
                    cursor = dbWrapper.fetchSearchedRecipes(
                            data.getStringExtra( "name"),
                            stringArrayListToArray(data.getStringArrayListExtra("dishType")),
                            stringArrayListToArray(data.getStringArrayListExtra("timeType")),
                            stringArrayListToArray(data.getStringArrayListExtra( "ingredients"))
                    );

                    while (cursor.moveToNext()){
                        Log.d("SEARCH", "RICETTA TROVATA: " + cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_NAME)));
                    }

                    dbWrapper.close();
                }
                break;
        }

    }

    private String[] stringArrayListToArray(ArrayList<String> dishType) {
        return dishType.toArray(new String[dishType.size()]);
    }
}
