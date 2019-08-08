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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;

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

    // Fragment widgets
    private FloatingActionButton camera_fab;

    private EditText recipeName_editText;
    private ArrayList<ToggleButton> dishToggleButtonGroup;
    private ArrayList<ToggleButton> timeToggleButtonGroup;
    private EditText minutes_editText;

    private ImageView imageView;

    private static final int REQUEST_IMAGE_GALLERY = 1, REQUEST_IMAGE_CAMERA = 2;

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

        imageView = view.findViewById(R.id.imageView);

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

        // Observer pattern
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
                        case R.id.toggleButton1:
                            recipe.setDishType(Recipe.DishType.FIRST);
                            break;
                        case R.id.toggleButton2:
                            recipe.setDishType(Recipe.DishType.SECOND);
                            break;
                        case R.id.toggleButton3:
                            recipe.setDishType(Recipe.DishType.APPETIZER);
                            break;
                        case R.id.toggleButton4:
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
        dishToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.toggleButton1));
        dishToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.toggleButton2));
        dishToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.toggleButton3));
        dishToggleButtonGroup.add((ToggleButton) view.findViewById(R.id.toggleButton4));
    }

    // Radio group behaviour for ToggleButtons
    // rimane selezionato solo un pulsante tra quelli presenti nel array
    // Si potrebbe anche utilizzare direttamente dei radio buttons ma dovremmo modificarne l'aspetto
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


    // Modificato a partire da:
    // https://demonuts.com/pick-image-gallery-camera-android/
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
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (galleryIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
            startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
        }
    }

    // Richiedi i permessi per utilizzare la fotocamera se necessario
    private void takePhotoFromCamera() {

        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            invokeCamera();
        } else {
            ActivityCompat.requestPermissions(mainActivity, new String[] {Manifest.permission.CAMERA}, REQUEST_IMAGE_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_IMAGE_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                invokeCamera();
            } else {
                Toast.makeText(mainActivity, "Inpossibile accedere alla Fotocamera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void invokeCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), contentURI);
                    imageView.setImageBitmap(imageBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mainActivity, "Impossibile aprire file!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == REQUEST_IMAGE_CAMERA) {

            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap thumbnailBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(thumbnailBitmap);
            }

        }

    }
}
