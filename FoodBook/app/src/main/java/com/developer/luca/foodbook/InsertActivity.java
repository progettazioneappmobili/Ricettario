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


public class InsertActivity extends AppCompatActivity {

    public Recipe recipe;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;


    private ProgressBar progressBar;
    private TextView currentProgress_textView;
    private TextView title_textView;

    private FloatingActionButton next_fab;

    public InsertActivity() {
        recipe = new Recipe();
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
        sectionsPagerAdapter.addFragment(new Insert2Fragment(), "Ingredienti");  // Change to Insert__2__Fragment()
        sectionsPagerAdapter.addFragment(new Insert3Fragment(), "Preparazione"); // Change to Insert__3__Fragment()

        vP.setAdapter(sectionsPagerAdapter);

        vP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
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
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public void nextPage(int pos){
        if(pos < viewPager.getAdapter().getCount())
            viewPager.setCurrentItem(pos + 1);
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
