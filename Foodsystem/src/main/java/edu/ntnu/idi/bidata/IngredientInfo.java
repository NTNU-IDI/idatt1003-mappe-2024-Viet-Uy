package edu.ntnu.idi.bidata;

public record IngredientInfo(String name, int amount, String unit) {

    public IngredientInfo(String name, int amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "Ingredient: " + name + ", Amount: " + amount + " " + unit;
    }
}