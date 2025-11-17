FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set the working directory in the image
WORKDIR /app

# Copy pom.xml and source code to the container
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean install -DskipTests

# Use openjdk 21 for running the application
FROM eclipse-temurin:21-jre-jammy

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application's port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]