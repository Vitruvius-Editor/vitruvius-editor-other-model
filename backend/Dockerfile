# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /backend

# Copy only the pom.xml file first
COPY pom.xml .

# Download the Maven dependencies
RUN mvn dependency:go-offline

# Copy the rest of the project files
COPY . .

# Build the Maven project
RUN mvn clean install

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /backend
COPY --from=build /backend/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
