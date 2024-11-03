package edu.ntnu.idi.bidata;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CookBook {
    private static final Logger logger = Logger.getLogger(CookBook.class.getName());
    private final HashMap<String, Recipe> recipes;

    public CookBook(String name, HashMap<String, IngredientInfo> ingredients, String instructions) {
        this.recipes = new HashMap<>();
    }

    public Recipe getRecipe(String name) {
        return recipes.get(name);
    }

    public void removeRecipe(String name) {
        recipes.remove(name);
    }

    public boolean containsRecipe(String name) {
        return recipes.containsKey(name);
    }

    /**
     * Adds a recipe to the cooking book.
     *
     * @param scanner the scanner object to read input from the user.
     */
    public void addRecipe(Scanner scanner) {
        ArrayList<IngredientInfo> ingredients = new ArrayList<>();
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
                ingredients.add(new IngredientInfo(ingredientName, amount, unit));
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
        System.out.println("Enter the description for the recipe: ");
        String description = scanner.nextLine();
        System.out.println("Enter the instructions for the recipe: ");
        String instructions = scanner.nextLine();
        System.out.println("Enter the number of people: ");
        int numberOfPeople = scanner.nextInt();
        scanner.nextLine();
        Recipe recipe = new Recipe(recipeName, description, instructions, ingredients, numberOfPeople);
        recipes.put(recipeName, recipe);

        saveRecipeToFile("recipes.txt",recipe);
    }

    /**
     * Saves a recipe to a file.
     *
     * @param filename the name of the file to which the recipe will be written.
     * @param recipe the recipe to save.
     */

    public void saveRecipeToFile(String filename, Recipe recipe) {
        URL resourceUrl = getClass().getClassLoader().getResource("");

        if (resourceUrl == null) {
            logger.log(Level.SEVERE, "Resource path is null");
            return;
        }
        String resourcePath = resourceUrl.getPath();

        String filePath = resourcePath + filename;
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(recipe.toString());
            writer.println();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save recipe to file", e);
        }
    }

    public void showRecipe() {
        URL resourceUrl = getClass().getClassLoader().getResource("recipes.txt");

        if (resourceUrl == null) {
            logger.log(Level.SEVERE, "Resource path is null");
            return;
        }
        String filePath = resourceUrl.getPath();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            boolean firstLine = true;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!firstLine) {
                        System.out.println();
                    }
                    firstLine = false;
                } else {
                    if (!firstLine) {
                        System.out.println();
                    }
                    System.out.println(line);
                    firstLine = true;
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not read recipes from file", e);
        }
    }

    public void suggestRecipes(FoodStorage foodStorage, String filename) {
        loadRecipesFromFile(filename);

        ArrayList<Recipe> suggestedRecipes = new ArrayList<>();
        System.out.println("Number of recipes: " + recipes.size());

        for (Recipe recipe : recipes.values()) {
            boolean canMakeRecipe = true;
            for (IngredientInfo ingredient : recipe.getIngredients()) {
                IngredientInfo availableIngredient = foodStorage.getIngredient(ingredient.getName());
                if (availableIngredient == null || availableIngredient.getAmount() < ingredient.getAmount() ||
                        !availableIngredient.getUnit().equals(ingredient.getUnit())) {
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
                System.out.println(recipe.getName());
            }
        }
    }

    private void loadRecipesFromFile(String filename) {
        URL resourceUrl = getClass().getClassLoader().getResource(filename);

        if (resourceUrl == null) {
            logger.log(Level.SEVERE, "Resource path is null");
            return;
        }
        String filePath = resourceUrl.getPath();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String recipeName = null;
            String description = null;
            ArrayList<IngredientInfo> ingredients = new ArrayList<>();
            String instructions = null;
            int numberOfPeople = 0;

            Pattern pattern = Pattern.compile("Recipe Name: (.+)|Description: (.+)|Ingredient: (.+), Amount: (\\d+) (.+)|Instructions: (.+)|Number of people: (\\d+)");
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    if (matcher.group(1) != null) {
                        if (recipeName != null) {
                            recipes.put(recipeName, new Recipe(recipeName, description, instructions, ingredients, numberOfPeople));
                        }
                        recipeName = matcher.group(1);
                        ingredients = new ArrayList<>();
                    } else if (matcher.group(2) != null) {
                        description = matcher.group(2);
                    } else if (matcher.group(3) != null) {
                        String ingredientName = matcher.group(3);
                        int amount = Integer.parseInt(matcher.group(4));
                        String unit = matcher.group(5);
                        ingredients.add(new IngredientInfo(ingredientName, amount, unit));
                    } else if (matcher.group(6) != null) {
                        instructions = matcher.group(6);
                    } else if (matcher.group(7) != null) {
                        numberOfPeople = Integer.parseInt(matcher.group(7));
                    }
                }
            }
            if (recipeName != null) {
                recipes.put(recipeName, new Recipe(recipeName, description, instructions, ingredients, numberOfPeople));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not read recipes from file", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Recipe recipe : recipes.values()) {
            sb.append(recipe.toString()).append("\n");
        }
        return sb.toString();
    }

}
