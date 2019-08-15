package com.developer.luca.foodbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe ausiliaria che mi consente di gestire i contenuti degli elementi di tipo ExpandableListView.
 * Questa classe gestisce anche gli id delle varie ricette che vengono poi passati alla classe che
 * mostrera i dettagli delle singole ricette.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private HashMap<String, ArrayList<String>> mStringListHashMap; // es: <"Antipasto",<"Patatine",...>>, <"Primo",<"Spaghetti","Pasta",..>>
    private String[] mListHeaderGroup;
    private Character separator = '§'; // separatore fra coppie di piatti
    private Character separator2 = '£'; // separatore fra piatto e suo id

    public MyExpandableListAdapter(HashMap<String, ArrayList<String>> stringListHashMap) {
        mStringListHashMap = stringListHashMap;
        mListHeaderGroup = mStringListHashMap.keySet().toArray(new String[0]);
    }

    @Override
    public int getGroupCount() {
        return mListHeaderGroup.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mStringListHashMap.get(mListHeaderGroup[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mListHeaderGroup[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mStringListHashMap.get(mListHeaderGroup[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition*childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_group, parent, false);

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(String.valueOf(getGroup(groupPosition)));

        return convertView;
    }

    // Funzione per ResultsActivity: gallery con imageview e textview per ogni piatto (2 piatti per ogni childView)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item, parent, false);

        String piatti = String.valueOf(getChild(groupPosition, childPosition));
        // Es: "spaghetti£12§pasta£38" => nome del piatto seguito da '£' e il suo id, seguito da '§' e il nome del secondo piatto

        ArrayList<String> piatti2 = splitStrings(separator, piatti);
        // Es: <"spaghetti£12","pasta£38">

        ArrayList<String> piatto1 = splitStrings(separator2, piatti2.get(0));
        // Es: <"spaghetti","12">

        ArrayList<String> piatto2 = splitStrings(separator2, piatti2.get(1));
        // Es: <"pasta","38">

        final Long id_piatto_1 = Long.parseLong(piatto1.get(1));
        final Long id_piatto_2 = Long.parseLong(piatto2.get(1));

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(piatto1.get(0)); // nome del piatto 1

        TextView textView2 = convertView.findViewById(R.id.textView2);
        textView2.setText(piatto2.get(0)); // nome del piatto 2

        // TODO l'id del piatto va calcolato (adesso e' una prova)

        // OnClickListener per le immagini presenti nella schermata (gallery) in modo da poter cambiare
        // activity quando clicco su un'immagine. In particolare da qui passo ai dettagli di una ricetta.
        ImageView imgview = convertView.findViewById(R.id.dishOne); // immagine a sx nella schermata
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_recipe_intent = new Intent(v.getContext(), ShowRecipeActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", id_piatto_1.intValue()); // id del piatto di cui mostro i dettagli
                show_recipe_intent.putExtras(b); // passo l'id al nuovo intent
                v.getContext().startActivity(show_recipe_intent);
            }
        });

        ImageView imgview2 = convertView.findViewById(R.id.dishTwo); // immagine a dx nella schermata
        imgview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_recipe_intent = new Intent(v.getContext(), ShowRecipeActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", id_piatto_2.intValue()); // id del piatto di cui mostro i dettagli
                show_recipe_intent.putExtras(b); // passo l'id al nuovo intent
                v.getContext().startActivity(show_recipe_intent);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    // Funzione ausiliaria per effettuare lo split di una stringa in base ad un carattere separatore;
    // salvo il risultato (due stringhe) in un ArrayList.
    private ArrayList<String> splitStrings(Character separatore, String toSplit){
        StringBuilder contenuto1 = new StringBuilder();
        StringBuilder contenuto2 = new StringBuilder();

        boolean separatorFound = false;

        while(toSplit.length() > 0){
            // controllo se ho trovato il separatore
            if(toSplit.charAt(0) == separatore){
                separatorFound = true;
                toSplit = toSplit.substring(1);
            }
            if(separatorFound){ // se si inserisco il testo nella seconda parola
                contenuto2.append(toSplit.charAt(0));
                toSplit = toSplit.substring(1);
            }else{ // se no continuo a scrivere sulla prima
                contenuto1.append(toSplit.charAt(0));
                toSplit = toSplit.substring(1);
            }
        }

        ArrayList<String> result = new ArrayList<>();
        result.add(contenuto1.toString());
        result.add(contenuto2.toString());

        return result;
    }

}
