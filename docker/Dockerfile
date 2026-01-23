# Dockerfile para a aplicação Spring Boot
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar arquivo de configuração do Maven primeiro (para cache de dependências)
COPY pom.xml .

# Baixar dependências (cache layer - só reexecuta se pom.xml mudar)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar e empacotar a aplicação
RUN mvn clean package -DskipTests

# Imagem final
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar o JAR da aplicação
COPY --from=build /app/target/*.jar app.jar

# Expor a porta da aplicação
EXPOSE 8080

# Variáveis de ambiente padrão
ENV DATABASE_URL=jdbc:postgresql://postgres:5432/tc-admin
ENV DATABASE_USER=postgres
ENV DATABASE_PASSWORD=postgres

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
