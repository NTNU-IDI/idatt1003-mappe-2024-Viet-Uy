package edu.ntnu.idi.bidata;

import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FoodStorageTest {

    @Test
    void testAddIngredient() {
        FoodStorage foodStorage = new FoodStorage();
        Scanner scanner = new Scanner(System.in);

        // Simulate user input for adding an ingredient
        String input = "Sugar\n1\n500\n2.5\n2024-11-30\n"; // Simulate correct input
        InputStream original = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes())); // Set simulated input

        foodStorage.addIngredient(scanner); // This will use the simulated input

        System.setIn(original); // Restore original System.in

        // Verify ingredient is added
        assertEquals(1, foodStorage.getIngredientsList().size(), "Ingredient should be added.");
    }

    @Test
    void testAddInvalidIngredient() {
        // Create a simulated input string with invalid data (e.g., non-numeric for numberOfUnits)
        String input = "Sugar\n1\nabc\n2.5\n2024-11-30\n"; // Invalid input for units
        InputStream originalSystemIn = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes())); // Simulate the user input

        Scanner scanner = new Scanner(System.in);
        FoodStorage foodStorage = new FoodStorage();
        foodStorage.addIngredient(scanner); // Run the method with invalid input

        System.setIn(originalSystemIn); // Restore original System.in

        // Assert the ingredient was not added due to invalid input
        assertEquals(0, foodStorage.getIngredientsList().size());
    }


    @Test
    void testRemoveIngredient() {
        FoodStorage foodStorage = new FoodStorage();
        Ingredient ingredient = new Ingredient("Sugar", "Gram", 500, 2.5, LocalDate.of(2024, 11, 30));

        // Add ingredient
        foodStorage.addIngredient(ingredient);

        // Remove ingredient
        foodStorage.removeIngredient("Sugar");

        // Verify removal
        assertEquals(0, foodStorage.getIngredientsList().size(), "Ingredient should be removed.");
    }


    @Test
    void testRemoveNonExistingIngredient() {
        FoodStorage foodStorage = new FoodStorage();

        // Try to remove an ingredient that doesn't exist
        foodStorage.removeIngredient("NonExistingIngredient");

        // Verify list size stays the same
        assertEquals(0, foodStorage.getIngredientsList().size(), "List should remain the same.");
    }


    @Test
    void testSaveAndLoadIngredients() {
        FoodStorage foodStorage = new FoodStorage();
        Ingredient ingredient = new Ingredient("Sugar", "Gram", 500, 2.5, LocalDate.of(2024, 11, 30));

        // Save ingredient to file
        foodStorage.saveIngredientsToFile("ingredients.txt", ingredient);

        // Load ingredients from file
        foodStorage.loadIngredientsFromFile("ingredients.txt");

        // Verify ingredient is loaded
        assertEquals(1, foodStorage.getIngredientsList().size(), "Ingredient should be loaded from file.");
        assertTrue(foodStorage.getIngredientsList().contains(ingredient), "Loaded ingredient should match the saved one.");
    }


    @Test
    void testLoadIngredientsFromNonExistingFile() {
        FoodStorage foodStorage = new FoodStorage();

        // Try loading from a non-existing file
        foodStorage.loadIngredientsFromFile("non_existing_file.txt");

        // Verify list is empty
        assertEquals(0, foodStorage.getIngredientsList().size(), "List should remain empty.");
    }

    @Test
    void testExpiredGoods() {
        FoodStorage foodStorage = new FoodStorage();
        Ingredient expiredIngredient = new Ingredient("Milk", "Liter", 2, 1.5, LocalDate.of(2023, 11, 1));

        // Add expired ingredient
        foodStorage.addIngredient(expiredIngredient);

        // Verify expired goods are displayed
        foodStorage.expiredGoods();

        // Assuming your expiredGoods method outputs expired ingredients.
        assertTrue(foodStorage.getIngredientsList().contains(expiredIngredient), "Expired ingredient should be listed.");
    }

    @Test
    void testNonExpiredGoods() {
        FoodStorage foodStorage = new FoodStorage();
        Ingredient nonExpiredIngredient = new Ingredient("Sugar", "Gram", 500, 2.5, LocalDate.of(2025, 12, 31));

        // Add non-expired ingredient
        foodStorage.addIngredient(nonExpiredIngredient);

        // Verify non-expired goods are not displayed
        foodStorage.expiredGoods();

        // Assuming expiredGoods prints expired ingredients, this should not match any expired goods.
        assertFalse(foodStorage.getIngredientsList().contains(nonExpiredIngredient), "Non-expired ingredient should not be listed.");
    }

    @Test
    void testClearIngredients() {
        FoodStorage foodStorage = new FoodStorage();
        foodStorage.addIngredient(new Ingredient("Sugar", "Gram", 500, 2.5, LocalDate.of(2024, 11, 30)));

        // Clear ingredients
        foodStorage.clearIngredients();

        // Verify list is empty
        assertTrue(foodStorage.getIngredientsList().isEmpty(), "Ingredients list should be cleared.");
    }

    @Test
    void testClearEmptyIngredients() {
        FoodStorage foodStorage = new FoodStorage();

        // Clear ingredients when list is empty
        foodStorage.clearIngredients();

        // Verify list is still empty
        assertTrue(foodStorage.getIngredientsList().isEmpty(), "Ingredients list should remain empty.");
    }

}
