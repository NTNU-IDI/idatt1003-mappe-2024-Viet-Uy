# Portfolio project IDATT1003

STUDENT NAME = "Viet-Uy"  
STUDENT ID = "137422"

# Project description

This project is a Java-based application developed using Maven, designed to help users efficiently manage their food ingredients and recipes🥕. Users can add new ingredients to the storage system, specify their quantities, and record expiration dates. The application tracks ingredient expiration and alerts users when items are close to expiring, helping to reduce food waste📅. Additionally, users can create and store recipes in a cookbook, associating them with specific ingredients🧑🏽‍🍳. The system can suggest recipes based on the ingredients currently available in the user's storage, making meal planning easier. This project utilizes Maven for dependency management and leverages Java’s robust libraries to deliver a user-friendly and efficient food storage and recipe management experience.

# Project structure

## Source Files📁:  
- All source files are stored in the src/main/java directory.
- The main package is edu.ntnu.idi.bidata, which contains classes such as FileHandler.

### Packages📦:  
- The project uses the package edu.ntnu.idi.bidata for organizing its classes.
- This package includes classes related to file handling and possibly other functionalities related to the recipe management system.

### JUnit Test Classes:  
- All JUnit test classes are stored in the src/test/java directory.
- The test classes are organized in the same package structure as the source files, i.e., edu.ntnu.idi.bidata.

This structure follows the standard Maven project layout, which separates source files and test files into different directories for better organization and maintainability.

# Link to repository

https://github.com/NTNU-IDI/idatt1003-mappe-2024-Viet-Uy

# How to run the project

### Build the Project👷🏽‍♂️:

Use Maven to build the project. Run the following command in the terminal:
mvn clean install

### Run the Project🏃🏽:  
- The main class of the project is edu.ntnu.idi.bidata.Main.
- You can run the project using the following command:
  mvn exec:java -Dexec.mainClass="edu.ntnu.idi.bidata.Main"

### Main Class and Method🧠:  
- The main class is edu.ntnu.idi.bidata.Main.
- The main method is the entry point of the program:

public class Main {
  public static void main(String[] args) {
    // Your code to start the application
  }
}

### Input and Output✏️:  
- Input: The program reads recipes from a file. The file should contain recipes formatted with the recipe name, ingredients, instructions, and the number of people it serves.
- Output: The program outputs the recipes to the console or saves them to a file with proper formatting.

### Expected Behavior:
- The program should load recipes from a specified file, parse the content, and store the recipes in a structured format.
- It should handle file operations such as reading from and writing to files.
- The program should log any errors encountered during file operations.
- The recipes should be displayed or saved with proper formatting, ensuring readability.


# How to run the tests🏃🏽

### Build the Project:  
- Use Maven to build the project. Run the following command in the terminal:
  mvn clean install

### Run the Tests:  
- Use Maven to run the tests. Execute the following command:
  mvn test

# References 🔗

https://www.w3schools.com 
https://www.geeksforgeeks.org
https://stackoverflow.com

Additional references are included in the project report.
