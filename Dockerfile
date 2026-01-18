# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn -q -e -B dependency:go-offline

COPY src ./src
RUN mvn -q -e -B clean package -DskipTests


# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /build/target/truck-load-planner-service-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
