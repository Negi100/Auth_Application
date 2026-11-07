<div align="center">

# 🔐 Auth Application

### Secure JWT Authentication API with Spring Security

*Stateless login, signed access and refresh tokens, BCrypt passwords, and protected user management.*

<br/>

![Java](https://img.shields.io/badge/Java-22-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-HS512-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

<br/>

[✨ Features](#-features) •
[🏗️ Architecture](#️-architecture) •
[🚀 Quick Start](#-quick-start) •
[📡 API](#-api-reference) •
[🧠 How It Works](#-how-it-works) •
[🗺️ Roadmap](#️-roadmap)

</div>

---

## 💡 About

**Auth Application** is a backend REST API that handles user **registration**, **login**, and **JWT-based authentication** using Spring Boot and Spring Security. After logging in, a user receives a signed **access token** and a **refresh token**. Every protected request is checked by a custom JWT filter. The server keeps no sessions, so it is fully **stateless** and easy to scale.

---

## ✨ Features

<table>
<tr>
<td width="50%" valign="top">

### 🔑 JWT Authentication
- **Access token**: valid for 1 hour (configurable)
- **Refresh token**: valid for 24 hours (configurable)
- Signed with **HMAC-SHA512**
- Tokens carry `email`, `roles` and a token type (`typ`)

</td>
<td width="50%" valign="top">

### 🛡️ Spring Security
- Custom **`JwtAuthenticationFilter`** on every request
- **Stateless** sessions (no server-side session)
- Passwords hashed with **BCrypt**
- Disabled users are blocked from logging in

</td>
</tr>
<tr>
<td width="50%" valign="top">

### 👤 User Management
- Full **CRUD** for users
- Look up a user by **ID** or **email**
- **UUID** primary keys
- Support for **roles** and **providers** (LOCAL, GOOGLE, GITHUB, FACEBOOK)

</td>
<td width="50%" valign="top">

### ⚠️ Clean Error Handling
- Global `@RestControllerAdvice`
- Consistent JSON errors: status, message, path, timestamp
- Clear messages for **expired** and **invalid** tokens
- `401`, `400` and `404` responses

</td>
</tr>
</table>

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| ☕ Language | Java 22 |
| 🌱 Framework | Spring Boot 4.0.2 (Web, Data JPA, Validation) |
| 🛡️ Security | Spring Security + JJWT 0.12.3 |
| 🐬 Database | MySQL |
| 🔁 Mapping | ModelMapper |
| 🧰 Utilities | Lombok |
| 📦 Build | Maven (wrapper included) |

---

## 🏗️ Architecture

```mermaid
flowchart LR
    C["Client"] -->|HTTP request| F["JwtAuthenticationFilter"]
    F -->|valid token| SC["Security Context"]
    SC --> AC["AuthController - /api/v1/auth"]
    SC --> UC["UserController - /api/v1/users"]
    AC --> AS["AuthService"]
    AC --> JS["JwtService"]
    UC --> US["UserService"]
    AS --> US
    US --> DB[("MySQL - auth_app")]
    AC --> DB
```

### 📁 Project Structure

```
src/main/java/com/example/auth/
├── 🚀 AuthApplication.java
├── ⚙️ config/
│   ├── SecurityConfig.java          # Filter chain, BCrypt, AuthenticationManager
│   └── ProjectConfig.java           # ModelMapper bean
├── 🛡️ Security/
│   ├── JwtService.java              # Create and parse JWTs
│   ├── JwtAuthenticationFilter.java # Validates Bearer token on each request
│   └── CustomUserDetailService.java # Loads a user by email
├── 🌐 controllers/
│   ├── AuthController.java          # /register, /login
│   └── UserController.java          # User CRUD
├── 🧩 services/                     # AuthService, UserService + implementations
├── 📦 entities/                     # User, Role, RefreshToken, Provider
├── 📨 dtos/                         # UserDto, LoginRequest, TokenResponse, ApiError...
├── 🗄️ repositories/                 # UserRepo, RefreshTokenRepo
├── ⚠️ exceptions/                   # GlobalExceptionHandler, ResouceNotFoundException
└── 🧰 helpers/UserHelper.java       # UUID parsing
```

### 🗃️ Data Model

```mermaid
erDiagram
    USERS ||--o{ USER_ROLES : has
    ROLES ||--o{ USER_ROLES : assigned
    USERS ||--o{ REFRESH_TOKENS : owns

    USERS {
        UUID user_id PK
        string user_email
        string user_name
        string password
        string image
        boolean enable
        string provider
        timestamp createdAt
        timestamp updatedAT
    }
    ROLES {
        UUID id PK
        string name
    }
    USER_ROLES {
        UUID user_id FK
        UUID role_id FK
    }
    REFRESH_TOKENS {
        UUID id PK
        string jti
        UUID user_id FK
        timestamp createdAt
        timestamp expiresAt
        boolean revoked
        string replaceByToken
    }
```

---

## 🚀 Quick Start

### 1️⃣ Prerequisites

- ☕ JDK **22+**
- 🐬 MySQL running on `localhost:3306`

Start MySQL with Docker:

```bash
docker run -d --name auth-mysql -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=yourpassword -e MYSQL_DATABASE=auth_app \
  mysql:8
```

### 2️⃣ Clone the repo

```bash
git clone https://github.com/Negi100/Auth_Application.git
cd Auth_Application
```

### 3️⃣ Configure

Edit `src/main/resources/application.properties`, or set these environment variables:

| Property | Env variable | Default |
|---|---|---|
| `spring.datasource.url` | `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/auth_app` |
| `spring.datasource.username` | `SPRING_DATASOURCE_USERNAME` | `root` |
| `spring.datasource.password` | `SPRING_DATASOURCE_PASSWORD` | *(your password)* |
| `security.jwt.secret` | `JWT_SECRET` | ⚠️ **at least 64 characters** |
| `security.jwt.issuer` | `JWT_ISSUER` | `api.substring.com` |
| `security.jwt.access-ttl-seconds` | `JWT_ACCESS_TTL_SECONDS` | `3600` (1 hour) |
| `security.jwt.refresh-ttl-seconds` | `JWT_REFRESH_TTL_SECONDS` | `86400` (24 hours) |

> ⚠️ The app **won't start** if the JWT secret is shorter than 64 characters. Generate one with:
> ```bash
> openssl rand -base64 64
> ```

### 4️⃣ Run

```bash
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

🎉 The API is now running at **`http://localhost:8080`**

---

## 📡 API Reference

### 🔓 Public endpoints (no token needed)

| Method | Endpoint | Description | ✅ Success | ❌ Error |
|:---:|---|---|:---:|:---:|
| ![POST](https://img.shields.io/badge/POST-49cc90?style=flat-square) | `/api/v1/auth/register` | Register a new user | `201` | `400` |
| ![POST](https://img.shields.io/badge/POST-49cc90?style=flat-square) | `/api/v1/auth/login` | Log in and receive tokens | `200` | `400` |

### 🔒 Protected endpoints (`Authorization: Bearer <accessToken>`)

| Method | Endpoint | Description |
|:---:|---|---|
| ![GET](https://img.shields.io/badge/GET-61affe?style=flat-square) | `/api/v1/users` | Get all users |
| ![GET](https://img.shields.io/badge/GET-61affe?style=flat-square) | `/api/v1/users/{userId}` | Get a user by ID |
| ![GET](https://img.shields.io/badge/GET-61affe?style=flat-square) | `/api/v1/users/email/{email}` | Get a user by email |
| ![POST](https://img.shields.io/badge/POST-49cc90?style=flat-square) | `/api/v1/users` | Create a user |
| ![PUT](https://img.shields.io/badge/PUT-fca130?style=flat-square) | `/api/v1/users/{userId}` | Update a user |
| ![DELETE](https://img.shields.io/badge/DELETE-f93e3e?style=flat-square) | `/api/v1/users/{userId}` | Delete a user |

<details>
<summary><b>📝 Register</b></summary>

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "negi@example.com", "name": "Negi", "password": "secret123"}'
```

</details>

<details>
<summary><b>🔑 Login</b></summary>

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "negi@example.com", "password": "secret123"}'
```

**Response:**

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "expiresIn": 3600,
  "tokenType": "Bearer",
  "user": { "id": "a1b2c3d4-...", "email": "negi@example.com", "name": "Negi" }
}
```

</details>

<details>
<summary><b>👥 Call a protected endpoint</b></summary>

```bash
curl http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer <accessToken>"
```

Without a valid token, the API returns:

```json
{
  "Status": 401,
  "error": "Unauthorized Access",
  "message": "Token Expired",
  "path": "/api/v1/users"
}
```

</details>

---

## 🧠 How It Works

```mermaid
sequenceDiagram
    autonumber
    participant U as Client
    participant F as JWT Filter
    participant A as AuthController
    participant J as JwtService
    participant DB as MySQL

    U->>A: POST /api/v1/auth/login with email and password
    A->>DB: Find user and check BCrypt password
    A->>DB: Save refresh token record with jti
    A->>J: Generate access and refresh tokens
    A-->>U: 200 OK with accessToken and refreshToken

    U->>F: GET /api/v1/users with Bearer token
    F->>J: Verify signature, expiry and type
    F->>DB: Load user and check enabled
    F->>F: Set authentication in Security Context
    F-->>U: 200 OK with users list
```

### 🎫 What's inside the tokens

| Claim | Access token | Refresh token |
|---|:---:|:---:|
| `jti` (unique ID) | ✅ | ✅ (also saved in the database) |
| `sub` (user ID) | ✅ | ✅ |
| `iss`, `iat`, `exp` | ✅ | ✅ |
| `email` | ✅ | — |
| `roles` | ✅ | — |
| `typ` | `access` | `refresh` |

> ⚡ **Why store the refresh token's `jti`?** It lets the server **revoke** a refresh token (log out, or rotate tokens) even though JWTs are stateless.

---

## 🗺️ Roadmap

- [ ] 🔄 `/api/v1/auth/refresh` endpoint to swap a refresh token for a new access token
- [ ] 🍪 Send the refresh token in an **HttpOnly cookie** (the config options already exist)
- [ ] 🚪 Logout endpoint that revokes refresh tokens
- [ ] 🏷️ Assign a default role (e.g. `ROLE_USER`) at registration
- [ ] 👮 Role-based access control (`@PreAuthorize`) for admin-only endpoints
- [ ] 🔒 Hash passwords on user update, and hide `password` in API responses
- [ ] ✅ Request validation (`@Valid`, `@Email`, password rules)
- [ ] 🌐 OAuth2 login with Google and GitHub (the `Provider` enum is ready)
- [ ] 📖 Swagger / OpenAPI documentation
- [ ] 🐳 `docker-compose.yml` for one-command setup

---

## 🤝 Contributing

Contributions are welcome!

1. 🍴 Fork the repo
2. 🌿 Create a branch: `git checkout -b feature/amazing-feature`
3. 💾 Commit: `git commit -m "Add amazing feature"`
4. 🚀 Push and open a **Pull Request**

---

<div align="center">

### ⭐ If you found this project helpful, give it a star!

Made with ☕ and 🌱 Spring Boot by [**Negi100**](https://github.com/Negi100)

</div>
