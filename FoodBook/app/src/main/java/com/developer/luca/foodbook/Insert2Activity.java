package com.developer.luca.foodbook;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Insert2Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert2);

        // Assegno titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Ingredienti");

        // Cliccando sul bottone passo alla schermata di inserimento ingredienti
        fab = findViewById(R.id.next2_floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert3_intent = new Intent(v.getContext(), Insert3Activity.class);
                startActivity(insert3_intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Assegno titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Ingredienti");
    }
}
