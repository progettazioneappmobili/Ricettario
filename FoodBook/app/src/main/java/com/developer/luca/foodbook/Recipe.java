package com.developer.luca.foodbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Recipe {

    public enum DishType {
        FIRST,
        SECOND,
        APPETIZER,
        DESSERT
    }

    // gli enum in java sono potenti: si possono mettere dei valori e recuperarli con un metodo
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
    //private list of ingredients;
    private ArrayList<Ingredient> ingredients;
    //private list of preparation phases;

    // Observer pattern -----
    interface RecipeTimeChangedListener {
        void OnTimeTypeChanged();
        void OnMinutesChanged();
    }

    // lista di subscribers
    private static List<RecipeTimeChangedListener> listeners;

    public Recipe(){
        listeners = new ArrayList<RecipeTimeChangedListener>();
        ingredients = new ArrayList<>();
    }

    public static void addRecipeTimeChangedListener(RecipeTimeChangedListener l){
        listeners.add(l);
    }

    public static void removeRecipeTimeChangedListener(RecipeTimeChangedListener l){
        listeners.remove(l);
    }
    // Fine Observer pattern -----

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
            // notifica del cambiamento tutti i listener (anche se è solo uno)
            listener.OnTimeTypeChanged();
        }
    }

    public int getMinutes() {
        return minutes;
    }

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
                // notifica del cambiamento tutti i listener (anche se è solo uno)
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
}
