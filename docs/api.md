# API Reference — PicPay Payment

> **Base:** `http://localhost:8080` · **Content:** `application/json` · **Auth:** `Authorization: Bearer <JWT>` unless whitelisted · **Source:** `application/controllers/**` · `domain/dto/**` · `infra/config/security/SecurityConfig.java`

Swagger interactive: `http://localhost:8080/swagger-ui/index.html` · OpenAPI JSON: `http://localhost:8080/v3/api-docs` (springdoc 2.2.0). **Remove springfox 3.0.0** before exposing prod.

---

## 1. Auth Matrix

| Need JWT? | Paths | Config |
|---|---|---|
| **No** (whitelist) | `/`, `/auth/login`, `/v3/api-docs/**`, `/api-docs.yaml`, `/swagger-resources/**`, `/swagger-ui/**`, `/swagger-ui.html` | `SecurityConfig.WHITE_LIST` (9 entries) `permitAll()` |
| **Yes** | everything else → `authenticated()` | `JWTAuthMiddleware` before `UsernamePasswordAuthenticationFilter` |
| **Extra RBAC** | `GET /users` → `hasAnyRole(USER,ADMIN)`; `POST /auth/register` → `hasAuthority(CREATE_USER)` (ADMIN only); `POST /transactions` → `EXECUTE_TRANSACTION`; `GET /transactions` → `READ_TRANSACTION` | `authorizeHttpRequests` line 37-48 |

Logout: `POST /auth/logout` → `SecurityContextHolder.clearContext()`.

---

## 2. Endpoints Catalog

### 2.1 Auth — `application/controllers/auth/AuthenticationController.java`

#### `POST /auth/login` — public

```http
POST /auth/login HTTP/1.1
Content-Type: application/json

{ "email": "alice@example.com", "password": "alice123" }
```

- **DTO:** `LoginDTO(@Email String email, @NotBlank String password)` via `AuthenticateUserUseCase` → `DaoAuthenticationProvider` (BCrypt).
- **Success 200:** `LoginResponseDTO { String token }` — JWT HMAC256, 2h expiry (see `security.md`). Token placed in `Authorization: Bearer`.
- **Errors:** `401 Unauthorized` bad credentials (`UsernameNotFoundException`/`BadCredentialsException` → `ControllerExceptionHandler`).
- **Example:**

```bash
curl -s -X POST http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"alice@example.com","password":"alice123"}' | jq
# {"token":"eyJhbGciOiJIUzI1NiIs..."}
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login -H 'Content-Type: application/json' -d '{"email":"alice@example.com","password":"alice123"}' | jq -r .token)
```

#### `POST /auth/register` — ADMIN only (`CREATE_USER`)

```http
POST /auth/register HTTP/1.1
Authorization: Bearer <ADMIN_JWT>
Content-Type: application/json

{
  "firstName":"Bob","lastName":"Buyer",
  "email":"bob@example.com","document":"12345678901",
  "balance":100.00,"password":"bob123",
  "userType":"COMMON","role":"USER"
}
```

- **DTO:** `UserDTO` record — `@NotBlank @Email @Size(min=3) @NotNull`. Validation errors → `400`.
- **Policy:** `EncryptPasswordBeforeRegisteringUserPolicy` BCrypt encodes before `SaveUserUseCase`.
- **Success 201/200:** persisted `User` JSON (no password returned? currently leaks — should omit).
- **Errors:** `403` if caller not ADMIN; `400` duplicate email/document (DB UK violation → 500 currently → should map 409).

```bash
curl -s -X POST http://localhost:8080/auth/register \
  -H "Authorization: Bearer $ADMIN_TOKEN" -H 'Content-Type: application/json' \
  -d '{"firstName":"Bob","lastName":"Buyer","email":"bob@example.com","document":"12345678901","balance":100,"password":"bob123","userType":"COMMON","role":"USER"}' | jq
```

#### `POST /auth/logout` — authenticated

```http
POST /auth/logout
Authorization: Bearer <JWT>
```
Clears `SecurityContextHolder`. No token blacklist — JWT remains valid until 2h expiry (stateless limitation).

### 2.2 Users — `application/controllers/UserController.java`

#### `GET /users` — `hasAnyRole(USER,ADMIN)`

```http
GET /users
Authorization: Bearer <JWT>
Accept: application/json
```

- **Success 200:** `List<User>` — **returns all** (no paging, no filter). Consider `Page<User>` with `Pageable`.
- **Errors:** `401` missing JWT, `403` insufficient role.

```bash
curl -s http://localhost:8080/users -H "Authorization: Bearer $TOKEN" | jq
```

> **Leak note:** Returns entity `User` including `password` hash + `balance`. Should return projection `UserDTO` without password.

### 2.3 Transactions — `application/controllers/TransactionController.java`

#### `POST /transactions` — `hasAuthority(EXECUTE_TRANSACTION)` (both COMMON & ADMIN via USER)

```http
POST /transactions
Authorization: Bearer <JWT>
Content-Type: application/json

{ "value": 25.50, "senderId": "aaaaaaaa-bbbb-...-...", "receiverId": "cccccccc-dddd-...-..." }
```

- **DTO:** `TransactionDTO(@NotNull BigDecimal value, @NotNull UUID senderId, @NotNull UUID receiverId)` — no `@Positive` so zero/negative passes validation.
- **Flow (happy):** `find sender → validate(R1 merchant, R2 balance, R3 authorizer Mocky) → find receiver → TransactionUseCase.execute() → save tx + update balances → notify`.
- **Success 200:** `Transaction {id, amount, sender:{…}, receiver:{…}, createdAt}`.
- **Errors:** `403 Merchant User is not authorized to transact`; `400 Balance is not enough`; `403 Transaction Not Authorized, try again later...` (from `TransactionServiceImpl.validate()` throwing generic `Exception` — maps to 500 now, should be 4xx).

```bash
curl -s -X POST http://localhost:8080/transactions \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d '{"value":10.00,"senderId":"<COMMON_UUID>","receiverId":"<MERCHANT_UUID>"}' | jq
```

**Failure catalog:**

```bash
# merchant as sender
curl -s -X POST http://localhost:8080/transactions -H "Authorization: Bearer $TOKEN" \
  -d '{"value":10,"senderId":"<MERCHANT_UUID>","receiverId":"<COMMON_UUID>"}' # 403 Merchant...

# insufficient funds
curl -s -X POST http://localhost:8080/transactions -H "Authorization: Bearer $TOKEN" \
  -d '{"value":9999,"senderId":"<COMMON_UUID>","receiverId":"<MERCHANT_UUID>"}' # 400 Balance...

# unauthorized by Mocky (simulate by stubbing auth URL down)
curl -v # → 403 Transaction Not Authorized
```

#### `GET /transactions` — `hasAuthority(READ_TRANSACTION)`

```http
GET /transactions
Authorization: Bearer <JWT>
```

- **Success 200:** `List<Transaction>` (all).

```bash
curl -s http://localhost:8080/transactions -H "Authorization: Bearer $TOKEN" | jq '.[].amount'
```

### 2.4 Misc

| Path | Method | Notes |
|---|---|---|
| `/` | GET | `AppController` root, whitelisted, 200 |

---

## 3. RBAC & Permissions Detail

`domain/entities/auth/Permissions.java` → `Role.java`

| Role | Permissions (granted as authorities) | Effective |
|---|---|---|
| **USER** | `READ_USER`, `EXECUTE_TRANSACTION`, `READ_TRANSACTION` | COMMON default |
| **ADMIN** | USER + `DELETE_USER`, `CREATE_USER`, `UPDATE_USER` | Full |

`getAuthorities()` adds both `permission.getPermission()` and `ROLE_USER/ROLE_ADMIN`. So `hasRole('USER')` and `hasAuthority('transaction:execute')` both work.

**Endpoint enforcement:**

```java
.requestMatchers(GET, "/users").hasAnyRole(USER.name(), ADMIN.name())
.requestMatchers(POST, "/auth/register").hasAuthority(CREATE_USER.getPermission())
.requestMatchers(POST,"/transactions").hasAuthority(EXECUTE_TRANSACTION.getPermission())
.requestMatchers(GET, "/transactions").hasAuthority(READ_TRANSACTION.getPermission())
.anyRequest().authenticated()
```

---

## 4. Error Contract

`infra/middleware/error/ControllerExceptionHandler.java` → `ExceptionDTO { String message }` + HTTP status. Current: `MethodArgumentNotValidException` → 400 with field errors; generic `Exception` from `validate()` bubbles as 500 (should map to 400/403). DTO validation errors are per-field.

```json
// 400 validation
{"message":"firstName: must not be blank"}
// 403 merchant
{"message":"Merchant User is not authorized to transact"}
```

Add: `DataIntegrityViolationException` → 409 (duplicate email/document), `AccessDeniedException` → 403.

---

## 5. OpenAPI

- **springdoc 2.2.0** auto-generates at `/v3/api-docs` (JSON) + `/v3/api-docs.yaml` + Swagger UI at `/swagger-ui/index.html`.
- `SwaggerConfig.java` is **disabled** (springfox) — delete file + deps.
- No manual `@Operation` annotations — add for richer docs.

Screenshot slot: `docs/assets/screenshots/swagger-ui.png`.

---

## 6. Curl Playbook (end-to-end)

```bash
BASE=http://localhost:8080

# 1) ensure admin exists (bootstrap via SQL: INSERT ... role='ADMIN', userType='COMMON', password BCrypt of 'admin123')
# or create first user via direct repo then promote

# 2) login as admin
ADMIN_TOKEN=$(curl -s -X POST $BASE/auth/login -H 'Content-Type: application/json' \
  -d '{"email":"admin@picpay.com","password":"admin123"}' | jq -r .token)

# 3) create two COMMON + one MERCHANT
curl -s -X POST $BASE/auth/register -H "Authorization: Bearer $ADMIN_TOKEN" -H 'Content-Type: application/json' \
  -d '{"firstName":"Alice","lastName":"A","email":"alice@example.com","document":"11111111111","balance":500,"password":"alice123","userType":"COMMON","role":"USER"}' | jq
curl -s -X POST $BASE/auth/register -H "Authorization: Bearer $ADMIN_TOKEN" -H 'Content-Type: application/json' \
  -d '{"firstName":"Bob","lastName":"B","email":"bob@example.com","document":"22222222222","balance":100,"password":"bob123","userType":"COMMON","role":"USER"}' | jq
curl -s -X POST $BASE/auth/register -H "Authorization: Bearer $ADMIN_TOKEN" -H 'Content-Type: application/json' \
  -d '{"firstName":"Shop","lastName":"Lda","email":"shop@example.com","document":"33333333333","balance":0,"password":"shop123","userType":"MERCHANT","role":"USER"}' | jq

# 4) login as alice (COMMON)
TOKEN=$(curl -s -X POST $BASE/auth/login -H 'Content-Type: application/json' \
  -d '{"email":"alice@example.com","password":"alice123"}' | jq -r .token)

# 5) resolve UUIDs
USERS=$(curl -s $BASE/users -H "Authorization: Bearer $TOKEN")
ALICE_ID=$(echo $USERS | jq -r '.[] | select(.email=="alice@example.com") | .id')
SHOP_ID=$(echo $USERS | jq -r '.[] | select(.email=="shop@example.com") | .id')

# 6) transact
curl -s -X POST $BASE/transactions -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d "{\"value\":50.00,\"senderId\":\"$ALICE_ID\",\"receiverId\":\"$SHOP_ID\"}" | jq

# 7) verify
curl -s $BASE/transactions -H "Authorization: Bearer $TOKEN" | jq

# 8) OpenAPI
curl -s $BASE/v3/api-docs | jq '.info'
open $BASE/swagger-ui/index.html
```

---

## 7. See Also

- [Security](security.md) — JWT & filter chain
- [Business Rules](business-rules.md) — validation logic behind 4xx
- [Configuration](configuration.md) — base URL & profiles
