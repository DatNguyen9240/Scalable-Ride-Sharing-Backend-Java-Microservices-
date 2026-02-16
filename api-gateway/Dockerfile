# Build stage: compile the project with Maven and cache dependencies
FROM eclipse-temurin:17 AS build
USER root
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
WORKDIR /workspace
COPY pom.xml /workspace/api-gateway/pom.xml
RUN mvn -f /workspace/api-gateway/pom.xml -DskipTests dependency:go-offline
COPY . /workspace/api-gateway
RUN mvn -f /workspace/api-gateway/pom.xml -DskipTests package

# Run stage: slim JRE
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /workspace/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml /app/application.yml
EXPOSE 8080
CMD ["java", "-Dspring.config.location=file:/app/", "-jar", "app.jar"]