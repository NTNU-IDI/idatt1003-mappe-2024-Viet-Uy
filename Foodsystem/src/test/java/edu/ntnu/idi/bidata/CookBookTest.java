package edu.ntnu.idi.bidata;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CookBookTest {

  private MockFoodStorage mockFoodStorage;

  @BeforeEach
  void setUp() {
    mockFoodStorage = new MockFoodStorage();
  }

  @Test
  void testMockAddIngredient() {
    String input = "Apple\n3\n2\n20\n";
    try (Scanner scanner = new Scanner(input)) {
      String name = scanner.nextLine();
      int numberOfUnits = scanner.nextInt();
      double price = scanner.nextDouble();
      String unit = scanner.next();
      mockFoodStorage.addIngredient(name, unit, numberOfUnits, price, LocalDate.now());
      assertTrue(mockFoodStorage.containsIngredient("Apple"));
    }
  }

  @Test
  void testMockRemoveIngredient() {
    // Add an ingredient first
    String input = "Apple\n3\n2\n20\n";
    try (Scanner scanner = new Scanner(input)) {
      String name = scanner.nextLine();
      int numberOfUnits = scanner.nextInt();
      double price = scanner.nextDouble();
      String unit = scanner.next();
      mockFoodStorage.addIngredient(name, unit, numberOfUnits, price, LocalDate.now());
    }
    assertTrue(mockFoodStorage.containsIngredient("Apple"));

    // Remove the ingredient
    mockFoodStorage.removeIngredient("Apple");
    assertFalse(mockFoodStorage.containsIngredient("Apple"));
  }

  // Mock FoodStorage class
  static class MockFoodStorage extends FoodStorage {
    private final HashMap<String, IngredientInfo> mockIngredients = new HashMap<>();

    @Override
    public boolean addIngredient(String name, String unit, int numberOfUnits, double price, LocalDate expirationDate) {
      if (name.isEmpty() || unit == null || numberOfUnits <= 0 || price <= 0 || expirationDate == null) {
        return false;
      }
      mockIngredients.put(name, new IngredientInfo(name, numberOfUnits, unit, price));
      System.out.println("Mock add ingredient: " + name + ", Amount: " + numberOfUnits + ", Price: " + price);
      return true;
    }

    public boolean containsIngredient(String name) {
      return mockIngredients.containsKey(name);
    }

    public void removeIngredient(String name) {
      if (mockIngredients.containsKey(name)) {
        mockIngredients.remove(name);
        System.out.println("Mock remove ingredient: " + name);
      } else {
        System.out.println("Ingredient not found: " + name);
      }
    }
  }
}