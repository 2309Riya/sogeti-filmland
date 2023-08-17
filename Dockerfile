# Use an official Maven image as the base image for building
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and the whole source code to the container
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package

# Use an official OpenJDK 17 image as the base image for the runtime image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build image to the runtime image
COPY --from=build /app/target/sogeti-filmland-0.0.1-SNAPSHOT.jar ./sogeti-filmland-0.0.1-SNAPSHOT.jar

# Start the Spring Boot application
CMD ["java", "-jar", "sogeti-filmland-0.0.1-SNAPSHOT.jar"]
