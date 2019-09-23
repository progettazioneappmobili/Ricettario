package com.developer.luca.foodbook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* Classe relativa al frammento che gestisce la prima pagina del viewpager del attività InsertActivity
   Raccoeglie informazioni generali sulla ricetta:
   - Nome
   - Tipo di piatto (primo, secondo...)
   - Tempo di preparazione indicativo (veloce, medio, lungo)
   - Tempo di preparazione in minuti
   - Foto del piatto
*/
public class Insert1Fragment extends Fragment {

    private Activity mainActivity;
    private View view;
    private Recipe recipe;


    private FloatingActionButton camera_fab;

    private EditText recipeName_editText;
    private ArrayList<ToggleButton> dishToggleButtonGroup;
    private ArrayList<ToggleButton> timeToggleButtonGroup;
    private EditText minutes_editText;

    private ImageView imageView;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1, REQUEST_IMAGE_CAMERA = 2, REQUEST_IMAGE_GALLERY =3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_insert1, container, false);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        // Recupera la ricetta dal attività principale
        mainActivity = getActivity();
        recipe = ((InsertActivity) mainActivity).recipe;


        recipeName_editText = view.findViewById(R.id.recipeName_editText);

        // Aggiunge tutti i pulsanti relativi al tipo di piatto al array dishToggleButtonGroup
        setDishToggleButtonGroup();
        // Aggiunge tutti i pulsanti relativi al tempo di preparazione al array timeToggleButtonGroup
        setTimeToggleButtonGroup();

        minutes_editText = view.findViewById(R.id.minutes_editText);

        imageView = view.findViewById(R.id.photo_imageView);

        // Aggiorna il nome della ricetta quando questo viene mdificato
        recipeName_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                recipe.setName(s.toString());
            }
        });

        // Utilizzo un observer pattern per aggiornare il campo di testo dei minuti o i bottoni di TimeType
        // quando l'altro viene modificato. In questo modo quando scrivo il numero dei minuti viene selezionato
        // automaticamente il bottone corretto e quando seleziono un bottone pulisco il campo di testo e lascio
        // la durata approssimativa della ricetta come hint.
        Recipe.addRecipeTimeChangedListener(new Recipe.RecipeTimeChangedListener() {

            // Quando cambiano i toggle button cambio i minuti
            @Override
            public void OnTimeTypeChanged() {
                minutes_editText.setText("");
                minutes_editText.setHint("" + ((InsertActivity) mainActivity).recipe.getMinutes());
            }


            // Quando cambiano i minuti aggiorno i toggle button
            @Override
            public void OnMinutesChanged() {
                switch (((InsertActivity) mainActivity).recipe.getTimeType()) {
                    case FAST:
                        toggleButtonGroupRadioBehaviour(timeToggleButtonGroup, (CompoundButton) view.findViewById(R.id.fast_toggleButton));
                        break;
                    case MEDIUM:
                        toggleButtonGroupRadioBehaviour(timeToggleButtonGroup, (CompoundButton) view.findViewById(R.id.medium_toggleButton));
                        break;
                    case LONG:
                        toggleButtonGroupRadioBehaviour(timeToggleButtonGroup, (CompoundButton) view.findViewById(R.id.long_toggleButton));
                        break;
                    default:
                }
            }
        });


        for (ToggleButton toggleButton : dishToggleButtonGroup) {

            // Quando viene premuto un pulsante di dishToggleButtonGroup aggiorna la recipe
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleButtonGroupRadioBehaviour(dishToggleButtonGroup, (CompoundButton) v);

                    switch (v.getId()) {
                        case R.id.dishType1_toggleButton:
                            recipe.setDishType(Recipe.DishType.FIRST);
                            break;
                        case R.id.dishType2_toggleButton:
                            recipe.setDishType(Recipe.DishType.SECOND);
                            break;
                        case R.id.dishType3_toggleButton:
                            recipe.setDishType(Recipe.DishType.APPETIZER);
                            break;
                        case R.id.dishType4_toggleButton:
                            recipe.setDishType(Recipe.DishType.DESSERT);
                            break;
                    }

                    // Nascondi automaticamente la tastiera per migliorare l'esperienza del utente
                    hideSoftKeyboard(mainActivity, v);
                }
            });

        }

        for (ToggleButton toggleButton : timeToggleButtonGroup) {

            // Quando viene premuto un pulsante di timeToggleButtonGroup aggiorna la recipe
            // ci pensarà l'observer pattern ad aggiornare i minuti
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleButtonGroupRadioBehaviour(timeToggleButtonGroup, (CompoundButton) v);

                    switch (v.getId()) {
                        case R.id.fast_toggleButton:
                            recipe.setTimeType(Recipe.TimeType.FAST);
                            break;
                        case R.id.medium_toggleButton:
                            recipe.setTimeType(Recipe.TimeType.MEDIUM);
                            break;
                        case R.id.long_toggleButton:
                            recipe.setTimeType(Recipe.TimeType.LONG);
                            break;
                        default:
                    }
                    hideSoftKeyboard(mainActivity, v);
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

                if (s.toString().equals("")) {
                    //Imposta l'hint dei minuti al valore corrispondente al timeType
                    recipe.setMinutes(recipe.getTimeType().getMinutes());
                    minutes_editText.setHint("" + recipe.getMinutes());
                } else {
                    try{
                        recipe.setMinutes(Integer.parseInt(s.toString()));
                    } catch (Exception e){
                        minutes_editText.setText("");
                    }
                }
            }
        });


        camera_fab = view.findViewById(R.id.camera_floatingActionButton);
        camera_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Controlla i permessi prima di utilizzare camera e/o memoria
                if (checkAndRequestPermissions())
                    showPictureDialog();
            }
        });
    }


    private void setTimeToggleButtonGroup() {
        timeToggleButtonGroup = new ArrayList<>();
        timeToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.fast_toggleButton));
        timeToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.medium_toggleButton));
        timeToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.long_toggleButton));
    }

    private void setDishToggleButtonGroup() {
        dishToggleButtonGroup = new ArrayList<>();
        dishToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.dishType1_toggleButton));
        dishToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.dishType2_toggleButton));
        dishToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.dishType3_toggleButton));
        dishToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.dishType4_toggleButton));
    }

    // Radio group behaviour for ToggleButtons
    // rimane selezionato solo un pulsante tra quelli presenti nel array
    // Si potrebbero anche utilizzare direttamente dei radio buttons ma dovremmo modificarne l'aspetto
    private void toggleButtonGroupRadioBehaviour(ArrayList<ToggleButton> toggleButtonGroup, CompoundButton toggledButton){
        for(ToggleButton tB : toggleButtonGroup){
            if (toggledButton.getId() != tB.getId() && tB.isChecked()) tB.setChecked(false);
        }

        toggledButton.setChecked(true);
    }


    // Per avere un flow di inserimento più fluido nascondo la tastiera quando viene selezionato un pulsante
    public static void hideSoftKeyboard (Activity activity, View view){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }


    // Controlla se sono stati dati i permessi per accedere alla camera e alla memoria, in caso contrario li chiede.
    private  boolean checkAndRequestPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA);
        int galleryWritePermission = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (galleryWritePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS){

            // Se tutti i permessi sono stati accettati
            for(int i = 0; i<2; i++){
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    return;
            }

            // Apri la finestra di dialogo
            showPictureDialog();
        }
    }

    // Mostra una finestra di dialogo in cui scegliere se prendere un immagine dalla galleria o scattare una nuova foto
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(mainActivity);
        pictureDialog.setTitle("Aggiungi immagine");
        String[] pictureDialogItems = {
                "Scegli immagine dalla Galleria",
                "Cattura foto dalla Fotocamera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                invokeGallery();
                                break;
                            case 1:
                                invokeCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    // È stata scelta l'opzione di utilizare la camera.
    // Crea l'intent adeguato e prepara un file in cui salvare la foto, poi crea una nuova attività.
    private void invokeCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(mainActivity, "Impossibile creare file!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mainActivity, "com.developer.luca.foodbook", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
            }
        }
    }

    // Crea il file immagine in cui verrà salfata la foto
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        String imageFileName = "" + Calendar.getInstance().getTimeInMillis();

        File storageDir = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // È stata scelta l'opzione di scegliere una immagine presente nella galleria.
    // Crea l'intent adeguato e chiama una nuova attività.
    private void invokeGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (galleryIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
            startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (data != null) {
                // È ternimata l'attività della galleria, ho ottenuto l'uri dell'immagine selezionata
                Uri contentURI = data.getData();
                try {
                    // Recupera bitmap da URI immagine selezionata e imposta l'imageview
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), contentURI);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageBitmap(imageBitmap);

                    // Crea file in cartella personale
                    String imageFileName = "" + Calendar.getInstance().getTimeInMillis();
                    File storageDir = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File image = File.createTempFile(imageFileName, ".jpg", storageDir);

                    // Salva immagine in file
                    FileOutputStream out = new FileOutputStream(image);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    // Aggiorna la ricetta
                    recipe.setImageUri(Uri.fromFile(image));
                    Log.d("RECIPE SET", "uri: "+Uri.fromFile(image).toString());

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mainActivity, "Impossibile aprire file!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAMERA) {
            File file = new File(mCurrentPhotoPath);
            // È terminata l'attività della camera, l'immagine è gia salvata.
            try {
                Uri contentURI = Uri.fromFile(file);
                Bitmap thumbnailBitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), contentURI);
                recipe.setImageUri(contentURI);
                Log.d("RECIPE SET", "uri: "+contentURI.toString());

                if (thumbnailBitmap != null){
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageBitmap(thumbnailBitmap);
                }
            } catch (IOException e) {
                Toast.makeText(mainActivity, "Errore nel ottenere la foto!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
