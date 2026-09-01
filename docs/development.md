# Development Guide — PicPay Payment

> **Stack:** Java 17 · Maven Wrapper · Spring Boot 3.1.5 · PostgreSQL 16 · Docker Compose · H2 (test) · **Entry:** `payment/` root

---

## 1. Prerequisites

| Tool | Version | Check |
|---|---|---|
| Java | 17 (Temurin) | `java -version` |
| Maven Wrapper | 3.9 (bundled `./mvnw`) | `./mvnw -v` — no global Maven needed |
| Docker & Compose | Docker 24+, Compose v2 | `docker compose version` |
| Git | 2.x | `git --version` |
| curl / jq | any | `curl --version` |

IDE: IntelliJ IDEA / VS Code with Java Extension Pack + Lombok plugin + Spring Boot support.

---

## 2. Setup

```bash
git clone <repo-url>
cd payment

cp .env.example .env
# edit .env: DATABASE_URL/USERNAME/PASSWORD, JWT_SECRET (>=32 random), AUTHORIZATION_API, NOTIFICATION_API_URL, PGADMIN_*

# optional test overrides
cp .env.test.example .env.test

# untrack accidental PGDATA (if cloned with it)
git rm -r --cached .docker/postgres 2>/dev/null; echo ".docker/" >> .gitignore

docker compose up --build -d
docker compose logs -f api          # wait for "Started PaymentApplication"

curl http://localhost:8080/swagger-ui/index.html  # 200
```

`.env` is **gitignored** — never commit. Generate `JWT_SECRET`: `openssl rand -base64 32`.

---

## 3. Running

| Mode | Command | Notes |
|---|---|---|
| Docker (recommended) | `docker compose up --build` | api 8080, postgres 5432, pgadmin 5050 |
| JVM bare | `export $(cat .env \| xargs) && ./mvnw spring-boot:run` | reads `application.properties` env |
| Test profile | `./mvnw test -Dspring.profiles.active=test` | H2 `mem:testdb` `create-drop`, Mocky stub |
| JAR | `./mvnw package -DskipTests && java -jar target/payment-*.jar` | needs env vars |

Rebuild after pom change: `docker compose build --no-cache`.

---

## 4. Project Structure

```
src/main/java/com/picpay/payment/
  application/    # policies, usecases, services, dtos
  domain/         # entities (User, Transaction), enums, repository interfaces
  infra/          # config (security, app), middleware (JWT filter), gateways (http)
src/main/resources/application.properties
src/test/{java,resources}
pom.xml
docker-compose.yaml
Dockerfile
```

Package rules: `domain` must not depend on `infra`; `application` orchestrates, not leaks.

---

## 5. Conventions

| Area | Rule |
|---|---|
| Java | Records for DTOs, Lombok `@Getter` only on entities (avoid `@Setter` on `id`), constructor injection (`@RequiredArgsConstructor`), no field `@Autowired` |
| Naming | DTO `value` vs entity `amount` → align (prefer `amount`); policies `*Policy`, usecases `*UseCase` |
| Enums | `EnumType.STRING` (never ORDINAL — `Role` fix pending) |
| HTTP | `ExceptionDTO {message}` today; migrate to RFC 7807 `ProblemDetail` |
| Commits | Conventional Commits: `feat:`, `fix:`, `refactor:`, `test:`, `docs:`; one logical change per commit |
| Branches | `main` protected; feature `feat/<slug>`, fix `fix/<slug>`; PR with checklist |
| Formatting | `google-java-format` via Spotless (to add): `./mvnw spotless:apply` |

---

## 6. Testing

```bash
./mvnw test                           # unit + @DataJpaTest (H2)
./mvnw verify                         # + jacoco check (80%) + archunit (after gate added)
./mvnw test -Dtest=MerchantUserCantTransactPolicyTest
```

See [Testing](testing.md) for coverage matrix and JaCoCo gate to add.

**Before pushing:** `./mvnw verify` must be green (tests + coverage + ArchUnit `domain !→ infra`).

---

## 7. Debugging

- **Swagger JSON:** `curl http://localhost:8080/v3/api-docs | jq .info`
- **DB:** `docker exec -it payment-postgres-1 psql -U postgres -d picpay` → `\dt`, `select * from tb_users;`
- **pgAdmin:** `http://localhost:5050` → Add Server: host `postgres`, port `5432`, db `picpay`.
- **Logs:** `docker compose logs -f api`; JPA SQL: `spring.jpa.show-sql=true` (dev only).

---

## 8. Troubleshooting

| Symptom | Cause | Fix |
|---|---|---|
| `DataSource` connection refused | postgres not ready | `docker compose ps`; `depends_on` without healthcheck — wait then retry |
| `JWT verification failed` 401 | `JWT_SECRET` mismatch between `.env` and container env | recreate: `docker compose down && docker compose up` |
| Swagger 404 | springfox conflict | remove springfox deps + `SwaggerConfig.java` |
| `ddl-auto=update` alters schema silently | prod property leaked to dev | switch prod to `validate` + Flyway |
| Port 8080 in use | other service | `lsof -i :8080` then `docker compose down` or change `server.port` |

---

## 9. Contributing Checklist (PR must pass)

- [ ] `./mvnw verify` green (incl. JaCoCo `>=80%` once gate added)
- [ ] No `System.out`, no hard-coded secret, no `.env` committed
- [ ] DTO validation: `@Positive` on `value`, `@CPF/@CNPJ` if touching User
- [ ] `Role` STRING, `balance` precision, `UserResponseDTO` (no password)
- [ ] Docs updated (`docs/` + `README.md` if endpoint added)
- [ ] ArchUnit `domain !→ infra` passes

---

## 10. See Also

- [Configuration](configuration.md) — env & profiles
- [Testing](testing.md) — coverage gate
- [Code Quality](code-quality.md) — lint gates
