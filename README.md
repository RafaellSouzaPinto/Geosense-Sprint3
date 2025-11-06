<div align="center">

# 🏍️ GeoSense

**Transforme seu pátio em um espaço inteligente, seguro e eficiente**

[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?style=for-the-badge&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Oracle](https://img.shields.io/badge/Oracle-Database-red?style=for-the-badge&logo=oracle)](https://www.oracle.com/database/)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue?style=for-the-badge&logo=apache-maven)](https://maven.apache.org/)

> 🚀 **Sistema inteligente de gestão de pátios para oficinas mecânicas**  
> Transforme seu espaço de trabalho em um ambiente organizado, eficiente e totalmente digital

[![Status](https://img.shields.io/badge/Status-Em%20Produção-success?style=for-the-badge)](https://geosense-sprint3-1.onrender.com)
[![Versão](https://img.shields.io/badge/Versão-1.0.0-blue?style=for-the-badge)](https://github.com/geosense/geosense/releases)
[![Documentação](https://img.shields.io/badge/Documentação-Completa-green?style=for-the-badge&logo=book)](README.md)
[![Licença](https://img.shields.io/badge/Licença-MIT-yellow?style=for-the-badge&logo=scale)](LICENSE)

---

## 📋 Índice

[🎯 Sobre o Projeto](#-sobre-o-projeto) • [🏗️ Arquitetura](#️-arquitetura-do-sistema) • [⚙️ Funcionalidades](#️-funcionalidades) • [🚀 Instalação](#-instalação-e-execução) • [🌐 Deploy](#-deploy-e-configuração) • [📱 Acesso](#-acesso-à-aplicação) • [🧪 Testes](#-testes)

---

</div>

## 🎯 Sobre o Projeto

<div align="center">

> 🚀 **GeoSense** é uma aplicação web desenvolvida em Spring Boot que **revoluciona a gestão de pátios** para oficinas mecânicas. O sistema oferece controle inteligente de vagas, alocação de motocicletas, gestão de usuários e rastreamento completo do histórico de serviços.

</div>

### 🎯 Objetivos Principais

|               🎯 **Gestão Inteligente**               |               🔒 **Segurança**                |              ⚡ **Eficiência**              |            📊 **Rastreabilidade**             |
| :---------------------------------------------------: | :-------------------------------------------: | :-----------------------------------------: | :-------------------------------------------: |
| Controle automatizado de vagas e alocação de veículos | Sistema de autenticação e autorização robusto | Otimização do fluxo de trabalho em oficinas | Histórico completo de alocações e manutenções |

---

## 👥 Equipe de Desenvolvimento

<div align="center">

| **Rafael de Souza Pinto** | **Luis Paulo Freitas Fernandes** | **Enzo Marsola** |
| :-----------------------: | :------------------------------: | :--------------: |
|         RM 555130         |            RM 555497             |    RM 556310     |

</div>

---

## 🏗️ Arquitetura do Sistema

### 🛠️ Stack Tecnológico

<div align="center">

|                                                        **Backend**                                                        |                                             **Frontend**                                             |                                        **Database**                                        |                                                  **Tools**                                                  |
| :-----------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------: |
|                     ![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)                      |   ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-green?style=flat-square&logo=thymeleaf)    |   ![Oracle](https://img.shields.io/badge/Oracle-19c+-red?style=flat-square&logo=oracle)    |          ![Maven](https://img.shields.io/badge/Maven-3.x-blue?style=flat-square&logo=apache-maven)          |
|      ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?style=flat-square&logo=spring-boot)      |   ![Bootstrap](https://img.shields.io/badge/Bootstrap-5.x-purple?style=flat-square&logo=bootstrap)   | ![Flyway](https://img.shields.io/badge/Flyway-9.x-lightblue?style=flat-square&logo=flyway) | ![Font Awesome](https://img.shields.io/badge/Font%20Awesome-6.x-yellow?style=flat-square&logo=font-awesome) |
| ![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen?style=flat-square&logo=spring-security) | ![JavaScript](https://img.shields.io/badge/JavaScript-ES6+-yellow?style=flat-square&logo=javascript) |                                                                                            |               ![CSS3](https://img.shields.io/badge/CSS3-3.x-blue?style=flat-square&logo=css3)               |
|    ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-3.x-brightgreen?style=flat-square&logo=spring)     |                                                                                                      |                                                                                            |                                                                                                             |

</div>

### 🏛️ Padrões Arquiteturais

<div align="center">

|              🎨 **MVC**              |           🗃️ **Repository**           |                 📦 **DTO**                  |      ⚙️ **Service Layer**      |
| :----------------------------------: | :-----------------------------------: | :-----------------------------------------: | :----------------------------: |
| Separação clara de responsabilidades | Abstração da camada de acesso a dados | Transferência segura de dados entre camadas | Lógica de negócio centralizada |

</div>

### 📁 Estrutura do Projeto

```mermaid
graph TD
    A[📁 geosense/Geosense] --> B[⚙️ config/]
    A --> C[🎮 controller/]
    A --> D[📦 dto/]
    A --> E[🗃️ entity/]
    A --> F[💾 repository/]
    A --> G[🔧 service/]
    A --> H[🔒 security/]
    A --> I[✅ validation/]

    B --> B1[AdminInitializer.java]
    B --> B2[FlywayConfig.java]
    B --> B3[SecurityConfig.java]

    C --> C1[AdminApiController.java]
    C --> C2[AlocacaoMotoController.java]
    C --> C3[AuthController.java]
    C --> C4[HomeController.java]
    C --> C5[MotoController.java]
    C --> C6[PatioController.java]
    C --> C7[UsuarioController.java]

    D --> D1[AlocacaoMotoDTO.java]
    D --> D2[CredentialsDTO.java]
    D --> D3[MotoDTO.java]
    D --> D4[UsuarioDTO.java]

    E --> E1[Usuario.java]
    E --> E2[Moto.java]
    E --> E3[Patio.java]
    E --> E4[Vaga.java]
    E --> E5[AlocacaoMoto.java]
    E --> E6[Defeito.java]

    F --> F1[UsuarioRepository.java]
    F --> F2[MotoRepository.java]
    F --> F3[PatioRepository.java]
    F --> F4[AlocacaoMotoRepository.java]

    G --> G1[UsuarioService.java]
    G --> G2[MotoService.java]
    G --> G3[PatioService.java]
    G --> G4[AlocacaoMotoService.java]

    H --> H1[CustomUserDetails.java]
    H --> H2[CustomUserDetailsService.java]
    H --> H3[PasswordEncoderConfig.java]

    I --> I1[SenhaValidator.java]
    I --> I2[ProblemaValidator.java]
    I --> I3[EmailValidator.java]
    I --> I4[UsuarioValidator.java]
    I --> I5[SpringContextHelper.java]
```

<details>
<summary>📋 <strong>Ver estrutura completa do projeto</strong></summary>

```
src/main/java/geosense/Geosense/
├── ⚙️ config/                 # Configurações da aplicação
│   ├── AdminInitializer.java
│   ├── FlywayConfig.java
│   └── SecurityConfig.java
├── 🎮 controller/             # Controladores REST/Web
│   ├── AdminApiController.java
│   ├── AlocacaoMotoController.java
│   ├── AuthController.java
│   ├── HomeController.java
│   ├── MotoController.java
│   ├── PatioController.java
│   └── UsuarioController.java
├── 📦 dto/                    # Data Transfer Objects
│   ├── AlocacaoMotoDTO.java
│   ├── CredentialsDTO.java
│   ├── DefeitoDTO.java
│   ├── MotoDTO.java
│   ├── PatioDTO.java
│   ├── UsuarioDTO.java
│   └── ...
├── 🗃️ entity/                 # Entidades JPA
│   ├── Usuario.java
│   ├── Moto.java
│   ├── Patio.java
│   ├── Vaga.java
│   ├── AlocacaoMoto.java
│   ├── Defeito.java
│   └── ...
├── 💾 repository/             # Repositórios de dados
│   ├── UsuarioRepository.java
│   ├── MotoRepository.java
│   ├── PatioRepository.java
│   └── ...
├── 🔧 service/                # Serviços de negócio
│   ├── UsuarioService.java
│   ├── MotoService.java
│   ├── PatioService.java
│   ├── ValidacaoOracleService.java
│   └── ...
├── 🔒 security/               # Configurações de segurança
│   ├── CustomUserDetails.java
│   ├── CustomUserDetailsService.java
│   └── PasswordEncoderConfig.java
└── ✅ validation/             # Validadores customizados
    ├── SenhaValidator.java
    ├── EmailValidator.java
    ├── UsuarioValidator.java
    ├── SpringContextHelper.java
    └── ...
```

</details>

---

## 🗄️ Modelo de Dados

### 🏗️ Diagrama ER

```mermaid
erDiagram
    USUARIO {
        bigint ID PK
        varchar NOME
        varchar EMAIL UK
        varchar SENHA
        enum TIPO
    }

    PATIO {
        bigint ID PK
        varchar LOCALIZACAO
        varchar ENDERECO_DETALHADO
        varchar NOME_UNIDADE
        int CAPACIDADE
    }

    VAGA {
        bigint ID PK
        int NUMERO
        enum STATUS
        enum TIPO
        bigint PATIO_ID FK
    }

    MOTO {
        bigint ID PK
        varchar MODELO
        varchar PLACA UK
        varchar CHASSI UK
        varchar PROBLEMA_IDENTIFICADO
        bigint VAGA_ID FK
    }

    ALOCACAO_MOTO {
        bigint ID PK
        bigint MOTO_ID FK
        bigint VAGA_ID FK
        bigint MECANICO_RESPONSAVEL_ID FK
        timestamp DATA_HORA_ALOCACAO
        timestamp DATA_HORA_FINALIZACAO
        enum STATUS
        varchar MOTIVO_FINALIZACAO
        varchar OBSERVACOES
    }

    DEFEITO {
        bigint ID PK
        enum TIPOS_DEFEITOS
        varchar DESCRICAO
        bigint MOTO_ID FK
    }

    USUARIO ||--o{ ALOCACAO_MOTO : "mecanico_responsavel"
    PATIO ||--o{ VAGA : "possui"
    VAGA ||--o| MOTO : "aloca"
    MOTO ||--o{ ALOCACAO_MOTO : "historico"
    MOTO ||--o{ DEFEITO : "possui"
    VAGA ||--o{ ALOCACAO_MOTO : "registra"
```

### 📊 Entidades Principais

<details>
<summary>👤 <strong>Usuario</strong> - Sistema de Autenticação</summary>

| Campo   | Tipo           | Descrição                         |
| ------- | -------------- | --------------------------------- |
| `ID`    | `BIGINT`       | 🔑 Identificador único (PK)       |
| `NOME`  | `VARCHAR(100)` | 👤 Nome completo do usuário       |
| `EMAIL` | `VARCHAR(255)` | 📧 Email único para login (UK)    |
| `SENHA` | `VARCHAR(255)` | 🔒 Senha criptografada com BCrypt |
| `TIPO`  | `ENUM`         | 🎭 ADMINISTRADOR ou MECANICO      |

</details>

<details>
<summary>🏢 <strong>Patio</strong> - Gestão de Locais</summary>

| Campo                | Tipo           | Descrição                     |
| -------------------- | -------------- | ----------------------------- |
| `ID`                 | `BIGINT`       | 🔑 Identificador único (PK)   |
| `LOCALIZACAO`        | `VARCHAR(255)` | 📍 Localização do pátio       |
| `ENDERECO_DETALHADO` | `VARCHAR(255)` | 🏠 Endereço completo          |
| `NOME_UNIDADE`       | `VARCHAR(255)` | 🏢 Nome da unidade            |
| `CAPACIDADE`         | `INTEGER`      | 📊 Capacidade máxima de vagas |

</details>

<details>
<summary>🏍️ <strong>Moto</strong> - Gestão de Veículos</summary>

| Campo                   | Tipo           | Descrição                      |
| ----------------------- | -------------- | ------------------------------ |
| `ID`                    | `BIGINT`       | 🔑 Identificador único (PK)    |
| `MODELO`                | `VARCHAR(50)`  | 🏍️ Modelo da motocicleta       |
| `PLACA`                 | `VARCHAR(10)`  | 🚗 Placa única do veículo (UK) |
| `CHASSI`                | `VARCHAR(50)`  | 🔧 Número do chassi (UK)       |
| `PROBLEMA_IDENTIFICADO` | `VARCHAR(255)` | ⚠️ Problema relatado           |
| `VAGA_ID`               | `BIGINT`       | 🔗 Vaga alocada (FK)           |

</details>

<details>
<summary>🅿️ <strong>Vaga</strong> - Controle de Espaços</summary>

| Campo      | Tipo      | Descrição                              |
| ---------- | --------- | -------------------------------------- |
| `ID`       | `BIGINT`  | 🔑 Identificador único (PK)            |
| `NUMERO`   | `INTEGER` | 🔢 Número da vaga                      |
| `STATUS`   | `ENUM`    | 🟢 LIVRE, 🔴 OCUPADA, 🟡 MANUTENCAO    |
| `TIPO`     | `ENUM`    | 📋 NORMAL, ⭐ PRIORITARIA, 🏆 ESPECIAL |
| `PATIO_ID` | `BIGINT`  | 🔗 Pátio ao qual pertence (FK)         |

</details>

<details>
<summary>📋 <strong>AlocacaoMoto</strong> - Controle de Alocações</summary>

| Campo                     | Tipo            | Descrição                                           |
| ------------------------- | --------------- | --------------------------------------------------- |
| `ID`                      | `BIGINT`        | 🔑 Identificador único (PK)                         |
| `MOTO_ID`                 | `BIGINT`        | 🏍️ Motocicleta alocada (FK)                         |
| `VAGA_ID`                 | `BIGINT`        | 🅿️ Vaga ocupada (FK)                                |
| `MECANICO_RESPONSAVEL_ID` | `BIGINT`        | 👨‍🔧 Mecânico responsável (FK)                        |
| `DATA_HORA_ALOCACAO`      | `TIMESTAMP`     | ⏰ Timestamp da alocação                            |
| `DATA_HORA_FINALIZACAO`   | `TIMESTAMP`     | ⏱️ Timestamp da finalização                         |
| `STATUS`                  | `ENUM`          | 🔄 ATIVA, ➡️ REALOCADA, ✅ FINALIZADA, ❌ CANCELADA |
| `MOTIVO_FINALIZACAO`      | `VARCHAR(500)`  | 📝 Motivo da finalização                            |
| `OBSERVACOES`             | `VARCHAR(1000)` | 📄 Observações adicionais                           |

</details>

---

## ⚙️ Funcionalidades

### 🔐 Sistema de Autenticação

<div align="center">

|    🛡️ **Login Seguro**    |       📝 **Registro**       | 🔑 **Controle de Acesso** | ⏰ **Sessões Seguras** |
| :-----------------------: | :-------------------------: | :-----------------------: | :--------------------: |
| Spring Security integrado | Validação completa de dados |    Por tipo de usuário    |   Timeout automático   |

</div>

### ✅ Validação de Senha e Email (Oracle)

O sistema utiliza a função Oracle `FN_VALIDAR_SENHA_E_LIMITES` para validação completa de senha e email. Esta função valida:

- **Senha**:
  - Mínimo 6 e máximo 20 caracteres
  - Deve conter pelo menos uma letra maiúscula
  - Deve conter pelo menos uma letra minúscula
  - Deve conter pelo menos um número
  - Não pode conter espaços
- **Email**:
  - Formato válido (exemplo@dominio.com)
  - Máximo 255 caracteres
  - Verificação de duplicidade (na operação INSERT)
- **Tipo de Usuário**:
  - Deve ser um dos tipos válidos: MECANICO, ADMINISTRADOR, OPERADOR

**Mensagens de erro padronizadas**: Todos os erros de validação são exibidos em formato visual consistente (caixa vermelha com ícone triangular) no frontend.

### 👥 Gestão de Usuários

<div align="center">

|     📋 **CRUD Completo**     | 👥 **Tipos de Usuário**  | ✅ **Validação** | 🔒 **Criptografia** |
| :--------------------------: | :----------------------: | :--------------: | :-----------------: |
| Create, Read, Update, Delete | Administrador e Mecânico | Bean Validation  | BCrypt para senhas  |

</div>

### 🏢 Gestão de Pátios

<div align="center">

|    🏗️ **Cadastro**     |   📊 **Capacidade**   |     🤖 **Auto-Geração**      |  📱 **Tempo Real**   |
| :--------------------: | :-------------------: | :--------------------------: | :------------------: |
| Informações detalhadas | Configuração flexível | Vagas baseadas na capacidade | Status em tempo real |

</div>

### 🏍️ Gestão de Motocicletas

<div align="center">

|      📝 **Cadastro**       |      ⚠️ **Problemas**      |    📚 **Histórico**     |   🔍 **Busca**    |
| :------------------------: | :------------------------: | :---------------------: | :---------------: |
| Dados completos do veículo | Identificação de problemas | Manutenções e alocações | Filtros avançados |

</div>

### 🅿️ Sistema de Alocação

<div align="center">

| 🤖 **Automática** |     📊 **Status**      |    📈 **Histórico**     | 🔔 **Notificações** |
| :---------------: | :--------------------: | :---------------------: | :-----------------: |
| Vagas disponíveis | Controle em tempo real | Movimentações completas | Mudanças de status  |

</div>

### 📊 Dashboard Administrativo

<div align="center">

| 📊 **Métricas** |  📈 **Gráficos**   |     📋 **Relatórios**      |        🚨 **Alertas**         |
| :-------------: | :----------------: | :------------------------: | :---------------------------: |
|   Tempo real    | Ocupação por pátio | Produtividade por mecânico | Vagas ocupadas há muito tempo |

</div>

---

## 🚀 Instalação e Execução

### 📋 Pré-requisitos

<div align="center">

|                                    ☕ **Java**                                    |                                        🔧 **Maven**                                        |                                   🗄️ **Oracle DB**                                    |                                                     💻 **IDE**                                                      |
| :-------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------: |
| ![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=java) | ![Maven](https://img.shields.io/badge/Maven-3.6+-blue?style=flat-square&logo=apache-maven) | ![Oracle](https://img.shields.io/badge/Oracle-19c+-red?style=flat-square&logo=oracle) | ![IDE](https://img.shields.io/badge/IDE-IntelliJ%20IDEA%20%7C%20Eclipse%20%7C%20VS%20Code-purple?style=flat-square) |

</div>

### 1️⃣ Clone o Repositório

```bash
# Clone o repositório
git clone <url-do-repositorio>
cd Geosense

# Verifique se está na pasta correta
ls -la
```

### 2️⃣ Configuração do Banco de Dados

<details>
<summary>🗄️ <strong>Oracle Database</strong> - Configuração do Banco</summary>

```sql
-- Criar usuário (se necessário)
CREATE USER RM555130 IDENTIFIED BY 040506;

-- Conceder permissões
GRANT CONNECT, RESOURCE TO RM555130;
GRANT CREATE SESSION TO RM555130;
GRANT CREATE TABLE TO RM555130;
GRANT CREATE SEQUENCE TO RM555130;

-- Verificar conexão
SELECT USER FROM DUAL;
```

</details>

<details>
<summary>⚙️ <strong>Configuração da Aplicação</strong> - application.properties</summary>

Edite o arquivo `src/main/resources/application.properties`:

```properties
# 🗄️ Configurações do Oracle Database
spring.datasource.url=jdbc:oracle:thin:@seu-host:1521/seu-servico
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# 🔧 Configurações JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false

# 🌐 Configurações do servidor
server.port=8081
```

</details>

### 3️⃣ Execução da Aplicação

<div align="center">

|     🔧 **Maven**     |           💻 **IDE**            |      📦 **JAR**       | ⚡ **Script** |
| :------------------: | :-----------------------------: | :-------------------: | :-----------: |
| Via linha de comando | IntelliJ IDEA, Eclipse, VS Code | Executável standalone | Windows Batch |

</div>

<details>
<summary>🔧 <strong>Opção 1: Maven</strong> - Linha de Comando</summary>

```bash
# 🧹 Limpar e compilar o projeto
mvn clean compile

# 🚀 Executar a aplicação
mvn spring-boot:run

# 📊 Ver logs em tempo real
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

</details>

<details>
<summary>💻 <strong>Opção 2: IDE</strong> - Ambiente de Desenvolvimento</summary>

### IntelliJ IDEA

1. **File** → **Open** → Selecione a pasta do projeto
2. Aguarde o Maven importar as dependências
3. Navegue até `src/main/java/geosense/Geosense/GeosenseApplication.java`
4. Clique com botão direito → **Run 'GeosenseApplication'**

### Eclipse

1. **File** → **Import** → **Existing Maven Projects**
2. Selecione a pasta do projeto
3. Aguarde o import
4. Clique com botão direito na classe → **Run As** → **Java Application**

### VS Code

1. Abra a pasta do projeto
2. Instale a extensão **Extension Pack for Java**
3. Pressione `Ctrl+Shift+P` → **Java: Run**

</details>

<details>
<summary>📦 <strong>Opção 3: JAR</strong> - Executável Standalone</summary>

```bash
# 📦 Gerar JAR executável
mvn clean package -DskipTests

# 🚀 Executar JAR
java -jar target/Geosense-0.0.1-SNAPSHOT.jar

# 🔧 Com parâmetros customizados
java -jar target/Geosense-0.0.1-SNAPSHOT.jar --server.port=8082
```

</details>

<details>
<summary>⚡ <strong>Opção 4: Script Windows</strong> - Execução Rápida</summary>

Execute o arquivo `run.bat` para inicialização automática:

```batch
@echo off
title GeoSense - Sistema de Gestao de Patios
color 0A

echo.
echo  ================================================
echo  🏍️  GeoSense - Sistema de Gestao de Patios
echo  ================================================
echo.
echo  ⚡ Iniciando aplicacao...
echo.

mvn spring-boot:run

echo.
echo  ⏸️  Pressione qualquer tecla para sair...
pause > nul
```

</details>

---

## 🚀 Deploy

### ☁️ Deploy no Render

O GeoSense está deployado no [Render](https://render.com) e está disponível em produção:

#### 🔗 **URL da Aplicação**

```
https://geosense-sprint3-t1e8.onrender.com
```

#### 📋 **Variáveis de Ambiente (Produção)**

```properties
# 🗄️ Configurações do Banco de Dados (Render)
DATABASE_URL=jdbc:oracle:thin:@[host]:[port]/[service]
DB_USERNAME=[usuario_producao]
DB_PASSWORD=[senha_producao]

# 🌐 Configurações do Servidor
SERVER_PORT=10000
SPRING_PROFILES_ACTIVE=production

# 🔧 Configurações JPA
SPRING_JPA_HIBERNATE_DDL_AUTO=none
SPRING_JPA_SHOW_SQL=false
```

### 🛠️ Configuração Local vs Produção

<div align="center">

| Aspecto     | 💻 Local                | 🌍 Produção                                  |
| ----------- | ----------------------- | -------------------------------------------- |
| **URL**     | `http://localhost:8081` | `https://geosense-sprint3-t1e8.onrender.com` |
| **Banco**   | Oracle Local/Cloud      | Oracle Cloud                                 |
| **Porta**   | 8081                    | 10000 (Render)                               |
| **Profile** | `default`               | `production`                                 |
| **SSL**     | ❌ HTTP                 | ✅ HTTPS                                     |
| **Domain**  | localhost               | onrender.com                                 |

</div>

---

## 🌐 Acesso à Aplicação

### 🚀 Deploy em Produção

<div align="center">

|                                         🌐 **Aplicação Online**                                          |                                   📊 **Status**                                   |                                      🏷️ **Versão**                                      |
| :------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------: |
| ![Live App](https://img.shields.io/badge/URL-geosense--sprint3--1.onrender.com-blue?style=for-the-badge) | ![Status](https://img.shields.io/badge/Status-Online-success?style=for-the-badge) | ![Versão](https://img.shields.io/badge/Versão-Produção-1.0.0-green?style=for-the-badge) |

</div>

### 🔗 URLs de Acesso

#### 🌍 **Produção (Render)**

<div align="center">

|                                          🏠 **Página Inicial**                                          |                                                🔐 **Login**                                                |                                                  📝 **Registro**                                                  |                                                ⚙️ **Admin**                                                 |
| :-----------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------: |
| ![Dashboard](https://img.shields.io/badge/URL-geosense--sprint3--1.onrender.com-blue?style=flat-square) | ![Login](https://img.shields.io/badge/URL-geosense--sprint3--1.onrender.com/login-green?style=flat-square) | ![Registro](https://img.shields.io/badge/URL-geosense--sprint3--1.onrender.com/register-orange?style=flat-square) | ![Admin](https://img.shields.io/badge/URL-geosense--sprint3--1.onrender.com/admin-purple?style=flat-square) |
|                                           Dashboard principal                                           |                                           Página de autenticação                                           |                                               Cadastro de usuários                                                |                                            Painel administrativo                                            |

</div>

#### 💻 **Desenvolvimento Local**

<div align="center">

|                                🏠 **Página Inicial**                                 |                                      🔐 **Login**                                       |                                        📝 **Registro**                                         |                                       ⚙️ **Admin**                                       |
| :----------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------: |
| ![Dashboard](https://img.shields.io/badge/URL-localhost:8081-blue?style=flat-square) | ![Login](https://img.shields.io/badge/URL-localhost:8081/login-green?style=flat-square) | ![Registro](https://img.shields.io/badge/URL-localhost:8081/register-orange?style=flat-square) | ![Admin](https://img.shields.io/badge/URL-localhost:8081/admin-purple?style=flat-square) |
|                                 Dashboard principal                                  |                                 Página de autenticação                                  |                                      Cadastro de usuários                                      |                                  Painel administrativo                                   |

</div>

<div align="center">

> ⚠️ **IMPORTANTE**: As credenciais de produção são diferentes das de desenvolvimento local!

</div>

---

## 🧪 Testes

### Estrutura de Testes

```
src/test/java/geosense/Geosense/
└── GeosenseApplicationTests.java
```

### Execução de Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com relatório
mvn test jacoco:report

# Executar testes específicos
mvn test -Dtest=NomeDoTeste
```

### Cobertura de Testes

- **Testes Unitários**: Validação de lógica de negócio
- **Testes de Integração**: Validação de endpoints
- **Testes de Segurança**: Validação de autenticação
- **Testes de Interface**: Validação de templates

---

## 📱 Interface do Usuário

### Características da UI

- **Design Responsivo**: Adaptável a diferentes dispositivos
- **Tema Escuro**: Interface moderna e elegante
- **Componentes Interativos**: Cards, modais e formulários
- **Feedback Visual**: Mensagens de sucesso/erro
- **Navegação Intuitiva**: Menu lateral e breadcrumbs

### Tecnologias Frontend

- **Thymeleaf**: Template engine server-side
- **Bootstrap 5**: Framework CSS
- **Font Awesome**: Ícones
- **JavaScript Vanilla**: Interatividade
- **CSS3**: Estilização customizada

---

## 🛠️ Desenvolvimento

### Comandos Úteis

```bash
# Limpar e compilar
mvn clean compile

# Executar com profile específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Verificar dependências
mvn dependency:tree

# Analisar código
mvn spotbugs:check

# Formatar código
mvn formatter:format
```

### Estrutura de Branches

- **main**: Código de produção
- **develop**: Desenvolvimento ativo
- **feature/\***: Novas funcionalidades
- **hotfix/\***: Correções urgentes

---

## 🚨 Troubleshooting

### Problemas Comuns

#### 1. Erro de Conexão com Banco

```
Caused by: java.sql.SQLException: Listener refused the connection
```

**Solução**: Verifique se o Oracle está rodando e as credenciais estão corretas.

#### 2. Porta em Uso

```
Web server failed to start. Port 8081 was already in use.
```

**Solução**: Altere a porta no `application.properties` ou mate o processo.

#### 3. Erro de Migração Flyway

```
FlywayException: Validate failed
```

**Solução**: Execute `mvn flyway:repair` ou limpe o banco.

#### 4. Erro ao Criar Função Oracle

```
ORA-01031: insufficient privileges
```

**Solução**: Verifique se o usuário tem permissão `CREATE FUNCTION`. Execute:

```sql
GRANT CREATE FUNCTION TO SEU_USUARIO;
```

#### 5. Erros de Validação Não Aparecem

Se os erros de validação não aparecem no formato padronizado:

**Solução**:

- Verifique se a função Oracle `FN_VALIDAR_SENHA_E_LIMITES` foi criada corretamente
- Verifique os logs da aplicação para erros de conexão com o banco
- Certifique-se de que a migration `V16` foi executada com sucesso
- Verifique se o usuário do banco tem permissões adequadas

### Logs de Debug

```properties
# Habilitar logs detalhados
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

## 📚 Documentação Adicional

### APIs Disponíveis

#### Usuários

- `GET /usuarios` - Listar usuários
- `POST /usuarios` - Criar usuário
- `PUT /usuarios/{id}` - Atualizar usuário
- `DELETE /usuarios/{id}` - Deletar usuário

#### Motos

- `GET /motos` - Listar motocicletas
- `POST /motos` - Cadastrar moto
- `GET /motos/{id}` - Buscar moto específica
- `PUT /motos/{id}` - Atualizar moto

#### Alocações

- `GET /alocacoes` - Listar alocações
- `POST /alocacoes` - Criar alocação
- `PUT /alocacoes/{id}/finalizar` - Finalizar alocação

### Banco de Dados

#### Migrações Flyway

- `V1__create_tables.sql` - Criação das tabelas base
- `V2__constraints_and_indexes.sql` - Constraints e índices
- `V3__seed_data.sql` - Dados iniciais
- `V15__fix_existing_alocacao_data.sql` - Correções de dados
- `V16__create_function_validar_senha_limites.sql` - Função Oracle de validação de senha e email

#### Funções Oracle

##### `FN_VALIDAR_SENHA_E_LIMITES`

Função que valida senha, email e tipo de usuário conforme regras de negócio:

**Parâmetros:**

- `p_senha VARCHAR2` - Senha a ser validada
- `p_email VARCHAR2` - Email a ser validado
- `p_tipo_usuario VARCHAR2` - Tipo de usuário (MECANICO, ADMINISTRADOR, OPERADOR)
- `p_operacao VARCHAR2` - Operação (VALIDACAO ou INSERT)

**Retorno:**

- JSON com status da validação, mensagens e lista de erros (se houver)

**Exceções tratadas:**

- `VALUE_ERROR` - Erro de valor nos parâmetros
- `NO_DATA_FOUND` - Dados não encontrados
- `TOO_MANY_ROWS` - Múltiplos registros
- `OTHERS` - Erros genéricos

**Nota**: A função é criada automaticamente pela migration `V16__create_function_validar_senha_limites.sql` quando a aplicação é executada pela primeira vez.

---

<div align="center">

## 🏆 Desenvolvido pela equipe GeoSense

| **Rafael de Souza Pinto** | **Luis Paulo Freitas Fernandes** | **Enzo Marsola** |
| :-----------------------: | :------------------------------: | :--------------: |
|         RM 555130         |            RM 555497             |    RM 556310     |

### 🎓 FIAP - Faculdade de Informática e Administração Paulista

![Curso](https://img.shields.io/badge/Curso-Análise%20e%20Desenvolvimento%20de%20Sistemas-red?style=for-the-badge)
![Disciplina](https://img.shields.io/badge/Disciplina-Desenvolvimento%20Web%20com%20Spring%20Boot-blue?style=for-the-badge)
![Ano](https://img.shields.io/badge/Ano-2025-green?style=for-the-badge)

---
