# Kurkod project instructions

## Scope and working agreements

These instructions apply to the entire repository. All paths below are relative to the repository root unless stated otherwise.
Respond to the user in Russian unless requested otherwise. Write project instruction files in English.
Before editing, inspect `git status` and any applicable nested `AGENTS.md` files. Preserve unrelated user changes.
Use `pom.xml`, `package.json`, configuration, and source code as the source of truth for versions and behavior; README files may be outdated.
For work using an ExecPlan, read the entire root `PLANS.md` and follow its format. Small documentation or formatting changes do not need a detailed ExecPlan.

## Project and repository layout

Kurkod is a poultry farm management application covering chickens, breeds, diets, cages, workshops, staff, egg production, and reporting.

- `apps/backend`: Java 21, Spring Boot, Maven, PostgreSQL, Spring Data JPA, Liquibase, Spring Security, JWT, Lombok, and MapStruct.
- `apps/web`: Vue 3, Vite, JavaScript, Pinia, Vue Router, Axios, and SCSS. A TypeScript dependency does not mean the application is implemented entirely in TypeScript.
- `.github/dependabot.yml`: dependency update configuration.
- `.editorconfig`: shared editor settings.
- `README.md` and `apps/backend/README.md`: reference documentation; verify commands against the current configuration.

## Backend conventions

Base package: `apps/backend/src/main/java/io/github/artsobol/kurkod`.

- Organize domain functionality under `feature/<domain>`, using `web`, `service`, `repository`, `entity`, `mapper`, and `dto` as appropriate. Follow the neighboring domain code.
- `infrastructure` holds shared security, persistence, error handling, localization, and utilities; `config` holds application configuration; `exception` holds exception types.
- Controllers handle DTOs, services own business logic and transaction boundaries, and repositories access data. Reuse MapStruct mappers and constructor injection.
- The API context path is `/api/v1`; do not duplicate it in controller mappings.
- Preserve ETag / If-Match contracts and entity version checks when modifying update and delete operations. Preserve soft deletion where `isActive` / `deactivate` is used.
- Preserve authorization and session restrictions. Cover successful and rejected requests when changing authentication behavior.
- Use existing exceptions, error handlers, and localization keys. Check the i18n resources when adding messages.
- Liquibase migrations live in `apps/backend/src/main/resources/db/changelog`. Add new changesets and register them following `db.changelog-master.yaml`. Do not rewrite applied migrations to change the schema. Hibernate uses `ddl-auto: validate`.
- Configuration lives in `application.yml`, `application-dev.yml`, and `application-prod.yml`. Account for profile overrides. Never put real secrets in tracked files or logs.

## Frontend conventions

All paths in this section are relative to `apps/web`.

- API clients live in `src/api`; shared Axios configuration is in `src/api/http.js` and uses `VITE_API_URL`.
- Shared behavior lives in `src/composables`, state in `src/stores`, routing in `src/router`, and styles in `src/styles`.
- Reuse existing Vue components and neighboring conventions. Check frontend compatibility when changing backend DTOs or routes.
- Use npm and the existing `package-lock.json`. Update the lockfile when changing dependencies.
- Read `package.json` scripts before invoking commands. At the time of writing, `dev`, `build`, and `preview` exist; `lint` and `test` are not configured. A Vite build does not replace tests or a dedicated type check.

## Formatting

- Follow the root `.editorconfig`: UTF-8, spaces, a default indentation of 2 spaces, 4 spaces for XML, LF except CRLF for bat/cmd files.
- Java uses Spotless with `google-java-format`: Google Java Style, 2-space indentation, and a normal 100-character line limit. `pom.xml` defines the formatter version and steps.
- Spotless also removes unused imports and handles type annotations. Prefer explicit imports over wildcard imports.
- Keep repository-wide formatting separate from behavioral changes. For a local edit, restrict `spotless:apply` to the affected Java files using `-DspotlessFiles`; do not overwrite unrelated user edits.

## Commands and validation

Backend working directory: `apps/backend`. Requires JDK 21. PowerShell commands:

```powershell
.\mvnw.cmd test
.\mvnw.cmd '-Dtest=LoginServiceImplTest' test
.\mvnw.cmd spotless:check
.\mvnw.cmd verify
```

`spotless:apply` changes source files; `spotless:check` only checks them. The check is bound to `verify`, so `package` and `test` alone do not execute it.
On Linux/macOS, use `./mvnw` instead of `.\mvnw.cmd`. If the wrapper fails to start, diagnose Java, the wrapper, and the environment; do not report this as a test failure. An installed `mvn` may be used as an explicitly reported fallback.

Frontend working directory: `apps/web`. Use a Node.js version compatible with the installed Vite version:

```shell
npm ci
npm run build
npm run dev
```

For Java changes, run relevant tests and, where the environment allows, `verify` before completion. For frontend changes, run `build` and check the affected user flow. For documentation-only changes, check links, commands, and the diff instead of running application tests.
Report unrelated existing failures separately. State which checks passed, failed, or could not run, and explain any limitations.

## Docker

The current `Dockerfile` and `docker-compose.yml` are in `apps/backend`. Compose runs the backend and PostgreSQL; the frontend runs separately.
The Dockerfile copies a prebuilt `target/kurkod-0.0.1-SNAPSHOT.jar`; it does not compile Java. Run Maven `package` or `verify` before `docker compose up --build`. Check `ARG JAR_FILE` when changing the application version.
Do not treat development Compose settings as production configuration. Do not delete database volumes during routine troubleshooting.

## Code review

When reviewing code, follow the rules in `code_review.md`.

## Maintaining these instructions

Update this file when repository structure, commands, or conventions change. Do not store temporary task progress, secrets, or unimplemented plans as existing capabilities.

Organization reference: [official OpenAI AGENTS.md documentation](https://learn.chatgpt.com/docs/agent-configuration/agents-md). Repository-specific guidance is derived from this project, not prescribed by OpenAI.
