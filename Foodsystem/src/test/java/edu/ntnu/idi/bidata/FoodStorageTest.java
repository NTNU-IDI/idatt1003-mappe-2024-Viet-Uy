package edu.ntnu.idi.bidata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FoodStorageTest {

    private FoodStorage foodStorage;

    @BeforeEach
    void setUp() {
        foodStorage = new FoodStorage();
    }

    @Test
    void testAddIngredientPositive() {
        String input = "Apple\n1\n10\n1.99\n2024-12-12\n";
        Scanner scanner = new Scanner(input);
        foodStorage.addIngredient(scanner);

        // Check the size of the ingredients list
        assertEquals(1, foodStorage.getIngredients().size(), "The number of ingredients should be 1");

        // Check the details of the added ingredient
        Ingredient addedIngredient = foodStorage.getIngredients().get(0);
        assertNotNull(addedIngredient, "The added ingredient should not be null");
        assertEquals("Apple", addedIngredient.getName(), "The ingredient name should be 'Apple'");
        assertEquals("Gram", addedIngredient.getUnit(), "The ingredient unit should be 'Gram'");
        assertEquals(10, addedIngredient.getNumberOfItems(), "The number of units should be 10");
        assertEquals(1.99, addedIngredient.getPrice(), "The price should be 1.99");
        assertEquals(LocalDate.of(2024, 12, 12), addedIngredient.getExpirationDate(), "The expiration date should be 2024-12-12");
    }

    @Test
    void testAddIngredientNegative() {
        String input = "Apple\n4\n10\n1.99\n2024-12-12\n";
        Scanner scanner = new Scanner(input);
        foodStorage.addIngredient(scanner);
        assertEquals(0, foodStorage.getIngredients().size());
    }

    @Test
    void testSaveIngredientsToFile() {
        Ingredient ingredient = new Ingredient("Apple", "Gram", 10, 1.99, LocalDate.of(2024, 12, 12));
        foodStorage.saveIngredientsToFile("test_ingredients.txt", ingredient);

        File file = new File("test_ingredients.txt");
        assertTrue(file.exists());
        assertTrue(file.delete(), "Failed to delete the test file");
    }

    @Test
    void testLoadIngredientsFromFilePositive() throws IOException {
        FileWriter writer = new FileWriter("test_ingredients.txt");
        writer.write("Ingredient{name='Apple', unit='Gram', numberOfUnits=10, price=1.99, expirationDate=2024-12-12}\n");
        writer.close();

        foodStorage.loadIngredientsFromFile("test_ingredients.txt");
        assertEquals(1, foodStorage.getIngredients().size());

        File file = new File("test_ingredients.txt");
        assertTrue(file.delete(), "Failed to delete the test file");
    }

    @Test
    void testLoadIngredientsFromFileNegative() throws IOException {
        FileWriter writer = new FileWriter("test_ingredients.txt");
        writer.write("Invalid data\n");
        writer.close();

        foodStorage.loadIngredientsFromFile("test_ingredients.txt");
        assertEquals(0, foodStorage.getIngredients().size());

        File file = new File("test_ingredients.txt");
        assertTrue(file.delete(), "Failed to delete the test file");
    }

    @Test
    void testRemoveIngredientPositive() throws IOException {
        Ingredient ingredient = new Ingredient("Apple", "Gram", 10, 1.99, LocalDate.of(2024, 12, 12));
        foodStorage.saveIngredientsToFile("test_ingredients.txt", ingredient);

        FileWriter writer = new FileWriter("test_ingredients.txt", true);
        writer.write(ingredient + "\n");
        writer.close();

        Scanner scanner = new Scanner("Apple\nyes\n");

        foodStorage.removeIngredient(scanner);

        foodStorage.loadIngredientsFromFile("test_ingredients.txt");
        assertEquals(0, foodStorage.getIngredients().size());

        File file = new File("test_ingredients.txt");
        assertTrue(file.delete(), "Failed to delete the test file");
    }

    @Test
    void testRemoveIngredientNegative() throws IOException {
        Ingredient ingredient = new Ingredient("Apple", "Gram", 10, 1.99, LocalDate.of(2024, 12, 12));
        foodStorage.saveIngredientsToFile("test_ingredients.txt", ingredient);

        FileWriter writer = new FileWriter("test_ingredients.txt", true);
        writer.write(ingredient + "\n");
        writer.close();

        Scanner scanner = new Scanner("Banana\n");

        foodStorage.removeIngredient(scanner);

        foodStorage.loadIngredientsFromFile("test_ingredients.txt");
        assertEquals(1, foodStorage.getIngredients().size());

        File file = new File("test_ingredients.txt");
        assertTrue(file.delete(), "Failed to delete the test file");
    }
}