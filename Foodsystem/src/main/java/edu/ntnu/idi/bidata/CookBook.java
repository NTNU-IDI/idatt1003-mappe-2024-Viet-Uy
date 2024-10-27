package edu.ntnu.idi.bidata;

import java.util.HashMap;
import java.util.Map;

public class CookBook {
    private final String name;
    private final HashMap<String, IngredientInfo> ingredients;
    private final String instructions;

    public CookBook(String name, HashMap<String, IngredientInfo> ingredients, String instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }



    public HashMap<String, IngredientInfo> getRecipeIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recipe Name: ").append(name).append("\n");
        sb.append("Ingredients:\n");
        for (Map.Entry<String, IngredientInfo> entry : ingredients.entrySet()) {
            sb.append(entry.getValue().toString()).append("\n");
        }
        sb.append("Instructions: ").append(instructions).append("\n");
        return sb.toString();
    }

}
