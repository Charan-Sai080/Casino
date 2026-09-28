# Project Risks

1. **Concurrency Bugs**: Double-spending is a major risk in a virtual wallet. We must use robust row-level locking or optimistic concurrency control, and prove it with aggressive concurrency tests.
2. **State Machine Desync**: Game state on the server getting out of sync with WebSocket clients, especially during reconnects.
3. **Event Delivery Failures**: Workers failing to process events, resulting in stale leaderboards or missing audit logs. (Mitigated by Dead Letter Queues and idempotent consumers).
4. **Seed Exposure**: Unintentionally exposing the unhashed server seed to the client before a round is completed, which breaks the provable fairness guarantee.
