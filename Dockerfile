FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "-DdbUrl=${DB_URL}", "-DdbUsername=${DB_USERNAME}", "-DdbPassword=${DB_PASSWORD}", "app.jar"]

EXPOSE 8080