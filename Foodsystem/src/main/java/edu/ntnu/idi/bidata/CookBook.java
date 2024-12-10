package edu.ntnu.idi.bidata;

import java.util.HashMap;

/**
 * A class to represent a cookbook.
 */
public class CookBook {
  private final RecipeManager recipeManager;
  private final String name;

  /**
   * Create a new cookbook.
   *
   * @param name         the name of the cookbook.
   * @param recipes      the recipes in the cookbook.
   */
  public CookBook(String name, HashMap<String, Recipe> recipes) {
    this.recipeManager = new RecipeManager();
    this.name = name;
  }

  public RecipeManager getRecipeManager() {
    return recipeManager;
  }

  public String getName() {
    return name;
  }

  /**
   * Suggest recipes based on the ingredients in the food storage.
   *
   * @param foodStorage the food storage to suggest recipes from.
   * @param filename    the name of the file to write the suggested recipes to.
   */
  public void suggestRecipes(FoodStorage foodStorage, String filename) {
    recipeManager.suggestRecipes(foodStorage, filename);
  }

  //Returns the toString method of recipeManager to display the recipes in the cookbook
  @Override
  public String toString() {
    return recipeManager.toString();
  }
}