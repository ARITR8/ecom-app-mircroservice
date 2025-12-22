FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the JAR file
COPY target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

# Expose Eureka port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]