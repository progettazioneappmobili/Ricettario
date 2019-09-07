package com.developer.luca.foodbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
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
    private DataBaseWrapper dbWrapper; // comunicazione db
    private Cursor cursor; // ausiliario per scorrere i record trovati con la query

    Activity mainActivity;

    public MyExpandableListAdapter(HashMap<String, ArrayList<String>> stringListHashMap, Activity activity) {
        mStringListHashMap = stringListHashMap;
        mListHeaderGroup = mStringListHashMap.keySet().toArray(new String[0]);
        mainActivity = activity;
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

        // Inizializzo questa classe per la comunicazione con il db
        dbWrapper = new DataBaseWrapper(convertView.getContext());

        String piatti = String.valueOf(getChild(groupPosition, childPosition));
        // Es: "spaghetti£12§pasta£38" => nome del piatto seguito da '£' e il suo id, seguito da '§' e il nome del secondo piatto

        ArrayList<String> piatti2 = splitStrings(separator, piatti);
        // Es: <"spaghetti£12","pasta£38">

        ArrayList<String> piatto1 = splitStrings(separator2, piatti2.get(0));
        // Es: <"spaghetti","12">

        // Nome del piatto nella textview sopra l'immagine
        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(piatto1.get(0)); // nome del piatto 1

        // Id del piatto, per passarlo alla ShowRecipeActivity
        final Long id_piatto_1 = Long.parseLong(piatto1.get(1));

        // OnClickListener per le immagini presenti nella schermata (gallery) in modo da poter cambiare
        // activity quando clicco su un'immagine. In particolare da qui passo ai dettagli di una ricetta.
        ImageView imgview = convertView.findViewById(R.id.dishOne); // immagine a sx nella schermata
        imgview = setImage(id_piatto_1, imgview, convertView); // scelgo l'immagine in base al nome del file (db)
        setListener(id_piatto_1, imgview);

        // Icona ricetta preferita (da nascondere o mostrare)
        showIcon(id_piatto_1, 1, convertView);

        if(piatti2.get(1).equals("")){ // non c'e il secondo piatto => non serve estrarre id ricetta e settare il listener
            // Hide textView
            TextView textView2 = convertView.findViewById(R.id.textView2);
            textView2.setVisibility(View.INVISIBLE);
            // Hide imageView
            ImageView imgview2 = convertView.findViewById(R.id.dishTwo); // immagine a dx nella schermata
            imgview2.setVisibility(View.INVISIBLE);
            // Hide icon preferred
            ImageView icon = convertView.findViewById(R.id.starTwo);
            icon.setVisibility(View.INVISIBLE);
        }else{ // c'e anche il secondo piatto
            ArrayList<String> piatto2 = splitStrings(separator2, piatti2.get(1)); // Es: <"pasta","38">
            // Nome del piatto nella textview sopra l'immagine
            TextView textView2 = convertView.findViewById(R.id.textView2);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(piatto2.get(0)); // nome del piatto 2
            // Id del piatto, per passarlo alla ShowRecipeActivity
            final Long id_piatto_2 = Long.parseLong(piatto2.get(1));
            // ImageView e listener
            ImageView imgview2 = convertView.findViewById(R.id.dishTwo); // immagine a dx nella schermata
            imgview2 = setImage(id_piatto_2, imgview2, convertView); // scelgo l'immagine in base al nome del file (db)
            imgview2.setVisibility(View.VISIBLE);
            setListener(id_piatto_2, imgview2);
            // Icona ricetta preferita (da nascondere o mostrare)
            showIcon(id_piatto_2, 2, convertView);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * Funzione ausiliaria per effettuare lo split di una stringa in base ad un carattere separatore;
     * salvo il risultato (due stringhe) in un ArrayList.
     * @param separatore: carattere separatore
     * @param toSplit: stringa da splittare
     * @return ArrayList<String> che contiene le due stringhe risultanti dallo split della stringa di partenza,
     * la seconda puo essere vuota.
     */
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
            if(separatorFound && toSplit.length() > 0){ // se si inserisco il testo nella seconda parola
                contenuto2.append(toSplit.charAt(0));
                toSplit = toSplit.substring(1);
            }else if(!separatorFound){ // se no continuo a scrivere sulla prima
                contenuto1.append(toSplit.charAt(0));
                toSplit = toSplit.substring(1);
            }
        }

        ArrayList<String> result = new ArrayList<>();
        result.add(contenuto1.toString());
        result.add(contenuto2.toString());

        return result;
    }

    /**
     * Dato l'id di una ricetta effettuo una query al db per ottenere il nome del file che
     * contiene la foto del piatto.
     * @param id: id della ricetta di cui voglio trovare il nome del file
     * @return nome del file (stringa)
     */
    private String getFilename(long id){
        dbWrapper.open();
        cursor = dbWrapper.fetchRecipe(id);
        String filename = "";
        while (cursor.moveToNext()) {
            filename = cursor.getString(cursor.getColumnIndex(DataBaseWrapper.KEY_FILENAME));
        }
        // Chiudo la connessione al db
        cursor.close();
        dbWrapper.close();
        Log.d("LOG", "getFilename: "+ filename);
        return filename;
    }

    /**
     * Dato l'id di una ricetta effettuo una query al db per sapere se la ricetta si trova
     * fra le preferite
     * @param id: id della ricetta di cui voglio trovare il nome del file
     * @return true se si, false altrimenti
     */
    private boolean isPreferred(long id){
        dbWrapper.open();
        cursor = dbWrapper.fetchRecipe(id);
        int is_preferred = 0;
        while (cursor.moveToNext()) {
            is_preferred = cursor.getInt(cursor.getColumnIndex(DataBaseWrapper.KEY_ISPREFERRED));
        }
        // Chiudo la connessione al db
        cursor.close();
        dbWrapper.close();

        return (is_preferred != 0);
    }

    /**
     * Setto l'immagine di una ricetta (se non presente ne scelgo una di default)
     * @param id: id della ricetta
     * @param img: imageView di cui devo settare l'immagine
     * @param convertView: mi serve per cercare la risorsa
     * @return imageView con immagine settata
     */
    private ImageView setImage(long id, ImageView img, View convertView){
        String filename = getFilename(id);
        if (filename.equals("dish_icon") || filename.equals("lasagne")){ // icona di default => foto non presente
            img.setImageResource(R.drawable.dish_icon);
        }else{ // icona presente, cerco l'id del file
            //int resID = convertView.getResources().getIdentifier(filename , "drawable", "com.developer.luca.foodbook");
            //img.setImageResource(resID);

            try {
                Uri contentURI = Uri.parse(filename);
                Bitmap thumbnailBitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), contentURI);

                if (thumbnailBitmap != null){
                    img.setImageBitmap(thumbnailBitmap);
                } else {
                    img.setImageResource(R.drawable.dish_icon);
                }
            } catch (IOException e) {
                e.printStackTrace();
                img.setImageResource(R.drawable.dish_icon);
            }
        }
        return img;
    }

    /**
     * Nascondo o mostro l'icona a stella dei preferiti sulla foto del piatto
     * @param id: id della ricetta
     * @param num_piatto: per sapere l'id dell'icona
     * @param convertView: per cercare l'icona nella schermata
     */
    private void showIcon(Long id, int num_piatto, View convertView){
        int viewId;
        if(num_piatto == 1){
            viewId = R.id.starOne;
        }else{
            viewId = R.id.starTwo;
        }
        if(isPreferred(id)){
            ImageView icon = convertView.findViewById(viewId);
            icon.setVisibility(View.VISIBLE);
        }else{
            ImageView icon = convertView.findViewById(viewId);
            icon.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Funzione che data una image view e un parametro che poi devo passare ad un'altra activity
     * mi genera l'on click listener per limage view.
     * @param id_piatto: da passare al listener per fare in modo che venga passato all'activity show
     * @param imgview: image view di cui devo settare il listener
     */
    private void setListener(final Long id_piatto, ImageView imgview){
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show_recipe_intent = new Intent(v.getContext(), ShowRecipeActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", id_piatto.intValue()); // id del piatto di cui mostro i dettagli
                show_recipe_intent.putExtras(b); // passo l'id al nuovo intent
                v.getContext().startActivity(show_recipe_intent);
            }
        });
    }

}
