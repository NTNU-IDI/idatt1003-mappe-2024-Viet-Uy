package edu.ntnu.idi.bidata;

import java.util.HashMap;
import java.util.Scanner;

/**
 * A class to represent a cookbook.
 */
public class CookBook {
  private final RecipeManager recipeManager;
  private final String name;
  private final HashMap<String, IngredientInfo> ingredients;
  private final String instructions;


  /**
   * Create a new CookBook.
   *
   * @param name         the name of the cookbook.
   * @param ingredients  the ingredients available in the cookbook.
   * @param instructions the instructions for the cookbook.
   */
  public CookBook(String name, HashMap<String, IngredientInfo> ingredients, String instructions) {
    this.recipeManager = new RecipeManager();
    this.name = name;
    this.ingredients = ingredients;
    this.instructions = instructions;
  }

  public String getName() {
    return name;
  }

  /**
   * Add a recipe to the cookbook.
   *
   * @param scanner the scanner to read input from the user.
   */
  public void addRecipe(Scanner scanner) {
    recipeManager.addRecipe(scanner, ingredients, instructions);
  }

  /**
   * Show all recipes in the cookbook.
   */
  public void showRecipe(String filename) {
    recipeManager.showRecipe(filename);
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