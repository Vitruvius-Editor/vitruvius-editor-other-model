# VitruvEditorAdapter

VitruvEditorAdapter is a Kotlin-based Spring project that serves as an adapter between the Vitruvius framework and the editor. This project allows users to interact with Vitruvius models and perform model-related operations directly from their preferred editor.

## Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher

## Building the Project

1. Clone the repository:

   ```
   git clone https://github.com/Vitruv-Editor/vitruv-editor
   ```

2. Navigate to the project directory:

   ```
   cd backend
   ```

3. Build the project using Maven:

   ```
   mvn clean install
   ```

   This command will compile the source code, run the tests, and package the application into a JAR file.

## Running the Application

1. After the build is successful, you can run the application using the following command:

   ```
   java -jar target/vitruvEditorAdapter-1.0.0.jar
   ```

   This will start the Spring Boot application and make it available at `http://localhost:8080`.

2. Alternatively, you can run the application directly from Maven:

   ```
   mvn spring-boot:run
   ```

   This command will start the application and keep the terminal session attached to the running process.

## Configuring the Application

The application's configuration is stored in the `application.properties` file located in the `src/main/resources` directory. You can customize the settings, such as the server port, database connection, and other application-specific properties, by modifying this file.

## Interacting with the Application

The VitruvEditorAdapter provides a set of REST API endpoints that allow you to interact with Vitruvius models from your editor environment. You can find the documentation for these endpoints in the project's `README.md` file or by using a tool like Swagger UI.

## Contributing

If you would like to contribute to the VitruvEditorAdapter project, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Implement your changes and ensure that all tests pass.
4. Submit a pull request with a detailed description of your changes.

We welcome contributions from the community and appreciate your support in improving the VitruvEditorAdapter project.

## License

This project is licensed under the [MIT License](LICENSE).