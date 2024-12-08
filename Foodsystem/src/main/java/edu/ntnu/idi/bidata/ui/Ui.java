package edu.ntnu.idi.bidata.ui;

import edu.ntnu.idi.bidata.CookBook;
import edu.ntnu.idi.bidata.FoodStorage;
import edu.ntnu.idi.bidata.exceptions.IngredientNotFound;
import edu.ntnu.idi.bidata.exceptions.RecipeNotFound;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * Console user interface for the food system.
 */
public class Ui {
  // Menu choices
  private static final int ADD_INGREDIENT = 1;
  private static final int REMOVE_INGREDIENT = 2;
  private static final int SHOW_ALL_INGREDIENTS = 3;
  private static final int EXPIRED_GOODS = 4;
  private static final int ADD_RECIPE = 5;
  private static final int SHOW_RECIPE = 6;
  private static final int RECOMMEND_DISHES = 7;
  private static final int EXIT = 8;

  private FoodStorage foodStorage;
  private Scanner scanner;
  private CookBook cookBook;


  /**
   * Initialize the user interface.
   */
  public void init() {
    scanner = new Scanner(System.in); // Scanner to read input from the user
    foodStorage = new FoodStorage();
    cookBook = new CookBook("MyCookBook", new HashMap<>(), "Instructions");
    System.out.println("Welcome to the Food Storage System!");

  }

  /**
   * Start the user interface.
   */
  public void start() {
    boolean checker = true;
    while (checker) {
      displayMenu();
      try {
        int choice = scanner.nextInt();
        scanner.nextLine();
        checker = handleMenuChoice(choice);
      } catch (InputMismatchException e) {
        System.out.println("Invalid input, please try again");
        scanner.nextLine();
      }
    }
    scanner.close();
  }

  /**
   * Display the menu.
   */
  private void displayMenu() {
    String choices = """
        
        1: Add ingredient
        2: Remove ingredient
        3: Show all ingredients
        4: Expired goods
        5: Add recipe
        6: Show recipe
        7: Recommend dishes based on current ingredients
        8: Exit
        """;
    System.out.println("What do you want to do?" + choices);
  }

  /**
   * Handle the user's menu choice.
   *
   * @param choice the user's choice.
   * @return true if the user should be prompted again, false if the program should exit.
   */
  private boolean handleMenuChoice(int choice) {
    switch (choice) {
      case ADD_INGREDIENT -> foodStorage.addIngredient(scanner);
      case REMOVE_INGREDIENT ->
        {
          System.out.println("Removing ingredient");
          try {
            foodStorage.removeIngredient(scanner);
          } catch (IngredientNotFound ingredientNotFound) {
            System.out.println(ingredientNotFound.getMessage() + "\n");
          }
        }
      case SHOW_ALL_INGREDIENTS ->
        {
          System.out.println("Showing all ingredients");
          foodStorage.clearIngredients(); // Clear the ingredients
          try {
            foodStorage.loadIngredientsFromFile("ingredients.txt");
          } catch (IngredientNotFound ingredientNotFound) {
            System.out.println(ingredientNotFound.getMessage() + "\n");
          }
        }
      case EXPIRED_GOODS ->
        {
          System.out.println("Food storage: ");
          try {
            foodStorage.expiredGoods();
          } catch (IngredientNotFound ingredientNotFound) {
            System.out.println(ingredientNotFound.getMessage() + "\n");
          }
        }
      case ADD_RECIPE ->
        {
          System.out.println("Add recipes to the cooking book:");
          cookBook.addRecipe(scanner);
        }
      case SHOW_RECIPE ->
        {
        System.out.println("Showing recipes: \n");
        try {
          cookBook.showRecipe("recipes.txt");
        } catch (RecipeNotFound recipeNotFound) {
          System.out.println(recipeNotFound.getMessage() + "\n");
        }

        }
      case RECOMMEND_DISHES ->
        {
        System.out.println("Recommend dishes based on these current ingredients: \n");

          try {
            cookBook.suggestRecipes(foodStorage, "recipes.txt");
          } catch (RecipeNotFound recipeNotFound) {
            System.out.println(recipeNotFound.getMessage() + "\n");
          }

        }
      case EXIT ->
        {
        System.out.println("Exiting");
        return false;
        }
      default -> System.out.println("Invalid choice");
    }
    return true;
  }
}