package com.developer.luca.foodbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Insert3Fragment extends Fragment {

    private Activity mainActivity;
    private View view;
    private Recipe recipe;

    private LinearLayout phases_linearLayout;
    private ImageButton addPhase_imageButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_insert3, container, false);

        return view;
    }


    private static boolean firstTime = true;

    @Override
    public void onStart() {
        super.onStart();

        mainActivity = getActivity();
        recipe = ((InsertActivity) getActivity()).recipe;

        phases_linearLayout = view.findViewById(R.id.phases_linearLayout);

        addPhase_imageButton = view.findViewById(R.id.addPhase_imageButton);
        addPhase_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhaseFragment();
            }
        });

        // Inserisci prima fase
        if(firstTime){
            addPhaseFragment();
            firstTime = false;
        }
    }

    public void addPhaseFragment(){
        getFragmentManager().beginTransaction().add(phases_linearLayout.getId(), NewPhaseFragment.newInstance(), "PHASE_FRAGMENT_" + phases_linearLayout.getChildCount()).commit();
    }

    public void setPhases(){
        recipe.clearPhases();
        int t = phases_linearLayout.getChildCount();
        NewPhaseFragment npf;
        Phase phs;

        for (int i = 0; i < t; i++) {
            npf = (NewPhaseFragment) getFragmentManager().findFragmentByTag("PHASE_FRAGMENT_" + i);
            phs = npf.getPhase();

            if (!phs.getPhaseDescription().equals("")){
                Log.d("SET PHASE", "setPhases: "+phs.getPhaseDescription());
                recipe.addPhase(phs);
            }
        }
    }

}
