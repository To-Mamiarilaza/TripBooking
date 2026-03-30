# Build stage
FROM maven:3.9.1-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Set environment variables (can be overridden at runtime)
ENV DB_URL=jdbc:postgresql://dpg-d756qulactks738r3bg0-a.oregon-postgres.render.com:5432/tripbooking_ir0w \
    DB_USERNAME=tripbooking_ir0w_user \
    DB_PASSWORD=kMd2QNfo5i6fvTNHpJkqWaLTs0MQ2XYw

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
