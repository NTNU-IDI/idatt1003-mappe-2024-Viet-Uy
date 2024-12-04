package edu.ntnu.idi.bidata;

import java.time.LocalDate;

/**
 * Ingredients class, this is where ingredients are to be added and stored.
 */
public class Ingredient {
  private final String name;
  private final String unit;
  private final int numberOfUnits;
  private final double price;
  private final LocalDate expirationDate;

  /**
   * Constructor for Ingredient. makes it possible to create an ingredient object.
   *
   * @param name name of food.
   * @param unit unit of measurement.
   * @param numberOfUnits number of items.
   *
   * @param price price of food.
   * @param expirationDate expiration date of food.
   */
  public Ingredient(
      String name,
      String unit,
      int numberOfUnits,
      double price,
      LocalDate expirationDate) {

    this.name = name;
    this.numberOfUnits = numberOfUnits;
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
            + ", numberOfUnits=" + numberOfUnits
            + ", price=" + price
            + ", expirationDate=" + expirationDate
            + '}';
  }

  /**
   * Gets name of ingredient.
   */
  public String getName() {
    return name;
  }

  public String getUnit() {
    return unit;
  }

  public int getNumberOfItems() {
    return numberOfUnits;
  }

  public double getPrice() {
    return price;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }


}
