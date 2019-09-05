package com.developer.luca.foodbook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/* Attività che gestisce l'inserimento delle informazioni necessarie alla creazione di una nuova ricetta.

   La schermata ha un view pager contenente frammenti di interfacia grafica per l'inserimento di:
   - informazioni generali
   - ingredienti
   - fasi di preparazione
   inoltre c'è un progression tracker che mostra il titolo della schermata corrente.

   Per spostarisi tra le varie pagine si possono utilizzare il floating action button e il pulsante indietro
   oppure si può utilizzare lo swipe a sinistra/destra per passare alla schermata successiva/precedente.
   Non si può passare alla schermata sucessiva se la schermata attuale non è competata correttamente.
 */
public class InsertActivity extends AppCompatActivity {

    private Activity mainActivity;

    public Recipe recipe;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private int currentViewPagerItem = 0;

    private ProgressBar progressBar;
    private TextView currentProgress_textView;
    private TextView title_textView;

    private FloatingActionButton next_fab;

    public InsertActivity() {

        // La ricetta viene tenuta nel attività principale,
        // così non c'è bisogno di passarla tra le pagine
        recipe = new Recipe();
        mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nasconde l'actionbar di default.
        if (getActionBar() != null)
            getActionBar().hide();

        setContentView(R.layout.activity_insert);

        progressBar = findViewById(R.id.progressBar);
        title_textView = findViewById(R.id.title_textView);
        currentProgress_textView = findViewById(R.id.currentProgress_textView);

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);


        next_fab = findViewById(R.id.next_floatingActionButton);
        next_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage(viewPager.getCurrentItem());
            }
        });

    }

    private void setupViewPager(ViewPager vP){
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Aggiunge le pagine al viewpager
        sectionsPagerAdapter.addFragment(new Insert1Fragment(), "Informazioni");
        sectionsPagerAdapter.addFragment(new Insert2Fragment(), "Ingredienti");
        sectionsPagerAdapter.addFragment(new Insert3Fragment(), "Preparazione");

        vP.setAdapter(sectionsPagerAdapter);

        // Impedisce la distruzione delle pagine che non vengono visualizzate
        vP.setOffscreenPageLimit(2);

        vP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {

                // Se cerco di andare alla pagina successiva e quella attuale non è compilata correttamente
                // viene rimandato alla pagina corrente.
                if (currentViewPagerItem < i && !isInsertCompiled(currentViewPagerItem)){
                    viewPager.setCurrentItem(currentViewPagerItem);
                    return ;
                }

                // Aggiorna il progress tracker per la schermata corrente
                switch (i){
                    case 0:
                        progressBar.setProgress(1); // second argument "animate = true" not supported by all API
                        currentProgress_textView.setText("1");
                        title_textView.setText(sectionsPagerAdapter.getPageTitle(i));
                        next_fab.setImageResource(R.drawable.ic_icon_arrow);
                        break;
                    case 1:
                        progressBar.setProgress(2);
                        currentProgress_textView.setText("2");
                        title_textView.setText(sectionsPagerAdapter.getPageTitle(i));
                        next_fab.setImageResource(R.drawable.ic_icon_arrow);
                        break;
                    case 2:
                        progressBar.setProgress(3);
                        currentProgress_textView.setText("3");
                        title_textView.setText(sectionsPagerAdapter.getPageTitle(i));
                        next_fab.setImageResource(R.drawable.ic_icon_tick);
                        break;
                }
                currentViewPagerItem = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });

    }

    // Se la pagina i è compilata in modo corretto ritorna true, false altrimenti.
    private boolean isInsertCompiled(int i) {
        switch (i){
            case 0:
                // Controlla se nome, tipo di piatto e tempo di preparazione sono stati impostati
                // il numero esatto di minuti e l'immmagine sono opzionali
                if (recipe.getName() == null || recipe.getName().equals("") ||
                        recipe.getDishType() == null || recipe.getTimeType() == null){
                    Toast.makeText(mainActivity, "Compila tutti i campi!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;

            case 1:
                // Salva gli ingredienti nella ricetta
                // vengono ignorati gli ingredienti senza nome
                ((Insert2Fragment)viewPager.getAdapter().instantiateItem(viewPager, 1)).setIngredients();

                // Controlla se è stato inserito almeno un ingrediente.
                if (recipe.getIngredients().isEmpty()){
                    Toast.makeText(mainActivity, "Inserisci almeno un ingrediente!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case 2:
                // Salva i passi nella ricetta
                // vengono ignorati i passi senza testo
                ((Insert3Fragment)viewPager.getAdapter().instantiateItem(viewPager, 2)).setPhases();

                // Controlla se è stato inserito almeno un passo
                if (recipe.getPhases().isEmpty()){
                    Toast.makeText(mainActivity, "Inserisci almeno un passo!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            default: return false;
        }

        return true;
    }


    // Se pos è una posizione valida passa alla pagina successiva
    // se sono state inserite tutte le informazioni per la preparazione passa alla attività successiva
    public void nextPage(int pos){
        if(pos < 0)
            return;

        if(pos < viewPager.getAdapter().getCount()-1){
            viewPager.setCurrentItem(pos + 1);
        } else {
            if( isInsertCompiled(pos)){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("name",recipe.getName());
                returnIntent.putExtra("phases", recipe.getPhasesString());
                returnIntent.putExtra("dishType", recipe.getDishType().getDishTypeString());
                if(recipe.getImageUri() != null){
                    returnIntent.putExtra("imageUri", recipe.getImageUri().toString());
                } else {
                    returnIntent.putExtra("imageUri", "dish_icon");
                }
                returnIntent.putExtra("timeType", recipe.getTimeType().getTimeTypeString());
                returnIntent.putExtra("minutes", recipe.getMinutes());
                returnIntent.putExtra("ingredients", recipe.getIngredientsString());
                returnIntent.putExtra("isPreferred", 0);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }


    // Se pos è una posizione valida passa alla pagina precendente
    public void prevPage(int pos){
        if(pos > 0)
            viewPager.setCurrentItem(pos - 1);
    }

    // Imposta il comportamento per il pulsante indietro
    // se siamo alla prima pagina chiedi la conferma per l'annullamento del inserimento
    // altrimenti passa alla pagina precedente
    @Override
    public void onBackPressed() {
        int count = viewPager.getCurrentItem();
        if (count == 0) {
            showCancelAlertDialog();
        } else {
            prevPage(count);
        }
    }


    private void showCancelAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Anullare l'inserimento?");
        alertDialog.setPositiveButton("Sì", dialogClickListener);
        alertDialog.setNegativeButton("No", dialogClickListener);
        alertDialog.show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    // Anullare l'inserimento? Sì -> Esci dal attività
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    // Anullare l'inserimento? No -> Non fare niente
                    break;
            }
        }
    };



}
