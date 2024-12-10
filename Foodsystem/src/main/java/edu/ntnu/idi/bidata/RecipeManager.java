package edu.ntnu.idi.bidata;

import edu.ntnu.idi.bidata.exceptions.RecipeNotFound;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
   * Adds a new recipe to the recipe manager.
   *
   * @param recipeName the name of the recipe
   * @param ingredientList the list of ingredients for the recipe
   * @param instructions the instructions for the recipe
   * @param numberOfPeople the number of people the recipe serves
   */
  public void addRecipe(String recipeName, ArrayList<IngredientInfo> ingredientList,
                        String instructions, int numberOfPeople, String directoryPath) {
    Recipe recipe = new Recipe(recipeName, instructions, ingredientList, numberOfPeople);
    recipes.put(recipeName, recipe);
    writeRecipeToFile(recipeName, ingredientList, instructions, numberOfPeople, directoryPath);
  }

  /**
   * Write a recipe to a file.
   *
   * @param recipeName the name of the recipe
   * @param ingredientList the list of ingredients for the recipe
   * @param instructions the instructions for the recipe
   * @param numberOfPeople the number of people the recipe serves
   */
  public void writeRecipeToFile(String recipeName, ArrayList<IngredientInfo> ingredientList,
                                String instructions, int numberOfPeople, String directoryPath) {
    String filePath = directoryPath + "/recipes.txt";
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write("Recipe Name: " + recipeName + "\n");
      for (IngredientInfo ingredient : ingredientList) {
        writer.write("Ingredient: " + ingredient.name() + ", Amount: " + ingredient.amount()
            + " " + ingredient.unit() + ", Price: " + ingredient.price() + "\n");
      }
      writer.write("Instructions: " + instructions + "\n");
      writer.write("Number of people: " + numberOfPeople + "\n");
      writer.write("\n");
      System.out.println("Recipe added successfully!");
    } catch (IOException e) {
      System.out.println("An error occurred while writing the recipe to the file: "
          + e.getMessage());
    }
  }


  /**
   * Show a recipe.
   *
   * @param filename the name of the file containing the recipe.
   */
  public String showRecipe(String filename) {
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
    return FileHandler.readFromFile(filePath);
  }



  /**
   * Suggest recipes that can be made with the available ingredients.
   *
   * @param foodStorage the food storage containing the available ingredients.
   * @param filename    the name of the file containing the recipes.
   */
  public List<String> suggestRecipes(FoodStorage foodStorage, String filename) {
    List<String> suggestedRecipes = new ArrayList<>();
    try {
      foodStorage.loadIngredientsFromFile("ingredients.txt");
      loadRecipesFromFile(filename);

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
          }
        }
        if (canMakeRecipe) {
          suggestedRecipes.add(String.format("Recipe: %s, Total Price: %.2f",
              recipe.name(), totalPrice));
        }
      }
    } catch (Exception e) {
      throw new RecipeNotFound("No recipes added yet");
    }
    return suggestedRecipes;
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