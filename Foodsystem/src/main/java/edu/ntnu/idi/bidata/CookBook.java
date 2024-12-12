package edu.ntnu.idi.bidata;

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
   */
  public CookBook(String name) {
    this.recipeManager = new RecipeManager();
    this.name = name;
  }

  public RecipeManager getRecipeManager() {
    return recipeManager;
  }

  public String getName() {
    return name;
  }

  //Returns the toString method of recipeManager to display the recipes in the cookbook
  @Override
  public String toString() {
    return recipeManager.toString();
  }
}