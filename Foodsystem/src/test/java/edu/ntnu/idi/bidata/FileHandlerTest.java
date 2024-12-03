package edu.ntnu.idi.bidata;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import org.junit.jupiter.api.*;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

  private static final String TEMP_FILE_NAME = "testFile.txt";
  private File tempFile;

  @BeforeEach
  void setUp() throws IOException {
    tempFile = new File(TEMP_FILE_NAME);
    if (!tempFile.exists()) {
      boolean isFileCreated = tempFile.createNewFile();
      if (!isFileCreated) {
        throw new IOException("Failed to create new file: " + TEMP_FILE_NAME);
      }
    }
  }

  @AfterEach
  void tearDown() {
    if (tempFile.exists()) {
      boolean isFileDeleted = tempFile.delete();
      if (!isFileDeleted) {
        System.err.println("Failed to delete file: " + TEMP_FILE_NAME);
      }
    }
  }


  @Test
  void testWriteToFile() throws IOException {
    String content = "Hello, world!";
    FileHandler.writeToFile(tempFile.getPath(), content);

    String writtenContent = new String(java.nio.file.Files.readAllBytes(tempFile.toPath()));
    assertTrue(writtenContent.contains(content), "File should contain the written content");
  }

  @Test
  void testReadFromFile() throws IOException {
    String content = "Test content for reading";
    try (PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
      writer.println(content);
    }

    String fileContent = FileHandler.readFromFile(tempFile.getPath());
    assertTrue(fileContent.contains(content), "Read content should match written content");
  }


  @Test
  void testWriteToFileIOException() {
    File readOnlyFile = new File(tempFile.getPath());

    String content = "This will fail";
    assertDoesNotThrow(() -> FileHandler.writeToFile(readOnlyFile.getPath(), content));

  }
}
