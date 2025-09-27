FROM openjdk:24-jdk-slim
WORKDIR /app

COPY applications/app-service/build/libs/ReactiveProject.jar app.jar


EXPOSE 8080

# Copiar script de espera
#COPY deployment/wait-for-mysql.sh /wait-for-mysql.sh
#RUN chmod +x /wait-for-mysql.sh

ENTRYPOINT ["java", "-jar", "app.jar"]

#ENTRYPOINT ["/wait-for-mysql.sh", "mysql", "java", "-jar", "app.jar"]

