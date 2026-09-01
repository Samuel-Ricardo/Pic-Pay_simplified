# Testing Strategy — PicPay Payment

> **Verdict: ❌ REJECTED — DoD 80% FAIL** · **Estimated coverage ~12–18%** · **No JaCoCo, no ArchUnit, no contract** · Source: `src/test/**` · `pom.xml` test deps · `application-test.properties`

---

## 1. Inventory

| # | File | Type | Claims | Actually tests | Isolation | Coverage |
|---|---|---|---|---|---|---|
| 1 | `PaymentApplicationTests.contextLoads()` | smoke | boot | H2 boot, no assertions | `@SpringBootTest` full | 0% logic |
| 2 | `data/UserData.java` | helper | fixtures | 2 static + 2 random factories | — | — |
| 3 | `data/TransactionData.java` | helper | fixtures | static SENDER/RECEIVER via `RANDOM_VALID_COMMON_USER()` — **non-deterministic shared state** | — | — |
| 4 | `data/NotificationData.java` | helper | fixtures | single email+message | — | — |
| 5 | `policy/transaction/CannotTransactWithoutSufficientBalancePolicyTest` | unit | balance policy | 2 cases (50 ok, 5000 insufficient) | `@SpringBootTest` heavy | ~100% of that policy |
| 6 | `policy/transaction/MerchantUserCantTransactPolicyTest` | unit | merchant block | COMMON false / MERCHANT true | `@SpringBootTest` | 100% policy |
| 7 | `policy/transaction/SendNotificationOnTransactPolicyTest` | unit (mocked) | notification | `assertDoesNotThrow` + verify 2× `sendNotification` | `@SpringBootTest` + manual `initMocks` | ~60% (no failure) |
| 8 | `repositories/user/UserRepositoryTest` | integration | JPA | `findUserByDocument` + save happy path | `@DataJpaTest` H2 | ~20% |
| 9 | `repositories/transaction/TransactionRepositoryTest` | integration | JPA | only `findAll()!=null` — does not persist/assert mapping | `@DataJpaTest` | ~5% |
| 10 | `service/user/UserServiceTest` | unit mocked | UserService facade | save/findById/findAll with `any(Class)` mocks | `@SpringBootTest` + `@Mock` + `@InjectMocks` | ~40% (no exception) |
| 11 | `service/authorization/AuthorizationServiceTest` | unit mocked | auth gateway | `isTransactionAuthorized true` delegates | `@SpringBootTest` | ~30% |
| 12 | `service/notification/NotificationServiceTest` | unit mocked | notification | verify `send.execute` once | `@SpringBootTest` | ~30% |
| 13 | `service/transaction/TransactionServiceTest` | unit mocked | transact | validate + transact happy path, verify 7 mocks | `@SpringBootTest` | ~50% happy only |

**Totals:** 13 test files (incl. 3 builders + 1 smoke), **9 real test classes**, ~74 prod `.java` → **74% untested**. No coverage gate; `maven-surefire`/`failsafe` not configured; build green at 5%.

**Missing deps:** `jacoco`, `testcontainers`, `rest-assured`/`mockMvc` enhanced, `awaitility`, `archunit`, `pitest`, `wiremock`.

---

## 2. Coverage Heatmap

| Layer | Components | Tests | Est. Coverage | Risk |
|---|---|---|---|---|
| `policy/transaction` | 3 policies | 3 tests | ~85% | Low |
| `repositories` | 2 repos | 2 tests | ~15% | High |
| `service` | 4 services | 4 tests | ~35% | High |
| `infra` (security, middleware, gateways, http) | ~12 classes | **0** | **0%** | Critical |
| `controllers` | 4 | **0** | **0%** | Critical |
| `domain/entities` | 4 | **0** | **0%** | Med |
| `usecases` | ~10 | **0** | **0%** | Critical |
| `auth/jwt` | 2 | **0** | **0%** | Critical |

---

## 3. Anti-patterns Found

| Anti-pattern | Where | Impact | Fix |
|---|---|---|---|
| `@SpringBootTest` for pure unit | `MerchantUserCantTransactPolicyTest` etc | 5–10s startup per class | use plain JUnit + constructor injection |
| Manual `MockitoAnnotations.openMocks(this)` | `SendNotification…Test` | leaks, init duplicate with `@SpringBootTest` | `@ExtendWith(MockitoExtension.class)` |
| Non-deterministic fixtures | `TransactionData.SENDER` random COMMON | flaky CI | deterministic builders (e.g. Fixture Factory) |
| `assertDoesNotThrow` as only assertion | `SendNotification…Test` | no behavior proof | assert interactions + state |
| `findAll()!=null` assertion | `TransactionRepositoryTest` | tautology | `assertThat(saved.getId()).isNotNull()` |
| No `@Transactional` on data tests | repos | DB leaks between tests | add `@Transactional` or cleanup |
| Principle misattribution | `MerchantUserCantTransactPolicyImpl:769b38eb` commit says SRP but is OCP | docs confusion | fix commit msg |

---

## 4. Required Test Matrix (to reach 80% DoD)

| Suite | Tooling | Scope |
|---|---|---|
| **Unit** — policies, usecases, services (mocked deps) | JUnit5 + Mockito `MockitoExtension` | every `application/**` class, happy + exception paths |
| **Slice** — `controllers` via `@WebMvcTest` + `MockMvc` + `spring-security-test` | `MockMvc`, `WithMockUser` | auth matrix, validation 400, RBAC 403 |
| **JPA** — `@DataJpaTest` H2 | AssertJ | repositories, query methods, constraints (unique) |
| **Integration** — `Testcontainers` PostgreSQL | `testcontainers:postgresql` | real dialect, Flyway migrations, transactions |
| **Gateway** — `WireMock` | `wiremock-jre8` | authorizer/notify timeout, 5xx, parse errors |
| **Security** — JWT | `spring-security-test`, `java-jwt` | token generation/validation/expiry/whitelist |
| **Arch** — `ArchUnit` | `com.tngtech.archunit` | hexagonal deps: `domain` must not depend on `infra` |
| **Mutation** — PITest | `pitest-maven` | policy logic `>=70%` kill rate |

---

## 5. JaCoCo Gate (to add)

`pom.xml`:

```xml
<plugin>
  <groupId>org.jacoco</groupId><artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.11</version>
  <executions>
    <execution><goals><goal>prepare-agent</goal></goals></execution>
    <execution><id>report</id><phase>verify</phase><goals><goal>report</goal></goals></execution>
    <execution><id>check</id><phase>verify</phase><goals><goal>check</goal></goals>
      <configuration><rules><rule><element>BUNDLE</element>
        <limits><limit><counter>LINE</counter><value>COVEREDRATIO</value><minimum>0.80</minimum></limit></limits>
      </rule></rules></configuration>
    </execution>
  </executions>
</plugin>
```

Fail build if `<80%` line coverage. Exclusions: `Application.java`, `config/*`, DTO records.

---

## 6. How to Run

```bash
./mvnw test                          # unit + slice (H2)
./mvnw verify                        # + jacoco check + pitest
./mvnw test -Dtest=MerchantUserCantTransactPolicyTest
./mvnw test -Dspring.profiles.active=test  # uses application-test.properties
```

---

## 7. Next Steps (prioritized)

1. Add `jacoco-maven-plugin` + `0.80` gate (1h, blocks release).
2. Replace `@SpringBootTest` in pure units with `MockitoExtension` (1h).
3. Deterministic builders (30m).
4. `@WebMvcTest` for 4 controllers + 5xx mapping (3h).
5. `WireMock` for 2 gateways (2h).
6. `Testcontainers` PostgreSQL for repos (1h).
7. `ArchUnit` rule `domain → infra` is forbidden (30m).
8. PITest on `policy/` (30m).

---

## 8. See Also

- [Code Quality](code-quality.md) — lint gates
- [Business Rules](business-rules.md) — rules to test
