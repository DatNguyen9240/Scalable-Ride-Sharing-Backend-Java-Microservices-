# Build stage: compile the project with Maven and cache dependencies
FROM eclipse-temurin:17 AS build
USER root
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
WORKDIR /workspace
COPY pom.xml /workspace/user-service/pom.xml
RUN mvn -f /workspace/user-service/pom.xml -DskipTests dependency:go-offline
COPY . /workspace/user-service
RUN mvn -f /workspace/user-service/pom.xml -DskipTests package

# Run stage: slim JRE
FROM eclipse-temurin:17
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
COPY --from=build /workspace/user-service/target/user-service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties /app/application.properties
EXPOSE 8081
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 CMD curl -f http://localhost:8081/v3/api-docs || exit 1
CMD ["java", "-Dspring.config.location=file:/app/", "-jar", "app.jar"]