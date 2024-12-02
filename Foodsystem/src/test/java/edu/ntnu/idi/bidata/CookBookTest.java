package edu.ntnu.idi.bidata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CookBookTest {

    private CookBook cookBook;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        HashMap<String, IngredientInfo> ingredients = new HashMap<>();
        cookBook = new CookBook("TestCookBook", ingredients, "TestInstructions");
        ArrayList<IngredientInfo> ingredientList = new ArrayList<>();
        recipe = new Recipe("Apple Pie", "Mix and bake", ingredientList, 4);
    }


    @Test
    void testAddRecipePositive() {
        cookBook.addRecipe(new Scanner("Apple Pie\nApple\n3\n2\nPieces\nno\nMix and bake\n4\n"));
        assertTrue(cookBook.containsRecipe("Apple Pie"));
    }

    @Test
    void testAddRecipeNegative() {
        cookBook.addRecipe(new Scanner("Apple Pie\nApple\n3\n2\nPieces\nno\nMix and bake\n4\n"));
        assertFalse(cookBook.containsRecipe("Banana Bread"));
    }

    @Test
    void testGetRecipePositive() {
        cookBook.addRecipe(new Scanner("Apple Pie\nApple\n3\n2\nPieces\nno\nMix and bake\n4\n"));
        Recipe retrievedRecipe = cookBook.getRecipe("Apple Pie");
        assertNotNull(retrievedRecipe);
      assertEquals("Apple Pie", retrievedRecipe.name());
    }

    @Test
    void testGetRecipeNegative() {
        cookBook.addRecipe(new Scanner("Apple Pie\nApple\n3\n2\nPieces\nno\nMix and bake\n4\n"));
        Recipe retrievedRecipe = cookBook.getRecipe("Banana Bread");
        assertNull(retrievedRecipe);
    }

    @Test
    void testRemoveRecipePositive() {
        cookBook.addRecipe(new Scanner("Apple Pie\nApple\n3\n2\nPieces\nno\nMix and bake\n4\n"));
        cookBook.removeRecipe("Apple Pie");
        assertFalse(cookBook.containsRecipe("Apple Pie"));
    }

    @Test
    void testRemoveRecipeNegative() {
        cookBook.addRecipe(new Scanner("Apple Pie\nApple\n3\n2\nPieces\nno\nMix and bake\n4\n"));
        cookBook.removeRecipe("Banana Bread");
        assertTrue(cookBook.containsRecipe("Apple Pie"));
    }
}