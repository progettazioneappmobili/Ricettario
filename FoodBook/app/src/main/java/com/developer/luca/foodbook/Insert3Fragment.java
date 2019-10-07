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

/** Classe relativa al frammento che gestisce la terza pagina del viewpager del attività InsertActivity
 *  Raccoglie la lista delle fasi necessarie per la preparazione della ricetta.
 *  Le fasi vengono visualizzate a schermo con dei frammenti.
 */
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


    private boolean firstTime = true;

    @Override
    public void onStart() {
        super.onStart();

        mainActivity = getActivity();
        recipe = ((InsertActivity) mainActivity).recipe;

        phases_linearLayout = view.findViewById(R.id.phases_linearLayout);

        addPhase_imageButton = view.findViewById(R.id.addPhase_imageButton);
        addPhase_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhaseFragment();
            }
        });

        // Inserisci prima fase
        // utilizzo firstTime perchè altrimenti aggiungerebbe ingredienti anche quando nascondo e riapro l'app
        if(firstTime){
            addPhaseFragment();
            firstTime = false;
        }
    }

    /**
     * Aggiunge un frammento relativo ad una fase alla lista
     */
    public void addPhaseFragment(){
        getFragmentManager().beginTransaction().add(phases_linearLayout.getId(), NewPhaseFragment.newInstance(), "PHASE_FRAGMENT_" + phases_linearLayout.getChildCount()).commit();
    }

    /**
     * Questa funzione viene chiamata per evitare di dover aggiornare la ricetta
     * ogni volta che viene agiunta o modificata una fase
     */
    public void setPhases(){
        recipe.clearPhases();
        int t = phases_linearLayout.getChildCount();
        NewPhaseFragment npf;
        Phase phase;
        int counter = 1;

        for (int i = 0; i < t; i++) {
            // Recupera il frammento del i-esima fase
            npf = (NewPhaseFragment) getFragmentManager().findFragmentByTag("PHASE_FRAGMENT_" + i);
            phase = npf.getPhase();

            // Vengono ignorate le fasi senza testo
            if (!phase.getPhaseDescription().equals("")){
                // Rinumera le fasi considerando solo quelle valide
                phase.setPhaseNumber(counter++);
                Log.d("RECIPE SET", "n:"+phase.getPhaseNumber()+" setPhases: "+phase.getPhaseDescription());
                recipe.addPhase(phase);
            }
        }
    }

}
