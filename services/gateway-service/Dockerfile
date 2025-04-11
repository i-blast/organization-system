FROM eclipse-temurin:24-jre-alpine

WORKDIR /app
COPY build/libs/gateway-service-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
