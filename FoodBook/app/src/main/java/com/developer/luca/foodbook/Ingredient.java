package com.developer.luca.foodbook;

// Classe che rappresenta un ingrediente
public class Ingredient {

    public enum Unit {
        GR("gr"), // Grammi
        ML("ml"), // Millilitri
        UNIT(""); // Quantità senza unità di muisura (eg. 3 uova) ;

        private String unitString;

        Unit(String unitString){ this.unitString = unitString; }

        public String getUnitString() { return unitString; }
    }

    private String ingredient = "";
    private int quantity = 0;
    private Unit unit = Unit.UNIT;

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity >= 0)
            this.quantity = quantity;
        else
            this.quantity = 0;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
