package edu.ntnu.idi.bidata.ui;

import edu.ntnu.idi.bidata.FoodStorage;
import edu.ntnu.idi.bidata.Ingredient;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Console user interface for the food system.
 */
public class Ui {

  /**
   * Main method for terminal user interface.
   *
   * @param args the command line arguments.
   */
  public static void main(String[] args) {
    String currentDir = System.getProperty("user.dir");
    System.out.println("Current working directory: " + currentDir);
    Scanner scanner = new Scanner(System.in); // Scanner to read input from the user
    FoodStorage foodStorage = new FoodStorage();
    String choices = "\n1:Add ingredient\n2:Remove ingredient\n3:Show all ingredients\n4:Exit";
    System.out.println("What do you want to do?" + choices);
    int choice = scanner.nextInt();
    scanner.nextLine();
    switch (choice) {
      // Store variables as final so there are immutable and can't be changed.
      case 1:
        addIngredient(foodStorage, scanner);
        break;
      case 2:
        // MAKE NEW FUNCTIONS HERE TO SEPARATE THE CODE
        System.out.println("Removing ingredient");
        break;
      case 3:
        System.out.println("Showing all ingredients");
        foodStorage.loadIngredientsFromFile("ingredients.txt");
        break;
      case 4:
        System.out.println("Exiting");
        break;
      default:
        System.out.println("Invalid choice");
    }
  }

  public static void addIngredient(FoodStorage foodStorage, Scanner scanner) {
    System.out.println("Whats your ingredient name?");
    final String name = scanner.nextLine();
    // Name is a string since it can be any name.

    System.out.println("What unit? \n 1. Kg \n 2. Liter \n 3. Pieces");
    final String unitType = scanner.nextLine();
    final String unit = switch (unitType) {
      case "1" -> "Kg";
      case "2" -> "Liter";
      case "3" -> "Pieces";
      default -> {
        System.out.println("Invalid choice");
        yield "Invalid";
      }
    };
    if (unit.equals("Invalid")) {
      System.exit(0);
    }

    System.out.println("How many " + unit + " do you have?");
    final int numberOfItems = scanner.nextInt();
    // Number of items is an integer since it can only be a whole number.
    scanner.nextLine();

    System.out.println("What price?");
    final double price = scanner.nextDouble();
    // Price is a double since we can include decimals, providing a more accurate price.
    scanner.nextLine();

    System.out.println("What is the expiration date? (YYYY-MM-DD)");
    final String dateInput = scanner.nextLine();
    // Expiration date is a string since it can be any date.
    LocalDate expirationDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
    // LocalDate is used to store the date in a more readable format.

    Ingredient ingredient = new Ingredient(name, unit, numberOfItems, price, expirationDate);
    ingredient.addingIngredient(foodStorage);
  }
}