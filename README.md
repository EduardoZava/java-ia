# Microservice Template

A production-ready microservices monorepo template using Spring Boot 4.0.1, Angular 17, Traefik, PostgreSQL, MongoDB, and Redis — with Microsoft Entra ID (Azure AD) authentication preparation.

---

## Architecture Overview

```
                        ┌──────────────────────────────────────┐
                        │          Traefik API Gateway          │
                        │          (Port 80 / 8080)             │
                        └──────┬───────────────────────────────┘
                               │
          ┌────────────────────┼────────────────────┐
          │                    │                    │
    ┌─────▼──────┐    ┌────────▼───────┐    ┌──────▼──────┐
    │  Frontend  │    │  BFF Service   │    │ Admin RBAC  │
    │  Angular   │    │  (Port 8080)   │    │ (Port 8084) │
    │  (Port 80) │    └──────┬─────────┘    └──────┬──────┘
    └────────────┘           │                     │
                   ┌─────────┼──────────┐          │
                   │         │          │          │
            ┌──────▼──┐ ┌────▼────┐ ┌──▼──────┐   │
            │ Ordem   │ │Pagament.│ │Catalogo │   │
            │ Serviço │ │ Serviço │ │ Serviço │   │
            │ :8081   │ │  :8082  │ │  :8083  │   │
            └────┬────┘ └────┬────┘ └────┬────┘   │
                 │           │           │         │
            ┌────▼───────────▼───┐  ┌────▼──┐  ┌──▼─────┐
            │     PostgreSQL     │  │ Redis │  │MongoDB │
            │  ordem_db          │  │       │  │        │
            │  pagamento_db      │  └───────┘  └────────┘
            │  catalogo_db       │
            └────────────────────┘
```

---

## Folder Structure

```
microservice-template/
├── frontend-angular/       # Angular 17 SPA with MSAL (Entra ID)
├── api-gateway/            # Traefik v3 configuration
│   └── dynamic/            # Dynamic routing rules
├── bff-service/            # Spring Boot BFF (JWT, ViaCEP, Redis)
├── ordem-servico/          # Spring Boot - Service Orders domain
├── pagamento-servico/      # Spring Boot - Payments domain
├── catalogo-servico/       # Spring Boot - Product catalog (Redis cache)
├── admin-rbac/             # Spring Boot - MongoDB RBAC admin
├── scripts/                # Init scripts
├── docker-compose.yml      # Full orchestration
└── README.md
```

---

## Prerequisites

- Docker 24+ and Docker Compose v2
- Node.js 20+ (for local Angular development)
- Java 25 + Maven (for local Spring Boot development)

---

## Running with Docker Compose

### 1. Clone and configure environment

```bash
git clone <repo-url>
cd microservice-template
cp .env.example .env   # Edit with your values
```

### 2. Start all services

```bash
docker compose up -d
```

### 3. Check status

```bash
docker compose ps
docker compose logs -f bff-service
```

### 4. Stop

```bash
docker compose down
```

---

## Service Ports & Responsibilities

| Service            | Port  | Technology         | Responsibility                  |
|--------------------|-------|--------------------|---------------------------------|
| Traefik Dashboard  | 8080  | Traefik v3         | API Gateway + routing dashboard |
| frontend-angular   | 4200* | Angular 17         | SPA with MSAL auth              |
| bff-service        | 8080  | Spring Boot 4.0.1  | BFF: auth, ViaCEP, aggregation  |
| ordem-servico      | 8081  | Spring Boot 4.0.1  | Service order CRUD              |
| pagamento-servico  | 8082  | Spring Boot 4.0.1  | Payment processing CRUD         |
| catalogo-servico   | 8083  | Spring Boot 4.0.1  | Product catalog with cache      |
| admin-rbac         | 8084  | Spring Boot 4.0.1  | RBAC user/role management       |
| PostgreSQL         | 5432  | PostgreSQL 16      | Relational data store           |
| MongoDB            | 27017 | MongoDB 7          | Document store for RBAC         |
| Redis              | 6379  | Redis 7            | Cache layer                     |
| Kafka              | 9092  | Kafka 3.7          | Event streaming / SAGA          |
| Zipkin             | 9411  | OpenZipkin         | Distributed tracing             |
| Prometheus         | 9090  | Prometheus         | Metrics scraping                |
| Grafana            | 3000  | Grafana            | Dashboards                      |

*4200 is for `ng serve` local dev. In Docker, served via Nginx on port 80.

---

## Swagger / OpenAPI

Each Spring Boot service exposes Swagger UI:

| Service           | URL                                      |
|-------------------|------------------------------------------|
| bff-service       | http://localhost:8080/swagger-ui.html    |
| ordem-servico     | http://localhost:8081/swagger-ui.html    |
| pagamento-servico | http://localhost:8082/swagger-ui.html    |
| catalogo-servico  | http://localhost:8083/swagger-ui.html    |
| admin-rbac        | http://localhost:8084/swagger-ui.html    |

---

## ViaCEP Integration

The BFF service integrates with [ViaCEP](https://viacep.com.br):

```
GET /api/cep/{cep}
```

Example:
```bash
curl http://localhost:8080/api/cep/01001000
```

Results are cached in Redis for 10 minutes.

---

## Environment Variables Guide

Create a `.env` file at the root:

```env
# PostgreSQL
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_secure_password

# MongoDB
MONGO_USER=admin
MONGO_PASSWORD=your_secure_password

# Redis
REDIS_PASSWORD=your_secure_password

# Admin RBAC
ADMIN_USER=admin
ADMIN_PASSWORD=your_secure_password

# Entra ID / OAuth2
OAUTH2_ISSUER_URI=https://login.microsoftonline.com/YOUR_TENANT_ID/v2.0
OAUTH2_CLIENT_ID=your_client_id
OAUTH2_CLIENT_SECRET=your_client_secret
OAUTH2_CLIENT_SCOPE=api://YOUR_API_CLIENT_ID/access_as_user

# Internal API security
INTERNAL_API_KEY=change-me-in-env

# Observability
ZIPKIN_ENDPOINT=http://zipkin:9411/api/v2/spans

# Grafana
GRAFANA_USER=admin
GRAFANA_PASSWORD=admin
```

## Security hardening added

- `bff-service` now uses `OAuth2 Client` with proactive token refresh (60-second clock skew), plus Redis-backed session.
- API Gateway validates JWT via `forwardAuth` against `bff-service` and injects `X-Internal-Api-Key` for internal calls.
- `catalogo-servico`, `ordem-servico`, and `pagamento-servico` validate JWT and require `X-Internal-Api-Key` on `/api/**`.

## Distributed observability

- Zipkin: http://localhost:9411
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

## Kafka SAGA (event-driven)

- Topic `ordem-criada-topic`: published by `ordem-servico` when a new order is created.
- Topic `pagamento-status-topic`: published by `pagamento-servico` with payment processing result.
- `ordem-servico` consumes payment status and updates order status (`CONCLUIDA` or `CANCELADA`).

---

## Microsoft Entra ID (Azure AD) Authentication

### Setup Steps

1. **Register Application** in Azure Portal → Azure Active Directory → App Registrations
2. **Note** the Application (client) ID and Directory (tenant) ID
3. **Configure Redirect URIs**: `http://localhost:4200` (dev), your production URL
4. **Expose an API** and add a scope: `access_as_user`
5. **Update frontend** config in `frontend-angular/src/environments/environment.ts`:

```typescript
export const environment = {
  msalConfig: {
    auth: {
      clientId: 'YOUR_ACTUAL_CLIENT_ID',
      authority: 'https://login.microsoftonline.com/YOUR_ACTUAL_TENANT_ID',
      redirectUri: 'http://localhost:4200',
    },
  },
  apiConfig: {
    scopes: ['api://YOUR_API_CLIENT_ID/access_as_user'],
    uri: 'http://localhost:8080/api/',
  },
};
```

6. **Update BFF service** `application.yml`:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://login.microsoftonline.com/YOUR_TENANT_ID/v2.0
```

Or set the `OAUTH2_ISSUER_URI` environment variable.

---

## Tech Stack

| Layer           | Technology                    |
|-----------------|-------------------------------|
| Frontend        | Angular 17, MSAL Angular      |
| API Gateway     | Traefik v3                    |
| Backend         | Spring Boot 4.0.1, Java 25    |
| Authentication  | OAuth2/JWT, Entra ID          |
| Relational DB   | PostgreSQL 16                 |
| Document DB     | MongoDB 7                     |
| Cache           | Redis 7                       |
| Build           | Maven, Docker multi-stage     |
| Documentation   | Swagger / OpenAPI 3           |

---

## Development

## Operational Runbook

Para troubleshooting rápido do fluxo Angular + Entra ID + Traefik + Spring Boot, consulte:

- [`docs/OPERATIONS-RUNBOOK.md`](docs/OPERATIONS-RUNBOOK.md)

### Running a single service locally

```bash
cd bff-service
mvn spring-boot:run
```

### Running only infrastructure

```bash
docker compose up -d postgres mongodb redis
```

### Frontend local dev

```bash
cd frontend-angular
npm install --legacy-peer-deps
npm start
# Access: http://localhost:4200
```
