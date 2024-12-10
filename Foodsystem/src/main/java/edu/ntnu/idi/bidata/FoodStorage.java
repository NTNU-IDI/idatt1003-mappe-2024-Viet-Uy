package edu.ntnu.idi.bidata;

import edu.ntnu.idi.bidata.exceptions.IngredientNotFound;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
   * Gets the list of ingredients.
   *
   * @return the list of ingredients.
   */

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  /**
   * Gets the ingredient by name. This the function used to get the ingredient info. in the menu.
   *
   * @param name the name of the ingredient.
   * @return the ingredient info.
   */

  public IngredientInfo getIngredients(String name) {
    return ingredients.stream() //Stream of ingredients
        .filter(ingredient -> ingredient.getName().equalsIgnoreCase(name))
        .findFirst()
        .map(ingredient -> new IngredientInfo(ingredient.getName(),
            ingredient.getNumberOfItems(), ingredient.getUnit(), ingredient.getPrice()))
        .orElse(null);
  }

  /**
   * Checks if an ingredient exists in the list of ingredients. Used for testing purposes.
   *
   * @param name the name of the ingredient.
   * @return true if the ingredient exists, false otherwise.
   */
  public boolean ingredientExists(String name) {
    return ingredients.stream().anyMatch(ingredient -> ingredient.getName().equalsIgnoreCase(name));
  }

  /**
   * Adds ingredients directly to the list of ingredients, without having to go through the scanner.
   *
   * @param ingredient the ingredient to be added.
   */
  public void addIngredientDirectly(Ingredient ingredient) {
    ingredients.add(ingredient);
  }


  /**
   * Adds ingredients to the list of ingredients.
   *
   * @param name the name of the ingredient.
   * @param unit the unit of the ingredient.
   * @param numberOfUnits the number of units of the ingredient.
   * @param price the price of the ingredient.
   * @param expirationDate the expiration date of the ingredient.
   * @return true if the ingredient is added successfully, false otherwise.
   */
  public boolean addIngredient(String name, String unit, int numberOfUnits, double price,
                               LocalDate expirationDate) {
    // Validate the input parameters
    if (name.isEmpty() || unit == null || numberOfUnits <= 0 || price <= 0
        || expirationDate == null) {
      return false; // Return false if any validation fails
    }

    // Create a new ingredient and add it to the list
    Ingredient ingredient = new Ingredient(name, unit, numberOfUnits, price, expirationDate);
    ingredients.add(ingredient);
    saveIngredientsToFile("ingredients.txt", ingredient); // Save the ingredient to the file
    return true; // Return true if the ingredient is added successfully
  }

  /**
   * Removes ingredients from the list of ingredients.
   */
  public boolean removeIngredient(String name, int unitsToRemove) {
    String filename = "ingredients.txt";
    String regex = "name='(.*?)'";
    Pattern pattern = Pattern.compile(regex);
    FoodStorage foodStorage = new FoodStorage();
    foodStorage.loadIngredientsFromFile(filename);

    if (name.isEmpty()) {
      return false;
    }

    if (!ingredientExists(name)) {
      return false;
    }

    URL resourceUrl = getClass().getClassLoader().getResource(filename);
    if (resourceUrl == null) {
      logger.log(Level.SEVERE, "Resource path is null");
      return false;
    }

    String filePath = FileHandler.getResourcePath("ingredients.txt");

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          String nameIngredient = matcher.group(1);
          if (nameIngredient.equalsIgnoreCase(name)) {
            removeLineFromFile(line, unitsToRemove);
            return true;
          }
        }
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error processing file", e);
      return false;
    }
    return false;
  }

  /**
   * Removes a line from a file.
   *
   * @param lineToRemove the line to remove.
   * @param unitsToRemove the number of units to remove.
   */
  private void removeLineFromFile(String lineToRemove, int unitsToRemove) {

    String tempFile = "temp.txt"; //Creating a new file
    String resourcePath = FileHandler.getResourcePath("ingredients.txt");
    File oldFile = new File(resourcePath); //Creating a new file
    File newFile = new File(oldFile.getParent(), tempFile); //Creating a new file

    try (BufferedReader br = new BufferedReader(new FileReader(oldFile));
         PrintWriter pw = new PrintWriter(new FileWriter(newFile))) { //Reading the file

      String currentLine;
      while ((currentLine = br.readLine()) != null) { //Reading the file line by line
        //Checking if the line is equal to the line to remove:
        if (currentLine.equals(lineToRemove)) {
          //Matching the pattern:
          Matcher matcher = Pattern.compile("numberOfUnits=(\\d+)").matcher(currentLine);
          if (matcher.find()) { //Checking if the pattern is found
            int numberOfUnits = Integer.parseInt(matcher.group(1)); //Getting the number of units
            //Checking if the number of units is greater than the units to remove:
            if (numberOfUnits > unitsToRemove) {
              if (unitsToRemove == 0) { //Checking if the units to remove is 0
                continue; //Continuing the loop
              } else { //If the units to remove is not 0
                //Calculating the new number of units:
                int newNumberOfUnits = numberOfUnits - unitsToRemove;
                //Replacing the number of units with the new number of units:
                currentLine = currentLine.replace("numberOfUnits=" + numberOfUnits,
                    "numberOfUnits=" + newNumberOfUnits);
              }
            }
          }
        }
        pw.println(currentLine); //Printing the line
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error processing file", e);
      return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(newFile)); //Reading the file
         PrintWriter pw = new PrintWriter(new FileWriter(oldFile))) { //Reading the file

      String currentLine;
      while ((currentLine = br.readLine()) != null) { //Reading the file line by line
        pw.println(currentLine);
      }
    } catch (IOException e) { //Catching the exception
      logger.log(Level.SEVERE, "Error copying file contents", e);
    }

    if (!newFile.delete()) { //Checking if the file is deleted
      logger.log(Level.SEVERE, "Could not delete temporary file: " + newFile.getAbsolutePath());
    }

  }


  /**
   * Saves ingredients to a file. Writes line by line; if the file already exists
   * it appends the new elements. Otherwise, it creates a new file in the given directory.
   *
   * @param filename the name of the file to which the ingredients will be written.
   */
  public void saveIngredientsToFile(String filename, Ingredient ingredient) {
    URL resourceUrl = getClass().getClassLoader().getResource(""); //Getting the resource path
    if (resourceUrl == null) {
      logger.log(Level.SEVERE, "Resource path is null"); //Logging the error
      return;
    }

    String decodedPath = URLDecoder.decode(resourceUrl.getPath(), StandardCharsets.UTF_8);
    String filePath = decodedPath + filename;
    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
      writer.println(ingredient.toString()); //Writing the ingredient to the file
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
    ingredients.clear();
    String filePath;
    String decodedPath;
    try {
      filePath = FileHandler.getResourcePath(filename);
      decodedPath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new IngredientNotFound("No ingredients added yet");
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(decodedPath))) {
      String regex = "Ingredient\\{name='(.+)', unit='(.+)', numberOfUnits=(\\d+), "
          + "price=(\\d+\\.\\d+), expirationDate=(\\d{4}-\\d{2}-\\d{2})}";
      Pattern pattern = Pattern.compile(regex);
      ingredients.addAll(reader.lines()
          .map(pattern::matcher)
          .filter(Matcher::matches)
          .map(matcher -> new Ingredient(
              matcher.group(1),
              matcher.group(2),
              Integer.parseInt(matcher.group(3)),
              Double.parseDouble(matcher.group(4)),
              LocalDate.parse(matcher.group(5))
          ))
          .toList());
    } catch (IOException e) {
      throw new IngredientNotFound("No ingredients added yet");
    }

    ingredients.sort(Comparator.comparing(Ingredient::getName));
  }



  /**
   * Prints out the ingredients that are expired.
   */
  public List<Ingredient> getExpiredGoods() {
    loadIngredientsFromFile("ingredients.txt"); // Loading the ingredients from the file
    return ingredients.stream()
        .filter(ingredient -> ingredient.getExpirationDate().isBefore(LocalDate.now()))
        .toList();
  }

  /**
   * Clears the ingredients list.
   */
  public void clearIngredients() {
    ingredients.clear();
  }

}

