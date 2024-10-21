package edu.ntnu.idi.bidata.ui;

import edu.ntnu.idi.bidata.FoodStorage;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Console user interface for the food system.
 */
public class Ui {
  private FoodStorage foodStorage;
  private Scanner scanner;

  /**
   * Initialize the user interface.
   */
  public void init() {
    scanner = new Scanner(System.in); // Scanner to read input from the user
    foodStorage = new FoodStorage();
    System.out.println("Welcome to the Food Storage System!");

  }

  /**
   * Start the user interface.
   */
  public void start() {
    boolean checker = true;
    while (checker) {
      String choices = "\n1:Add ingredient\n2:Remove ingredient\n3:Show all ingredients\n4:Exit";
      System.out.println("What do you want to do?" + choices);
      try {
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
          case 1:
            foodStorage.addIngredient(scanner);
            break;
          case 2:
            System.out.println("Removing ingredient");
            foodStorage.removeIngredient(scanner);
            break;
          case 3:
            System.out.println("Showing all ingredients");
            foodStorage.loadIngredientsFromFile("ingredients.txt");
            break;
          case 4:
            checker = false;
            System.out.println("Exiting");
            break;
          default:
            System.out.println("Invalid choice");
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input, please try again");
        scanner.nextLine();
      }

    }
  }
}