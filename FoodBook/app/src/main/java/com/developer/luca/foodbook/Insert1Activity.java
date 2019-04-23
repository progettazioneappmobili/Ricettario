package com.developer.luca.foodbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


public class Insert1Activity extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<ToggleButton> dishToggleButtonGroup;
    private ArrayList<ToggleButton> timeToggleButtonGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert1);

        // Assegno titolo alla toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.toolbar_insert1);


        dishToggleButtonGroup = new ArrayList<ToggleButton>();
        dishToggleButtonGroup.add((ToggleButton) findViewById(R.id.toggleButton1));
        dishToggleButtonGroup.add((ToggleButton) findViewById(R.id.toggleButton2));
        dishToggleButtonGroup.add((ToggleButton) findViewById(R.id.toggleButton3));
        dishToggleButtonGroup.add((ToggleButton) findViewById(R.id.toggleButton4));

        timeToggleButtonGroup = new ArrayList<ToggleButton>();
        timeToggleButtonGroup.add((ToggleButton) findViewById(R.id.fast_toggleButton));
        timeToggleButtonGroup.add((ToggleButton) findViewById(R.id.medium_toggleButton));
        timeToggleButtonGroup.add((ToggleButton) findViewById(R.id.long_toggleButton));
        timeToggleButtonGroup.add((ToggleButton) findViewById(R.id.minutes_toggleButton));

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

                    EditText minutes_editText = findViewById(R.id.minutes_editText);
                    minutes_editText.setEnabled(v.getId() == R.id.minutes_toggleButton);

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
