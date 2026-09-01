# Architecture Decision Records — PicPay Payment

> **Format:** ADR — Status: `Accepted (retroactive)` = documents code as-is; `Proposed` = recommended change · **Template:** `docs/templates/adr-template.md` (via Paiga)

---

## ADR-001 — Layered / Pseudo-Hexagonal Packaging

**Status:** Accepted (retroactive) · **Date:** 2026-08-31 · **Context:** Greenfield clone needed clear separation without DDD overhead.

**Decision:** Structure `com.picpay.payment` as `application` / `domain` / `infra`; `application` holds `policies/use-cases/services/gateways`, `domain` holds `entities/dto/repositories`, `infra` holds `config/middleware/gateways/http`. No explicit `ports` package; gateways/services act as ports.

**Consequences:** Positive — readable, Interview-friendly. Negative — `application` imports `infra.SecurityConfig.WHITE_LIST` (upward leak); no ArchUnit enforcement.

**Alternatives:** True hexagonal with `ports/in+out`; Modulith.

**Follow-up:** Add ArchUnit rule `domain !→ infra`; extract `ports` package; move `User implements UserDetails` adapter to `infra`.

---

## ADR-002 — Spring Boot 3.1.5 + Java 17 Baseline

**Status:** Accepted (retroactive) · **Date:** 2026-08-31 · **Context:** Project bootstrapped 2023-2024 on latest LTS.

**Decision:** `spring-boot-starter-parent:3.1.5` (Spring 6.0.13, Jakarta EE 10) + `java.version=17`, PostgreSQL + JPA `ddl-auto=update`, H2 for tests.

**Consequences:** Positive — Jakarta-ready, LTS. Negative — **EOL 2024 (OSS ≥12–13 mo window closed per [support policy](https://spring.io/support-policy))**, 2 LTS behind Bradesco target (Java 25, Boot 4.0.x); no Virtual Threads.

**Follow-up (ADR-002.1 Proposed):** Incremental upgrade 3.1.5→3.2.x→3.4.x→4.0.x and 17→21→25; add toolchain: `maven-toolchains-plugin` with Java 17+21.

---

## ADR-003 — JWT via Auth0 java-jwt (HMAC256) + Spring Security Stateless

**Status:** Accepted (retroactive) · **Date:** 2026-08-31 · **Context:** Need stateless auth without OAuth2 server.

**Decision:** `com.auth0:java-jwt:4.4.0` with `HMAC256("${app.security.token.secret}")`, `OncePerRequestFilter` (`JWTAuthMiddleware`), `SecurityFilterChain` `STATELESS`, 2h expiry (`plusHours(2)` on `ZoneOffset.of("-03:00")`), `BCryptPasswordEncoder`.

**Consequences:** Positive — simple, no Authorization Server. Negative — `ZoneOffset` hardcoded (testability), no refresh token, logout is `clearContext()` only (token lives 2h).

**Alternatives:** `spring-security-oauth2-jose` (Nimbus, already in Boot 3) — recommended to migrate; Spring Authorization Server for refresh.

**Follow-up:** Inject `Clock`; shorten access to 15m + refresh; add revocation list (Redis).

---

## ADR-004 — API Documentation: springdoc vs springfox Duplication (Debt)

**Status:** Accepted (retroactive) with **debt** · **Date:** 2026-08-31 · **Context:** Boot 3 migration.

**Decision:** **Both** `springdoc-openapi-starter-webmvc-ui:2.2.0` **and** `springfox-swagger2:3.0.0` (+ `swagger-ui`) declared. `SwaggerConfig.java` is commented out ("not compatible with Boot3 javax→jakarta") but dependencies remain.

**Consequences:** **Negative** — Springfox abandoned 2020, `javax.*` breaks Boot 3 classpath, ~18 MB bloat. Springdoc 2.2.0 is correct for Boot 3.

**Decision (corrective):** **Remove** `springfox-swagger2` + `swagger-ui` + `SwaggerConfig.java`; keep `springdoc` only. Exposure at `/swagger-ui/index.html`, `/v3/api-docs`.

---

## ADR-005 — JPA ddl-auto=update + No Migrations

**Status:** Accepted (retroactive) with **debt** · **Date:** 2026-08-31 · **Context:** Rapid prototyping.

**Decision:** `spring.jpa.hibernate.ddl-auto=update`, `PostgreSQLDialect`, `show-sql=true`.

**Consequences:** **Negative** for prod — silent ALTER, no versioned migrations, no rollback, no review.

**ADR-005.1 Proposed:** Switch to `ddl-auto=validate` + **Flyway** (`V1__create_users_transactions.sql`) or Liquibase; keep `create-drop` for `test` profile only.

---

## ADR-006 — External Integration via RestTemplate + HttpGateway

**Status:** Accepted (retroactive) · **Date:** 2026-08-31 · **Context:** Authorizer + notification via Mocky.

**Decision:** `RestTemplate` wrapped in `HttpGateway` → `AuthorizationGateway` / `NotificationGateway`; URLs externalized as `app.external.api.*`.

**Consequences:** Positive — URLs configurable. Negative — `RestTemplate` **maintenance-only since Spring 6.x; officially deprecated in Spring Framework 7.0 (Boot 4)**; no timeout/retry/circuit breaker; blocking.

**Follow-up (Proposed):** `RestTemplateBuilder` with `connectTimeout 1s / readTimeout 2s`; add Resilience4j circuit breaker; migrate to `WebClient` / `RestClient` (Boot 3.2+).

---

## ADR-007 — Policy / UseCase Pattern for Business Rules

**Status:** Accepted (retroactive) · **Date:** 2026-08-31 · **Context:** Challenge rules (merchant block, balance, authorization).

**Decision:** Policies (`MerchantUserCantTransactPolicy`, `CannotTransactWithoutSufficientBalancePolicy`, `SendNotificationOnTransactPolicy`) orchestrated by `TransactionService.validate()` + `TransactionUseCase.execute()`.

**Consequences:** Positive — single-responsibility, testable. Negative — `TransactionService` still procedural Transaction Script; notification failure swallowed.

**Follow-up:** Introduce `Transaction` aggregate with `transfer()` domain method; outbox for notifications.

---

## ADR-008 — Upgrade Path to Java 25 + Spring Boot 4 (Proposed)

**Status:** Proposed · **Date:** 2026-08-31 · **Context:** Bradesco standard; EOL pressure.

**Decision:** Phased:

1. Remove springfox, fix `Role ORDINAL→STRING`, add JaCoCo/ArchUnit, `validate`+Flyway.
2. Boot `3.1.5→3.2.x` (Jakarta stable), Java 17→21 (Virtual Threads behind flag).
3. Boot `3.2→3.4`, WebClient migration.
4. Boot `4.0.x`, Java 25, confirm springdoc 3.x, buildpacks `jammy→noble`.

**Risks:** `RestTemplate` replacement, `javax→jakarta` already done, no native image in scope.

---

## ADR Index

| # | Title | Status |
|---|---|---|
| 001 | Layered / Pseudo-Hexagonal | Accepted |
| 002 | Boot 3.1.5 + Java 17 | Accepted + debt |
| 003 | JWT HMAC256 Stateless | Accepted |
| 004 | springdoc vs springfox debt | Debt → Remove springfox |
| 005 | ddl-auto=update | Debt → Flyway |
| 006 | RestTemplate + HttpGateway | Accepted → WebClient |
| 007 | Policy / UseCase Pattern | Accepted |
| 008 | Java 25 + Boot 4 Path | Proposed |

---

*Add new ADRs by copying `docs/templates/adr-template.md` → increment number, record context/decision/consequences, link from here.*
