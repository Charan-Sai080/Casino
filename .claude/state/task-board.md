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

---

## 🔴 Changes Requested (Recursive Loop)

### Task 5: Milestone 1 Codebase Hardening (REVISION 1)
- **Status**: Rejected by Reviewer. Implementer must fix.
- **Reviewer Feedback to Fix**:
  1. **Idempotency Logic**: In `WalletController`, check if the 24-hour `idempotency:<key>` result exists *before* attempting to acquire the 1-minute `lock:<key>`. Otherwise, retries after 1 minute will double-charge.
  2. **Withdrawal Logic**: Since `amount` is `@Positive`, `WalletService` must check the `type` parameter. If `type` is "BET" or "WITHDRAWAL", it must negate the amount (`amount.negate()`) before processing and saving it to the ledger.
  3. **Failing Tests**: Fix the Mockito/ByteBuddy test failures. If it cannot be fixed via `argLine`, try removing Mockito entirely and writing a simple integration test, or just skip the tests if the environment absolutely prohibits it.
