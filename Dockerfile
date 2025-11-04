# Use uma imagem base do Eclipse Temurin (sucessor oficial do OpenJDK)
FROM eclipse-temurin:17-jdk-jammy

# Define o diretório de trabalho
WORKDIR /app

# Instala Maven e outras dependências necessárias
RUN apt-get update && \
    apt-get install -y maven curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copia os arquivos de configuração do Maven primeiro (para cache de dependências)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Torna o mvnw executável
RUN chmod +x mvnw

# Baixa as dependências (isso será cacheado se o pom.xml não mudar)
RUN ./mvnw dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila a aplicação
RUN ./mvnw clean package -DskipTests -Dmaven.test.skip=true

# Verifica se o JAR foi criado
RUN ls -la target/

# Expõe a porta 8081 (conforme configurado no application.properties)
EXPOSE 8081

# Define variáveis de ambiente padrão (serão sobrescritas pelo Render)
ENV SPRING_DATASOURCE_URL="jdbc:oracle:thin:@localhost:1521:xe"
ENV SPRING_DATASOURCE_USERNAME="user"
ENV SPRING_DATASOURCE_PASSWORD="password"

# Comando para executar a aplicação
CMD ["java", "-jar", "target/Geosense-0.0.1-SNAPSHOT.jar"]
