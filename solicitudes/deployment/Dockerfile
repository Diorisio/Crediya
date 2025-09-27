FROM openjdk:24-jdk-slim
WORKDIR /app

# Copiar jar
COPY applications/app-service/build/libs/ReactiveProject.jar app.jar

# Exponer puerto
EXPOSE 8081

# Copiar script de espera
#COPY deployment/wait-for-mysql.sh /wait-for-mysql.sh
#RUN chmod +x /wait-for-mysql.sh

# ENTRYPOINT usando el script para esperar MySQL
ENTRYPOINT ["java", "-jar", "app.jar"]

#ENTRYPOINT ["/wait-for-mysql.sh", "mysql", "java", "-jar", "app.jar"]

