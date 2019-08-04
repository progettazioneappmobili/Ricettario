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
import java.util.List;

/**
 * Classe ausiliaria che mi consente di gestire i contenuti degli elementi di tipo ExpandableListView.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private HashMap<String, List<String>> mStringListHashMap;
    private String[] mListHeaderGroup;
    private Character separator = '§';
    private String forActivity; // in questo modo so che elementi visualizzare all'interno dei group item


    public MyExpandableListAdapter(HashMap<String, List<String>> stringListHashMap, String activityName) {
        mStringListHashMap = stringListHashMap;
        mListHeaderGroup = mStringListHashMap.keySet().toArray(new String[0]);
        forActivity = activityName;
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

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        switch(forActivity){
            case ("ShowRecipe1"):
                return getChildView1(groupPosition, childPosition, convertView, parent);
            case("ShowRecipe2"):
                return getChildView2(groupPosition, childPosition, convertView, parent);
            case("ShowRecipe3"):
                return getChildView3(groupPosition, childPosition, convertView, parent);
            case ("Results"):
                return getChildView4(groupPosition, childPosition, convertView, parent);
            default:
                return null;
            }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    // Funzione per ResultsActivity: gallery con imageview e textview per ogni piatto (2 piatti per ogni childView)
    private View getChildView4(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item, parent, false);

        String piatti = String.valueOf(getChild(groupPosition, childPosition));

        ArrayList<String> piatti2 = splitStrings(piatti);

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(piatti2.get(0));

        TextView textView2 = convertView.findViewById(R.id.textView2);
        textView2.setText(piatti2.get(1));

        // TODO l'id del piatto va calcolato (adesso e' una prova)

        // OnClickListener per le immagini presenti nella schermata (gallery) in modo da poter cambiare activity quando clicco su un'immagine
        ImageView imgview = convertView.findViewById(R.id.dishOne);
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_recipe_intent = new Intent(v.getContext(), ShowRecipeActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", 1); // id del piatto di cui mostro i dettagli
                show_recipe_intent.putExtras(b); // passo l'id al nuovo intent
                v.getContext().startActivity(show_recipe_intent);
            }
        });

        ImageView imgview2 = convertView.findViewById(R.id.dishTwo);
        imgview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_recipe_intent = new Intent(v.getContext(), ShowRecipeActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", 2); // id del piatto di cui mostro i dettagli
                show_recipe_intent.putExtras(b); // passo l'id al nuovo intent
                v.getContext().startActivity(show_recipe_intent);
            }
        });

        return convertView;
    }

    // Funzione per ShowRecipeActivity 1: Foto del piatto, textview con tipo di portata e textview con tempo di preparazione
    private View getChildView1(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item2, parent, false);

        String contenuti = String.valueOf(getChild(groupPosition, childPosition));

        ArrayList<String> coppieContenuti = splitStrings(contenuti);

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(coppieContenuti.get(0));

        TextView textView2 = convertView.findViewById(R.id.textView2);
        textView2.setText(coppieContenuti.get(1));

        return convertView;
    }

    // Funzione per ShowRecipeActivity 2: lista ingredienti e loro quantita
    private View getChildView2(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item3, parent, false);

        TextView textView = convertView.findViewById(R.id.textView3);
        textView.setText( String.valueOf(getChild(groupPosition, childPosition)) ); // mi faccio passare la stringa con gli ingredienti e
        // le loro quantita, separati da \n

        return convertView;
    }

    // Funzione per ShowRecipeActivity 3: textview con i passi della preparazione
    private View getChildView3(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item4, parent, false);

        TextView textView = convertView.findViewById(R.id.textView4);
        textView.setText( String.valueOf(getChild(groupPosition, childPosition)) ); // mi faccio passare la stringa con i passi della
        // preparazione, separati da \n

        return convertView;
    }

    // Funzione ausiliaria per effettuare lo split di una stringa in base ad un carattere "separator" definito all'inizio della classe
    private ArrayList<String> splitStrings(String toSplit){
        StringBuilder contenuto1 = new StringBuilder();
        StringBuilder contenuto2 = new StringBuilder();

        boolean separatorFound = false;

        while(toSplit.length() > 0){
            if(toSplit.charAt(0) == separator){
                separatorFound = true;
                toSplit = toSplit.substring(1);
            }
            if(separatorFound){
                contenuto2.append(toSplit.charAt(0));
                toSplit = toSplit.substring(1);
            }else{
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
