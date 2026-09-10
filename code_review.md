# Code review

These rules apply to reviews across the Kurkod repository. Read `AGENTS.md` for project structure and validation commands.

## Review approach

- Review the requested diff and enough surrounding code to understand its behavior. Trace affected callers, configuration, migrations, and tests when relevant.
- Report actionable defects introduced by the change. Explain the triggering conditions and concrete impact; distinguish confirmed findings from questions or unverified assumptions.
- Prioritize correctness, security, data integrity, and regressions. Do not present personal style preferences or speculative redesigns as defects.
- Attach findings to the smallest relevant changed line range. Include severity and a concise explanation; suggest a fix when it is clear.
- Leave Java formatting checks to Spotless. Do not duplicate automated formatting findings unless the formatter configuration itself is incorrect.
- State validation performed and its limits. If no actionable defects are found, say so; do not invent findings or imply unrun tests passed.

## Backend and API

- Check that request and response DTOs, HTTP statuses, route paths, and frontend consumers stay compatible, unless the change intentionally updates the contract and its consumers.
- Preserve the `/api/v1` context path without duplicating it in controller mappings.
- Check authorization on changed operations, including service-level restrictions. Flag missing access checks or accidental exposure of sensitive fields.
- Check ETag / If-Match parsing and entity version handling on affected updates and deletes. Flag changes that allow stale writes or silently drop the established concurrency contract.
- Preserve soft deletion and active-record filtering where the domain uses them. Flag unintended hard deletion or newly exposed inactive records.
- Check transaction boundaries and bidirectional JPA relationship updates where affected. Report query or lazy-loading problems only with a concrete execution path.
- Check validation, exception translation, and localization keys so invalid requests produce the intended response rather than unexpected server errors.

## Database changes

- Check that entity and schema changes have corresponding Liquibase changesets and are reachable from the master changelog.
- Flag edits to previously applied changesets that can cause checksum failures, and migrations that can lose existing data unexpectedly.
- Check constraints, nullability, defaults, and backfills against existing rows. For destructive changes, verify the intended migration and recovery approach.
- Do not suggest `ddl-auto: update` as a substitute for a migration.

## Authentication and configuration

- For changed JWT, refresh-token, session, and cookie code, check expiration, revocation, rotation, and invalid-input behavior along the affected paths.
- Check profile and environment overrides: a configured variable must actually reach the setting it claims to control.
- Flag secrets committed in code, configuration, or logs. Do not reproduce their values in review output.
- Review production cookie and CORS changes in deployment context; do not flag explicitly development-only settings as production defects without evidence.

## Frontend

- Trace changed UI behavior through components, composables, stores, and API clients. Check loading, empty, error, and successful states relevant to the change.
- Check request payloads, response handling, authentication behavior, and optimistic concurrency headers against the backend contract.
- Check reactive state and asynchronous requests for concrete stale-state or race conditions introduced by the change.
- Check interaction and accessibility of changed controls, including labels and keyboard operation where applicable.
- Check `package.json` / `package-lock.json` consistency when dependencies change. Do not treat a successful Vite build as proof of runtime correctness or type safety.

## Build, Docker, and tests

- Check Maven lifecycle bindings, working directories, and file paths in build or automation changes. Spotless `check` is currently bound to `verify`, not `package`.
- Check Docker build context, required build artifacts, environment variables, service connectivity, and database persistence when those files change.
- For behavior changes, look for tests covering the actual regression and relevant failure paths. Do not require application tests for documentation-only or formatting-only edits.
- Separate environment failures and pre-existing failures from defects introduced by the patch. Do not run destructive database operations to validate a review.
