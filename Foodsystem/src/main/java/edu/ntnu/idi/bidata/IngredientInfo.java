package edu.ntnu.idi.bidata;

public record IngredientInfo(String name, int amount, String unit) {
    @Override
    public String toString() {
        return "Ingredient: " + name + ", Amount: " + amount + " " + unit;
    }
}