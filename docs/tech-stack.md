# Tech Stack — PicPay Payment

> **Source:** `pom.xml` · `mvn dependency:tree` effective · `docker-compose.yaml` · `application.properties` · `infra/config/*`

---

## 1. At a Glance

| Layer | Choice | Version | Status |
|---|---|---|---|
| **Language** | Java (Jakarta EE) | **17** (pom `java.version`) | LTS until 2029, but **target 25** for virtual threads |
| **Framework** | Spring Boot | **3.1.5** (parent) → Spring 6.0.13, Hibernate 6.2.9, Tomcat 10.1.15, Jackson 2.15.2 | EOL 2024 — OSS window (≥12–13 mo/minor per [support policy](https://spring.io/support-policy)) closed; current GA **4.1.1** |
| **Build** | Maven Wrapper 3.8.5 | `mvnw` / `pom.xml` | Single module fat jar via `spring-boot-maven-plugin` + Paketo |
| **Runtime** | OpenJDK Slim 17 (Docker) | `maven:3.8.5-openjdk-17-slim` | Dev image, not multistage prod |
| **DB** | PostgreSQL (prod) + H2 (test) | PG `latest` ⚠ / H2 mem | Needs pin + Testcontainers parity |
| **Docs** | springdoc-openapi | **2.2.0** (+ springfox 3.0.0 deprecated) | Remove springfox |

---

## 2. Dependency Table (pom.xml lines 16-103)

| # | Group:Artifact | Version (effective) | Scope | Purpose | Lines | Verdict |
|---|---|---|---|---|---|---|
| 1 | `spring-boot-starter-parent` | **3.1.5** | `pom import` | BOM: manages Spring 6.0.13, Hibernate 6.2.9, etc. | 5-10 | ⚠️ EOL — upgrade to **3.4.5** (Spring 6.2) before Boot 4 |
| 2 | `spring-boot-starter-web` | 3.1.5 | compile | Spring MVC, DispatcherServlet, Jackson, Tomcat | 20-23 | ✅ |
| 3 | `spring-boot-starter-data-jpa` | 3.1.5 | compile | Spring Data JPA + Hibernate ORM | 20-23 | ✅ |
| 4 | `spring-boot-starter-security` | 3.1.5 | compile | SecurityFilterChain, DaoAuthProvider, BCrypt | 56-59 | ✅ |
| 5 | `spring-boot-starter-validation` | 3.1.5 | compile | Jakarta Validation 3.0 (HV) → `@NotBlank @Email` | 60-63 | ✅ |
| 6 | `org.postgresql:postgresql` | 42.5.4 (managed) | runtime | PostgreSQL JDBC driver | 35-38 | ✅ |
| 7 | `com.h2database:h2` | managed | test | In-memory DB for `@DataJpaTest` | 41-44 | ✅ but divergence vs PG |
| 8 | `org.projectlombok:lombok` | 1.18.30 | optional | `@Getter @Setter @AllArgsConstructor` | 45-48 | ⚠️ `@EqualsAndHashCode(of=id)` + mutable = risk |
| 9 | `spring-boot-starter-test` | 3.1.5 | test | JUnit 5, Mockito, AssertJ, Hamcrest | 50-54 + **60-63 duplicate** | ❌ **Declared twice (L50 & L64)** — remove one |
| 10 | `spring-security-test` | managed | test | `@WithMockUser`, MockMvc security | 70-74 | ✅ |
| 11 | `com.auth0:java-jwt` | **4.4.0** | compile | HMAC256 JWT (Auth0) | 76-80 | ✅ works; latest **4.5.2** (4.4.0 = Mar 2023) — bump; native `spring-security-oauth2-jose` (Nimbus) alternative on Boot 3 |
| 12 | `io.springfox:springfox-swagger2` | **3.0.0** | compile | Swagger 2 (Springfox) | 82-87 | ❌ **Incompatible with Boot 3** (`javax`→`jakarta`) — commented `SwaggerConfig.java:8` — remove |
| 13 | `io.springfox:springfox-swagger-ui` | **3.0.0** | compile | Swagger UI (Springfox) | 90-95 | ❌ same — remove |
| 14 | `org.springdoc:springdoc-openapi-starter-webmvc-ui` | **2.2.0** | compile | OpenAPI 3.1 + Swagger UI for Boot 3 / Jakarta | 97-101 | ✅ **Keep only this** |
| 15 | `spring-boot-devtools` | managed | runtime optional | LiveReload, restart | 29-34 | ⚠️ `.:/picpay` mount must not ship to prod; add profile condition |
| 16 | `spring-boot-maven-plugin` | managed | build | Fat jar + Paketo `builder:jammy` | 106-123 | ✅ — add `<layers>` + `<image>` tag |

**Effective transitive notables:** `hibernate-core 6.2.9`, `spring-security-config 6.1.5`, `jackson-databind 2.15.2`, `tomcat-embed 10.1.15`, `jakarta.persistence-api 3.1.0`.

**MISSING (should add):**
- `spring-boot-starter-actuator` → `/actuator/health`, Micrometer
- `org.flywaydb:flyway-core` or `liquibase` → versioned migrations
- `org.testcontainers:postgresql` + `junit-jupiter` → PG parity tests
- `com.tngtech.archunit:archunit` → enforce hexagonal dependency
- `io.micrometer:micrometer-registry-prometheus` → metrics
- `org.springdoc:springdoc-openapi-starter-common` already via UI; add `springdoc-openapi-starter-webmvc-api` if splitting
- `io.github.resilience4j:resilience4j-spring-boot3` → timeout/retry/circuit for gateways
- `org.mapstruct:mapstruct` → replace `BeanUtils`

---

## 3. Build & Runtime

### Maven

```xml
<parent>spring-boot-starter-parent:3.1.5</parent>
<properties><java.version>17</java.version></properties>
<build>
  <plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
      <image><builder>paketobuildpacks/builder-jammy-base:latest</builder></image>
      <excludes><exclude>lombok</exclude></excludes>
    </configuration>
  </plugin>
</build>
```

One module, one artifact: `com.picpay:payment:0.0.1-SNAPSHOT:jar` — no BOM, no multi-module. Build: `./mvnw clean install` (downloads ~180 MB). Fat jar `target/payment-0.0.1-SNAPSHOT.jar`.

### Docker

| File | Image | Command | Volumes | Env |
|---|---|---|---|---|
| `Dockerfile` | `maven:3.8.5-openjdk-17-slim` | `mvn clean install` → `mvn spring-boot:run` | `/picpay` | `ENV DATABASE_URL_TEST ...` (mis-scoped, should be compose-only) |
| `docker-compose.yaml` | `build: .` (api) + `postgres:latest` + `dpage/pgadmin4` | `restart: on-failure` / `always` | `.:/picpay`, `.docker/postgres:/data/postgres` | `env_file: [.env,.env.test]` (leak) |

See [deployment.md](deployment.md) for hardening (multistage `eclipse-temurin:17-jre-alpine` + layers).

---

## 4. Why Each Choice (Rationale)

| Choice | Why | Trade-off | Alternative |
|---|---|---|---|
| **Spring Boot 3.1** | Latest stable at project inception, Jakarta EE 10, Boot 3 auto-config | EOL quickly; should track 3.4.x LTS | Boot 3.4.5 (`3.4` support until Nov 2025) |
| **java-jwt 4.4.0** (Auth0) | Minimal, no Spring OAuth server needed, HMAC256 single secret | Reinvents refresh/RSA, Nimbus already in Boot 3 | `spring-boot-starter-oauth2-resource-server` + Nimbus JOSE |
| **springdoc 2.2.0** | Official OpenAPI for Boot 3 / Jakarta, Swagger UI auto | Need to drop springfox first | Spring REST Docs for contract tests |
| **H2 test** | Zero-infra, fast | Dialect/lock divergence → false green | Testcontainers PG |
| **Lombok** | Cuts `User`/`Transaction` boilerplate | Equals/hashCode pitfalls, hides intent | Java records + MapStruct |
| **RestTemplate** | Familiar, simple `getForEntity` | Maintenance-only since Spring 6.x; **deprecated in Spring Framework 7.0 (Boot 4)**; blocking, no timeout | `RestClient` (Spring 6.1+/Boot 3.2+) or `WebClient` |

---

## 5. Version Matrix — Current vs Target

```
Current: Java 17 ── Boot 3.1.5 (Spring 6.0.13) ── Hibernate 6.2 ── Jakarta 10
Target:  Java 25 ── Boot 3.4.x (Spring 6.2) ──── Hibernate 6.6 ── Jakarta 11 ── Boot 4.x (GA Nov 2025; 4.1.1 current)
Path:    17 → 21 (LTS, virtual threads) → 25 + Boot 3.2 → 3.4 → 4.0
DB:      postgres:latest → postgres:16-alpine + PG 17 readiness
Deps:    springfox remove → springdoc 2.8.x, java-jwt 4.4 → Nimbus, RestTemplate → RestClient
```

Upgrade note: Boot 3.1→3.2 needs `spring.security.filter.dispatcher-types` change (already Spring Security 6), and `jakarta` package is stable. Automated via `spring-boot-upgrade`.

---

## 6. External Resources

| URL | Usage | Env var | Status |
|---|---|---|---|
| `https://run.mocky.io/v3/9b89b419-a2f7-4885-aa86-5ddcea24d520` | Authorization mock `{message:Autorizado}` | `AUTHORIZATION_API_URL` → `AuthorizationGatewayImpl` | Mocky sunset risk — vendor to your own stub (WireMock) |
| `https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6` | Notification mock `{message}` | `NOTIFICATION_API_URL` → `NotificationGatewayImpl` | Same |
| `https://run.mocky.io/...` + H2/Postgres connection strings in `.env` | — | `DATABASE_URL`, `DATABASE_URL_TEST` | Move to Vault/Secrets Manager in prod |

---

## 7. Dependency Health (2026-08-31)

- **Known EOL:** Boot 3.1.5 (CVE patch window closed — check `spring-boot-admin` or `dependabot`)
- **Known deprecated:** `springfox 3.0.0` (abandoned 2020), `RestTemplate` (officially deprecated Spring Framework 7.0), `version:` in compose
- **Known duplicate:** `spring-boot-starter-test` ×2 (wastes resolver time, confuses IDE)
- **SBOM missing:** No `cyclonedx-maven-plugin` — add for audit

```bash
mvn dependency:tree | grep springfox   # should be empty after fix
mvn dependency:analyze | grep unused
mvn versions:display-dependency-updates
```

---

## 8. See Also

- [Architecture](architecture.md)
- [Configuration](configuration.md) — profiles & env precedence
- [Deployment](deployment.md) — Docker hardening + pinning
- [Code Quality](code-quality.md) — duplication & debt register

---

*✅ Fact-checked against live sources (spring.io, mvnrepository, springdoc.org, GitHub) on 2026-08-31. Boot GA **4.1.1**; springdoc 2.8.x/3.1.x; java-jwt **4.5.2**; RestTemplate deprecated Spring 7.0.*
