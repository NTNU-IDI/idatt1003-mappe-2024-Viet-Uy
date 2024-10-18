package edu.ntnu.idi.bidata.ui;

import edu.ntnu.idi.bidata.FoodStorage;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class UiTest {
    static class MockFoodStorage extends FoodStorage{
        @Override
        public void saveIngredientsToFile(String filename){
            // Do nothing
        }
    }

    @Test
    void testAddIngredient() {
        String simulatedInput = "Apple\n4\n10\n2.5\n2022-12-12\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        FoodStorage foodStorage = new MockFoodStorage();
<<<<<<< Updated upstream
        Ui.addIngredient(foodStorage, new Scanner(System.in));
=======
        //Ui.saveIngredientsToFile(foodStorage, new Scanner(System.in));
>>>>>>> Stashed changes

        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Invalid choice"));

    }
}