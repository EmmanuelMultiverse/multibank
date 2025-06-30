# Dockerfile for a Spring Boot application using a multi-stage build.
# This approach helps create a smaller, more secure final image.

# --- Stage 1: Build the application ---
# This stage uses a full JDK 21 image to compile your Java application and build the JAR.
# 'eclipse-temurin:21-jdk-jammy' provides OpenJDK 21 on a Ubuntu Jammy base.
# 'AS builder' names this stage so we can reference it later.
FROM eclipse-temurin:21-jdk-jammy AS builder

# Set the working directory inside the container for this stage.
WORKDIR /workspace

# Copy your entire project directory into the container.
# The '.' at the end means "copy everything from the current directory on my host machine".
COPY . .

# Build the Spring Boot application using Maven Wrapper.
# '-DskipTests' is often used in Docker builds to speed up the process, assuming
# tests are run in a separate CI step. Remove if you need tests in the image.
RUN ./mvnw clean package -DskipTests

# --- Stage 2: Create the final, smaller runtime image ---
# This stage uses a minimal JRE 21 image, which is much smaller than a JDK image.
# 'eclipse-temurin:21-jre-alpine' provides OpenJDK 21 JRE on Alpine Linux,
# known for its small size and security.
# For Cloud Run, you could also consider Google's optimized image:
# FROM us-docker.pkg.dev/cloudrun/container/java21
FROM eclipse-temurin:21-jre-alpine

# Set the working directory for the final application.
WORKDIR /app

# Copy the built JAR file from the 'builder' stage into the final image.
# '/workspace/target/*.jar' assumes your Maven build outputs a single JAR in 'target/'.
# 'app.jar' is the name it will have in the final image.
COPY --from=builder /workspace/target/*.jar app.jar

# Expose the port your Spring Boot application listens on.
# Cloud Run automatically detects this if it's set via the PORT environment variable,
# but explicitly exposing it is good practice.
EXPOSE 8080

# Define the command to run your Spring Boot application when the container starts.
# 'java -jar app.jar' is the standard way to run an executable Spring Boot JAR.
ENTRYPOINT ["java", "-jar", "app.jar"]

# Optional: JVM optimizations for better startup/memory on platforms like Cloud Run.
# You can uncomment these lines and adjust values as needed.
# ENV JAVA_TOOL_OPTIONS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Xss256k"
# ENV SPRING_MAIN_LAZY_INITIALIZATION=true


