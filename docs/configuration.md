# Configuration — PicPay Payment

> **Source:** `src/main/resources/application.properties` · `src/test/resources/application-test.properties` · `.env.example` · `.env.test.example` · `docker-compose.yaml` · **Stack:** Spring Boot 3.1.5 + PostgreSQL + JWT HMAC256

---

## 1. Environment Variables

| Variable | Required | Example | Purpose | Used by |
|---|---|---|---|---|
| `DATABASE_URL` | ✅ | `jdbc:postgresql://localhost:5432/picpay` | JDBC URL (prod) — H2 in test | `spring.datasource.url` |
| `DATABASE_USERNAME` | ✅ | `postgres` | DB user | `spring.datasource.username` |
| `DATABASE_PASSWORD` | ✅ | `postgres` | DB password | `spring.datasource.password` |
| `JWT_SECRET` | ✅ | `a-very-long-random-base64-secret-32+chars` | HMAC256 signing key — **rotate every 90d, never log** | `app.security.token.secret` via `@Value` |
| `AUTHORIZATION_API` | ✅ | `https://run.mocky.io/v3/9b89b419-a2f7-4885-aa86-5ddcea24d520` | External authorizer endpoint | `app.external.api.authorization.url` |
| `NOTIFICATION_API_URL` | ✅ | `https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf27b2a8480` | Notification endpoint | `app.external.api.notification.url` |
| `PGADMIN_DEFAULT_EMAIL` | for pgAdmin only | `admin@picpay.com` | pgAdmin login | `docker-compose pgadmin` |
| `PGADMIN_DEFAULT_PASSWORD` | for pgAdmin only | `admin` | pgAdmin password | `docker-compose pgadmin` |

**Files:**

| File | Tracked | Purpose |
|---|---|---|
| `.env` | ❌ `.gitignore` | real secrets (copy from `.env.example`) |
| `.env.example` | ✅ | template — fill then `cp .env.example .env` |
| `.env.test` | ❌ | test-only vars (H2) |
| `.env.test.example` | ✅ | test template |

> **Never commit `.env`.** `.docker/postgres/` (binary PGDATA) is currently tracked — **untrack** via `git rm -r --cached .docker/postgres` + add `.docker/` to `.gitignore`.

---

## 2. application.properties (effective)

`src/main/resources/application.properties`:

```properties
# DataSource
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA — ⚠️ prod anti-pattern: update alters schema
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

# Jackson
spring.jackson.serialization.write-dates-as-timestamps=false

# External gateways
app.external.api.authorization.url=${AUTHORIZATION_API}
app.external.api.notification.url=${NOTIFICATION_API_URL}

# Security
app.security.token.secret=${JWT_SECRET}

# Server
server.port=8080
```

`src/test/resources/application-test.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
app.security.token.secret=test-secret-key-for-tests-only-32-chars
app.external.api.authorization.url=http://localhost:1080/authorize
app.external.api.notification.url=http://localhost:1080/notify
```

**Recommended prod overlay** `application-prod.properties`:

```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.root=INFO
management.endpoints.web.exposure.include=health,info,metrics
```

---

## 3. Docker Compose Wiring

`docker-compose.yaml` (3 services):

```yaml
services:
  api:
    build: .
    restart: on-failure
    depends_on: [postgres]
    ports: ["8080:8080"]
    volumes: [".:/picpay"] # dev mount — remove in prod
    env_file: [.env, .env.test] # BUG — double load, last wins; .env.test must NOT load in prod
  postgres:
    image: postgres:16-alpine # pin tag — bare `postgres` → latest non-deterministic
    restart: always
    env_file: [.env]
    ports: ["5432:5432"]
    volumes: [".docker/postgres:/var/lib/postgresql/data"] # fix path
  pgadmin:
    image: dpage/pgadmin4:8
    restart: always
    depends_on: [postgres]
    env_file: [.env]
    ports: ["5050:80"]
    volumes: [".docker/pgadmin:/var/lib/pgadmin"]
```

**Fixes:** remove `version:'3.8'` (deprecated), pin image tags, drop `volumes:.` in prod, split to `docker-compose.override.yaml` for dev, add `healthcheck` on postgres + `depends_on: condition: service_healthy` on api.

---

## 4. Secrets Management

| Secret | Current | Recommended prod |
|---|---|---|
| `JWT_SECRET` | `.env` file, `@Value` | env var via Docker Swarm/K8s Secret or Vault; `length >= 32`, base64 random |
| `DATABASE_PASSWORD` | `.env` plaintext | K8s Secret / AWS Secrets Manager; rotate; not in image |
| `PGADMIN_DEFAULT_PASSWORD` | `.env` | separate secret; not reused |

**Rules:** never `echo $JWT_SECRET`, never log, rotate 90d, add `gitleaks` pre-commit + CI.

---

## 5. Spring Profiles

| Profile | How to run | DB | External mocks |
|---|---|---|---|
| default (no profile) | `docker compose up` | PostgreSQL via compose | Mocky real URLs |
| `test` | `./mvnw test -Dspring.profiles.active=test` | H2 `mem:testdb` `create-drop` | `localhost:1080` stub (WireMock) |
| `prod` (to add) | `java -jar target/*.jar --spring.profiles.active=prod` | PostgreSQL managed (RDS/Cloud SQL) | real authorizer/notify |

Activate via `SPRING_PROFILES_ACTIVE=prod` env var or `--spring.profiles.active=prod`.

---

## 6. Operational Gaps

| Gap | Impact | Fix |
|---|---|---|
| `ddl-auto=update` in prod | schema drift, silent ALTER | `validate` + Flyway/Liquibase |
| No `management.*` exposure | no health/readiness probe | expose `health`, add `spring-boot-starter-actuator` |
| `.env.test` leaked into api env_file | test vars may override prod | split compose files; `env_file: [.env]` only |
| PGDATA tracked in git | binary bloat, secrets risk | `git rm --cached` + `.gitignore` |
| No `logback-spring.xml` | unstructured logs | add JSON appender |

---

## 7. See Also

- [Deployment](deployment.md) — compose & Dockerfile
- [Security](security.md) — JWT secret handling
- [Tech Stack](tech-stack.md) — versions
