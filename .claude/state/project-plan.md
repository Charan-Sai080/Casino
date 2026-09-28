# CasinoCore Project Plan

## Overview
CasinoCore is a virtual casino simulation platform showcasing production-grade distributed backend engineering. The system allows users to play games (Blackjack, Roulette, Dice, Baccarat) using virtual credits via anonymous, automatically generated sessions.

## Project Classification
**Large / Enterprise** - Requires a distributed architecture (PostgreSQL, Redis, background workers, SQS/SNS style event queue, WebSockets).

## Operational Complexity
High. Requires:
- Transactional integrity for wallet operations (idempotency, double-spend protection).
- Real-time WebSocket game state synchronization.
- Event-driven background processing (SNS/SQS).
- Cryptographically secure fairness verification.
- Rate limiting and Redis session state.
- Dockerized deployment with multiple services (Backend, Frontend, Workers).

## Implementation Strategy
1. **Infrastructure & Persistence Layer**: Set up PostgreSQL, Redis, and base Docker environment.
2. **Domain Layer (Core)**: Implement Account, Session, and Wallet (Ledger) systems with strict transactional guarantees.
3. **Event Architecture**: Implement the event publisher and background worker consumers.
4. **Game Engines**: Implement the state machines for the games, starting with a simple game like Dice, then moving to Blackjack.
5. **Real-time Layer**: Implement WebSocket servers for multiplayer and live state sync.
6. **Observability & Polish**: Implement metrics, logging, tracing, and the Admin Dashboard.
