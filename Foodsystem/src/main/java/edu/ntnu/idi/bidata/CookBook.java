package edu.ntnu.idi.bidata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class to represent a cookbook.
 */
public class CookBook {
  private static final Logger logger = Logger.getLogger(CookBook.class.getName());
  // Logger object to log messages
  private final HashMap<String, Recipe> recipes; // HashMap to store recipes
  private final String name;
  private final HashMap<String, IngredientInfo> ingredients;
  private final String instructions;

  /**
   * Creates a new cookbook with the given name, ingredients, and instructions.
   *
   * @param name         the name of the cookbook.
   * @param ingredients  the ingredients in the cookbook.
   * @param instructions the instructions in the cookbook.
   */
  public CookBook(String name, HashMap<String, IngredientInfo> ingredients, String instructions) {
    this.recipes = new HashMap<>();
    this.name = name;
    this.ingredients = ingredients;
    this.instructions = instructions;
  }

  /**
   * Creates a new cookbook with the given name.
   *
   * @param name the name of the cookbook.
   */
  public Recipe getRecipe(String name) {
    return recipes.get(name);
  } // Returns the recipe with the given name

  public String getName() {
    return name;
  }

  public HashMap<String, IngredientInfo> getIngredients() {
    return ingredients;
  }

  public String getInstructions() {
    return instructions;
  }

  /**
   * removes a recipe from the cooking book.
   *
   * @param name the recipe to add.
   */
  public void removeRecipe(String name) {
    recipes.remove(name);
  } // Removes the recipe with the given name

  /**
   * Checks if the cooking book contains a recipe with the given name.
   *
   * @param name the name of the recipe to check.
   * @return true if the cooking book contains a recipe with the given name, false otherwise.
   */
  public boolean containsRecipe(String name) {
    return recipes.containsKey(name);
  }

  /**
   * Adds a recipe to the cooking book.
   *
   * @param scanner the scanner object to read input from the user.
   */
  public void addRecipe(Scanner scanner) {
    // ArrayList to store ingredients
    ArrayList<IngredientInfo> ingredients = new ArrayList<>(getIngredients().values());
    boolean checker = true;

    System.out.println("Enter the name of the recipe: ");
    final String recipeName = scanner.nextLine();

    while (checker) {
      try {
        System.out.println("Enter the ingredient name: ");
        final String ingredientName = scanner.nextLine();
        System.out.println("What unit? \n 1. Gram \n 2. Liter \n 3. Pieces");
        String unitType = scanner.nextLine();
        String unit = switch (unitType) {
          case "1" -> "Gram";
          case "2" -> "Liter";
          case "3" -> "Pieces";
          default ->
            {
            System.out.println("Invalid choice");
            yield null;
            }
        };

        if (unit == null) {
          continue;
        }

        System.out.println("How many " + unit + " do you need?");
        int amount;
        try {
          amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
          System.out.println("Invalid amount. Please enter a valid integer.");
          continue;
        }

        ingredients.add(new IngredientInfo(ingredientName, amount, unit));

        System.out.println("Do you want to add more ingredients? (yes/no)");
        String answer = scanner.nextLine().toLowerCase();
        if (answer.equals("no")) {
          checker = false;
        } else if (!answer.equals("yes")) {
          System.out.println("Invalid input!");
          return;
        }
      } catch (Exception e) {
        System.out.println("An error occurred while adding the ingredient: " + e.getMessage());
      }
    }
    System.out.println("Enter the instructions for the recipe: ");
    String instructions = scanner.nextLine();

    if (instructions.isEmpty()) {
      instructions = getInstructions(); // Use getInstructions method if no input is provided
    }

    System.out.println("Enter the number of people: ");
    int numberOfPeople = scanner.nextInt();
    scanner.nextLine();

    Recipe recipe = new Recipe(recipeName, instructions, ingredients, numberOfPeople);
    recipes.put(recipeName, recipe);

    saveRecipeToFile("recipes.txt", recipe); // Save the recipe to a file
  }

  /**
   * Saves a recipe to a file.
   *
   * @param filename the name of the file to which the recipe will be written.
   * @param recipe   the recipe to save.
   */

  public void saveRecipeToFile(String filename, Recipe recipe) {
    URL resourceUrl = getClass().getClassLoader().getResource(""); // Get the resource URL

    if (resourceUrl == null) { // If the resource URL is null, log a message
      logger.log(Level.SEVERE, "Resource path is null");
      return;
    }
    String resourcePath = resourceUrl.getPath(); // Get the resource path

    String filePath = resourcePath + filename; // Create the file path
    try (PrintWriter writer = new PrintWriter(
        new FileWriter(filePath, true))) { // Try to write to the file
      writer.println(recipe.toString());
      writer.println();
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Could not save recipe to file", e);
    }
  }

  /**
   * Shows the recipes in the cookbook.
   */
  public void showRecipe() {
    URL resourceUrl = getClass().getClassLoader().getResource("recipes.txt");

    if (resourceUrl == null) {
      logger.log(Level.SEVERE, "Resource path is null");
      return;
    }
    String filePath = resourceUrl.getPath();

    try (BufferedReader reader = new BufferedReader(
        new FileReader(filePath))) { // Try to read from the file
      boolean firstLine = true;
      String line;
      while ((line = reader.readLine()) != null) { // Read each line
        if (line.trim().isEmpty()) { // If the line is empty, print a new line
          if (!firstLine) { // If it is not the first line, print a new line
            System.out.println();
          }
          firstLine = false;
        } else {
          System.out.println(line);
          firstLine = true;
        }
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Could not read recipes from file", e);
    }
  }

  /**
   * Suggests recipes based on the available ingredients in the food storage.
   *
   * @param foodStorage the food storage object.
   * @param filename    the name of the file containing the recipes.
   */
  public void suggestRecipes(FoodStorage foodStorage, String filename) {
    try {
      loadRecipesFromFile(filename);

      ArrayList<Recipe> suggestedRecipes = new ArrayList<>();
      System.out.println("Number of recipes: " + recipes.size());

      for (Recipe recipe : recipes.values()) {
        boolean canMakeRecipe = true;
        for (IngredientInfo ingredient : recipe.ingredients()) {
          IngredientInfo availableIngredient = foodStorage.getIngredients(ingredient.getName());
          if (availableIngredient == null
              || availableIngredient.getAmount() < ingredient.getAmount()
              || !availableIngredient.getUnit().equals(ingredient.getUnit())) {
            canMakeRecipe = false;
            break;
          }
        }
        if (canMakeRecipe) {
          suggestedRecipes.add(recipe);
        }
      }

      if (suggestedRecipes.isEmpty()) {
        System.out.println("No recipes can be made with the available ingredients.");
      } else {
        System.out.println("You can make the following recipes:");
        for (Recipe recipe : suggestedRecipes) {
          System.out.println(recipe.name());
        }
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "An error occurred while suggesting recipes", e);
    }
  }

  /**
   * Loads recipes from a file.
   *
   * @param filename the name of the file from which to load the recipes.
   */
  private void loadRecipesFromFile(String filename) {
    URL resourceUrl = getClass().getClassLoader().getResource(filename);

    if (resourceUrl == null) {
      logger.log(Level.SEVERE, "Resource path is null");
      return;
    }
    String filePath = resourceUrl.getPath();

    try (BufferedReader reader = new BufferedReader(
        new FileReader(filePath))) { // Try to read from the file
      String line; // String to store the line
      String recipeName = null; // String to store the recipe name
      ArrayList<IngredientInfo> ingredients = new ArrayList<>(); // ArrayList to store ingredients
      String instructions = null; // String to store the instructions
      int numberOfPeople = 0; // Integer to store the number of people

      Pattern pattern = Pattern.compile(
          "Recipe Name: (.+)|Ingredient: (.+), Amount: (\\d+) (.+)|"
              + "Instructions: (.+)|Number of people: (\\d+)");
      while ((line = reader.readLine()) != null) {
        Matcher matcher = pattern.matcher(line); // Create a matcher object
        if (matcher.matches()) { // If the matcher matches
          if (matcher.group(1) != null) { // If the first group is not null
            if (recipeName != null) { // If the recipe name is not null
              // Add the recipe to the recipes:
              recipes.put(recipeName,
                  new Recipe(recipeName, instructions, ingredients, numberOfPeople));
            }
            recipeName = matcher.group(1); // Set the recipe name
            ingredients = new ArrayList<>();
          } else if (matcher.group(3) != null) { // If the third group is not null
            String ingredientName = matcher.group(2); // Get the ingredient name
            int amount = Integer.parseInt(matcher.group(3)); // Get the amount
            String unit = matcher.group(4); // Get the unit
            // Add the ingredient to the list:
            ingredients.add(new IngredientInfo(ingredientName, amount, unit));
          } else if (matcher.group(5) != null) {
            instructions = matcher.group(5); // Get the instructions
          } else if (matcher.group(6) != null) {
            numberOfPeople = Integer.parseInt(matcher.group(6)); // Get the number of people
          }
        }
      }
      if (recipeName != null) {
        // Add the recipe to the recipes
        recipes.put(recipeName, new Recipe(recipeName, instructions, ingredients, numberOfPeople));
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Could not read recipes from file", e);
    }
  }

  @Override // Override the toString method
  public String toString() {
    StringBuilder sb = new StringBuilder(); // Create a StringBuilder object
    for (Recipe recipe : recipes.values()) { // For each recipe in the recipes
      sb.append(recipe.toString()).append("\n"); // Append the recipe to the StringBuilder
    }
    return sb.toString(); // Return the StringBuilder as a string
  }

}
