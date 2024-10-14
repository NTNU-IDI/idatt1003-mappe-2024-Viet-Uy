package edu.ntnu.idi.bidata;

import java.time.LocalDate;

/**
 * Ingredients class, this is where ingredients are to be added and stored.
 */
public class Ingredient {
  private final String name;
  private final String unit;
  private final int numberOfItems;
  private final double price;
  private final LocalDate expirationDate;

  /**
   * Constructor for Ingredient. makes it possible to create an ingredient object.
   *
   * @param name name of food.
   * @param unit unit of measurement.
   * @param numberOfItems number of items.
   *
   * @param price price of food.
   * @param expirationDate expiration date of food.
   */
  public Ingredient(
      String name,
      String unit,
      int numberOfItems,
      double price,
      LocalDate expirationDate) {

    this.name = name;
    this.numberOfItems = numberOfItems;
    this.unit = unit;
    this.price = price;
    this.expirationDate = expirationDate;
  }

  @Override
    public String toString() {
    //an override toString method where we convert from object to string
    return "Ingredient{"
            + "name='" + name + '\''
            + ", unit='" + unit + '\''
            + ", numberOfItems=" + numberOfItems
            + ", price=" + price
            + ", expirationDate=" + expirationDate
            + '}';
  }

  /**
   * Add all the values called in the constructor to the food storage function.
   *
   * @param foodStorage Makes it so I can interact with the food storage class.
   */
  public void addingIngredient(FoodStorage foodStorage) {
    foodStorage.addToStorage(this); //Adding the output of the toString method to the food storage.
    foodStorage.saveIngredientsToFile("ingredients.txt"); //Saving the ingredients to a file.
  }


}
