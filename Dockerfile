FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "-Ddb-url=${DB_URL}", "-Ddb-username=${DB_USERNAME}", "-Ddb-password=${DB_PASSWORD}", "app.jar"]

EXPOSE 8080