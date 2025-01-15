# Stage 1: Dependency stage
FROM amazoncorretto:17-alpine3.19-jdk AS dependency

# Set the working directory inside the container
WORKDIR /app

# Copy only the Gradle-related files first to leverage caching
COPY gradlew .
COPY gradle/ ./gradle/
COPY build.gradle settings.gradle ./

# Give the correct permissions to gradlew
RUN chmod +x gradlew

# Download and cache Gradle dependencies
RUN ./gradlew --version

# Copy the Gradle dependencies definition files
RUN ./gradlew dependencies --no-daemon

# Stage 2: Build stage
FROM dependency AS builder

# Copy the rest of the application source code
COPY src ./src/

# Build the application
RUN ./gradlew build --no-daemon

# Stage 3: Runtime stage
FROM amazoncorretto:17-alpine3.19-jdk AS runner

# Set the working directory inside the container
WORKDIR /app

# Copy the built application from the builder stage
COPY --from=builder /app/build/libs/*.war app.war

# Set the entrypoint command
ENTRYPOINT ["java", "-jar", "app.war"]
