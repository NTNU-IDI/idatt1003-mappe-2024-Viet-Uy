package edu.ntnu.idi.bidata;

import java.util.ArrayList;

// This class represents a recipe, which consists of a name, instructions,
// ingredients and the number of people it serves.

/**
 * Recipe class, this is where recipes are to be added and stored.
 */
public record Recipe(String name, String instructions, ArrayList<IngredientInfo> ingredients,
                     int servings) {

  @Override
public String toString() {
    StringBuilder sb =
        new StringBuilder();
    sb.append("Recipe Name: ").append(name)
        .append("\n");
    sb.append("Ingredients:\n");
    for (IngredientInfo ingredient : ingredients) {
      sb.append(ingredient.toString()).append(
          "\n");
    }
    sb.append("Instructions: ").append(instructions)
        .append("\n");
    sb.append("Number of people: ").append(servings)
        .append("\n");
    sb.append("\n");
    return sb.toString();
  }

}
