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
   * Remove a recipe from the cookbook.
   *
   * @param name the name of the recipe to remove.
   */
  public void removeRecipe(String name) {
    recipeManager.removeRecipe(name);
  }

  /**
   * Check if the cookbook contains a recipe.
   *
   * @param name the name of the recipe to check.
   * @return true if the cookbook contains the recipe, false otherwise.
   */
  public boolean containsRecipe(String name) {
    return recipeManager.containsRecipe(name);
  }

  /**
   * Get a recipe from the cookbook.
   *
   * @param name the name of the recipe to get.
   * @return the recipe with the given name, or null if no such recipe exists.
   */
  public Recipe getRecipe(String name) {
    return recipeManager.getRecipe(name);
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

  @Override
  public String toString() {
    return recipeManager.toString();
  }
}