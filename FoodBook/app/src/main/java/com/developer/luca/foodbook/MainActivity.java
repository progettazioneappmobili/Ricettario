package com.developer.luca.foodbook;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    private FloatingActionButton fab; // bottone + sulla home

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cliccando sul bottone passo alla schermata di inserimento ricetta
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert_intent = new Intent(v.getContext(), InsertActivity.class);
                startActivity(insert_intent);
            }
        });

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
                startActivity(search_intent);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
