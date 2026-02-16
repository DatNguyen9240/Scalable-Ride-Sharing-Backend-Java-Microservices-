# Build stage: compile the project with Maven and cache dependencies
FROM eclipse-temurin:17 AS build
USER root
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
WORKDIR /workspace
COPY pom.xml /workspace/payment-service/pom.xml
RUN mvn -f /workspace/payment-service/pom.xml -DskipTests dependency:go-offline
COPY . /workspace/payment-service
RUN mvn -f /workspace/payment-service/pom.xml -DskipTests package

# Run stage: slim JRE
FROM eclipse-temurin:17
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
COPY --from=build /workspace/payment-service/target/payment-service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties /app/application.properties
EXPOSE 8083
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 CMD curl -f http://localhost:8083/v3/api-docs || exit 1
CMD ["java", "-Dspring.config.location=file:/app/", "-jar", "app.jar"]