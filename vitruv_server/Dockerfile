# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /vitruv_server

# Create a non-root user
RUN useradd -m -u 1000 appuser && chown -R appuser /vitruv_server
USER appuser

# Copy the Maven project file and the local dependencies to the container
COPY pom.xml .
COPY Dependencies/ Dependencies/

# Download the dependencies
RUN mvn dependency:go-offline

# Copy the source code to the container
COPY src/ src/

# Package the application
RUN mvn clean package -DskipTests

# Expose the port the application runs on
EXPOSE 8000

# Rename the built JAR file and change permissions
RUN mv target/vitruv_server-*.jar target/vitruv_server.jar

# Run the application
CMD ["java", "-jar", "target/vitruv_server.jar"]