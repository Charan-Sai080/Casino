# Milestone 1: Detailed Plan & Granular Scope
**Phase:** Core Foundation & Identity

## Introduction
Following industry best practices for distributed systems, this milestone is heavily optimized and divided into hyper-granular steps. This ensures an error-free approach by validating each component in isolation before combining them.

---

## 🏗️ Architectural Resolutions (Research-Backed)

1. **Idempotency Strategy (Redis)**: 
   We will store the exact HTTP response in Redis using `idempotency:<uuid>` with a 24-hour TTL. If a duplicate request arrives, Spring Boot will check Redis first and immediately return the cached response, completely bypassing the database and avoiding SQL Constraint violations.
2. **Session Management (Spring Session Data Redis)**:
   Instead of writing custom cookie parsers, we will use the industry-standard `spring-session-data-redis` library. It automatically hijacks Spring's session management, creates cryptographically secure `HttpOnly` cookies, and stores the session mapping perfectly in Redis.
3. **Cache Consistency (Cache-Aside + TTL)**:
   For wallet balances, we will use the "Cache-Aside" pattern. We read from Redis. On a write (a Bet), we update PostgreSQL and immediately trigger an `@CacheEvict` to delete the Redis balance. The next read will safely repopulate the cache from Postgres. A TTL acts as a secondary safety net.

---

## 📋 Granular Milestones

### Milestone 1.1: Core Entities & JPA Repositories
- **Goal**: Establish the raw database layer.
- **Tasks**:
  1. Create `Account` entity (UUID, createdAt).
  2. Create `WalletTransaction` entity (UUID, account_id, amount, type, idempotency_key).
  3. Create standard JPA Repositories for both.
  4. Write a unit test ensuring the tables are auto-generated.

### Milestone 1.2: Distributed Sessions
- **Goal**: Implement anonymous identity.
- **Tasks**:
  1. Configure `spring-session-data-redis` in `application.properties`.
  2. Create the `POST /api/sessions` controller.
  3. Logic: Create a new `Account` in Postgres, save the `account_id` in the `HttpSession`, and let Spring Session automatically return the secure cookie.

### Milestone 1.3: Wallet Ledger & Concurrency
- **Goal**: Safely handle money.
- **Tasks**:
  1. Implement `WalletService.calculateBalance(UUID accountId)`.
  2. Implement `WalletService.processTransaction(accountId, amount)`.
  3. Add `@Lock(LockModeType.PESSIMISTIC_WRITE)` to ensure the velvet-rope concurrency protection.
  4. Write a multithreaded test hitting `processTransaction` 50 times simultaneously to prove the DB rejects double-spending.

### Milestone 1.4: Caching & Idempotency Wrappers
- **Goal**: Optimize for speed and network failures.
- **Tasks**:
  1. Add `@Cacheable` to `calculateBalance` and `@CacheEvict` to `processTransaction`.
  2. Create an `IdempotencyFilter` that intercepts requests, checks Redis for the `Idempotency-Key` header, and either returns the cached response or proceeds to the controller and caches the new result.
