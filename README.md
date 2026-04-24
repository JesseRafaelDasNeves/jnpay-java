# 💰 JNPay - API de Faturamento

API de faturamento REST desenvolvida em **Java 17** com **Spring Boot**, criada para demonstração de conhecimentos técnicos em desenvolvimento back-end.

---

## 📋 Sobre o Projeto

O JNPay é uma REST API que gerencia faturas (invoices) e seus itens, oferecendo funcionalidades completas de CRUD e controle de pagamentos. O sistema permite criar, consultar, atualizar e excluir faturas, além de registrar pagamentos parciais ou totais com cálculo automático de proporções.

### Funcionalidades

- **Criação de faturas** com itens associados
- **Listagem e consulta** de faturas por ID
- **Atualização** de dados da fatura e seus itens
- **Exclusão** de faturas
- **Registro de pagamentos** com cálculo automático de proporção paga por item
- **Controle de status**: `PENDENTE`, `PARCIALMENTE_PAGO`, `PAGO`

---

## 🛠️ Tecnologias e Recursos

| Tecnologia / Recurso | Descrição |
|---|---|
| **Java 17** | Linguagem de programação utilizada |
| **Spring Boot 3.5** | Framework para criação da aplicação e servidor web |
| **Spring Data JPA** | Abstração para acesso e persistência de dados com Hibernate |
| **Spring Validation** | Validação do body das requisições com Bean Validation (Jakarta Validation) |
| **PostgreSQL 18** | Banco de dados relacional |
| **Lombok** | Redução de código boilerplate (getters, setters, construtores, etc.) |
| **Docker / Docker Compose** | Conteinerização do banco de dados |
| **Maven** | Gerenciamento de dependências e build do projeto |

### Padrões e Práticas

- **DTO (Data Transfer Object)**: Utilização de `record` do Java para Request e Response DTOs, separando a representação da API das entidades do banco de dados
- **Lombok**: Geração automática de boilerplate como getters, setters, `equals()`, `hashCode()` e `toString()` através da anotação `@Data`
- **Bean Validation**: Validação declarativa dos dados de entrada com anotações como `@NotBlank`, `@NotNull`, `@Size`, `@Pattern` e `@DecimalMin`
- **Spring Data JPA**: Mapeamento objeto-relacional (ORM) com anotações JPA e repositórios automáticos via `JpaRepository`

---

## ⚙️ Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java 17** (JDK) — [Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven** (ou utilizar o wrapper `mvnw` já incluso no projeto)
- **Docker** e **Docker Compose**

### 🪟 Configuração no Windows

Para executar o Docker no Windows, você tem duas opções:

1. **Docker Desktop** (recomendado para iniciantes)
   - Baixe e instale o [Docker Desktop para Windows](https://www.docker.com/products/docker-desktop/)
   - Certifique-se de que a virtualização está habilitada na BIOS
   - O Docker Desktop já inclui o Docker Compose

2. **WSL 2 (Windows Subsystem for Linux)**
   - Instale o WSL 2 seguindo a [documentação oficial da Microsoft](https://learn.microsoft.com/pt-br/windows/wsl/install)
   - Instale o Docker dentro da distribuição Linux no WSL
   - Esta opção é recomendada para quem já utiliza o WSL no dia a dia

---

## 🚀 Como Executar

### 1. Clonar o repositório

```bash
git clone https://github.com/JesseRafaelDasNeves/jnpay-java.git
cd jnpay-java
```

### 2. Criar o volume do Docker

O projeto utiliza um volume externo para persistir os dados do banco. Crie-o antes de iniciar:

```bash
docker volume create pgbanco_jnpay
```

### 3. Iniciar o banco de dados com Docker Compose

```bash
docker compose up -d
```

Esse comando irá baixar a imagem do **PostgreSQL 18** e iniciar o container com as seguintes configurações:

| Configuração | Valor |
|---|---|
| Host | `localhost` |
| Porta | `5432` |
| Banco de dados | `o_pgbanco_jnpay` |
| Usuário | `app_web` |
| Senha | `123456` |

> **Nota:** Aguarde o container ficar saudável (healthy) antes de iniciar a aplicação. Você pode verificar o status com `docker ps`.

### 4. Iniciar a aplicação Java

Na raiz do projeto, acesse a pasta do módulo e execute:

```bash
cd jnpay
./mvnw spring-boot:run
```

No Windows (CMD/PowerShell):

```powershell
cd jnpay
.\mvnw.cmd spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

---

## 📡 Endpoints da API

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/invoice` | Lista todas as faturas |
| `GET` | `/invoice/{id}` | Busca fatura por ID |
| `POST` | `/invoice` | Cria uma nova fatura |
| `PUT` | `/invoice/{id}` | Atualiza uma fatura |
| `DELETE` | `/invoice/{id}` | Exclui uma fatura |
| `PUT` | `/invoice/{id}/pay` | Registra pagamento em uma fatura |

### Exemplo de Request — Criar Fatura

```json
POST /invoice
Content-Type: application/json

{
  "number": "FAT-001",
  "issueDate": "2026-04-24",
  "paidAmount": 0,
  "status": "PENDENTE",
  "items": [
    {
      "description": "Serviço de consultoria",
      "amount": 1500.00
    }
  ]
}
```

### Exemplo de Request — Registrar Pagamento

```json
PUT /invoice/1/pay
Content-Type: application/json

{
  "paidAmount": 500.00
}
```

---

## 📁 Estrutura do Projeto

```
jnpay-java/
├── docker-compose.yml              # Configuração do container PostgreSQL
├── jnpay/
│   ├── pom.xml                     # Dependências e configuração Maven
│   ├── mvnw / mvnw.cmd            # Maven Wrapper (Linux / Windows)
│   └── src/main/java/com/example/jnpay/
│       ├── JnpayApplication.java   # Classe principal (Spring Boot)
│       ├── controller/
│       │   └── InvoiceController.java  # Endpoints REST
│       └── invoice/
│           ├── Invoice.java            # Entidade JPA
│           ├── InvoiceItem.java        # Entidade JPA (itens da fatura)
│           ├── InvoiceRepository.java  # Repositório Spring Data
│           ├── InvoiceRequestDto.java  # DTO de entrada
│           ├── InvoiceResponseDto.java # DTO de saída
│           ├── InvoiceItemRequestDto.java  # DTO de entrada (itens)
│           ├── InvoiceItemResponseDto.java # DTO de saída (itens)
│           ├── InvoiceStatus.java      # Enum de status
│           └── PaymentInvoiceRequestDto.java # DTO de pagamento
```

---

## 🛑 Parando a Aplicação

Para parar o servidor Java, pressione `Ctrl + C` no terminal.

Para parar o container do banco de dados:

```bash
docker compose down
```
