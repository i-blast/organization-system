FROM eclipse-temurin:24-jre-alpine

WORKDIR /app
COPY build/libs/config-service-*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
