FROM eclipse-temurin:24-jre-alpine

WORKDIR /app
COPY build/libs/company-service-*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
