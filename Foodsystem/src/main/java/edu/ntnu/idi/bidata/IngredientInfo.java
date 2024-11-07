package edu.ntnu.idi.bidata;

/**
 * Ingredients class, this is where ingredients are to be added and stored.
 */
public record IngredientInfo(String name, int amount, String unit) {

  /**
   * Gets name of ingredient.
   */
  public String getName() {
    return name;
  }

  public int getAmount() {
    return amount;
  }

  public String getUnit() {
    return unit;
  }

  @Override
  public String toString() {
    return "Ingredient: " + name + ", Amount: " + amount + " " + unit;
  }

}