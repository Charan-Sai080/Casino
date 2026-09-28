# Task Board

## ✅ Completed

### Task 1: Milestone 1.1 - Core Entities & JPA Repositories (REVISION 1)
- **Status**: Fixes implemented and verified.

### Task 2: Milestone 1.2 - Distributed Sessions (REVISION 2)
- **Status**: Fixes implemented and verified.

---

## 🔴 Changes Requested (Recursive Loop)

### Task 3: Milestone 1.3 - Wallet Ledger & Pessimistic Locking (REVISION 1)
- **Status**: Rejected by Reviewer. Implementer must fix.
- **Reviewer Feedback to Fix**:
  1. **Null Session**: In `WalletController`, do not inject `HttpSession`. Use `@SessionAttribute(name = "account_id", required = false) String accountIdStr`. If null, return 401 Unauthorized.
  2. **Invalid UUID Cast**: Parse the `accountIdStr` securely using `UUID.fromString(accountIdStr)` rather than a blind `(UUID)` cast.
  3. **BigDecimal NPE**: Validate `amount != null` in `WalletService` before doing `.compareTo()`.
  4. **JPQL ClassCastException**: In `WalletTransactionRepository`, change `COALESCE(SUM(t.amount), 0)` to `COALESCE(SUM(t.amount), 0.0)` so it correctly returns a BigDecimal when empty.
  5. **Exception Handling**: Add `@ResponseStatus(HttpStatus.BAD_REQUEST)` to `InsufficientFundsException`.

---

## 🟢 Ready for Implementation

### Task 4: Milestone 1.4 - Caching & Idempotency Wrappers
- **Depends On**: Task 3
- **Goal**: Optimize read performance and protect against network retries.
- **Requirements**: Implement `@CacheEvict` and Redis Idempotency filter.
