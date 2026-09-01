# Domain Model — PicPay Payment

> **Source:** `domain/entities/**` · `domain/dto/**` · `domain/repositories/**` · `application/usecase/**` · `domain/services/**`

---

## 1. Ubiquitous Language

| Term | Meaning | System type | Notes |
|---|---|---|---|
| **User** | Wallet holder | `domain/entities/user/User.java` implements `UserDetails` | Identity = `email` (username), unique `document` (CPF/CNPJ), `balance`, `userType`, `role` |
| **COMMON** | Individual — can send & receive | `UserType.COMMON` | `MerchantUserCantTransactPolicy` returns false → allowed to transact |
| **MERCHANT (Lojista)** | Shop — receives only | `UserType.MERCHANT` | Policy returns true → blocked as sender (R1) |
| **Transaction** | Value transfer sender→receiver | `domain/entities/transaction/Transaction.java` | `ManyToOne sender/receiver` → `User`, `amount`, `createdAt` |
| **Authorization** | External permission check | `AuthorizationGateway` → Mocky `{message: Autorizado}` | `IsAuthorizedUseCase` interprets string match |
| **Notification** | Fire-and-post-persist callback | `NotificationGateway.post(email,message)` | `SendNotificationOnTransactPolicy`, swallowed |

---

## 2. Entities

### 2.1 User — `domain/entities/user/User.java` (61 lines)

```java
@Entity(name="tb_users") @Table(name="tb_users")
@AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(of="id")
@Getter @Setter
public class User implements UserDetails {
  @Id @GeneratedValue(strategy=UUID) private UUID id;
  private String firstName, lastName;
  @Column(unique=true) private String email;      // UserDetails.getUsername()
  private String password;                         // BCrypt
  @Column(unique=true) private String document;    // CPF/CNPJ
  private BigDecimal balance;                      // ⚠ no precision/scale
  @Enumerated(EnumType.STRING) private UserType userType;
  @Enumerated private Role role;                   // ★ bug: ORDINAL, not STRING
  public static User from(UserDTO dto){ BeanUtils.copyProperties(dto,user); }
  // UserDetails: authorities = role.getAuthorities(), all *Non*Expired/Locked = true
}
```

| Field | Type | JPA | Constraints | Issue |
|---|---|---|---|---|
| `id` | `UUID` | `@GeneratedValue(UUID)` (Hibernate 6 `UUIDGenerator` random) | PK | ✅ maps to Postgres `uuid`; inconsistency with `Transaction` `AUTO` |
| `firstName/lastName` | `String` | default | none | missing `@Column(length, nullable)` → defaults 255 |
| `email` | `String` | `@Column(unique)` | UK, also `UserDetails.username` | no `@Email` here (only DTO) |
| `password` | `String` | default | BCrypt stored | no `nullable=false`, no `@Transient` prevention |
| `document` | `String` | `@Column(unique)` | UK, CPF/CNPJ | no format validation (CPF regex) |
| `balance` | `BigDecimal` | default | `numeric` w/out precision | ⚠ `precision=19,scale=2` missing → rounding bugs |
| `userType` | `UserType` | `@Enumerated(STRING)` | `COMMON/MERCHANT` | ✅ correct STRING |
| `role` | `Role` | `@Enumerated` (=ORDINAL) | `USER=0, ADMIN=1` | ❌ **ORDINAL bug** — reorder enum = privilege escalation; fix `STRING` + Flyway V2 |
| `from(dto)` | mapper | `BeanUtils.copyProperties` | reflection, ignores `balance` scale | Use MapStruct |

UserDetails: `getAuthorities()` → `role.getAuthorities()` (see Auth), `isEnabled*` all `true` (no soft-delete/lock).

### 2.2 Transaction — `domain/entities/transaction/Transaction.java` (25 lines)

```java
@Entity(name="tb_transactions") @Table(name="tb_transactions")
@AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(of="id")
@Getter @Setter
public class Transaction {
  @Id @GeneratedValue(strategy=AUTO) private UUID id;   // ≠ User UUID
  private BigDecimal amount;                             // mapped from DTO.value
  @ManyToOne @JoinColumn(name="sender_id") User sender;
  @ManyToOne @JoinColumn(name="receiver_id") User receiver;
  @CreatedDate LocalDateTime createdAt;                  // Auditing, no @Version
}
```

| Field | JPA | Note |
|---|---|---|
| `id` | `AUTO` | Hibernate `AUTO` for `UUID` picks `AUTO` → `UUIDGenerator`; but diverges from `User` `UUID` strategy; unify to `UUID` |
| `amount` | `numeric` | again no precision; comes from `TransactionDTO.value` |
| `sender/receiver` | `ManyToOne` lazy? defaults `EAGER` → N+1 risk | Consider `@ManyToOne(fetch=LAZY)` + `EntityGraph` |
| `createdAt` | `@CreatedDate` | Requires `@EnableJpaAuditing` + `AuditorAware`; present in `infra/config/AppConfig` → works, but `User` lacks timestamps |
| Missing | — | `idempotencyKey`, `status {PENDING,COMMITTED,FAILED}`, `@Version` optimistic lock |

### 2.3 Enums

**UserType** `domain/entities/user/UserType.java`
```java
public enum UserType { COMMON, MERCHANT }
```

**Role** `domain/entities/auth/Role.java` (43 lines)
```java
@RequiredArgsConstructor public enum Role {
  USER(Set.of(READ_USER, EXECUTE_TRANSACTION, READ_TRANSACTION)),
  ADMIN(Stream.concat(USER.permissions.stream(),
        Set.of(DELETE_USER, CREATE_USER, UPDATE_USER).stream()).collect(toSet()));
  private final Set<Permissions> permissions;
  public List<SimpleGrantedAuthority> getAuthorities(){
    var auths = permissions.stream().map(p->new SimpleGrantedAuthority(p.getPermission())).collect(toSet());
    auths.add(new SimpleGrantedAuthority("ROLE_"+name())); // both permission & role granted
    return auths.stream().toList();
  }
}
```

**Permissions** `domain/entities/auth/Permissions.java` (8 values)
```
READ_USER, CREATE_USER, UPDATE_USER, DELETE_USER,
EXECUTE_TRANSACTION, READ_TRANSACTION,
(ADMIN inherits) ...
```
`getPermission()` → string e.g. `"user:read"`, `"transaction:execute"` (exact strings defined in `Permissions` enum). `hasAuthority()` checks this; `hasRole()` checks `ROLE_USER`.

---

## 3. DTOs & Validation (Jakarta Validation 3.0)

| DTO | Fields | Validation | Notes |
|---|---|---|---|
| **UserDTO** `domain/dto/user/UserDTO.java` | `firstName,lastName,document,balance,email,password,userType,role` (record) | `@NotBlank @Email @Size(min=3) @NotNull @NotBlank` | `balance` `BigDecimal` `@NotNull`; no `@Positive`; `document` no `@Pattern` CPF; `role` enforces RBAC binding |
| **TransactionDTO** `domain/dto/transaction/TransactionDTO.java` | `value, senderId, receiverId` (record) | `@NotNull` all three | `value` no `@Positive` — zero/negative would pass validation → service must reject |
| **LoginDTO** `domain/dto/auth/LoginDTO.java` | `email, password` | `@Email @NotBlank` | Used by `AuthenticationController` → `AuthenticateUserUseCase` |
| **LoginResponseDTO** `domain/dto/auth/LoginResponseDTO.java` | `token` | — | `GenerateJWTUseCase` result |
| **NotificationDTO** `domain/dto/notification/NotificationDTO.java` | `email, message` | — | Unvalidated before `HttpGateway.post()` |
| **ExceptionDTO** `domain/dto/error/ExceptionDTO.java` | `message` (+ status?) | — | Mapped by `ControllerExceptionHandler` |

> **Gap:** `@Valid` on `UserController.create(@Valid @RequestBody UserDTO)` and `TransactionController.transact(@Valid …)` — good, but missing on `AuthenticationController`. Add.

---

## 4. Repositories (Ports out → JPA adapters)

```java
// domain/repositories/UserRepository.java
public interface UserRepository extends JpaRepository<User,UUID> {
  Optional<User> findByEmail(String email);        // UserDetailsService
  Optional<User> findByDocument(String document);  // uniqueness / registration
}
// domain/repositories/TransactionRepository.java
public interface TransactionRepository extends JpaRepository<Transaction,UUID> {}
```

Adapters: `UserRepository` backed by Postgres `tb_users`, `TransactionRepository` by `tb_transactions`. No custom queries, no `@Lock`/`@Query`. `TransactionRepositoryTest` only checks `findAll()!=null`.

---

## 5. Services (Ports) & UseCases

### Services (interfaces) `domain/services/*`

| Interface | Method | Impl | Notes |
|---|---|---|---|
| `TransactionService` | `validate(User,amount)`, `transact(TransactionDTO)`, `findAll()` | `TransactionServiceImpl` | validate → merchant + balance + authorize; transact → orchestration |
| `UserService` | `findUserById`, `findByEmail`, `findAll`, `save` | `UserServiceImpl` + `SaveUserUseCase`, `Find*UseCase` | Save encrypts password via policy |
| `AuthorizationService` | `isTransactionAuthorized(User,amount)` | `AuthorizationServiceImpl` → `IsAuthorizedUseCase` | Delegates to `AuthorizationGateway.get()` |
| `NotificationService` | `sendNotification(Transaction)` | `NotificationServiceImpl` → `SendNotificationUseCase` | |
| `auth.AuthenticationService` | `authenticate(LoginDTO)` | `AuthenticationServiceImpl` → `AuthenticateUserUseCase` | |
| `auth.AuthContextService` | `saveContext / getUser` | `AuthContextServiceImpl` | `SecurityContextHolder` |
| `auth.token.TokenService` | `generate / validate / getFromHeader` | `JWTService` → `GenerateJWTUseCase` etc. | |

### UseCases (`application/usecase/*` 15) — Transaction Script style

| Package | UseCase | What it does | Issue |
|---|---|---|---|
| `transaction` | `TransactionUseCase.execute(Transaction)` | `sender.balance -= amount; receiver.balance += amount; return transaction` | Pure, but caller must handle persistence; missing `@Transactional` domain boundary |
| `transaction` | `SaveTransactionUseCase` | `transactionRepository.save()` | — |
| `transaction` | `FindAllTransactionsUseCase` | `findAll()` | — |
| `authorization` | `IsAuthorizedUseCase.execute()` | `gateway.get(AUTHORIZATION_API_URL)` → check `map.get("message").equals("Autorizado")` | Stringly-typed; Mocky contract brittle; no timeout |
| `auth` | `AuthenticateUserUseCase` | `authenticationManager.authenticate()` | |
| `auth.jwt` | `GenerateJWTUseCase` | `JWT.create().withSubject(email).withExpiresAt(2h).sign(algorithm)` via `TokenMustExpiresInTwoHoursPolicy` | `Algorithm HMAC256(JWT_SECRET)` |
| `auth.jwt` | `ValidateJWTUseCase` | `JWT.require(algorithm).build().verify(token)` | |
| `auth.context` | `SaveAuthContextUseCase` / `GetUserFromContextUseCase` | `SecurityContextHolder` | |
| `user` | `SaveUserUseCase`, `FindUserById/Email/FindAll` | CRUD + encrypt policy | |

---

## 6. Policies — Domain Invariants

All follow `domain/policy/*` IF → `application/policy/*Impl` `@Component`.

| Policy IF | Impl | Logic | Location |
|---|---|---|---|
| `MerchantUserCantTransactPolicy` | `MerchantUserCantTransactPolicyImpl` | `return userType == MERCHANT` (true = block) | `application/policy/transaction` |
| `CannotTransactWithoutSufficientBalancePolicy` | `...CannotTransactWithoutSufficientBalancePolicyImpl` | `sender.balance < amount` → true (block) |  |
| `SendNotificationOnTransactPolicy` | `SendNotificationOnTransactPolicyImpl` | `notificationService.sendNotification(tx)` (recoverable) | try/catch swallow |
| `EncryptPasswordBeforeRegisteringUserPolicy` | `EncryptPasswordBeforeRegisteringUserPolicyImpl` | `passwordEncoder.encode(dto.password())` |  |
| `NotRequireAuthInWhiteListPolicy` | `NotRequireAuthInWhiteListPolicyImpl` | `SecurityConfig.WHITE_LIST.contains(request URI)` → skip JWT | **Leak**: imports `infra` into `application` |
| `TokenMustExpireInTwoHoursPolicy` | `TokenMustExpiresInTwoHoursPolicyImpl` | `Instant.now().plus(2, CHRONO_HOURS)` |  |

Policies are unit-tested ~75% (3/5 fully). See [business-rules.md](business-rules.md) & [testing.md](testing.md).

---

## 7. Domain Gaps & Roadmap

- Add `balance @Column(precision=19, scale=2)`, `role STRING`, `@Version Long version` for optimistic lock.
- Add `Transaction.idempotencyKey @Column(unique)` + `status` enum.
- Move `User.from(dto)` to `MapStruct UserMapper` (remove `BeanUtils`).
- Add `@CreationTimestamp / @UpdateTimestamp` to both entities; expose DTO not entity (avoid `UserDetails` leakage).

See [data-model.md](data-model.md) for ERD + Flyway steps.

