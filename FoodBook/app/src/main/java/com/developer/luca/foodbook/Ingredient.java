package com.developer.luca.foodbook;

public class Ingredient {

    public enum Unit {
        GM,
        ML,
        UNIT
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
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
