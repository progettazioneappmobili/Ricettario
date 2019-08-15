package com.developer.luca.foodbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Classe ausiliaria che mi consente di gestire i contenuti degli elementi di tipo ExpandableListView.
 * Uso questa classe per mostrare i dettagli di una ricetta (ShowRecipeActivity, activity_show).
 */

public class ShowExpandableListAdapter extends BaseExpandableListAdapter {

    private HashMap<String, List<String>> mStringListHashMap; // es: <"Antipasto",<"Patatine",...>>, <"Primo",<"Spaghetti","Pasta",..>>
    private String[] mListHeaderGroup;
    private String forActivity; // in questo modo so che elementi visualizzare all'interno dei group item


    public ShowExpandableListAdapter(HashMap<String, List<String>> stringListHashMap, String activityName) {
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
            default:
                return null;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    // Funzione per ShowRecipeActivity 1: Foto del piatto, nome del piatto, tipo di portata e tempo di preparazione;
    // i vari campi sono separati da "\n", verranno inseriti in un unico TextView.
    private View getChildView1(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item2, parent, false);

        String contenuti = String.valueOf(getChild(groupPosition, childPosition));

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(contenuti);

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

}

