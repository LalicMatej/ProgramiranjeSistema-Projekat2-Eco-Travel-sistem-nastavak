# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY shared-events/pom.xml shared-events/pom.xml
COPY shared-events/src shared-events/src
RUN mvn -f shared-events/pom.xml clean install -DskipTests -B

COPY booking-service/pom.xml .
RUN mvn dependency:go-offline -B
COPY booking-service/src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=build --chown=appuser:appgroup /app/target/*.jar app.jar
USER appuser
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
