# Assumptions

1. The tech stack for the backend is still loosely defined, but based on previous parent folder context, it is likely **Java/Spring Boot**, though `requirements.md` leaves it open to any language that supports this architecture. (Requires user confirmation).
2. The infrastructure (PostgreSQL, Redis, Message Queue) will run in Docker Compose for local development.
3. The frontend is considered a "thin client" and is secondary to the backend architecture. The focus is on the backend engineering.
4. "Virtual Credits" means we do not need to integrate with Stripe, PayPal, or any actual fiat/crypto payment processor.
