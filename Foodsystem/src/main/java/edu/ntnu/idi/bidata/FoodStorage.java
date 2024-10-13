package edu.ntnu.idi.bidata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for storing food ingredients.
 */
public class FoodStorage {
  private static final Logger logger = Logger.getLogger(FoodStorage.class.getName());
  //Logger to log errors instead of exception as it is more informative and easier to read.
  private final List<Ingredient> ingredients;
  //List of ingredients, this is where we store the ingredients.

  /**
   * Constructor for FoodStorage and initializing ingredients list.
   */
  public FoodStorage() {
    ingredients = new ArrayList<>(); //Creating a new arraylist of ingredients.
  }

  /**
   * Add ingredients to storage, this allows us to add the ingredient array to the txt document,
   * Making it possible to save the foods that are stored.
   *
   * @param ingredient the ingredient to add to storage
   */
  public void addToStorage(Ingredient ingredient) {
    ingredients.add(ingredient);

  }


  /**
   * Saves ingredients to a file. Writes line by line; if the file already exists
   * it appends the new elements. Otherwise, it creates a new file in the given directory.
   *
   * @param filename the name of the file to which the ingredients will be written.
   */
  public void saveIngredientsToFile(String filename) {
    String path = "idatt1003-mappe-2024-Viet-Uy/Foodsystem/src/main/java/edu/ntnu/idi/bidata/";
    String filePath = path + filename;

    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
      for (Ingredient ingredient : ingredients) {
        // Write the ingredient to the file as a string.
        writer.println(ingredient.toString());
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Could not save ingredients to file", e);
    }
  }

  /**
   * This method is under development and will be commented when it's finished.
   *
   * @param filename the name of the file you want to load ingredients from.
   */
  public void loadIngredientsFromFile(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        String name = parts[0];
        int numberOfItems = Integer.parseInt(parts[1]);
        String unit = parts[2];
        double price = Double.parseDouble(parts[3]);
        LocalDate expirationDate = LocalDate.parse(parts[4]);
        Ingredient ingredient = new Ingredient(name, numberOfItems, unit, price, expirationDate);
        ingredients.add(ingredient);
      }
    } catch (IOException e) {
      // If the file does not exist, then it will throw an error.
      logger.log(Level.SEVERE, "Could not load ingredients from file", e);
    }
  }


  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  /**
   *Calls an instance of FoodStorage and iterates through the list of ingredients in getIngredients.
   *
   * @param args the command line arguments.
   */
  public static void main(String[] args) {
    FoodStorage foodStorage = new FoodStorage();
    foodStorage.getIngredients().forEach(System.out::println);
  }
}