package edu.ntnu.idi.bidata;

import java.io.*;
import java.net.URL;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.HashMap;
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

  /**
   * Adds ingredients to the list of ingredients.
   *
   * @param scanner the scanner object to read input from the user.
   */
  public void addIngredient(Scanner scanner) {
    boolean loop = true;
    while (loop) {
      System.out.println("Whats your ingredient name?");
      final String name = scanner.nextLine();
      // Name is a string since it can be any name.

      System.out.println("What unit? \n 1. Gram \n 2. Liter \n 3. Pieces");
      final String unitType = scanner.nextLine();
      final String unit = switch (unitType) {
        case "1" -> "Gram";
        case "2" -> "Liter";
        case "3" -> "Pieces";
        default ->
          {
            System.out.println("Invalid choice");
            yield null; // Use yield to return a value from the switch expression

          }
      };

      if (unit == null) {
        return; // Exit the method if the unit is invalid
      }
      int numberOfUnits;
      System.out.println("How many " + unit + " do you have?");
      try {
        numberOfUnits = Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Invalid number of units. Please enter a valid integer.");
        return;
      }

      System.out.println("What price?");
      if (!scanner.hasNextDouble()) {
        System.out.println("Invalid price (use a decimal point with comma instead of dot)");
        scanner.nextLine(); // Consume the invalid input
        return;
      }


      final double price = scanner.nextDouble();
      // Price is a double since we can include decimals, providing a more accurate price.
      scanner.nextLine();

      System.out.println("What is the expiration date? (YYYY-MM-DD)");
      final String dateInput = scanner.nextLine();
      LocalDate expirationDate;
      try {
        expirationDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
      } catch (Exception e) {
        System.out.println("Invalid date format");
        return;
      }
      Ingredient ingredient = new Ingredient(name, unit, numberOfUnits, price, expirationDate);
      ingredients.add(ingredient);
      saveIngredientsToFile("ingredients.txt", ingredient); //Saving the ingredients to a file.
      System.out.println("Ingredient added successfully!");
      loop = false;
    }

  }

  /**
   * Removes ingredients from the list of ingredients.
   */
  public void removeIngredient(Scanner scanner) {
    String filename = "ingredients.txt";
    String regex = "name='(.*?)'";
    Pattern pattern = Pattern.compile(regex);
    FoodStorage foodStorage = new FoodStorage();
    foodStorage.loadIngredientsFromFile(filename);
    System.out.println("Enter the name of the ingredient you want to remove: ");
    String name = scanner.nextLine();

    URL resourceUrl = getClass().getClassLoader().getResource(filename);
    if (resourceUrl == null) {
      logger.log(Level.SEVERE, "Resource path is null");
      return;
    }
    String filePath = resourceUrl.getPath();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          String nameIngredient = matcher.group(1);
          if (nameIngredient.equalsIgnoreCase(name)) {
            System.out.println("Ingredient found!");
            System.out.println("Do you want to remove this ingredient? (yes/no)");
            String answer = scanner.nextLine().toLowerCase();
            if (answer.equals("yes")) {
              System.out.println("How many units do you want to remove? (0 to remove all)");
              int unitsToRemove = scanner.nextInt();
              removeLineFromFile(line, unitsToRemove);

              System.out.println("Ingredient removed!");
            } else if (answer.equals("no")) {
              System.out.println("Ingredient not removed!");
            } else {
              System.out.println("Invalid input!");
            }
          } else {
            System.out.println("Ingredient not found!");
          }
        }

      }
    } catch (IOException e) {
      System.out.println("Error");
    }
  }

  private void removeLineFromFile(String lineToRemove, int unitsToRemove) {

    String tempFile = "temp.txt";

    URL resourceUrl = getClass().getClassLoader().getResource("ingredients.txt");
    if (resourceUrl == null) {
      logger.log(Level.SEVERE, "Resource path is null");
      return;
    }
    String resourcePath = resourceUrl.getPath();
    File oldFile = new File(resourcePath);
    File newFile = new File(oldFile.getParent(), tempFile);

    try (BufferedReader br = new BufferedReader(new FileReader(oldFile));
         PrintWriter pw = new PrintWriter(new FileWriter(newFile))) {

      String currentLine;
      while ((currentLine = br.readLine()) != null) {
        if (currentLine.equals(lineToRemove)) {
          Matcher matcher = Pattern.compile("numberOfUnits=(\\d+)").matcher(currentLine);
          if (matcher.find()) {
            int numberOfUnits = Integer.parseInt(matcher.group(1));
            if (numberOfUnits > unitsToRemove) {
              if (unitsToRemove == 0) {
                continue;
              }else {
                int newNumberOfUnits = numberOfUnits - unitsToRemove;
                currentLine = currentLine.replace("numberOfUnits=" + numberOfUnits, "numberOfUnits=" + newNumberOfUnits);
              }
            } else {
              continue;
            }
          }
        }
        pw.println(currentLine);
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error processing file", e);
      return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(newFile));
         PrintWriter pw = new PrintWriter(new FileWriter(oldFile))) {

      String currentLine;
      while ((currentLine = br.readLine()) != null) {
        pw.println(currentLine);
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error copying file contents", e);
    }

    if (!newFile.delete()) {
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
    URL resourceUrl = getClass().getClassLoader().getResource("");

    if (resourceUrl == null) {
      logger.log(Level.SEVERE, "Resource path is null");
      return;
    }
    String resourcePath = resourceUrl.getPath();

    String filePath = resourcePath + filename;
    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
      writer.println(ingredient.toString());
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
    String regex = "name='(.*?)', unit='(.*?)', numberOfUnits=(\\d+), price=(\\d+\\.\\d+),"
        + " expirationDate=(\\d{4}-\\d{2}-\\d{2})";
    Pattern pattern = Pattern.compile(regex);

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
      if (inputStream == null) {
        logger.log(Level.SEVERE, "File not found: " + filename);
        return;
      }
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        while ((line = reader.readLine()) != null) {
          Matcher matcher = pattern.matcher(line);
          if (matcher.find()) {
            String name = matcher.group(1);
            String unit = matcher.group(2);
            int numberOfItems = Integer.parseInt(matcher.group(3));
            double price = Double.parseDouble(matcher.group(4));
            LocalDate expirationDate = LocalDate.parse(matcher.group(5));
            Ingredient ingredient = new Ingredient(name, unit, numberOfItems,
                price, expirationDate);
            ingredients.add(ingredient);
          }
        }
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Could not load ingredients from file", e);
    }

    ingredients.sort(Comparator.comparing((Ingredient::getName)));

    System.out.printf("%-20s %-20s %-10s %-10s %-15s%n", "Name", "Number of Units",
        "Unit", "Price", "Expiration Date");
    System.out.println("-----------------------------------------------"
        + "---------------------------------------");
    for (Ingredient ingredient : ingredients) {
      System.out.printf("%-20s %-20d %-10s %-10.2f %-15s%n",
          ingredient.getName(),
          ingredient.getNumberOfItems(),
          ingredient.getUnit(),
          ingredient.getPrice(),
          ingredient.getExpirationDate());
    }
    System.out.println("-----------------------------------------------"
        + "---------------------------------------\n");

  }

  public void expiredGoods() {
    loadIngredientsFromFile("ingredients.txt");
    final String RED = "\u001B[31m";
    final String RESET = "\u001B[0m";

    System.out.println("Expired goods: ");
    System.out.printf("%-20s %-20s %-10s %-10s %-15s%n", "Name", "Number of Units",
        "Unit", "Price", "Expiration Date");
    System.out.println("-----------------------------------------------"
        + "---------------------------------------");
    for (Ingredient ingredient : ingredients) {
      if (ingredient.getExpirationDate().isBefore(LocalDate.now())) {
        System.out.printf("%-20s %-20d %-10s %-10.2f "+ RED + "%-15s"+ RESET + "%n",
            ingredient.getName(),
            ingredient.getNumberOfItems(),
            ingredient.getUnit(),
            ingredient.getPrice(),
            ingredient.getExpirationDate());
      }
    }
    System.out.println("-----------------------------------------------"
        + "---------------------------------------\n");
  }

    /**
     * Adds a recipe to the cooking book.
     *
     * @param scanner the scanner object to read input from the user.
     */
    public void addRecipe(Scanner scanner) {
      HashMap<String, IngredientInfo> cookBook = new HashMap<>();
      boolean checker = true;
      System.out.println("Enter the name of the recipe: ");
      String recipeName = scanner.nextLine();
      while (checker) {
        System.out.println("Enter the ingredient name: ");
        String ingredientName = scanner.nextLine();
        System.out.println("What unit? \n 1. Gram \n 2. Liter \n 3. Pieces");
        String unitType = scanner.nextLine();
        String unit = switch (unitType) {
          case "1" -> "Gram";
          case "2" -> "Liter";
          case "3" -> "Pieces";
          default -> {
            System.out.println("Invalid choice");
            yield null;
          }

        };
        System.out.println("How many " + unit + " do you need?");
        int amount = scanner.nextInt();
        scanner.nextLine();

        if (unit != null) {
          cookBook.put(ingredientName, new IngredientInfo(ingredientName, amount, unit));
        }
        System.out.println("Do you want to add more ingredients? (yes/no)");
        String answer = scanner.nextLine().toLowerCase();
        if (answer.equals("no")) {
          checker = false;
        } else if (!answer.equals("yes")) {
          System.out.println("Invalid input!");
          return;
        }
      }
      System.out.println("Enter the instructions for the recipe: ");
      String instructions = scanner.nextLine();

      saveRecipeToFile("recipes.txt", new CookBook(recipeName, cookBook, instructions));
    }

    /**
     * Saves a recipe to a file.
     *
     * @param filename the name of the file to which the recipe will be written.
     * @param cookBook the recipe to be saved to the cook book.
     */

    public void saveRecipeToFile(String filename, CookBook cookBook) {
      URL resourceUrl = getClass().getClassLoader().getResource("");
      System.out.println(resourceUrl);

        if (resourceUrl == null) {
            logger.log(Level.SEVERE, "Resource path is null");
            return;
        }
        String resourcePath = resourceUrl.getPath();

        String filePath = resourcePath + filename;
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(cookBook.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save recipe to file", e);
        }
    }




  /**
   * Used for testing purposes in the test class.
   *
   * @return the list of ingredients.
   */

  public List<Ingredient> getIngredients() {
    return ingredients;
  }


}