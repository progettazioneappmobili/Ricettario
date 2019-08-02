package com.developer.luca.foodbook;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


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
        recipe = new Recipe();
        mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        sectionsPagerAdapter.addFragment(new Insert1Fragment(), "Informazioni");
        sectionsPagerAdapter.addFragment(new Insert2Fragment(), "Ingredienti");
        sectionsPagerAdapter.addFragment(new Insert3Fragment(), "Preparazione");

        vP.setAdapter(sectionsPagerAdapter);
        vP.setOffscreenPageLimit(2);

        vP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {

                if (currentViewPagerItem < i && !isInsertCompiled(currentViewPagerItem)){
                    viewPager.setCurrentItem(currentViewPagerItem);
                    return ;
                }

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
                ((Insert2Fragment)viewPager.getAdapter().instantiateItem(viewPager, 1)).setIngredients();
                
                if (recipe.getIngredients().isEmpty()){
                    Toast.makeText(mainActivity, "Inserisci almeno un ingrediente!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case 2:
                // Salva i passi nella ricetta
                ((Insert3Fragment)viewPager.getAdapter().instantiateItem(viewPager, 2)).setPhases();

                if (recipe.getPhases().isEmpty()){
                    Toast.makeText(mainActivity, "Inserisci almeno un passo!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
        }

        return true;
    }


    public void nextPage(int pos){
        if(pos < viewPager.getAdapter().getCount()){
            viewPager.setCurrentItem(pos + 1);
        } else {
            // TODO: vai alla prossima schermata
            isInsertCompiled(pos);
        }
    }

    public void prevPage(int pos){
        if(pos > 0)
            viewPager.setCurrentItem(pos - 1);
    }

    @Override
    public void onBackPressed() {
        int count = viewPager.getCurrentItem();
        if (count == 0) {
            showAlertDialog();
        } else {
            prevPage(count);
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    private void showAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Anullare l'inserimento?");
        alertDialog.setPositiveButton("Sì", dialogClickListener);
        alertDialog.setNegativeButton("No", dialogClickListener);
        alertDialog.show();
    }

}
