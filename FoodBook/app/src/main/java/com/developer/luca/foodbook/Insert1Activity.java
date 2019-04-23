package com.developer.luca.foodbook;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


public class Insert1Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private FloatingActionButton camera_fab;

    private ArrayList<ToggleButton> dishToggleButtonGroup;
    private ArrayList<ToggleButton> timeToggleButtonGroup;
    private EditText minutes_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert1);

        // Assegno titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.toolbar_insert1);


        // Cliccando sul bottone passo alla schermata di inserimento ingredienti
        fab = findViewById(R.id.next_floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert2_intent = new Intent(v.getContext(), Insert2Activity.class);
                startActivity(insert2_intent);
            }
        });

        // Cliccando sul bottone passo alla schermata di inserimento ingredienti
        camera_fab = findViewById(R.id.camera_floatingActionButton);
        camera_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent per fotocamera e galleria
                // aggiungere permessi fotocamera e memoria
            }
        });


        dishToggleButtonGroup = new ArrayList<ToggleButton>();
        dishToggleButtonGroup.add((ToggleButton) findViewById(R.id.toggleButton1));
        dishToggleButtonGroup.add((ToggleButton) findViewById(R.id.toggleButton2));
        dishToggleButtonGroup.add((ToggleButton) findViewById(R.id.toggleButton3));
        dishToggleButtonGroup.add((ToggleButton) findViewById(R.id.toggleButton4));

        timeToggleButtonGroup = new ArrayList<ToggleButton>();
        timeToggleButtonGroup.add((ToggleButton) findViewById(R.id.fast_toggleButton));
        timeToggleButtonGroup.add((ToggleButton) findViewById(R.id.medium_toggleButton));
        timeToggleButtonGroup.add((ToggleButton) findViewById(R.id.long_toggleButton));

        for(ToggleButton toggleButton : dishToggleButtonGroup){
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Radio group behaviour for ToggleButtons
                    for(ToggleButton tB : dishToggleButtonGroup){
                        tB.setChecked(tB.equals(v));
                    }
                }
            });
        }

        for(ToggleButton toggleButton : timeToggleButtonGroup){
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Radio group behaviour for ToggleButtons
                    for(ToggleButton tB : timeToggleButtonGroup){
                        tB.setChecked(tB.equals(v));
                    }

                    minutes_editText = findViewById(R.id.minutes_editText);

                    switch (v.getId()){
                        case R.id.fast_toggleButton:
                            minutes_editText.setText("15");
                            break;
                        case R.id.medium_toggleButton:
                            minutes_editText.setText("45");
                            break;
                        case R.id.long_toggleButton:
                            minutes_editText.setText("90");
                            break;
                            default:
                    }

                }
            });

        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Assegno titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.toolbar_insert1);
    }

}
