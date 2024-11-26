package edu.ntnu.idi.bidata;

/**
 * Ingredients class, this is where ingredients are to be added and stored.
 */
public class IngredientInfo {
  private String name;
  private int amount;
  private String unit;
  private double price;

  /**
   * Constructor for Ingredient. makes it possible to create an ingredient object.
   *
   * @param name name of food.
   * @param amount amount of food.
   * @param unit unit of measurement.
   * @param price price of food.
   */
  public IngredientInfo(String name, int amount, String unit, double price) {
    this.name = name;
    this.amount = amount;
    this.unit = unit;
    this.price = price;
  }

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

  public double getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return "Ingredient: " + name + ", Amount: " + amount + " " + unit + ", Price: " + price;
  }

}