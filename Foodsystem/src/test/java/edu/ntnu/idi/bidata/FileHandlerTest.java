package edu.ntnu.idi.bidata;

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
      tempFile.createNewFile();
    }
  }

  @AfterEach
  void tearDown() {
    if (tempFile.exists()) {
      tempFile.delete();
    }
  }

  @Test
  void testGetResourcePath() {
    String path = FileHandler.getResourcePath(TEMP_FILE_NAME);
    assertNotNull(path, "Resource path should not be null");
    assertTrue(path.endsWith(TEMP_FILE_NAME), "Path should end with the given filename");
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
  void testReadFromNonExistentFile() {
    String content = FileHandler.readFromFile("nonexistent.txt");
    assertTrue(content.isEmpty(), "Reading from a non-existent file should return empty content");
  }

  @Test
  void testWriteToFileIOException() {
    File readOnlyFile = new File(tempFile.getPath());
    readOnlyFile.setReadOnly();

    String content = "This will fail";
    assertDoesNotThrow(() -> FileHandler.writeToFile(readOnlyFile.getPath(), content));

    readOnlyFile.setWritable(true); // Reset the file for cleanup
  }
}
