# Stage 1: Build the Maven project
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the source code
COPY . .

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar server.jar

# Expose the port the app runs on (change if necessary)
EXPOSE 8000

# Command to run the JAR file
CMD ["java", "-jar", "server.jar"]
