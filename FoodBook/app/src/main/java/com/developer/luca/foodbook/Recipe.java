package com.developer.luca.foodbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


// Classe che rappresenta la ricetta
public class Recipe {

    public enum DishType {
        FIRST,
        SECOND,
        APPETIZER,
        DESSERT
    }

    // Assegno dei valori agli enum e li rendo recuperarli con il metodo getMinutes
    public  enum  TimeType {
        FAST(30),
        MEDIUM(60),
        LONG(90);

        private int minutes;

        TimeType(int minutes){
            this.minutes = minutes;
        }

        public int getMinutes() {
            return minutes;
        }
    }

    private String name;
    private DishType dishType;
    private TimeType timeType;
    private int minutes;
    private File photo;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Phase> phases;

    // Utilizzo un observer pattern per controllare quando il valore di minutes o timeType viene modificato
    // e im questo modo mantentere la coerenza tra varie parti del UI e i dati salvati.
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

    // Impostare il timeType richiede l'aggiornamento del valore di minuti inserito precedentemente
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

    // Impostare il valore dei minuti richiede l'aggiornamento del timeType inserito precedentemente
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

    public File getPhoto() {
        return photo;
    }

    // TODO: salva file in dimensioni massime
    public void setPhoto(File photo) {
        this.photo = photo;
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

    public List<Phase> getPhases() {
        return phases;
    }

    public void clearPhases(){
        phases.clear();
    }

    public void addPhase(Phase phase) {
        phases.add(phase);
    }
}
