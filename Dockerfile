# Étape 1 : Construire l'application (utilise un JDK complet pour la compilation)
FROM openjdk:17-jdk-slim AS builder

# Expose le port par défaut de Spring Boot (par exemple, 8080)
EXPOSE 8080

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

# Exécute la commande Gradle pour construire le JAR exécutable
# Le --no-daemon est important pour les environnements CI/CD pour éviter les problèmes de processus persistants
RUN ./gradlew bootJar --no-daemon

# Copie le JAR compilé (assurez-vous que le chemin et le nom sont corrects)
# L'étape BUILDER_IMAGE_NAME doit compiler le JAR avant cette étape si vous utilisez un build multi-étapes
# Pour un build simple avec votre Gradle Wrapper :
COPY build/libs/api-crypto-0.0.1-SNAPSHOT.jar app.jar

# Commande pour démarrer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
