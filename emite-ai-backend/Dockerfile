# Usar uma imagem oficial do Maven com JDK 17
FROM maven:3.9.2-eclipse-temurin-17 AS builder

# Definir o diretório de trabalho
WORKDIR /app

# Copiar os arquivos do projeto para o container
COPY pom.xml .
COPY src ./src

# Fazer o build do projeto e criar o JAR
RUN mvn clean package -DskipTests

# Usar uma imagem mais leve do Java para rodar o JAR
FROM eclipse-temurin:17-jre

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o JAR gerado para o container
COPY --from=builder /app/target/emite-ai-backend-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta usada pela aplicação
EXPOSE 8080

# Comando padrão para rodar o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
