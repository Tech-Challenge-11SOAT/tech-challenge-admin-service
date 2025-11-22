# Tech Challenge - Admin Microservice 🍔🔒

Este projeto é um microsserviço responsável pela gestão de administradores, autenticação e auditoria do sistema de autoatendimento de Fast Food.

Ele foi desenvolvido utilizando **Java 17**, **Spring Boot 3** e segue os princípios da **Arquitetura Hexagonal (Ports and Adapters)** e **Clean Architecture**, garantindo que as regras de negócio estejam isoladas de frameworks e bancos de dados.

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3.5.6
* **Banco de Dados:** MongoDB (NoSQL)
* **Segurança:** Spring Security + JWT (JSON Web Tokens)
* **Documentação:** Springdoc OpenAPI (Swagger UI)
* **Build:** Maven
* **Testes:** JUnit 5 + Mockito

---

## 🏛️ Estrutura do Projeto (Arquitetura Hexagonal)

O código foi organizado para separar claramente as responsabilidades:

```text
src/main/java/br/com/postech/techchallange_admin/
├── application/          # CAMADA DE APLICAÇÃO (Casos de Uso)
│   └── service/          # Implementação das regras de negócio (ex: CriarAdminService)
│
├── domain/               # CAMADA DE DOMÍNIO (O coração do sistema)
│   ├── model/            # Entidades puras (Admin, AdminLogAcao)
│   ├── exception/        # Exceções de negócio
│   └── port/             # Interfaces (Contratos)
│       ├── in/           # Portas de Entrada (Casos de Uso)
│       └── out/          # Portas de Saída (Repositórios)
│
└── infrastructure/       # CAMADA DE INFRAESTRUTURA (O mundo externo)
    ├── config/           # Configurações do Spring (Security, Swagger)
    ├── persistence/      # Implementação do MongoDB (Adapters, Documents, Repositories)
    ├── rest/             # Controladores REST e DTOs (Request/Response)
    └── security/         # Lógica de Tokens JWT e Filtros 
```

---

## ⚙️ Pré-requisitos

Para rodar este projeto localmente, você precisará de:

1.  **Java 17** instalado.
2.  **Maven** (ou usar o wrapper `mvnw` incluso no projeto).
3.  **MongoDB** rodando na porta padrão `27017`.

---

## 🏃‍♂️ Como Rodar

### 1. Subir o Banco de Dados (MongoDB)
Se você tiver o Docker instalado, pode subir um banco rapidamente com o comando:

```bash
docker run -d -p 27017:27017 --name mongo-admin mongo:latest
```

### 2. Executar a Aplicação
Na raiz do projeto, execute o comando:

#### Linux/Mac:
```bash
./mvnw spring-boot:run
```

#### Windows:
```bash
.\mvnw.cmd spring-boot:run
```
A aplicação iniciará na porta 8080.

---

## 📚 Documentação da API (Swagger)

Com a aplicação em execução, a documentação interativa dos endpoints está disponível em:

👉 **http://localhost:8080/swagger-ui.html**

---

## 🔐 Segurança e Autenticação

A API é protegida por tokens JWT. O fluxo de uso é:

### Rotas Públicas
* `/auth/login`: Para obter o `accessToken` e `refreshToken`.
* `/auth/refresh`: Para renovar um token expirado.
* `/swagger-ui/**`: Documentação e recursos visuais.

### Rotas Protegidas
* Todas as rotas `/admins` exigem um token válido.

> **Como usar no Swagger:** Copie o `accessToken` gerado no login, clique no botão **Authorize** no topo da página e cole no formato: `Bearer SEU_TOKEN_AQUI`.

---

## ✅ Funcionalidades Implementadas

### Gestão de Administradores
* **Criar Admin:** Cadastra nome, email, senha e roles. (A senha é criptografada automaticamente).
* **Listar Admins:** Retorna todos os cadastrados.
* **Buscar por ID:** Detalhes de um admin específico.
* **Atualizar:** Modifica dados cadastrais.
* **Inativar/Ativar:** `PATCH /admins/{id}/inactivate` e `activate`.
* **Deletar:** Remove um registro.

### Autenticação
* **Login:** Validação de credenciais e geração de JWT.
* **Alterar Senha:** Permite ao usuário logado trocar sua própria senha.
* **Logout:** Invalida o token atual (Blacklist).

### Auditoria (Logs)
Todas as ações críticas (Cadastro e Alteração de Senha) são registradas automaticamente em uma coleção de `logs` no MongoDB, contendo:
* Quem fez a ação.
* Qual foi a ação.
* Quando ocorreu.

---

## 🧪 Testes

Para executar os testes unitários e de integração via terminal:

```bash
./mvnw test
