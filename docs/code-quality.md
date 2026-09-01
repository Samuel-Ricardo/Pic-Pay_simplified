# Code Quality — PicPay Payment

> **Lens:** Clean Code, SOLID, Bradesco standards (Java 25, Spring Boot 4, hexagonal) · **Source:** full `src/main/java` audit (Tiago/Wilson) + `pom.xml` + `docker-compose.yaml`

---

## 1. Scorecard

| Dimension | Grade | Notes |
|---|---|---|
| Layering / Hexagonal | **C** | Intent hexagonal (`application/domain/infra`) but no `ports` package; `application` imports `infra` (`SecurityConfig.WHITE_LIST`) → upward coupling; field injection via `@Autowired` breaks inversion |
| Domain richness | **D** | Anemic entities + `TransactionService` Transaction Script; no aggregates/value objects; `User implements UserDetails` leaks infra into domain |
| Error handling | **C** | `ControllerExceptionHandler` exists but generic `Exception("Merchant...")` maps to 500; no `Problem+JSON` (RFC 7807) |
| Testability | **F** | 74% classes untested; `@SpringBootTest` for pure units; 0% in `infra`/`controllers` |
| Security hygiene | **C** | BCrypt ok, but `GET /users` leaks password hash + balance; no rate-limit; JWT 2h no refresh |
| Consistency | **C** | Mixed `value` vs `amount`, `Generate vs Generation`, `sendNotification` naming; `Role` ORDINAL bug |
| Tooling gates | **F** | No JaCoCo, Checkstyle, SpotBugs, ArchUnit, PITest, `.editorconfig` |

---

## 2. Critical Issues (fix before 80% DoD)

| # | Issue | Location | Fix |
|---|---|---|---|
| 1 | **Springfox 3.0.0 duplicated** + springdoc 2.2.0 | `pom.xml` 54-61 | Remove springfox `swagger2+swagger-ui` + delete `SwaggerConfig.java` (already disabled) |
| 2 | `spring-boot-starter-test` duplicated | `pom.xml` 32 + 48 | Keep one with `<scope>test</scope>` |
| 3 | `Role` mapped `@Enumerated` **ORDINAL** | `domain/entities/user/Role.java` | Change to `EnumType.STRING` — ordinal reorders corrupt data |
| 4 | `UserType` correct as STRING, `Role` not — inconsistency | same | Align both to STRING |
| 5 | `BigDecimal balance` no `precision/scale` | `User.java` | `@Column(precision=19,scale=2)` + `@PositiveOrZero` |
| 6 | `Transaction.amount` vs `TransactionDTO.value` naming mismatch | `domain/entities/transaction/Transaction.java` | Rename `amount` → `value` or DTO to `amount` |
| 7 | No `@Transactional` on `TransactionUseCaseImpl` (3 writes, no atomicity, lost update) | `application/usecases/transaction/TransactionUseCaseImpl.java` | Add `@Transactional` + `PESSIMISTIC_WRITE` lock on sender row |
| 8 | `User implements UserDetails` mixes security into domain | `User.java` | Extract `UserPrincipal implements UserDetails` adapter in `infra` |
| 9 | Field injection `@Autowired` | `AppConfig.java`, services | Constructor injection (Lombok `@RequiredArgsConstructor` or explicit) |
| 10 | `RestTemplate` maintenance-only (deprecated Spring 7.0), no timeout/retry | `infra/gateways/**`, `infra/http/HttpGateway.java` | Migrate to `RestClient` (Spring 6.1+) + Resilience4j or `WebClient` |
| 11 | `GET /users` returns entity with password hash | `UserController.java`, `UserService.java` | Return `UserResponseDTO` without `password` (`@JsonIgnore` not enough) |
| 12 | `ddl-auto=update` in prod | `application.properties` | `validate` + Flyway (`V1__create_users_transactions.sql`) |
| 13 | No coverage gate → build green at 5% | `pom.xml` | Add `jacoco-maven-plugin` with `0.80` `COVEREDRATIO` (see [Testing](testing.md)) |
| 14 | Version EOL: Boot 3.1.5 + Java 17 behind Bradesco target (25 + Boot 4) | `pom.xml` | Plan upgrade path 3.1.5→3.2→3.4→4.0; Java 17→21→25 (see [Decisions](decisions.md)) |

---

## 3. Code Smells & Refactors

| Smell | Example | Refactor |
|---|---|---|
| Generic `Exception` | `throw new Exception("Balance is not enough")` | Typed `InsufficientBalanceException extends BusinessException` + `@ExceptionHandler` → 400 |
| Policy naming inconsistent | `CannotTransactWithoutSufficientBalancePolicy` vs `IsAuthorizedUseCase` | Align to `*Policy` or `*Specification` (DDD) |
| `AppConfig` God Factory | 12 `registerXPolicy` beans + gateways | Split to `PolicyConfig`, `GatewayConfig`, rely on `@Component` scanning where possible |
| `BeanUtils.copyProperties` | `User.from(dto)` | Explicit mapper (MapStruct) — no silent ignore of new fields |
| `@EqualsAndHashCode(of="id")` + mutability | `User.java` `@Setter` | Remove `@Setter` on `id`, or use `@Getter` only + builder |
| `String message` comparison | `authResponse.message().equals("Autorizado")` | Enum `AuthorizationStatus.AUTHORIZED` |

---

## 4. Recommended Quality Gates (pom.xml)

```xml
<!-- JaCoCo 80% (see testing.md) -->
<!-- Checkstyle -->
<plugin><groupId>org.apache.maven.plugins</groupId><artifactId>maven-checkstyle-plugin</artifactId></plugin>
<!-- SpotBugs -->
<plugin><groupId>com.github.spotbugs</groupId><artifactId>spotbugs-maven-plugin</artifactId></plugin>
<!-- PMD / Spotless (google-java-format) -->
<plugin><groupId>com.diffplug.spotless</groupId><artifactId>spotless-maven-plugin</artifactId></plugin>
<!-- ArchUnit test -->
<dependency><groupId>com.tngtech.archunit</groupId><artifactId>archunit-junit5</artifactId><scope>test</scope></dependency>
```

**ArchUnit rule:**

```java
@Test void domainMustNotDependOnInfra() {
  noClasses().that().resideInAPackage("..domain..")
    .should().dependOnClassesThat().resideInAPackage("..infra..")
    .check(new ClassFileImporter().importPackages("com.picpay"));
}
```

---

## 5. Bradesco Target Gap

| Axis | Current | Target | Migration |
|---|---|---|---|
| Java | 17 (LTS to 2029) | 25 (LTS 2025) | 17→21 (Virtual Threads) →25 (pattern matching, sequenced collections) |
| Spring Boot | 3.1.5 (EOL 2024 — OSS window closed) | 4.1.x (current GA) | 3.1→3.2→3.4→4.0 incremental |
| API docs | springfox 3.0.0 (abandoned 2020) | springdoc 2.x→3.x | Remove springfox now |
| ORM | JPA `update` | Flyway + `validate` | Add `V1__init.sql` |

---

## 6. See Also

- [Testing](testing.md) — JaCoCo & test matrix
- [Decisions](decisions.md) — ADRs for upgrades
- [Architecture](architecture.md) — clean layering
