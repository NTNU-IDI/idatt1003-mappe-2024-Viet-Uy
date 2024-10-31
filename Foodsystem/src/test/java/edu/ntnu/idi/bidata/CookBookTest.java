package edu.ntnu.idi.bidata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CookBookTest {

    private CookBook cookBook;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        HashMap<String, IngredientInfo> ingredientsMap = new HashMap<>();
        cookBook = new CookBook("My CookBook", ingredientsMap, "A collection of recipes");
        ArrayList<IngredientInfo> ingredients = new ArrayList<>();
        ingredients.add(new IngredientInfo("Apple", 2, "Pieces"));
        recipe = new Recipe("Apple Pie", "Delicious apple pie", "Mix and bake", ingredients, 4);
    }

    @Test
    void testAddRecipePositive() {
        cookBook.addRecipe(recipe);
        assertTrue(cookBook.containsRecipe("Apple Pie"));
    }

    @Test
    void testGetRecipePositive() {
        cookBook.addRecipe(recipe);
        Recipe retrievedRecipe = cookBook.getRecipe("Apple Pie");
        assertNotNull(retrievedRecipe);
        assertEquals("Apple Pie", retrievedRecipe.getName());
    }

    @Test
    void testRemoveRecipePositive() {
        cookBook.addRecipe(recipe);
        cookBook.removeRecipe("Apple Pie");
        assertFalse(cookBook.containsRecipe("Apple Pie"));
    }

    @Test
    void testAddRecipeNegative() {
        cookBook.addRecipe(recipe);
        assertFalse(cookBook.containsRecipe("Banana Bread"));
    }

    @Test
    void testGetRecipeNegative() {
        cookBook.addRecipe(recipe);
        Recipe retrievedRecipe = cookBook.getRecipe("Banana Bread");
        assertNull(retrievedRecipe);
    }

    @Test
    void testRemoveRecipeNegative() {
        cookBook.addRecipe(recipe);
        cookBook.removeRecipe("Banana Bread");
        assertTrue(cookBook.containsRecipe("Apple Pie"));
    }
}