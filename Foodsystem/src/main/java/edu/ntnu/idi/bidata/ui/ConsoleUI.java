package edu.ntnu.idi.bidata.ui;
import edu.ntnu.idi.bidata.FoodStorage;
import edu.ntnu.idi.bidata.Ingredient;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleUI {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Scanner to read input from the user
        FoodStorage foodStorage = new FoodStorage();

        System.out.println("What do you want to do\n 1:Add ingredient\n 2:Remove ingredient\n 3:Show all ingredients\n 4:Exit");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:

                System.out.println("Whats your ingredient name?"); // Name is a string since it can be any name.
                String name = scanner.nextLine();

                System.out.println("How many items?"); // Number of items is an integer since it can only be a whole number.
                int numberOfItems = scanner.nextInt();
                scanner.nextLine();

                System.out.println("What unit?"); // Unit is a string since it can be any unit.
                String unit = scanner.nextLine();

                System.out.println("What price?"); // Price is a double since we can include decimals, providing a more accurate price.
                double price = scanner.nextDouble();
                scanner.nextLine();

                System.out.println("What is the expiration date? (YYYY-MM-DD)"); // Expiration date is a string since it can be any date.
                String dateInput = scanner.nextLine();
                LocalDate expirationDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE); // LocalDate is used to store the date in a more readable format.

                Ingredient ingredient = new Ingredient(name, numberOfItems, unit, price, expirationDate); // Creating a new ingredient after input, sending the inputs as a parameter
                ingredient.addingIngredient(foodStorage); // Adding the ingredient to the food storage, and then the food storage sends it to a txt file so the foods can be used and saved for later


                break;
            case 2:
                System.out.println("Removing ingredient");
                break;
            case 3:
                System.out.println("Showing all ingredients");
                break;
            case 4:
                System.out.println("Exiting");
                break;
            default:
                System.out.println("Invalid choice");
        }

    }
}
