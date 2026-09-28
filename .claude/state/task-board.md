# Task Board

## ✅ Completed

### Task 1: Milestone 1.1 - Core Entities & JPA Repositories (REVISION 1)
- **Status**: Fixes implemented and verified.

## 🔴 Changes Requested (Recursive Loop)

### Task 2: Milestone 1.2 - Distributed Sessions (REVISION 2)
- **Status**: Rejected by Reviewer. Implementer must fix.
- **Reviewer Feedback to Fix**:
  1. Fix the `IllegalStateException` in `SessionController`. Do not call `request.changeSessionId()` unconditionally if the session doesn't exist yet. Create the session first with `request.getSession(true)` and then change the ID, or wrap it in a conditional check.
  2. Fix `pom.xml`: Remove the 6 duplicated `spring-boot-starter-test` dependencies.

## 🟢 Ready for Implementation

### Task 3: Milestone 1.3 - Wallet Ledger & Pessimistic Locking
- **Depends On**: Task 2
- **Goal**: Safely process wallet transactions.
- **Requirements**: Implement `WalletService` with `@Lock(LockModeType.PESSIMISTIC_WRITE)`.

### Task 4: Milestone 1.4 - Caching & Idempotency Wrappers
- **Depends On**: Task 3
- **Goal**: Optimize read performance and protect against network retries.
- **Requirements**: Implement `@CacheEvict` and Redis Idempotency filter.
