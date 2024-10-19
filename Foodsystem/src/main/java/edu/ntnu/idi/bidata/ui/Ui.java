package edu.ntnu.idi.bidata.ui;

import edu.ntnu.idi.bidata.FoodStorage;
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
      int choice = scanner.nextInt();
      scanner.nextLine();
      switch (choice) {
        // Store variables as final so there are immutable and can't be changed.
        case 1:
          foodStorage.addIngredient(scanner);
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
          checker = false;
          System.out.println("Exiting");
          break;
        default:
          System.out.println("Invalid choice");
      }
    }
  }


  /**
   * Main method to start the program.
   *
   * @param args the command line arguments.
   */

  public static void main(String[] args) {
    Ui ui = new Ui();
    ui.init();
    ui.start();
  }
}