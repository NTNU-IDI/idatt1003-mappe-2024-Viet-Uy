package edu.ntnu.idi.bidata;

import java.util.ArrayList;

public class Recipe {
    private final String name;
    private final String instructions;
    private final ArrayList<IngredientInfo> ingredients;
    private final int numberOfPeople;

    public Recipe(String name, String instructions, ArrayList<IngredientInfo> ingredients, int numberOfPeople) {
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.numberOfPeople = numberOfPeople;
    }

    public String getName() {
        return name;
    }


    public String getInstructions() {
        return instructions;
    }

    public ArrayList<IngredientInfo> getIngredients() {
        return ingredients;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recipe Name: ").append(name).append("\n");
        sb.append("Ingredients:\n");
        for (IngredientInfo ingredient : ingredients) {
            sb.append(ingredient.toString()).append("\n");
        }
        sb.append("Instructions: ").append(instructions).append("\n");
        sb.append("Number of people: ").append(numberOfPeople).append("\n");
        return sb.toString();
    }


}
