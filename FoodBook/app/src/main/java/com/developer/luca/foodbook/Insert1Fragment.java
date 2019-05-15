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

import java.io.IOException;
import java.util.ArrayList;

public class Insert1Fragment extends Fragment {

    private final String fragment1 = "FRAGMENT1";
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

        mainActivity = getActivity();
        recipe = ((InsertActivity) mainActivity).recipe;


        // Fragment code
        recipeName_editText = view.findViewById(R.id.recipeName_editText);

        setDishToggleButtonGroup();
        setTimeToggleButtonGroup();

        minutes_editText = view.findViewById(R.id.minutes_editText);

        imageView = view.findViewById(R.id.imageView);


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

            // Quando viene premuto il pulsante aggiorna la recipe
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

                    hideSoftKeyboard(mainActivity, v);
                }
            });

        }

        // TODO: refactoring per rimuovere codice duplicato (for qua sopra e qua sotto)
        //       ? spostare gli switch
        for (ToggleButton toggleButton : timeToggleButtonGroup) {

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
                    //per impostare l'hint dei minuti al giusto valore
                    recipe.setMinutes(recipe.getTimeType().getMinutes());
                    minutes_editText.setHint("" + recipe.getMinutes());
                } else {
                    recipe.setMinutes(Integer.parseInt(s.toString()));
                }

            }
        });


        // Cliccando sul bottone passo alla schermata di inserimento ingredienti
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

    private void toggleButtonGroupRadioBehaviour(ArrayList<ToggleButton> toggleButtonGroup, CompoundButton toggledButton){

        // Radio group behaviour for ToggleButtons
        for(ToggleButton tB : toggleButtonGroup){
            if (toggledButton.getId() != tB.getId() && tB.isChecked()) tB.setChecked(false);
        }

        // non sono sicuro del perché sia necessario, ma lo è
        // altrimenti il pulsante selezionato non viene impostato come selezionato
        toggledButton.setChecked(true);
    }


    // Per avere un flow di inserimento più fluido nascondo la tastiera quando viene selezionato un pulsante
    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }


    // Fonte parziale:
    // https://demonuts.com/pick-image-gallery-camera-android/
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
