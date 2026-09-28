# Task Board

## ✅ Completed

### Task 1: Milestone 1.1 - Core Entities & JPA Repositories (REVISION 1)
- **Status**: Fixes implemented and verified.

### Task 2: Milestone 1.2 - Distributed Sessions (REVISION 2)
- **Status**: Fixes implemented and verified.

### Task 3: Milestone 1.3 - Wallet Ledger & Pessimistic Locking (REVISION 1)
- **Status**: Fixes implemented and verified.

### Task 4: Milestone 1.4 - Caching & Idempotency Wrappers (REVISION 1)
- **Status**: Fixes implemented and verified.

## 🔴 Changes Requested (Recursive Loop)

### Task 5: Milestone 1 Codebase Hardening
- **Status**: Rejected by Reviewer. Implementer must fix.
- **Reviewer Feedback to Fix**:
  1. **Idempotency Race Condition**: In `WalletController`, replace `get` and `set` with `setIfAbsent` (SETNX) to atomically lock the key and prevent `DataIntegrityViolationException`.
  2. **Global Exception Handler**: Create a `@ControllerAdvice` class to properly map `IllegalArgumentException` and `DataIntegrityViolationException` to 400 Bad Request.
  3. **Spring AOP Proxy Bypass**: Do not call `getBalance` from within `processTransaction` in the same class (bypasses `@Cacheable`). Instead, inject a `self` reference or restructure to ensure the cache proxy is hit.
  4. **Request Validation**: Add `@Valid`, `@NotNull`, and `@Positive` constraints to `TransactionRequest` in the controller.
  5. **Missing Tests**: Write a basic WebMvcTest or SpringBootTest for `WalletController`.

---
