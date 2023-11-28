# Use an official OpenJDK runtime as a parent image
FROM openjdk:17.0.1-jdk-slim
# Copy the application JAR into the container
COPY target/fxdeals-0.0.1-SNAPSHOT.jar .
# Expose the port your application runs on (if applicable)
EXPOSE 8080
# Define the command to run your Spring Boot application
CMD ["java", "-jar", "fxdeals-0.0.1-SNAPSHOT.jar"]
