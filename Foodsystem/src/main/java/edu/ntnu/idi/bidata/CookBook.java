package edu.ntnu.idi.bidata;

import java.util.HashMap;

public class CookBook {
    private final HashMap<String, Recipe> recipes;

    public CookBook(String name, HashMap<String, IngredientInfo> ingredients, String instructions) {
        this.recipes = new HashMap<>();
    }

    public void addRecipe(Recipe recipe) {
        recipes.put(recipe.getName(), recipe);
    }

    public Recipe getRecipe(String name) {
        return recipes.get(name);
    }

    public void removeRecipe(String name) {
        recipes.remove(name);
    }

    public boolean containsRecipe(String name) {
        return recipes.containsKey(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Recipe recipe : recipes.values()) {
            sb.append(recipe.toString()).append("\n");
        }
        return sb.toString();
    }

}
