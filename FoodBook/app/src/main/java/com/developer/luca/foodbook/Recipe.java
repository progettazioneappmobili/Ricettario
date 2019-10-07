package com.developer.luca.foodbook;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe che rappresenta la ricetta
 */
public class Recipe {

    /**
     * Enumerazione che rappresenta il tipo di portata della ricetta.
     * Assegno dei valori agli enum e li rendo recuperarli con il metodo getDishTypeString
     */
    public enum DishType {
        FIRST("Primo"),
        SECOND("Secondo"),
        APPETIZER("Antipasto"),
        DESSERT("Dessert");

        private String dishTypeString;

        DishType(String dishTypeString){ this.dishTypeString = dishTypeString; }

        public String getDishTypeString() { return dishTypeString; }
    }

    /**
     * Enumerazione che rappresenta la velocità indicativa con cui viene preparata una ricetta.
     * Assegno dei valori agli enum e li rendo recuperarli con i metodi getMinutes e getTimeTypeString.
     */
    public  enum  TimeType {
        FAST(30, "Veloce"),
        MEDIUM(60, "Media"),
        LONG(90, "Lunga");

        private int minutes;
        private String timeTypeString;

        TimeType(int minutes, String timeTypeString){
            this.minutes = minutes;
            this.timeTypeString = timeTypeString;
        }

        public int getMinutes() {
            return minutes;
        }
        public String getTimeTypeString(){ return timeTypeString; }
    }

    private String name;
    private DishType dishType;
    private TimeType timeType;
    private int minutes;
    private Uri imageUri;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Phase> phases;

    /**
     * Utilizzo un observer pattern per controllare quando il valore di minutes o timeType viene modificato
     * e im questo modo mantentere la coerenza tra varie parti del UI e i dati salvati.
     */
    interface RecipeTimeChangedListener {
        void OnTimeTypeChanged();
        void OnMinutesChanged();
    }

    // Lista di subscribers observer pattern
    private static List<RecipeTimeChangedListener> listeners;


    public Recipe(){
        listeners = new ArrayList<RecipeTimeChangedListener>();
        ingredients = new ArrayList<Ingredient>();
        phases = new ArrayList<Phase>();
    }

    // Observer pattern
    public static void addRecipeTimeChangedListener(RecipeTimeChangedListener l){
        listeners.add(l);
    }

    // Observer pattern
    public static void removeRecipeTimeChangedListener(RecipeTimeChangedListener l){
        listeners.remove(l);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishType getDishType() {
        return dishType;
    }

    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    /**
     * Imposta il timeType passato a parametro e aggiorna il valore di minuti corrispondente
     * @param timeType il TimeType da impostare
     */
    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;

        switch (timeType){
            case FAST: minutes = TimeType.FAST.getMinutes(); break;
            case MEDIUM: minutes = TimeType.MEDIUM.getMinutes(); break;
            case LONG: minutes = TimeType.LONG.getMinutes(); break;
            default: return;
        }

        // Observer pattern
        for (RecipeTimeChangedListener listener : listeners) {
            // Notifica del cambiamento tutti i listener (aggiorna UI)
            listener.OnTimeTypeChanged();
        }
    }

    public int getMinutes() {
        return minutes;
    }

    /**
     * Imposta il valore dei minuti passato a parametro e aggiorna il timeType corrispondente
     * @param minutes valore in minuti da impostare
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;

            if(minutes <= TimeType.FAST.getMinutes()) {
                timeType = TimeType.FAST;
            }
            else if (minutes <= TimeType.MEDIUM.getMinutes()) {
                timeType = TimeType.MEDIUM;
            }
            else {
                timeType = TimeType.LONG;
            }

            // Observer pattern
            for (RecipeTimeChangedListener listener : listeners) {
                // Notifica del cambiamento tutti i listener (aggiorna UI)
                listener.OnMinutesChanged();
            }
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }



    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void clearIngredients(){
        ingredients.clear();
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    /**
     * @return lista degli ingredienti in un'unica stringa
     */
    public String getIngredientsString(){
        StringBuilder ingredientsString = new StringBuilder();

        String prefix = "";
        for (Ingredient ingredient: ingredients) {
            ingredientsString.append(prefix).append("\u2022 ");

            if (ingredient.getQuantity() == 0){
                ingredientsString.append("qb");
            } else {
                ingredientsString.append(ingredient.getQuantity())
                        .append(" ").append(ingredient.getUnit().getUnitString());
            }

            ingredientsString.append(",  ").append(ingredient.getIngredient());
            prefix = "\n";
        }

        return ingredientsString.toString();
    }



    public List<Phase> getPhases() {
        return phases;
    }

    public void clearPhases(){
        phases.clear();
    }

    public void addPhase(Phase phase) {
        phases.add(phase);
    }

    /**
     * @return lista dei passi in un'unica stringa
     */
    public String getPhasesString(){
        StringBuilder phasesString = new StringBuilder();

        String prefix = "";
        for (Phase phase: phases) {
            phasesString.append(prefix)
                    .append("Passo ").append(phase.getPhaseNumber())
                    .append("\n").append(phase.getPhaseDescription());
            prefix = "\n\n";
        }

        return phasesString.toString();
    }
}
