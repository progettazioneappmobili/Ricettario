package com.developer.luca.foodbook;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe ausiliaria che mi consente di gestire i contenuti degli elementi di tipo ExpandableListView.
 * Uso questa classe per mostrare i dettagli di una ricetta (ShowRecipeActivity, activity_show).
 */

public class ShowExpandableListAdapter extends BaseExpandableListAdapter {

    private HashMap<String, List<String>> mStringListHashMap; // es: <"Antipasto",<"Patatine",...>>, <"Primo",<"Spaghetti","Pasta",..>>
    private String[] mListHeaderGroup;
    private DataBaseWrapper dbWrapper; // comunicazione db
    private Cursor cursor; // ausiliario per scorrere i record trovati con la query

    private Activity mainActivity;

    // Per gestire i tre possibili tipi di layout che possono avere gli elementi della ExpandableListView
    private static final int CHILD_TYPE_INFOS = 0;
    private static final int CHILD_TYPE_INGRED = 1;
    private static final int CHILD_TYPE_PREPARAZ = 2;

    public ShowExpandableListAdapter(HashMap<String, List<String>> stringListHashMap, Activity activity) {
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

        TextView textView = convertView.findViewById(R.id.textViewGroup);
        textView.setText(String.valueOf(getGroup(groupPosition)));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mainActivity.getLayoutInflater();

        int childType = getChildType(groupPosition, childPosition);

        // We need to create a new "cell container"
        if (convertView == null || (!convertView.getTag().equals(childType))) {
            switch (childType) {
                case CHILD_TYPE_INFOS:
                    convertView = inflater.inflate(R.layout.expandable_list_item_recipe_infos, null);
                    convertView.setTag(childType);
                    break;
                case CHILD_TYPE_INGRED:
                    convertView = inflater.inflate(R.layout.expandable_list_item_recipe_ingredients, null);
                    convertView.setTag(childType);
                    break;
                case CHILD_TYPE_PREPARAZ:
                    convertView = inflater.inflate(R.layout.expandable_list_item_recipe_preparation, null);
                    convertView.setTag(childType);
                    break;
                default:
                    // Maybe we should implement a default behaviour but it should be ok we know there are 4 child types right?
                    break;
            }
        }
        // We'll reuse the existing one
        else {
            // There is nothing to do here really we just need to set the content of view which we do in both cases
        }

        switch (childType) {
            case CHILD_TYPE_INFOS:
                // Inizializzo questa classe per la comunicazione con il db
                dbWrapper = new DataBaseWrapper(convertView.getContext());

                String contenuti = String.valueOf(getChild(groupPosition, childPosition));
                ArrayList<String> cont = split(contenuti); // divido info del piatto dall'id

                TextView textView = convertView.findViewById(R.id.textViewRecipeInfos);
                textView.setText(cont.get(0)); // info del piatto

                final Long id_piatto = Long.parseLong(cont.get(1)); // id

                ImageView imgview = convertView.findViewById(R.id.imageViewShowRecipe); // immagine a sx nella schermata
                imgview = setImage(id_piatto, imgview, convertView); // scelgo l'immagine in base al nome del file (db)

                break;
            case CHILD_TYPE_INGRED:
                TextView textView2 = convertView.findViewById(R.id.textViewIngredients);
                textView2.setText( String.valueOf(getChild(groupPosition, childPosition)) ); // mi faccio passare la stringa con gli ingredienti e
                // le loro quantita, separati da \n
                break;
            case CHILD_TYPE_PREPARAZ:
                TextView textView3 = convertView.findViewById(R.id.textViewPreparation);
                textView3.setText( String.valueOf(getChild(groupPosition, childPosition)) ); // mi faccio passare la stringa con i passi della
                // preparazione, separati da \n
                break;
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getChildTypeCount() {
        return 3;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        switch (groupPosition) {
            case 0:
                return CHILD_TYPE_PREPARAZ;
            case 1:
                return CHILD_TYPE_INGRED;
            case 2:
                return CHILD_TYPE_INFOS;
            default:
                return CHILD_TYPE_PREPARAZ; // primo gruppo in ordine
        }
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
        if (filename.equals("dish_icon")){ // icona di default => foto non presente
            img.setImageResource(R.drawable.dish_icon);
        }else{ // icona presente, cerco l'id del file
            //int resID = convertView.getResources().getIdentifier(filename , "drawable", "com.developer.luca.foodbook");
            //img.setImageResource(resID);

            try {
                Uri contentURI = Uri.parse(filename);
                /*

                Bitmap thumbnailBitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), contentURI);

                if (thumbnailBitmap != null){
                    img.setImageBitmap(thumbnailBitmap);
                } else {
                    img.setImageResource(R.drawable.dish_icon);
                }
                */
                //img.setImageBitmap(BitmapFactory.decodeFile(Uri.parse(filename).getPath()));

                InputStream is = mainActivity.getContentResolver().openInputStream(contentURI);
                img.setImageBitmap(BitmapFactory.decodeStream(is));
                is.close();

            } catch (Exception e) {
                e.printStackTrace();
                img.setImageResource(R.drawable.dish_icon);
            }
        }
        return img;
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
        return filename; // se arrivo qui => db vuoto
    }

    /**
     * Data una stringa contenente valori separati da \n separo i primi tre valori dall'ultimo
     * @param contenuti stringa da analizzare
     * @return ArrayList che nella prima posizione contiene: "nome ricetta\ntipo portata\ntempo preparazione",
     * mentre nella seconda posizione contiene l'id del piatto
     */
    private ArrayList<String> split(String contenuti) {
        ArrayList<String> result = new ArrayList<>();
        int count = 0; // numero di caratteri = '\n'
        StringBuilder contenuto = new StringBuilder();
        StringBuilder id_piatto = new StringBuilder();
        while(contenuti.length() > 0){
            if(contenuti.charAt(0) == '\n'){
                contenuto.append(contenuti.charAt(0));
                count = count + 1;
            }else if(count > 2){ // ho trovato 3 '\n'
                id_piatto.append(contenuti.charAt(0));
            }else{ // carattere != da \n
                contenuto.append(contenuti.charAt(0));
            }
            contenuti = contenuti.substring(1);
        }
        result.add(contenuto.toString());
        result.add(id_piatto.toString());
        return result;
    }

}

