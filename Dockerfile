# Étape 1 : Build avec Maven
FROM maven:3.9.8-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x ./mvnw && \
    ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests -B && \
    rm -rf /root/.m2/repository

# Étape 2 : Image finale
FROM bellsoft/liberica-runtime-container:jre-21-slim-musl

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/src/main/resources/db/migration /app/src/main/resources/db/migration

RUN chown -R 1001:1001 /app

USER 1001

EXPOSE 8082

CMD ["java", "-jar", "app.jar"]