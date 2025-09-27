FROM openjdk:24-jdk-slim
WORKDIR /app

# Copiar jar
COPY applications/app-service/build/libs/ReactiveProject.jar app.jar

# Exponer puerto
EXPOSE 8082

# ENTRYPOINT usando el script para esperar MySQL
ENTRYPOINT ["java", "-jar", "app.jar"]

