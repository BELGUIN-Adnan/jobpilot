# JobPilot 🧭

A Java console app to track job applications: add the offers you applied to, follow each
application's status (applied → interview → offer / rejected), and see statistics.

## Features (v1)

- Add an application (title, company, city, salary; 0 = unknown salary)
- List all applications
- Change an application's status, with rules: applied → interview → offer, or rejected at any step; no going back
- Filter applications by status (applied, interview, offer, rejected)
- Statistics: count per status, response rate, average known salary, best offer
- Duplicates are refused (same title, company and city, ignoring case)
- The menu never crashes on wrong input (letters, unknown ids, unknown statuses)

## How to run

- requirements: Java 25.
- run the JobPilotApp.java.
- this one for tests only JobPilotTests.java.

## Design

- `Status` is an **enum**: a fixed list of choices. A typo like `APLIED` is a compile error, not a hidden bug.
  It also owns the rule for which moves are allowed (`canMoveTo`).
- `JobOffer` is a **record**: the offer's details don't change once created, and Java generates
  `equals`, `hashCode` and `toString`.
- `Application` is a **class**: its status changes over time, but only through `moveTo`, which refuses
  illegal moves (no `setStatus`).
- `ApplicationTracker` stores the applications and calculates the statistics. It never prints:
  only `JobPilotApp` does. In v4, a REST API will replace the menu, and the tracker won't change.

## Tests

47 tests, written with a small `check(...)` method (JUnit comes in v2). They cover:

- the status rules (allowed moves, no going back, nothing after a final status)
- the cleaning in `JobOffer` (spaces, negative salary)
- duplicates, no capacity limit (50 000 offers), unknown ids
- the statistics, including the count by status and an empty tracker

## Roadmap

- [x] v1: console app (OOP: enum, record, encapsulation)
- [ ] v2: collections, exceptions, JUnit tests
- [ ] v3: database (PostgreSQL)
- [ ] v4: REST API with Spring Boot
- [ ] v5: AI features with Spring AI