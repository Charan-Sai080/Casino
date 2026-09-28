# Task Board

## ✅ Completed

## 🔴 Changes Requested (Recursive Loop)

### Task 1: Milestone 1.1 - Core Entities & JPA Repositories (REVISION 1)
- **Status**: Rejected by Reviewer. Implementer must fix.
- **Reviewer Feedback to Fix**:
  1. Remove `@Data` from Entities to avoid `equals/hashCode` lazy-loading crashes. Use `@Getter`, `@Setter`, and `@EqualsAndHashCode(onlyExplicitlyIncluded = true)`.
  2. Add strict `@Column` constraints to `WalletTransaction.amount` (precision=19, scale=4, nullable=false).
  3. Add `@Column(nullable = false)` to `transactionType` and `account_id`.
  4. Add `@Column(unique = true)` to `idempotencyKey` to enforce DB-level idempotency protection.
  5. Add `@CreationTimestamp` and `@Column(updatable = false)` to `createdAt` in all entities.
  6. *Note*: Disregard the Reviewer's request for an `Account.balance` field. As per the Architect's decision, the balance is a projection stored in Redis, not a hard column on the Account table.

## 🟢 Ready for Implementation

### Task 2: Milestone 1.2 - Distributed Sessions
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
