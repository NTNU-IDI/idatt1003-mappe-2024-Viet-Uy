package edu.ntnu.idi.bidata;

import java.util.ArrayList;

// This class represents a recipe, which consists of a name, instructions, ingredients and the number of people it serves.
public record Recipe(String name, String instructions, ArrayList<IngredientInfo> ingredients,
                     int numberOfPeople) {

    @Override
    public String toString() {
        StringBuilder sb =
            new StringBuilder(); // StringBuilder is used to concatenate strings efficiently
        sb.append("Recipe Name: ").append(name)
            .append("\n"); // append() is used to add strings to the StringBuilder
        sb.append("Ingredients:\n"); // \n is used to add a newline character
        for (IngredientInfo ingredient : ingredients) { // Loop through all ingredients
            sb.append(ingredient.toString()).append(
                "\n"); // Add the string representation of the ingredient to the StringBuilder
        }
        sb.append("Instructions: ").append(instructions)
            .append("\n"); // Add the instructions to the StringBuilder
        sb.append("Number of people: ").append(numberOfPeople)
            .append("\n"); // Add the number of people to the StringBuilder
        return sb.toString(); // Return the string representation of the StringBuilder
    }


}
