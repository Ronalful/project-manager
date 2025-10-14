# Этап 1: Сборка JAR
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY wait-for.sh .

# Даем права на запуск wait-for.sh
RUN chmod +x wait-for.sh

RUN mvn clean package -DskipTests

# Этап 2: Запуск
FROM eclipse-temurin:17-jdk-alpine

# Установка bash
RUN apk add --no-cache bash

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/wait-for.sh .

RUN chmod +x wait-for.sh

EXPOSE 8080

ENTRYPOINT ["./wait-for.sh", "-t", "0", "config-service:8080", "--", "java", "-jar", "app.jar"]
