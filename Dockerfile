FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/price-comparator-backend-*.jar app.jar

EXPOSE 8080

ENV APP_IMPORT_ENABLED=false

ENTRYPOINT ["java", "-jar", "app.jar"]
