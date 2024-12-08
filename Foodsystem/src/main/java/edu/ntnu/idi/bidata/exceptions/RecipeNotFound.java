package edu.ntnu.idi.bidata.exceptions;

/**
 * Exception thrown when a recipe is not found.
 */
public class RecipeNotFound extends RuntimeException {

  /**
   * Create a new RecipeNotFound exception.
   *
   * @param message the message to display.
   */
  public RecipeNotFound(String message) {
    super(message);
  }

}
