# CasinoCore: Distributed Backend Simulation

Welcome to **CasinoCore**, a production-grade, distributed backend engineering showcase.

> **Disclaimer**: This is a pure technology simulation project. It uses **virtual credits only**. There is no real-money gambling, no payment processing, and no conversion of virtual credits into monetary value. It is designed strictly to demonstrate advanced backend engineering patterns.

## 🎯 Project Vision
CasinoCore is a virtual multiplayer casino backend designed to highlight high-concurrency transaction management, real-time state synchronization, and event-driven architecture. 

The core philosophy of this project is: **Simple for the user, sophisticated underneath.** Users receive an anonymous, persistent identity immediately upon connecting, completely removing friction while the backend seamlessly manages distributed state, wallets, and cryptographically provable fairness.

## 🏗️ Architecture & Tech Stack
This project follows a modern microservice-style distributed architecture.

* **Language/Framework**: Java & Spring Boot
* **Primary Database**: PostgreSQL (Source of truth, ledger-based virtual wallets, strict transactional boundaries)
* **In-Memory Cache**: Redis (Ephemeral state, rate limiting, rapid session lookups)
* **Message Broker**: RabbitMQ (Event-driven architecture for asynchronous tasks like statistics and leaderboards)
* **Real-Time Communication**: WebSockets (Server-authoritative game state synchronization)
* **Infrastructure**: Docker & Docker Compose

## 🚀 Key Engineering Highlights
1. **Ledger-Based Wallets**: Virtual wallets are not simple `balance = x` columns. They are append-only transaction ledgers designed to withstand high concurrency and prevent double-spending using strict database row-locking.
2. **Idempotency**: All financial-style operations enforce idempotency to protect against network retries and duplicate messages.
3. **Provable Fairness**: Uses cryptographic commitments (Server Seed, Client Seed, Nonce) to generate deterministic, independently verifiable game results.
4. **Server-Authoritative State Machines**: The backend strictly manages game state transitions (e.g., `BETTING` -> `DEALING` -> `PLAYER_TURN`). The frontend is merely a presentation layer.
5. **Event-Driven Processing**: Non-critical paths (audit logs, leaderboards) are decoupled from the main request thread using message queues to ensure low API latency.

## 🛠️ Getting Started (Local Development)

### 1. Start the Infrastructure
The project relies on Docker to manage external dependencies.
```bash
docker compose up -d
```
This will start PostgreSQL (5432), Redis (6379), and RabbitMQ (5672/15672).

### 2. Run the Application
*(Instructions coming soon once the Spring Boot application is initialized)*
