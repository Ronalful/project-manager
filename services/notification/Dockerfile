FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY wait-for.sh ./wait-for.sh