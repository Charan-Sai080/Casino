# Project Milestones

## Milestone 1: Core Foundation & Identity
- Initialize project structure (Spring Boot / Node / Go - TBD).
- Implement database schemas for accounts, sessions, and wallets.
- Implement anonymous account generation and session cookie management.
- Implement ledger-based virtual wallet (deposits, bets, wins).
- Implement idempotency and double-spend protection (concurrency tests).

## Milestone 2: Game Engine Architecture & Provable Fairness
- Implement secure random number generation (server seed, client seed, nonce).
- Implement fairness verification logic.
- Implement the base GameEngine interface.
- Implement the Dice game engine (simplest state machine).

## Milestone 3: Real-Time WebSockets & Complex Games
- Implement WebSocket server (auth, presence, state sync).
- Implement Blackjack game engine (multi-step state transitions: DEALING, PLAYER_TURN, etc.).
- Ensure server-authoritative state pushes to clients.

## Milestone 4: Distributed Event Processing
- Introduce Message Queue (SQS/RabbitMQ/Kafka).
- Publish domain events (e.g., `GameResolved`).
- Implement background workers (Stats, Leaderboard, Audit).
- Implement Dead Letter Queues and retry logic.

## Milestone 5: Observability & Production Readiness
- Implement rate limiting (Redis).
- Add Prometheus/Grafana metrics.
- Add structured logging and request IDs for tracing.
- Build the Admin Dashboard.
- Final concurrency and integration tests.
