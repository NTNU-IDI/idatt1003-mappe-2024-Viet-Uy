package edu.ntnu.idi.bidata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FoodStorage {
    private static final Logger logger = Logger.getLogger(FoodStorage.class.getName()); //Logger to log errors instead of exception as it is more informative and easier to read.
    private final List<Ingredient> ingredients; //List of ingredients, this is where we store the ingredients.

    public FoodStorage() {
        ingredients = new ArrayList<>(); //Creating a new arraylist of ingredients.
    }

    public void addToStorage(Ingredient ingredient) { //Add ingredients to storage, this allows us to add the ingredient array to the txt document. Making it possible to save the foods that are stored.
        ingredients.add(ingredient);
    }

    public void saveIngredientsToFile(String filename){ // Save ingredients to file, writes line by line. If the file already exist, then write in it and add a new element, else create a new file in the given directory.
        String filePath = "idatt1003-mappe-2024-Viet-Uy/Foodsystem/src/main/java/edu/ntnu/idi/bidata/" + filename;
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath,true))) {
            for (Ingredient ingredient : ingredients) { // For each ingredient in the list, write the ingredient to the file. This for loop iterates through the list of ingredients and writes them to the file. The statement in the try indicates that if the file already exists, then always import the new food. Else make a new one.
                writer.println(ingredient.toString()); // Write the ingredient to the file as a string.
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save ingredients to file", e);
        }
    }
    //Under development:
    public void loadIngredientsFromFile(String filename){ // Load ingredients from file, reads line by line and gives name for variables in the line.
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                String name = parts[0];
                int numberOfItems = Integer.parseInt(parts[1]);
                String unit = parts[2];
                double price = Double.parseDouble(parts[3]);
                LocalDate expirationDate = LocalDate.parse(parts[4]);
                Ingredient ingredient = new Ingredient(name, numberOfItems, unit, price, expirationDate);
                ingredients.add(ingredient);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not load ingredients from file", e); // If the file does not exist, then it will throw an error.
        }
    }


    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public static void main(String[] args) {
        FoodStorage foodStorage = new FoodStorage();
        foodStorage.getIngredients().forEach(System.out::println);
    }
}
