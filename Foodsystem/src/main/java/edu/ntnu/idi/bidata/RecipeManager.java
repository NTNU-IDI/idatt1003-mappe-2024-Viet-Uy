package edu.ntnu.idi.bidata;

import edu.ntnu.idi.bidata.exceptions.RecipeNotFound;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class to manage recipes.
 */
public class RecipeManager {
  private static final Logger logger = Logger.getLogger(RecipeManager.class.getName());
  private final HashMap<String, Recipe> recipes;

  /**
   * Create a new RecipeManager.
   */
  public RecipeManager() {
    this.recipes = new HashMap<>();
  }

  /**
   * Add a recipe to the recipe manager.
   *
   * @param scanner     the scanner to read input from the user.
   * @param ingredients the ingredients available in the food storage.
   * @param instructions the instructions for the recipe.
   */
  public void addRecipe(Scanner scanner, HashMap<String, IngredientInfo> ingredients,
                        String instructions) {

    // Create a new recipe
    ArrayList<IngredientInfo> ingredientList = new ArrayList<>(ingredients.values());
    boolean checker = true;

    System.out.println("Enter the name of the recipe: ");
    final String recipeName = scanner.nextLine();

    if (recipeName.isEmpty()) {
      System.out.println("Recipe name cannot be empty.");
      return;
    }


    while (checker) {
      try {
        System.out.println("Enter the ingredient name: ");
        final String ingredientName = scanner.nextLine();

        if (ingredientName.isEmpty()) {
          System.out.println("Ingredient name cannot be empty.");
          continue;
        }

        System.out.println("What unit? \n 1. Gram \n 2. Liter \n 3. Pieces");
        String unitType = scanner.nextLine();
        String unit = switch (unitType) {
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

        System.out.println("How many " + unit + " do you need?");
        int amount;
        try {
          amount = Integer.parseInt(scanner.nextLine());
          if (amount <= 0) {
            System.out.println("Amount must be a positive integer.");
            continue;
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid amount. Please enter a valid integer.");
          continue;
        }

        System.out.println("Enter the price per unit: ");
        double price;
        try {
          price = Double.parseDouble(scanner.nextLine());
          if (price <= 0) {
            System.out.println("Price must be a positive number.");
            continue;
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid price. Please enter a valid number.");
          continue;
        }

        ingredientList.add(new IngredientInfo(ingredientName, amount, unit, price));

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
    String recipeInstructions = scanner.nextLine();

    if (recipeInstructions.isEmpty()) {
      recipeInstructions = instructions;
    }

    System.out.println("Enter the number of people: ");
    int numberOfPeople;
    try {
      numberOfPeople = Integer.parseInt(scanner.nextLine().trim());
      if (numberOfPeople <= 0) {
        System.out.println("Number of people must be a positive integer.");
        return;
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid number of people. Please enter a valid integer.");
      return;
    }

    URL resourceUrl = getClass().getClassLoader().getResource(""); // Getting the resource path

    if (resourceUrl == null) {
      logger.log(Level.SEVERE, "Resource path is null"); // Logging the error
      return;
    }

    String resourcePath = resourceUrl.getPath(); // Getting the path of the file
    String filePath = resourcePath + "recipes.txt"; // Creating a new file path


    // Save the recipe to the recipe manager
    Recipe recipe = new Recipe(recipeName, recipeInstructions, ingredientList, numberOfPeople);
    recipes.put(recipeName, recipe);
    FileHandler.writeToFile(filePath, recipe.toString());

  }

  /**
   * Show all recipes in the recipe manager.
   */
  public void showRecipe(String filename) {
    String filePath;
    try {
      filePath = FileHandler.getResourcePath(filename);
    } catch (Exception e) {
      throw new RecipeNotFound("No recipes made yet");
    }
    if (filePath == null) {
      throw new RecipeNotFound("No recipes made yet");
    }
    // Read the content of the file
    String content = FileHandler.readFromFile(filePath);
    System.out.println(content);
  }

  /**
   * Suggest recipes that can be made with the available ingredients.
   *
   * @param foodStorage the food storage containing the available ingredients.
   * @param filename    the name of the file containing the recipes.
   */
  public void suggestRecipes(FoodStorage foodStorage, String filename) {
    try {
      foodStorage.loadIngredientsFromFile("ingredients.txt");
      loadRecipesFromFile(filename);
      // Check which recipes can be made with the available ingredients
      ArrayList<Recipe> suggestedRecipes = new ArrayList<>();
      System.out.println("Number of recipes: " + recipes.size() + "\n");

      for (Recipe recipe : recipes.values()) {
        boolean canMakeRecipe = true;
        double totalPrice = 0.0;
        for (IngredientInfo ingredient : recipe.ingredients()) {
          IngredientInfo availableIngredient = foodStorage.getIngredients(ingredient.name());
          if (availableIngredient == null
              || availableIngredient.amount() < ingredient.amount()
              || !availableIngredient.unit().equals(ingredient.unit())) {
            canMakeRecipe = false;
            break;
          } else {
            double ingredientPrice = availableIngredient.price();
            totalPrice += ingredientPrice;
            System.out.printf("Ingredient: %s, Amount: %d, Subtotal: %.2f%n",
                ingredient.name(), ingredient.amount(), ingredientPrice);
          }
        }
        if (canMakeRecipe) {
          suggestedRecipes.add(recipe);
          System.out.printf("Recipe: %s, Total Price: %.2f%n", recipe.name(), totalPrice);
          System.out.println("\n");
        }
      }
      // Print the suggested recipes
      if (suggestedRecipes.isEmpty()) {
        System.out.println("No recipes can be made with the available ingredients.");
      } else {
        System.out.println("You can make the following recipes: \n");
        for (Recipe recipe : suggestedRecipes) {
          System.out.println("\u001B[32m" + recipe.name() + "\u001B[0m" + "\n");
        }
      }

    } catch (Exception e) {
      logger.log(Level.SEVERE, "An error occurred while suggesting recipes", e);
    }
  }

  private void loadRecipesFromFile(String filename) {
    String filePath = FileHandler.getResourcePath(filename);
    if (filePath == null) {
      logger.log(Level.SEVERE, "Resource path is null");
      return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      String recipeName = null;
      ArrayList<IngredientInfo> ingredients = new ArrayList<>();
      String instructions = null;
      int numberOfPeople = 0;

      Pattern pattern = Pattern.compile(
          "Recipe Name: (.+)|Ingredient: (.+), Amount: (\\d+) (.+), Price: (\\d+\\.\\d+)|"
              + "Instructions: (.+)|Number of people: (\\d+)");

      // Read the content of the file
      while ((line = reader.readLine()) != null) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
          if (matcher.group(1) != null) {
            if (recipeName != null) {
              recipes.put(recipeName, new Recipe(recipeName, instructions,
                  ingredients, numberOfPeople));
            }
            recipeName = matcher.group(1);
            ingredients = new ArrayList<>();
          } else if (matcher.group(3) != null) {
            String ingredientName = matcher.group(2);
            int amount = Integer.parseInt(matcher.group(3));
            String unit = matcher.group(4);
            double price = Double.parseDouble(matcher.group(5));
            ingredients.add(new IngredientInfo(ingredientName, amount, unit, price));
          } else if (matcher.group(6) != null) {
            instructions = matcher.group(6);
          } else if (matcher.group(7) != null) {
            numberOfPeople = Integer.parseInt(matcher.group(7));
          }
        }
      }
      if (recipeName != null) {
        recipes.put(recipeName, new Recipe(recipeName, instructions, ingredients, numberOfPeople));
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Could not read recipes from file", e);
    }
  }
}