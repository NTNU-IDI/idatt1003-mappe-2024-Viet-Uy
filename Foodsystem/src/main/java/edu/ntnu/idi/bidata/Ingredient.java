package edu.ntnu.idi.bidata;
import java.time.LocalDate;

public class Ingredient {
    private final String name;
    private final int numberOfItems;
    private final String unit;
    private final double price;
    private final LocalDate expirationDate;

    public Ingredient(String name, int numberOfItems, String unit, double price, LocalDate expirationDate ) {
            //Constructor implementation
        this.name = name;
        this.numberOfItems = numberOfItems;
        this.unit = unit;
        this.price = price;
        this.expirationDate = expirationDate;
    }

@Override
    public String toString() { //an override toString method where we convert from object to string. Making it possible to write the object to a file and readable.
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", numberOfItems=" + numberOfItems +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", expirationDate=" + expirationDate +
                '}';
    }

    public void addingIngredient(FoodStorage foodStorage) {
        foodStorage.addToStorage(this); //Adding the output of the toString method to the food storage.
        foodStorage.saveIngredientsToFile("ingredients.txt"); //Saving the ingredients to a file.
    }


}
