package edu.ntnu.idi.bidata;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FoodStorageTest {

    @Test
    void testAddIngredient() {
        FoodStorage foodStorage = new FoodStorage();

        // Directly create and add an ingredient
        Ingredient ingredient = new Ingredient("Sugar", "Gram", 500, 2.5, LocalDate.of(2024, 11, 30));
        foodStorage.addIngredientDirectly(ingredient);

        // Verify ingredient is added
        assertTrue(foodStorage.ingredientExists("Sugar"), "Ingredient should be added.");
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
    void testRemoveIngredientSuccessfully() {
        FoodStorage foodStorage = new FoodStorage();
        Scanner addScanner = new Scanner("Sugar\nGram\n500\n2.5\n2024-11-30\n");
        foodStorage.addIngredient(addScanner);

        // Simulate user input for removing an ingredient
        String input = "Sugar\n";
        InputStream original = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes())); // Set simulated input

        Scanner removeScanner = new Scanner(System.in);
        foodStorage.removeIngredient(removeScanner); // This will use the simulated input

        System.setIn(original); // Restore original System.in

        assertEquals(0, foodStorage.getIngredientsList().size(), "Ingredient should be removed.");
    }

    @Test
    void testRemoveNonExistingIngredient() {
        FoodStorage foodStorage = new FoodStorage();

        // Directly create and add an ingredient
        Ingredient ingredient = new Ingredient("Apple", "Gram", 10, 1.99, LocalDate.of(2024, 12, 12));
        foodStorage.addIngredientDirectly(ingredient);

        // Simulate user input for removing a non-existing ingredient
        String input = "NonExistingIngredient\n";
        InputStream original = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes())); // Set simulated input

        Scanner removeScanner = new Scanner(System.in);
        foodStorage.removeIngredient(removeScanner); // This will use the simulated input

        System.setIn(original); // Restore original System.in

        // Verify the non-existing ingredient was not removed
        assertTrue(foodStorage.ingredientExists("Apple"), "Ingredient should still exist.");
        assertFalse(foodStorage.ingredientExists("NonExistingIngredient"), "Non-existing ingredient should not exist.");
    }

}
