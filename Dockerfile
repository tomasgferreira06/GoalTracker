# Base image with OpenJDK 17
FROM maven:3.8.4-openjdk-17-slim

# Install required dependencies
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libgtk2.0-0 \
    libxt-dev \
    xauth \
    x11-utils

# Set the display environment variable
ENV DISPLAY={$DISPLAY}

# Set the working directory
WORKDIR /app

# Copy the source code
COPY . /app

# Build the JAR file
RUN mvn clean package

# Copy the JavaFX SDK
COPY javafx-sdk /app/javafx-sdk

# Set the classpath for the JavaFX application
ENV CLASSPATH /app/target/<gps_g12>-1.0-SNAPSHOT.jar:/app/javafx-sdk/lib/*

# Run the JavaFX application
CMD ["java", "--module-path", "/app/javafx-sdk/lib", "--add-modules", "javafx.controls,javafx.fxml", "com.example.gps_g12.goalTracker.GoalTrackerApplication"]