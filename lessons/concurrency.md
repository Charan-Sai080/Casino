# Concurrency in Distributed Systems

## 1. Introduction
**What is Concurrency?**
Concurrency is when multiple things happen at exactly the same time. In a distributed backend, this means two different users (or the same user pressing a button twice) sending requests at the exact same millisecond, which are handled by two different servers simultaneously.

**The Problem (The Double-Spend):**
Imagine a user has $100 in their virtual wallet. They quickly press "Bet $100" twice.
1. Spring Boot Instance A receives Bet 1. It checks the DB: Balance is $100.
2. Spring Boot Instance B receives Bet 2 at the same millisecond. It checks the DB: Balance is $100.
3. Instance A subtracts $100, saves $0 to the DB, and allows the game.
4. Instance B subtracts $100, saves $0 to the DB, and allows the game.

The user just placed $200 worth of bets with only $100. The system was exploited.

---

## 2. How We Solve It
We **never** try to solve this in the memory of the Java application (e.g., using Java's `synchronized` keyword). Why? Because in production, you have 50 different Spring Boot containers running on 50 different servers. They cannot share Java memory. 

Instead, **we push the problem down to the Database.**

There are two primary ways to handle this in PostgreSQL + Spring Boot:

### Approach A: Pessimistic Locking (The Bouncer)
We tell the database to literally lock the row.
- **How it works:** When Instance A reads the wallet, it sends a special SQL command: `SELECT * FROM wallets WHERE id = 1 FOR UPDATE;`. 
- PostgreSQL acts like a bouncer. It grabs the row for Instance A and puts up a velvet rope. 
- When Instance B tries to read the wallet 1 millisecond later, PostgreSQL forces Instance B to wait in line until Instance A finishes its transaction and commits. 
- **Pros/Cons:** 100% safe, but it can slow down the system if thousands of people are trying to access the same row simultaneously.

### Approach B: Optimistic Locking (The Version Check)
We add a `version` column to our database table.
- **How it works:** Instance A and Instance B both read the wallet: Balance $100, `version = 1`.
- Instance A subtracts $100 and sends the update: `UPDATE wallets SET balance = 0, version = 2 WHERE id = 1 AND version = 1;`
- The database successfully updates 1 row.
- A millisecond later, Instance B sends its update: `UPDATE wallets SET balance = 0, version = 2 WHERE id = 1 AND version = 1;`
- The database looks for `version = 1`, but it doesn't exist anymore! It returns "0 rows updated". 
- Spring Boot sees "0 rows updated" and throws an `OptimisticLockException`. We catch that error, cancel the bet, and tell the user "Transaction failed, please try again."
- **Pros/Cons:** Much faster because there is no waiting in line. Best for high-performance systems where conflicts are rare.

---

## 3. Interview Cheat Sheet
**Question:** *How do you prevent race conditions or double-spending when running multiple instances of a microservice?*

**Answer:** *You cannot use application-level locks like Java's `synchronized` because the instances don't share memory. You must use database-level locking. Depending on the read/write ratio, I would use either Pessimistic Locking (`SELECT FOR UPDATE`) to strictly serialize access, or Optimistic Locking (using a `@Version` column in JPA) to allow high throughput and fail fast upon a conflict.*
