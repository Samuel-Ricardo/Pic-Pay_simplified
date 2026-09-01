# Docs Hub — PicPay Payment

> **14 guides + 1 interactive diagram** · Progressive disclosure: 30s README → 5-min Quick Start → 2-hour deep dive · **Stack:** Java 17 · Spring Boot 3.1.5 · PostgreSQL · JWT 4.4.0

---

## 🚀 Start Here

| I want to… | Go to |
|---|---|
| **Run in 5 min** (Docker, .env, smoke test) | [`../README.md#quick-start-5-min`](../README.md#-quick-start--5-min) + [Configuration](configuration.md) |
| **Explore architecture** (C4, sequences, hotspot map) | [Interactive Diagrams](diagrams/architecture.html) · [Architecture](architecture.md) |
| **Call the API** (curl, RBAC, Swagger) | [API Reference](api.md) |
| **Understand the domain** (challenge, invariants) | [Domain](domain.md) · [Business Rules](business-rules.md) |
| **Audit security** (JWT, filter chain, gaps) | [Security](security.md) |
| **Work with data** (ERD, JPA, migrations) | [Data Model](data-model.md) |
| **Deploy** (compose, Dockerfile, CI) | [Deployment](deployment.md) |
| **Develop** (setup, conventions, troubleshooting) | [Development](development.md) |
| **Ship quality** (coverage, JaCoCo gate) | [Testing](testing.md) · [Code Quality](code-quality.md) |
| **Decide** (ADRs, upgrade path to Boot 4 / Java 25) | [Decisions](decisions.md) · [Tech Stack](tech-stack.md) |

<p align="center">
  <a href="diagrams/architecture.html"><b>▶ Open Interactive Architecture (light/dark, SVG export)</b></a>
</p>

---

## 📚 Full Catalog

| # | Guide | What’s inside | Deep audit source |
|---|---|---|---|
| 0 | [README](../README.md) (root) | Badges, TOC, 7-section overview, 5-min run, curl, mermaid mini-map | All 6 specialists consolidated |
| 1 | [Architecture](architecture.md) | Style (pseudo-hexagonal), C4 L1/L2/L3, auth + tx sequences, ERD, deployment, hotspot map | wilson-architect + Atlas |
| 2 | [Tech Stack](tech-stack.md) | 18-dep table with verdicts, springfox debt, Boot/Java EOL, migration path | tiago-dev |
| 3 | [Domain](domain.md) | Product vision (PicPay challenge), actors (COMMON/MERCHANT), entities, invariants, lifecycle | maria-analyst + joao-pm |
| 4 | [Data Model](data-model.md) | ERD + JPA annotations, UKs, gaps (ddl-auto, ORDINAL, scale, locking), migration sketch | tiago-dev + wilson |
| 5 | [API Reference](api.md) | 8 endpoints + DTOs + RBAC matrix + error contract + curl playbook + OpenAPI | tiago-dev |
| 6 | [Security](security.md) | JWT HMAC256 2h flow, filter chain, whitelist, Permissions/Roles, threat model | tiago-dev + Atlas |
| 7 | [Business Rules](business-rules.md) | R1–R7 invariants, state diagram, race/locking, notification & Mocky gaps | maria-analyst |
| 8 | [Configuration](configuration.md) | Env vars (`.env`/`.env.test`), `application.properties`, compose wiring, secrets & profiles | Atlas-DevOps |
| 9 | [Deployment](deployment.md) | Quick start, Dockerfile multistage, compose deep dive, CI (`docker-publish.yml`) gaps, health | Atlas-DevOps |
| 10 | [Testing](testing.md) | 13-file inventory, coverage heatmap 12–18%, anti-patterns, JaCoCo 80% gate, test matrix | carla-qa |
| 11 | [Code Quality](code-quality.md) | Scorecard, P0 critical 14-item fix list, smells/refactors, Bradesco gap, lint gates | tiago-dev + carla |
| 12 | [Decisions (ADRs)](decisions.md) | 8 ADRs (hexagonal, Boot/JWT/springfox/ddl/RestTemplate/policy, upgrade path) | wilson-architect |
| 13 | [Development](development.md) | Prerequisites, setup, run modes, structure, conventions, debugging, contrib checklist | paige-tech-writer |
|  | [Diagrams](diagrams/architecture.html) | Explorable HTML+SVG: C4, sequences, ERD, lifecycle, deployment — trace motion, PNG/JPEG/WebP/SVG/WebM export | archify |
|  | [Tech Stack](tech-stack.md) | (also #2, listed once) | — |

**Global fallback KB:** `~/.config/opencode/avanade-method/` · **Project KB:** `.avanade-method/` (if present)

---

## 🗺 Journey Map (Diátaxis)

| Mode | Guides | Audience |
|---|---|---|
| **Tutorial** (learn by doing) | README Quick Start → Configuration → API curl playbook | New contributor |
| **How-to** (solve a task) | Deployment · Development · API · Configuration | Dev / DevOps |
| **Reference** (look up) | Tech Stack · Data Model · API · Configuration · Decisions | Senior reviewer |
| **Explanation** (understand) | Architecture · Domain · Business Rules · Security · Code Quality · Testing | Architect / interviewer |

---

## ✅ Quality Gates (Avanade Method)

| Gate | Artifact | Status |
|---|---|---|
| Discovery → Planning | Brief approved | See [Domain](domain.md) vision |
| Planning → Solutioning | PRD validated | See [Business Rules](business-rules.md) invariants |
| Solutioning → Implementation | ADRs + INVEST stories | See [Decisions](decisions.md) + [Architecture](architecture.md) |
| Implementation → Review | Tests 80%+ QA passed | See [Testing](testing.md) — currently **FAIL** (15% → 80% plan inside) |
| Review → Documentation | All gates green | This hub — **14 guides + interactive diagram** |

Bradesco standards enforced: **Java 25 · Spring Boot 4 · hexagonal/MVC · `EnumType.STRING` · Flyway · JaCoCo 80%** — gaps flagged with fix in each guide.

---

## 🔗 Cross-Link Quick Reference

```
README.md ─┬─→ api.md ──→ security.md ──→ business-rules.md
           ├─→ architecture.md ──→ data-model.md ──→ configuration.md ──→ deployment.md
           ├─→ domain.md ──→ business-rules.md
           ├─→ testing.md ⇄ code-quality.md
           └─→ decisions.md ⇄ tech-stack.md
                diagrams/architecture.html (standalone, links back to all)
```

Every guide ends with **“See Also”** pointing to its neighbors — no orphan pages, ≤2 clicks from hub.

---

<p align="center"><sub>Generated 2026-08-31 · Deep audit party-mode (6 specialists) · Archify HTML+SVG · Economy OFF for docs · Source of truth: code under <code>src/main/java/com/picpay/payment</code></sub></p>
