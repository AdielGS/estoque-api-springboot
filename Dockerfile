# Estágio 1: Build da aplicação com o Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Criação da imagem final, leve e otimizada
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/estoque-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]