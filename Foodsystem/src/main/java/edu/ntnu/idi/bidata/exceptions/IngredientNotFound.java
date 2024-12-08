package edu.ntnu.idi.bidata.exceptions;

/**
 * Exception thrown when an ingredient is not found.
 */
public class IngredientNotFound extends RuntimeException {

  /**
   * Create a new IngredientNotFound exception.
   *
   * @param message the message to display.
   */
  public IngredientNotFound(String message) {
    super(message);
  }
}
