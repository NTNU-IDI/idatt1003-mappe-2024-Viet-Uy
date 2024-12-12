package edu.ntnu.idi.bidata.ui;

import edu.ntnu.idi.bidata.CookBook;
import edu.ntnu.idi.bidata.FileHandler;
import edu.ntnu.idi.bidata.FoodStorage;
import edu.ntnu.idi.bidata.Ingredient;
import edu.ntnu.idi.bidata.IngredientInfo;
import edu.ntnu.idi.bidata.exceptions.IngredientNotFound;
import edu.ntnu.idi.bidata.exceptions.RecipeNotFound;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
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
    cookBook = new CookBook("MyCookBook");
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
      case ADD_INGREDIENT -> handleAddIngredient();
      case REMOVE_INGREDIENT ->
        {
          System.out.println("Removing ingredient");
          try {
            handleRemoveIngredient();
          } catch (IngredientNotFound ingredientNotFound) {
            System.out.println(ingredientNotFound.getMessage() + "\n");
          }
        }
      case SHOW_ALL_INGREDIENTS ->
        {
          System.out.println("Showing all ingredients");
          foodStorage.clearIngredients(); // Clear the ingredients
          try {
            handleShowAllIngredients();
          } catch (IngredientNotFound ingredientNotFound) {
            System.out.println(ingredientNotFound.getMessage() + "\n");
          }
        }
      case EXPIRED_GOODS ->
        {
          System.out.println("Food storage: ");
          try {
            handleShowExpiredGoods();
          } catch (IngredientNotFound ingredientNotFound) {
            System.out.println(ingredientNotFound.getMessage() + "\n");
          }
        }
      case ADD_RECIPE ->
        {
          System.out.println("Add recipes to the cooking book:");
          handleAddRecipe();
        }
      case SHOW_RECIPE ->
        {
        System.out.println("Showing recipes: \n");
        try {
          handleShowRecipe();
        } catch (RecipeNotFound recipeNotFound) {
          System.out.println(recipeNotFound.getMessage() + "\n");
        }

        }
      case RECOMMEND_DISHES ->
        {
        System.out.println("Recommend dishes based on these current ingredients: \n");

          try {
            handleSuggestRecipes();
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

  /**
   * Handle the user's choice to add an ingredient.
   */
  public void handleAddIngredient() {
    Scanner scanner = new Scanner(System.in);

    // Prompt the user for ingredient details
    System.out.println("What's your ingredient name?");
    final String name = scanner.nextLine().trim();

    System.out.println("What unit? \n 1. Gram \n 2. Liter \n 3. Pieces");
    String unitType = scanner.nextLine();
    String unit = switch (unitType) {
      case "1" -> "Gram";
      case "2" -> "Liter";
      case "3" -> "Pieces";
      default -> null;
    };

    System.out.println("How many " + unit + " do you have?");
    int numberOfUnits;
    try {
      numberOfUnits = Integer.parseInt(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
      System.out.println("Invalid number of units. Please enter a valid integer.");
      return;
    }

    System.out.println("What price? Just the number");
    String priceInput = scanner.nextLine().replace(',', '.');
    double price;
    try {
      price = Double.parseDouble(priceInput);
    } catch (NumberFormatException e) {
      System.out.println("Invalid price. Please enter a valid number.");
      return;
    }

    System.out.println("What is the expiration date? (YYYY-MM-DD)");
    String dateInput = scanner.nextLine().trim();
    LocalDate expirationDate;
    try {
      expirationDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (Exception e) {
      System.out.println("Invalid date format. Please enter a date in the format YYYY-MM-DD.");
      return;
    }

    // Call the addIngredient method and check the result
    boolean success = foodStorage.addIngredient(name, unit, numberOfUnits, price, expirationDate);
    if (success) {
      System.out.println("Ingredient added successfully!");
    } else {
      System.out.println("Failed to add ingredient. Please check your inputs.");
    }
  }

  /**
   * Handle the user's choice to remove an ingredient.
   */
  public void handleRemoveIngredient() {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter the name of the ingredient you want to remove: ");
    String name = scanner.nextLine().trim();

    if (name.isEmpty()) {
      System.out.println("Ingredient name cannot be empty.");
      return;
    }

    if (!foodStorage.ingredientExists(name)) {
      System.out.println("Ingredient not found.");
      return;
    }

    System.out.println("Do you want to remove this ingredient? (yes/no)");
    String answer = scanner.nextLine().toLowerCase();
    if (answer.equals("yes")) {
      System.out.println("How many units do you want to remove? (0 to remove all)");
      int unitsToRemove;
      try {
        unitsToRemove = Integer.parseInt(scanner.nextLine().trim());
        if (unitsToRemove < 0) {
          System.out.println("Number of units must be a positive integer.");
          return;
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid number of units. Please enter a valid integer.");
        return;
      }

      boolean success = foodStorage.removeIngredient(name, unitsToRemove);
      if (success) {
        System.out.println("Ingredient removed!");
      } else {
        System.out.println("Failed to remove ingredient.");
      }
    } else if (answer.equals("no")) {
      System.out.println("Ingredient not removed!");
    } else {
      System.out.println("Invalid input!");
    }
  }

  /**
   * Handle the user's choice to show all ingredients.
   */
  public void handleShowAllIngredients() {
    try {
      foodStorage.loadIngredientsFromFile("ingredients.txt");
      System.out.printf("%-20s %-20s %-10s %-10s %-15s%n", "Name", "Number of Units",
          "Unit", "Price", "Expiration Date");
      System.out.println("-----------------------------------------------"
          + "---------------------------------------");
      foodStorage.getIngredients().forEach(ingredient -> System.out.printf(
          "%-20s %-20d %-10s %-10.2f %-15s%n",
          ingredient.getName(),
          ingredient.getNumberOfItems(),
          ingredient.getUnit(),
          ingredient.getPrice(),
          ingredient.getExpirationDate()));
      System.out.println("-----------------------------------------------"
          + "---------------------------------------\n");
    } catch (IngredientNotFound e) {
      System.out.println(e.getMessage() + "\n");
    }
  }

  /**
   * Handle the user's choice to show expired goods.
   */
  public void handleShowExpiredGoods() {
    try {
      final List<Ingredient> expiredGoods = foodStorage.getExpiredGoods();
      System.out.println("Expired goods: ");
      System.out.printf("%-20s %-20s %-10s %-10s %-15s%n", "Name", "Number of Units",
          "Unit", "Price", "Expiration Date");
      System.out.println("-----------------------------------------------"
          + "---------------------------------------");
      String red = "\u001B[31m";
      String reset = "\u001B[0m";
      String coloredText = "%-20s %-20d %-10s %-10.2f " + red + "%-15s" + reset + "%n";
      expiredGoods.forEach(ingredient -> System.out.printf(coloredText,
          ingredient.getName(),
          ingredient.getNumberOfItems(),
          ingredient.getUnit(),
          ingredient.getPrice(),
          ingredient.getExpirationDate()));
      System.out.println("-----------------------------------------------"
          + "---------------------------------------\n");
    } catch (IngredientNotFound e) {
      System.out.println(e.getMessage() + "\n");
    }
  }

  /**
   * Handle the user's choice to add a recipe.
   */
  public void handleAddRecipe() {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter the name of the recipe: ");
    final String recipeName = scanner.nextLine().trim();

    if (recipeName.isEmpty()) {
      System.out.println("Recipe name cannot be empty.");
      return;
    }

    ArrayList<IngredientInfo> ingredientList = new ArrayList<>();
    boolean checker = true;

    while (checker) {
      try {
        System.out.println("Enter the ingredient name: ");
        final String ingredientName = scanner.nextLine().trim();

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
          amount = Integer.parseInt(scanner.nextLine().trim());
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
          price = Double.parseDouble(scanner.nextLine().trim());
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
    String recipeInstructions = scanner.nextLine().trim();

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

    String directoryPath = FileHandler.getResourcePath(""); // Get the directory path
    cookBook.getRecipeManager().addRecipe(recipeName, ingredientList, recipeInstructions,
        numberOfPeople, directoryPath);
  }

  /**
   * Handle the user's choice to show a recipe.
   */

  public void handleShowRecipe() {
    String filename = "recipes.txt";
    try {
      String content = cookBook.getRecipeManager().showRecipe(filename);
      System.out.println(content);
    } catch (RecipeNotFound e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Handle the user's choice to suggest recipes.
   */
  public void handleSuggestRecipes() {
    String filename = "recipes.txt";
    try {
      List<String> suggestedRecipes = cookBook.getRecipeManager()
              .suggestRecipes(foodStorage, filename);
      if (suggestedRecipes.isEmpty()) {
        System.out.println("No recipes can be made with the available ingredients.");
      } else {
        System.out.println("You can make the following recipes: \n");
        for (String recipe : suggestedRecipes) {
          System.out.println("\u001B[32m" + recipe + "\u001B[0m" + "\n");
        }
      }
    } catch (RecipeNotFound e) {
      System.out.println(e.getMessage());
    }
  }

}

