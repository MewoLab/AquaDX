# Use a multi-stage build to keep the image size small
# Pin the builder so ARM64 deployments cannot reuse an older floating jdk25 image.
FROM gradle:9.6.1-jdk25 AS builder

# Copy the Gradle wrapper and configuration files separately to leverage Docker cache
COPY --chown=gradle:gradle gradlew /home/gradle/
COPY --chown=gradle:gradle gradle /home/gradle/gradle
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts /home/gradle/

# Set working directory
WORKDIR /home/gradle

# Replace CRLF with LF in gradlew to make it work on Linux
RUN sed -i 's/\r$//' ./gradlew

# Download dependencies - cached if build.gradle.kts and settings.gradle.kts are unchanged
RUN chmod +x ./gradlew
RUN java -version
RUN ./gradlew dependencies

# Copy the project source, this layer is rebuilt whenever a file has changed
COPY --chown=gradle:gradle src /home/gradle/src

# Build the application
RUN ./gradlew build -x test

# Start with a fresh image for the runtime
FROM eclipse-temurin:25-jre-alpine

# Set the deployment directory
WORKDIR /app

# Copy only the built JAR from the builder image
COPY --from=builder /home/gradle/build/libs/AquaDX-*.jar /app/

# The command to run the application
CMD java -jar AquaDX-*.jar
