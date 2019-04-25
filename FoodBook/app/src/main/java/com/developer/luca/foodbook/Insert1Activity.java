package com.developer.luca.foodbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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


    private int fast_time = 30;
    private int medium_time = 60;
    private int long_time = 90;


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

        minutes_editText = findViewById(R.id.minutes_editText);

        for(ToggleButton toggleButton : dishToggleButtonGroup){
            // onCheckedChanged quando lo stato del pulsante cambia, anche con .setChecked()
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                // da questa funzione modificherò gli stati dei triggerbutton,
                // quindi devo implementare un meccanismo per evitare cicli infiniti
                boolean avoidRecursions = false;

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(avoidRecursions) return;
                    avoidRecursions = true;

                    // Radio group behaviour for ToggleButtons
                    for(ToggleButton tB : dishToggleButtonGroup){
                        if (buttonView.getId() != tB.getId() && tB.isChecked()) tB.setChecked(false);
                    }

                    // non sono sicuro del perché sia necessario, ma lo è
                    // altrimenti il pulsante selezionato non viene impostato come selezionato
                    buttonView.setChecked(isChecked);

                    avoidRecursions = false;
                }
            });

            // onClick quando viene premuto il pulsante
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSoftKeyboard(Insert1Activity.this, v);
                }
            });

        }

        for(ToggleButton toggleButton : timeToggleButtonGroup){
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                boolean avoidRecursions = false;

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    // return per evitare i trigger causati dai setChecked nel loop
                    if(avoidRecursions) return;
                    avoidRecursions = true;

                    // Radio group behaviour for ToggleButtons
                    for(ToggleButton tB : timeToggleButtonGroup){
                        if (buttonView.getId() != tB.getId() && tB.isChecked()) tB.setChecked(false);
                    }

                    buttonView.setChecked(isChecked);

                    // setHint anziché setText per evitare ancora altri trigger del TextChangedListener
                    // e perché è più elegante
                    switch (buttonView.getId()){
                        case R.id.fast_toggleButton:
                            minutes_editText.setHint(Integer.toString(fast_time));
                            break;
                        case R.id.medium_toggleButton:
                            minutes_editText.setHint(Integer.toString(medium_time));
                            break;
                        case R.id.long_toggleButton:
                            minutes_editText.setHint(Integer.toString(long_time));
                            break;
                        default:
                    }

                    avoidRecursions = false;

                }
            });

            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSoftKeyboard(Insert1Activity.this, v);
                    minutes_editText.setText("");
                }
            });
        }

        minutes_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().equals("")){
                    return;
                } else if (Integer.parseInt(s.toString()) <= fast_time) {
                    ((ToggleButton) findViewById(R.id.fast_toggleButton)).setChecked(true);
                } else if (Integer.parseInt(s.toString()) <= medium_time) {
                    ((ToggleButton) findViewById(R.id.medium_toggleButton)).setChecked(true);
                } else if (Integer.parseInt(s.toString()) > medium_time) {
                    ((ToggleButton) findViewById(R.id.long_toggleButton)).setChecked(true);
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Assegno titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.toolbar_insert1);
    }


    // Per avere un flow di inserimento più fluido nascondo la tastiera quando viene selezionato un pulsante
    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

}
