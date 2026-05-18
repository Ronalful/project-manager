# Этап 1: Сборка JAR
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
COPY wait-for.sh .

# Преобразование CRLF в LF и установка прав
RUN sed -i 's/\r$//' wait-for.sh && \
    chmod +x wait-for.sh

RUN mvn clean package -DskipTests

# Копируем JAR
RUN cd target && \
    JAR_FILE=$(ls *.jar | grep -v sources | grep -v javadoc | head -1) && \
    cp $JAR_FILE /app/app.jar

# Этап 2: Запуск
FROM eclipse-temurin:17-jdk

WORKDIR /app

RUN apt-get update && \
    apt-get install -y netcat-openbsd && \
    rm -rf /var/lib/apt/lists/*

# Копирование файлов
COPY --from=build /app/app.jar .
COPY --from=build /app/wait-for.sh /wait-for.sh

# Еще раз преобразуем и даем права
RUN sed -i 's/\r$//' /wait-for.sh && \
    chmod +x /wait-for.sh

EXPOSE 8080

ENTRYPOINT ["/wait-for.sh", "-t", "0", "discovery-service:8080", "--", "java", "-jar", "app.jar"]