package edu.ntnu.idi.bidata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
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

  public List<Ingredient> getIngredientsList() {
    return new ArrayList<>(ingredients); // Return a copy to avoid modification outside the class
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
   * @param scanner the scanner object to read input from the user.
   */
  public void addIngredient(Scanner scanner) {
    boolean loop = true;
    while (loop) {
      try {
        System.out.println("Whats your ingredient name?");
        final String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
          System.out.println("Ingredient name cannot be empty.");
          continue;
        }

        System.out.println("What unit? \n 1. Gram \n 2. Liter \n 3. Pieces");
        final String unitType = scanner.nextLine();
        final String unit = switch (unitType) {
          case "1" -> "Gram";
          case "2" -> "Liter";
          case "3" -> "Pieces";
          default ->
            {
            System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            yield null;
            }
        };

        if (unit == null) {
          continue;
        }

        System.out.println("How many " + unit + " do you have?");
        int numberOfUnits;
        try {
          numberOfUnits = Integer.parseInt(scanner.nextLine().trim());
          if (numberOfUnits <= 0) {
            System.out.println("Number of units must be a positive integer.");
            continue;
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid number of units. Please enter a valid integer.");
          continue;
        }

        System.out.println("What price? Just the number");
        String priceInput = scanner.nextLine().replace(',', '.');
        double price;
        try {
          price = Double.parseDouble(priceInput);
          if (price <= 0) {
            System.out.println("Price must be a positive number.");
            continue;
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid price. Please enter a valid number.");
          return;
        }

        System.out.println("What is the expiration date? (YYYY-MM-DD)");
        final String dateInput = scanner.nextLine().trim();
        LocalDate expirationDate;
        try {
          expirationDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
          System.out.println("Invalid date format. Please enter a date in the format YYYY-MM-DD.");
          continue;
        }

        Ingredient ingredient = new Ingredient(name, unit, numberOfUnits, price, expirationDate);
        ingredients.add(ingredient);
        saveIngredientsToFile("ingredients.txt", ingredient);
        System.out.println("Ingredient added successfully!");
        loop = false;
      } catch (Exception e) {
        System.out.println("An error occurred while adding the ingredient: " + e.getMessage());
      }
    }

  }

  /**
   * Removes ingredients from the list of ingredients.
   */
  public void removeIngredient(Scanner scanner) {
    String filename = "ingredients.txt";
    String regex = "name='(.*?)'";
    Pattern pattern = Pattern.compile(regex); //Compiling the pattern to be used in the matcher.
    FoodStorage foodStorage = new FoodStorage();
    foodStorage.loadIngredientsFromFile(filename);
    System.out.println("Enter the name of the ingredient you want to remove: ");
    String name = scanner.nextLine().trim();

    if (name.isEmpty()) {
      System.out.println("Ingredient name cannot be empty.");
      return;
    }

    if (!ingredientExists(name)) {
      System.out.println("Ingredient not found.");
      return;
    }

    URL resourceUrl = getClass().getClassLoader().getResource(filename); //Getting the resource path
    if (resourceUrl == null) {
      logger.log(Level.SEVERE, "Resource path is null");
      return;
    }

    String filePath = resourceUrl.getPath(); //Getting the path of the file

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { //Reading the file
      String line;
      while ((line = br.readLine()) != null) {
        Matcher matcher = pattern.matcher(line); //Matching the pattern with the line
        if (matcher.find()) {
          String nameIngredient = matcher.group(1); //Getting the name of the ingredient
          //Checking if the name is equal to the name of the ingredient:
          if (nameIngredient.equalsIgnoreCase(name)) {
            System.out.println("Do you want to remove this ingredient? (yes/no)");
            String answer = scanner.nextLine().toLowerCase(); //Converting the input to lowercase
            if (answer.equals("yes")) { //Checking if the input is yes
              System.out.println("How many units do you want to remove? (0 to remove all)");
              int unitsToRemove;
              try {
                unitsToRemove = Integer.parseInt(scanner.nextLine().trim());
                if (unitsToRemove < 0) {
                  System.out.println("Number of units must be a positive integer.");
                  continue;
                }
              } catch (NumberFormatException e) {
                System.out.println("Invalid number of units. Please enter a valid integer.");
                continue;
              }
              removeLineFromFile(line, unitsToRemove);
              System.out.println("Ingredient removed!");
            } else if (answer.equals("no")) {
              System.out.println("Ingredient not removed!");
            } else {
              System.out.println("Invalid input!");
            }
          }
        }
      }
    } catch (IOException e) {
      System.out.println("Error");
    }
  }

  /**
   * Removes a line from a file.
   *
   * @param lineToRemove the line to remove.
   * @param unitsToRemove the number of units to remove.
   */
  private void removeLineFromFile(String lineToRemove, int unitsToRemove) {

    String tempFile = "temp.txt";
    //Getting the resource path:
    URL resourceUrl = getClass().getClassLoader().getResource("ingredients.txt");
    if (resourceUrl == null) {
      logger.log(Level.SEVERE, "Resource path is null");
      return;
    }
    String resourcePath = resourceUrl.getPath();  //Getting the path of the file
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
            } else {
              continue;
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
    String resourcePath = resourceUrl.getPath(); //Getting the path of the file

    String filePath = resourcePath + filename; //Creating a new file path
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
    URL resourceUrl = getClass().getClassLoader().getResource(filename);

    if (resourceUrl == null) {
      System.err.println("Resource path is null");
      return;
    }
    String filePath = resourceUrl.getPath();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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
      System.err.println("Could not read ingredients from file: " + e.getMessage());
    }

    ingredients.sort(Comparator.comparing(Ingredient::getName));

    System.out.printf("%-20s %-20s %-10s %-10s %-15s%n", "Name", "Number of Units",
        "Unit", "Price", "Expiration Date");
    System.out.println("-----------------------------------------------"
        + "---------------------------------------");
    ingredients.forEach(ingredient -> System.out.printf("%-20s %-20d %-10s %-10.2f %-15s%n",
            ingredient.getName(),
            ingredient.getNumberOfItems(),
            ingredient.getUnit(),
            ingredient.getPrice(),
            ingredient.getExpirationDate()));
    System.out.println("-----------------------------------------------"
            + "---------------------------------------\n");
  }

  /**
   * Gets the ingredient by name. This the function used to get the ingredient info. in the menu.
   *
   * @param name the name of the ingredient.
   * @return the ingredient info.
   */

  public IngredientInfo getIngredients(String name) {
    return ingredients.stream()
            .filter(ingredient -> ingredient.getName().equalsIgnoreCase(name))
            .findFirst()
            .map(ingredient -> new IngredientInfo(ingredient.getName(),
                    ingredient.getNumberOfItems(), ingredient.getUnit(), ingredient.getPrice()))
            .orElse(null);
  }

  /**
   * Prints out the ingredients that are expired.
   */
  public void expiredGoods() {
    try {
      loadIngredientsFromFile("ingredients.txt"); // Loading the ingredients from the file
      System.out.println("Expired goods: ");
      System.out.printf("%-20s %-20s %-10s %-10s %-15s%n", "Name", "Number of Units",
          "Unit", "Price", "Expiration Date");
      System.out.println("-----------------------------------------------"
          + "---------------------------------------");
      String coloredText = "\"%-20s %-20d %-10s %-10.2f \" + Red + \"%-15s\" + Reset + \"%n\"";
      ingredients.stream()
              .filter(ingredient -> ingredient.getExpirationDate().isBefore(LocalDate.now()))
              .forEach(ingredient -> System.out.printf(coloredText,
                      ingredient.getName(),
                      ingredient.getNumberOfItems(),
                      ingredient.getUnit(),
                      ingredient.getPrice(),
                      ingredient.getExpirationDate()));
      System.out.println("-----------------------------------------------"
              + "---------------------------------------\n");
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Error while checking expired goods", e);
    }
  }

  /**
   * Clears the ingredients list.
   */
  public void clearIngredients() {
    ingredients.clear();
  }

}