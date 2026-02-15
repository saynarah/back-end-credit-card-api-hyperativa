# API Wallet Backend - Credit Card Batch Processor

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-Swagger-yellow.svg)](https://springdoc.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-purple.svg)](https://www.postgresql.org/)

## 📋 **What the project does**

Secure REST API for **processing fixed-layout text files** containing batches of credit card numbers.

### **Main Functionalities:**
- 🛡️ **JWT Authentication** with Spring Security 6
- 💳 **AES Encryption** for sensitive data (PCI-DSS compliant)
- 📥 **File upload and parsing** - fixed 51 chars per line format
- ⚡ **Optimized batch processing** (1000 records/commit)
- 📊 **Complete logging** of all requests/responses
- 🧪 **Robust validation** with standardized responses
- 📖 **Automatic OpenAPI/Swagger** documentation

### **Complete Flow:**

Batch File (.txt) → Parser Layout Fixed → AES Crypto → MySQL Database

## 🛠️ **Stacks**

| Category | Tecnologies |
|-----------|-------------|
| **Backend** | Spring Boot 3.2, Spring Security, Spring Data JPA, Hibernate |
| **Database** | PostgreSQL 16 |
| **Security** | JWT, AES-256, BCrypt |
| **Tools** | Maven, SpringDoc OpenAPI |

## 🚀 **Install and Exec**

### **Requisites**
```bash
Java 17+ | Maven 3.8+ | PostgreSQL 16
``` 

## Step by step

### Clone the project

```bash
git clone https://github.com/seuusuario/api-wallet-backend.git
cd api-wallet-backend
``` 
### Setting environment variables

```bash
# Linux/Mac
export APP_KEY="your-app-key"
export DB_URL="your-database-url"
export DB_DATABASE="your-dataset-name"
export DB_USERNAME="your-database-username"
export DB_PASSWORD="your-database-password"
``` 

```bash
# Windows
set APP_KEY=sua-chave-super-secreta-32-caracteres-aqui12345678
set DB_URL=your-database-url
set DB_DATASE=your-dataset-name
set DB_USERNAME=your-database-username
set DB_PASSWORD=your-database-password
``` 

### Dependencies install

```bash
mvn clean install -DskipTests
``` 

### Docker execution (MySQL Database)


#### 1. Open Docker Desktop

#### 2. Go to 'docker' folder

```bash
cd docker
```

#### 3. Up MySQL

```bash
docker-compose up -d
```

#### 4. At the end, dowd MySQL

```bash
docker-compose down
```

### Application Exec

```bash
mvn spring-boot:run
```

## **API Documentation (OpenAPI)**

Documentation: http://localhost:8080/swagger-ui.index.html

### **🔐 Authentication**

Header: Authorization: Bearer <jwt-token>


### **👤 1. Create User**

- POST /users
- Content-Type: application/json

Request Body:
```bash
json
{
  "username": "usuario@example.com",
  "password": "MinhaSenha123"
}
```

Response 201 Created:

### **👤 🔑 2. Login / Auth**

- POST /auth
- Content-Type: application/json

Request Body:
```bash
json
{
  "username": "usuario@example.com",
  "password": "MinhaSenha123"
}
```

Response 200:
```bash
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresAt": "2026-02-12T12:00:00Z"
}
```


### **💳 3. Add single card**

- POST /cards/single
- Authorization: Bearer >jwt-token>
- Content-Type: application/json

Request Body:
```bash
json
{
  "cardNumber": "4456897999999999"
}
```

Response 201 Created:


### **💳 4. Retrieve a card**

- GET /cards/{cardNumber}
- Authorization: Bearer >jwt-token>

Exemplo: GET /cards/4456897999999999


Response 200 OK:

```bash
json
{
	"id": "e04c5781-b009-46e4-a8fe-792bb88862e3"
}
```

Response 404 Not Found:

Response 201 Created:


### **📤 5. Upload Cards in Batch**

- POST /cards/batch
- Authorization: Bearer >jwt-token>
- Content-Type: multipart/form-data

EBody (form-data):

Key: file  → Value: [lote-cartoes.txt]

Response 201 Created:


## **Database Model (MySQL)**


---

## 🗄️ **Database Model**

### **Table: `credit_cards`**
| Coluna | Tipo | Descrição | Constraints |
|--------|------|-----------|-------------|
| `id` | `UUID` | Identificador único | `PK` |
| `card_number` | `VARCHAR(255)` | AES Encrypted CardNumber | `NOT NULL` |
| `lote_sequence` | `VARCHAR(50)` | Lote Code | `NOT NULL` |
| `row_file_identifier` | `VARCHAR(10)` | Row Identifier (C1,C2...) | |
| `user_id` | `UUID` | Card's owner | `FK → users(id)` |
| `creation_date` | `DATEIME(6)` | Creation date | `DEFAULT NOW()` |

### **Table: `users`**
| Coluna | Tipo | Descrição | Constraints |
|--------|------|-----------|-------------|
| `id` | `UUID` | Unique identifier | `PK` |
| `username` | `VARCHAR(255)` | User email | `UNIQUE, NOT NULL` |
| `password` | `VARCHAR(255)` | BCrypt Password| `NOT NULL` |

---

## 📸 **Documentation Views**

### **1. Swagger UI**

![Swagger Documentation](git_swagger_openapi.gif)

### **3. Audit Logs**

![Audit APIs Logs](image.png)



- 👨‍💻 Developped by Saynarah Cruz Nabuco
- 📧 [saynarah.nabuco@gmail.com]
- 📅 02/12/2026

