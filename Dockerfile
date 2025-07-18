# Étape 1 : build avec Maven
FROM eclipse-temurin:21 AS builder

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Donner les droits d'exécution à mvnw
RUN chmod +x ./mvnw

# Télécharger les dépendances d'abord pour bénéficier du cache Docker
RUN ./mvnw dependency:go-offline

# Copier le code source
COPY src ./src

# Compiler l'application avec plus de logs
RUN ./mvnw clean package -DskipTests -X

# Étape 2 : image finale
FROM eclipse-temurin:21

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/src/main/resources/db/migration /app/src/main/resources/db/migration

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]