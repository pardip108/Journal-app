# --- STAGE 1: Build the Application JAR (Uses a Maven/JDK image) ---
FROM maven:3.8.3-openjdk-17 AS build

# Set a consistent working directory
WORKDIR /app

# Copy Maven wrapper files (needed for the mvnw command to work)
COPY .mvn .mvn
COPY mvnw .

# 1. Copy only the build file (pom.xml) first
# This layer rarely changes, so Docker caches it efficiently.
COPY pom.xml .

# 2. Download all dependencies
RUN mvn dependency:go-offline

# 3. Copy the source code
COPY src src

# 4. Build the final application JAR
RUN mvn clean install -DskipTests

# --- STAGE 2: Create the Final, Lean Runtime Image (Uses a JRE image) ---

# Use a JRE image for a smaller final package size
FROM eclipse-temurin:17-jre-alpine

# Set the application's default port as an environment variable (for documentation/default)
ENV SERVER_PORT 8080

# Set the working directory
WORKDIR /app

# The JAR is copied from the 'build' stage's 'target' directory.
# The JAR name is confirmed to be journalApp-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/journalApp-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port. EXPOSE is purely documentary, but helps with networking tools.
EXPOSE ${SERVER_PORT}

# Run the application with optimized JVM flags for container environments.
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]