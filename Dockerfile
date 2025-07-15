# Étape 1 : Construire l'application (utilise un JDK complet pour la compilation)
FROM openjdk:17-jdk-slim AS builder

# Définit le répertoire de travail à l'intérieur du conteneur
WORKDIR /app

# Copie les fichiers de build Gradle et le code source
COPY gradlew .
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/
COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

# Modification des droits
RUN chmod +x ./gradlew

RUN echo "Forcing rebuild at $(date)"

# Exécute la commande Gradle pour construire le JAR exécutable
# Le --no-daemon est important pour les environnements CI/CD pour éviter les problèmes de processus persistants
RUN ./gradlew bootJar --no-daemon

# Étape 2 : Créer l'image finale (utilise un JRE léger pour l'exécution)
FROM openjdk:17-jre-slim

# Expose le port par défaut de Spring Boot (par exemple, 8080)
EXPOSE 8080

# Définit le répertoire de travail dans l'image finale
WORKDIR /app

# Copie le JAR compilé depuis l'étape de construction précédente ('builder')
# Use the name you confirmed locally: api-crypto-0.0.1-SNAPSHOT.jar
COPY --from=builder /app/build/libs/api-crypto-0.0.1-SNAPSHOT.jar app.jar

# Commande pour démarrer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
