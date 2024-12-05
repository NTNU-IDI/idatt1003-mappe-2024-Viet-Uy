package edu.ntnu.idi.bidata;

/**
 * Ingredients class, this is where ingredients are to be added and stored.
 */
public record IngredientInfo(String name, int amount, String unit, double price) {

  /**
   * Gets name of ingredient.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets name of ingredient.
   */
  @Override
  public String name() {
    return name;
  }

  @Override
  public String toString() {
    return "Ingredient: " + name + ", Amount: " + amount + " " + unit + ", Price: " + price;
  }
}