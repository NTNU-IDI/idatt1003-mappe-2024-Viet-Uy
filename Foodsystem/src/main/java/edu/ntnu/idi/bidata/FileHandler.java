package edu.ntnu.idi.bidata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to handle file operations.
 */
public class FileHandler {
  // Logger to log messages
  private static final Logger logger = Logger.getLogger(FileHandler.class.getName());

  // Private constructor to prevent instantiation
  private FileHandler() {
    // Prevent instantiation of the class by throwing an exception in the constructor
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Get the path of a resource file.
   *
   * @param filename the name of the file.
   * @return the path of the file.
   */
  public static String getResourcePath(String filename) {
    URL resourceUrl = FileHandler.class.getClassLoader().getResource(filename);
    if (resourceUrl == null) {
      try {
        String resourcePath = Objects.requireNonNull(
            FileHandler.class.getClassLoader().getResource("")).getPath();
        String decodedPath = URLDecoder.decode(resourcePath, StandardCharsets.UTF_8);
        File newFile = new File(decodedPath, filename);
        if (newFile.createNewFile()) {
          return newFile.getPath();
        } else {
          throw new IOException("File already exists: " + filename);
        }
      } catch (IOException e) {
        throw new IllegalArgumentException("Error");
      }
    }
    return URLDecoder.decode(resourceUrl.getPath(), StandardCharsets.UTF_8);
  }

  /**
   * Write content to a file.
   *
   * @param filePath the path of the file.
   * @param content  the content to write.
   */
  public static void writeToFile(String filePath, String content) {
    // Write content to the file
    String decodedPath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
    try (PrintWriter writer = new PrintWriter(new FileWriter(decodedPath, true))) {
      writer.println(content);
      writer.println();
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Could not write to file: {0}", filePath);
      logger.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  /**
   * Read content from a file.
   *
   * @param filePath the path of the file.
   * @return the content of the file.
   */
  public static String readFromFile(String filePath) {
    String decodedPath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
    StringBuilder content = new StringBuilder(); // StringBuilder to store the content
    try (BufferedReader reader = new BufferedReader(new FileReader(decodedPath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line).append("\n");
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Could not read from file: {0}", decodedPath);
      logger.log(Level.SEVERE, e.getMessage(), e);
      return "";
    }
    return content.toString();
  }
}