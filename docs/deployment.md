# Deployment & Operations — PicPay Payment

> **Sources:** `docker-compose.yaml` · `Dockerfile` · `.github/workflows/docker-publish.yml` · `.env.example` · **Grade:** **C — demo-ready, not prod-ready** (see fixes)

---

## 1. Quick Start (dev)

```bash
cp .env.example .env
# fill DATABASE_* , JWT_SECRET (>=32 chars), AUTHORIZATION_API, NOTIFICATION_API_URL

docker compose up --build
# api:      http://localhost:8080  (Swagger /swagger-ui/index.html → 200)
# postgres: localhost:5432 (db picpay, user postgres)
# pgadmin:  http://localhost:5050 (admin@picpay.com / admin)

# health (once actuator added)
curl http://localhost:8080/actuator/health

# logs
docker compose logs -f api
```

Stop: `docker compose down` (add `-v` to wipe PGDATA).

**Bare JVM (no Docker):**

```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/picpay
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=postgres
export JWT_SECRET=change-me-32-chars-min
export AUTHORIZATION_API=https://run.mocky.io/v3/9b89b419-a2f7-4885-aa86-5ddcea24d520
export NOTIFICATION_API_URL=https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf27b2a8480
./mvnw spring-boot:run
```

---

## 2. Dockerfile

Current `Dockerfile` (dev-oriented):

```dockerfile
FROM eclipse-temurin:17-jdk
WORKDIR /picpay
COPY . .
RUN ./mvnw package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java","-jar","target/payment-0.0.1-SNAPSHOT.jar"]
```

**Problems:** copies entire repo (including `.docker/postgres` binary), reinstalls deps each build, no layer cache, JDK (not JRE) in runtime, mounts `.:/picpay` in compose.

**Recommended multistage:**

```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml ./
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

Add `.dockerignore`:

```
target/
.docker/
.git/
.mvn/
*.md
```

---

## 3. docker-compose.yaml Deep Dive

| Service | Image | Env | Ports | Volume | Issues |
|---|---|---|---|---|---|
| `api` | `build:.` | `env_file:[.env,.env.test]` | `8080:8080` | `.:/picpay` | DOUBLE env_file (test leaks), source mount in prod, no healthcheck, `depends_on` without condition |
| `postgres` | `postgres` (no tag) | `env_file:[.env]` | `5432:5432` | `.docker/postgres:/data/postgres` (wrong path) | non-pinned tag, nonstandard mount, no healthcheck |
| `pgadmin` | `dpage/pgadmin4` (no tag) | `env_file:[.env]` | `5050:80` | `.docker/pgadmin:/database/admin/pgadmin` (wrong) | wrong mount, no tag |

**Fixed compose (prod):**

```yaml
services:
  postgres:
    image: postgres:16-alpine
    restart: unless-stopped
    env_file: [.env]
    ports: ["5432:5432"]
    volumes: ["pgdata:/var/lib/postgresql/data"]
    healthcheck:
      test: ["CMD-SHELL","pg_isready -U ${DATABASE_USERNAME:-postgres}"]
      interval: 10s; timeout: 5s; retries: 5
  api:
    build: .
    restart: unless-stopped
    depends_on:
      postgres: { condition: service_healthy }
    ports: ["8080:8080"]
    env_file: [.env]
    healthcheck:
      test: ["CMD","curl","-f","http://localhost:8080/actuator/health"]
      interval: 15s
volumes: { pgdata: {} }
```

Use `docker-compose.override.yaml` for the dev source mount.

---

## 4. CI/CD — `.github/workflows/docker-publish.yml`

Current workflow: builds image, pushes to GHCR, **cosign signs** — but **no tests, no quality gate, no SBOM, no scan**. Not a real CI.

**Minimal CI before publish:**

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '17', distribution: 'temurin', cache: 'maven' }
      - run: mvn -B verify  # tests + jacoco gate
      - run: mvn -B org.owasp:dependency-check-maven:check
  publish:
    needs: build
    # ... existing docker buildx + cosign + SBOM (anchore/sbom-action)
```

Add: `trivy` image scan, `gitleaks` secret scan, `hadolint` for Dockerfile.

---

## 5. Health & Observability

| Capability | Current | Fix |
|---|---|---|
| Actuator `/actuator/health` | **missing** — add `spring-boot-starter-actuator` | `management.endpoints.web.exposure.include=health,info,metrics` |
| Metrics | none | Micrometer + Prometheus |
| Logging | `show-sql=true` only, no structured log | `logback-spring.xml` JSON appender |
| PGDATA backup | volume only | `pg_dump` cron + object storage |

---

## 6. Operational Risks (from DevOps audit)

| # | Risk | Severity | Fix |
|---|---|---|---|
| 1 | `.docker/postgres` binary tracked in git | High | `git rm -r --cached .docker/postgres` + `.gitignore` |
| 2 | `ddl-auto=update` in prod → silent ALTER | High | `validate` + Flyway |
| 3 | `api env_file` loads `.env.test` overriding prod | Med | split compose, single env_file |
| 4 | No healthcheck → orchestrator can't detect crash | Med | add healthchecks above |
| 5 | GHCR publish without test gate → broken image shipped | High | add `build` job before `publish` |
| 6 | Unpinned `postgres:latest` | Low | `postgres:16-alpine` |
| 7 | No `.dockerignore` → bloated context | Low | add file above |

---

## 7. See Also

- [Configuration](configuration.md) — env vars & profiles
- [Architecture](architecture.md) — system view
