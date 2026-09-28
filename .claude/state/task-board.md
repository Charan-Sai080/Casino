# Task Board

## 🟢 Ready for Implementation

### Task 1: Milestone 1.1 - Core Entities & JPA Repositories
- **Goal**: Establish the Postgres database schema using JPA Entities.
- **Requirements**:
  - Use `UUID` for primary keys to prevent ID enumeration.
  - Create `Account.java` (id, createdAt).
  - Create `WalletTransaction.java` (id, account_id, amount, transaction_type, idempotency_key, createdAt).
  - Create `AccountRepository.java` and `WalletTransactionRepository.java`.
  - Use `@Entity`, `@Table`, and standard Lombok annotations for clean code.
  - Create a basic test to ensure the Spring Application Context loads and connects to the database successfully.

---

## ⏳ Blocked (Waiting on Dependencies)

### Task 2: Milestone 1.2 - Distributed Sessions
- **Depends On**: Task 1
- **Goal**: Implement anonymous identity via Spring Session and Redis.
- **Requirements**: Configure `spring-session-data-redis` and create `SessionController` to return the `HttpOnly` cookie.

### Task 3: Milestone 1.3 - Wallet Ledger & Pessimistic Locking
- **Depends On**: Task 2
- **Goal**: Safely process wallet transactions.
- **Requirements**: Implement `WalletService` with `@Lock(LockModeType.PESSIMISTIC_WRITE)`.

### Task 4: Milestone 1.4 - Caching & Idempotency Wrappers
- **Depends On**: Task 3
- **Goal**: Optimize read performance and protect against network retries.
- **Requirements**: Implement `@CacheEvict` and Redis Idempotency filter.
