FROM eclipse-temurin:24-jre-alpine

WORKDIR /app
COPY build/libs/user-service-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
