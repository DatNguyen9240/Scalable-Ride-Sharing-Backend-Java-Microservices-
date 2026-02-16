# Build stage: compile the project with Maven and cache dependencies
FROM eclipse-temurin:17 AS build
USER root
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
WORKDIR /workspace
# copy only pom first to cache dependency download layer
COPY trip-service/pom.xml /workspace/trip-service/pom.xml
RUN mvn -f /workspace/trip-service/pom.xml -DskipTests dependency:go-offline
# copy sources and build
COPY trip-service /workspace/trip-service
RUN mvn -f /workspace/trip-service/pom.xml -DskipTests package

# Run stage: use a slim JRE image
FROM eclipse-temurin:17
WORKDIR /app
# install curl for HEALTHCHECK (small, optional) and clean apt lists
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
# copy the built jar from the build stage
COPY --from=build /workspace/trip-service/target/trip-service-0.0.1-SNAPSHOT.jar app.jar
# Optional external config expected by the app
COPY trip-service/src/main/resources/application-docker.properties /app/application-docker.properties

EXPOSE 8082
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8082/v3/api-docs || exit 1
CMD ["java", "-Dspring.config.location=file:/app/", "-jar", "app.jar"]