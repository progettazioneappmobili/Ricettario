package com.developer.luca.foodbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class NewPhaseFragment extends Fragment {

    private Activity mainActivity;
    private View view;

    private EditText phaseDescription_editText;
    private TextView phaseNumber_textView;

    private Phase phase;
    private static int phaseNumber = 1;


    public static NewPhaseFragment newInstance(){
        return new NewPhaseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_new_phase, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mainActivity = getActivity();
        phase = new Phase();

        phaseDescription_editText = view.findViewById(R.id.phaseDescription_editText);
        phaseNumber_textView = view.findViewById(R.id.phaseNumber_textView);
        phaseNumber_textView.setText(String.valueOf(phaseNumber));

        phase.setPhaseNumber(phaseNumber++);
    }

    public Phase getPhase(){
        phase.setPhaseDescription(phaseDescription_editText.getText().toString().trim());
        return phase;
    }
}