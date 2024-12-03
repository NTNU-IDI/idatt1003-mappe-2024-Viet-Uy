package edu.ntnu.idi.bidata;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CookBookTest {

    private CookBook cookBook;


    @BeforeEach
    void setUp() {
        HashMap<String, IngredientInfo> ingredients = new HashMap<>();
        cookBook = new CookBook("TestCookBook", ingredients, "TestInstructions");
    }

    @Test
    void testAddRecipePositive() {
        String input = "Apple Pie\nApple\n3\n2\n20\nno\nMix and bake\n4\n";
        try (Scanner scanner = new Scanner(input)) {
            cookBook.addRecipe(scanner);
            assertTrue(cookBook.containsRecipe("Apple Pie"));
        }
    }

    @Test
    void testRemoveRecipe() {
        // Ensure the recipe exists before attempting to remove it
        assertTrue(cookBook.containsRecipe(" Apple Pie"));

        // Remove the recipe
        cookBook.removeRecipe("Apple Pie");
        assertFalse(cookBook.containsRecipe("Apple Pie"));
    }

}