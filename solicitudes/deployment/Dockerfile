FROM openjdk:24-jdk-slim
WORKDIR /app
COPY applications/app-service/build/libs/ReactiveProject.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
