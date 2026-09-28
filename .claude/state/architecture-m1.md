# Milestone 1: Architecture & Contracts

## 1. Concurrency & Performance Strategy
**Decision 1: Caching Layer**
To prevent expensive `SUM(amount)` queries on the `wallet_transactions` ledger for users with thousands of bets, we will use **Redis** as a caching layer. 
- When a user requests their balance, we check Redis first (O(1) time).
- If there is a cache miss, we calculate the sum from PostgreSQL, and save it in Redis.
- Every time a new transaction occurs, we invalidate or update the Redis cache.

**Decision 2: Pessimistic Locking**
To prevent double-spending, we will use PostgreSQL's `SELECT ... FOR UPDATE` (Pessimistic Locking). 
- In Spring Data JPA, this is achieved using the `@Lock(LockModeType.PESSIMISTIC_WRITE)` annotation on the Repository method.
- This guarantees strict, sequential processing of wallet transactions, which is standard for financial-grade systems.

## 2. Database Schema (PostgreSQL)

### Table: `accounts`
- `id` (UUID, Primary Key)
- `created_at` (Timestamp)

### Table: `wallet_transactions`
- `id` (UUID, Primary Key)
- `account_id` (UUID, Foreign Key -> accounts.id)
- `amount` (Decimal/Numeric) - Positive for credits, negative for debits.
- `transaction_type` (String) - e.g., 'INITIAL_GRANT', 'BET', 'WIN'
- `idempotency_key` (String, UNIQUE constraint per account)
- `created_at` (Timestamp)

## 3. API Contracts

### A. Create Anonymous Session
**Request**: `POST /api/sessions`
**Response**: 
- Headers: `Set-Cookie: session_token=<UUID>; HttpOnly; Secure; SameSite=Strict`
- Body: 
```json
{
  "account_id": "uuid",
  "balance": 10000.00
}
```
*Behind the scenes*: Generates the Account, gives them 10,000 credits via the Ledger, and caches the balance in Redis.

### B. Place a Bet (Test Endpoint for M1)
**Request**: `POST /api/wallet/bet`
**Headers**: `Idempotency-Key: <unique-string-from-client>`
**Body**:
```json
{
  "amount": 500.00
}
```
**Response**: 200 OK
```json
{
  "transaction_id": "uuid",
  "new_balance": 9500.00
}
```
*Behind the scenes*: Uses Pessimistic locking. Checks Redis for balance (or DB if miss). Inserts a negative transaction. Updates Redis. If `Idempotency-Key` exists, returns the previous result without charging again.
