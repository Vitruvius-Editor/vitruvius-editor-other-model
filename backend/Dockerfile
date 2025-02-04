# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /backend

# Copy pom and local dependencies only
COPY pom.xml .
COPY Dependencies/ Dependencies/

# Download dependencies
RUN mvn dependency:go-offline

# Copy the source code
COPY src/ src/

# Build the Maven project
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /backend

# Create a non-root user
RUN useradd -m -u 1000 appuser
USER appuser

# Copy the built application from the build stage
COPY --from=build /backend/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
