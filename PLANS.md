# Kurkod Execution Plans (ExecPlans)

An ExecPlan is a living implementation plan for a substantial feature or system change. A contributor must be able to resume work using the plan, repository instructions, and current working tree without the previous conversation.

## When to use an ExecPlan

Use an ExecPlan for substantial features spanning backend and frontend, migrations with data compatibility concerns, large refactors, or work with significant uncertainty and multiple stages. Also use one when the user explicitly requests it.

Small bug fixes, dependency configuration, documentation edits, and formatting changes usually need only a short working plan and appropriate validation. Scale detail to the task; do not turn routine work into a planning exercise.

## Location and scope

Write plans in English under `docs/plans/YYYY-MM-DD-short-topic.md`, using the creation date and a descriptive lowercase topic. Create the directory when the first plan is needed. Continue an existing plan for the same task rather than creating duplicates. Keep completed plans at their original paths and record completion in `Outcomes & Retrospective`.

This root `PLANS.md` defines the planning format; it is not a task progress log. Read `AGENTS.md` for project conventions and `code_review.md` when performing a review. A plan does not override current user instructions or expand authorization.

## Working with a plan

Read this file in full before authoring or implementing an ExecPlan. Inspect the affected source, configuration, and tests before prescribing changes. Use the skeleton below and replace its prompts with task-specific information.

Continue through the authorized milestones without asking for permission at every step. Resolve routine implementation choices from repository evidence and record material assumptions. Ask for clarification when an unresolved product decision blocks correct implementation. Commit or push only when explicitly requested by the user; completing a milestone does not authorize either action.

Keep the plan current at meaningful checkpoints and before handing work off. Record completed work, remaining steps, discoveries, and changes of direction. Preserve unrelated working-tree changes. When resuming, compare the plan with the current code before executing its next step.

## Required content

Start with the user-visible outcome: what someone will be able to do after the change and how to observe it. Define scope and explicit non-goals when they prevent misunderstanding.

Provide enough context for someone unfamiliar with Kurkod: repository-relative file paths, relevant classes or components, existing behavior, and the contracts being changed. Define unfamiliar domain terms where they first appear. Do not copy the entire repository overview from `AGENTS.md`.

Clearly distinguish existing behavior, proposed behavior, assumptions, and verified observations. Summarize essential facts from earlier work within the plan. Official documentation and checked-in plans may be linked as supporting references, but links must not replace the context needed to execute the task.

Every ExecPlan must maintain `Progress`, `Surprises & Discoveries`, `Decision Log`, and `Outcomes & Retrospective`. Initially empty sections may say that nothing has been recorded yet; do not invent discoveries or outcomes to fill them.

## Milestones and implementation detail

Every ExecPlan has one or more milestones under `Plan of Work`. Each milestone describes its scope, the concrete result it produces, the relevant edits, and how that result will be validated. Milestones describe the implementation sequence; `Progress` tracks its actual completion. Both are required.

Prefer incremental, independently verifiable changes. Specify affected files, methods, DTOs, migrations, or components precisely enough to guide implementation without dictating incidental details that are better resolved from the code.

Use a prototype milestone only when it resolves a significant uncertainty, such as testing a PostgreSQL migration on disposable data or checking a Vue component against a proposed API contract. State the question, experiment, acceptance criteria, and whether the prototype will be retained or removed.

Temporary coexistence of old and new implementations is acceptable when needed for a safe migration. Explain how both paths are validated and when the old path can be removed. This is an implementation strategy, not a requirement to delegate work to multiple agents.

## Validation and evidence

Define acceptance in observable terms with concrete inputs and outcomes. For example, an authorized update with a stale `If-Match` value must return the conflict response defined by the API contract and leave the stored record unchanged. Merely adding a helper or compiling successfully is not sufficient proof of behavioral correctness.

Specify exact commands, their working directories, required environment, and how to interpret results. Select tests covering the changed behavior and relevant failure paths, with effort proportional to risk. For a regression fix, describe how the test demonstrates the old failure and the corrected behavior. Avoid unrelated test suites unless failures or shared dependencies justify them.

Separate expected results from actual observations. Never invent successful runs, test counts, or logs. Keep evidence concise and remove credentials and personal data from excerpts. If a check cannot run, record the blocker, any alternative evidence, and the outstanding validation. Do not mark acceptance complete solely because the code was written.

Use current project files as the source of truth:

- Backend commands run from `apps/backend` with JDK 21. In PowerShell use `.\mvnw.cmd test`, `.\mvnw.cmd spotless:check`, and `.\mvnw.cmd verify`; on Linux/macOS use `./mvnw`. `verify` includes the configured Spotless check; `test` and `package` do not. Diagnose wrapper startup failures separately from test failures.
- Frontend commands run from `apps/web`: `npm ci` and `npm run build`. Only invoke lint or test scripts if present in `package.json`. Include the affected browser scenario for UI changes; a Vite build alone does not prove runtime correctness or type safety.
- Validate Liquibase changes against representative existing data in a disposable database. Check registration in the master changelog, data preservation, and compatibility with the entity changes. Never use the user's database for destructive validation.
- The current backend Dockerfile requires a prebuilt JAR. Plans using `docker compose up --build` must account for that prerequisite and confirm the current Compose location and services.

Recheck these commands against `AGENTS.md`, `pom.xml`, and `package.json` when authoring the plan. Report pre-existing failures separately from regressions introduced by the task.

## Recovery and safe execution

Describe how steps can be repeated and how to recover after partial completion. For migrations or destructive operations, identify data affected, backup or recovery requirements, and a safe validation environment. Do not assume schema rollback restores lost data.

Record required approvals or unavailable inputs explicitly. Elapsed time is not approval. Prefer additive changes when they make deployment or recovery safer; do not remove unrelated files, edits, or database volumes as cleanup.

## Maintaining the living sections

Update `Progress` after meaningful milestones and before interruption or handoff. Split partially completed steps into completed and remaining work. Use actual timestamps with an explicit timezone where they clarify the sequence.

Record unexpected constraints and their evidence in `Surprises & Discoveries`, such as transaction behavior, migration restrictions, API mismatches, or environment failures.

Record material decisions and their rationale in `Decision Log`. Omit routine editing choices. If the approach changes, update affected milestones and acceptance criteria as well as the log.

At completion or handoff, update `Outcomes & Retrospective` with achieved behavior, validation results, remaining work, and relevant lessons. Keep these statements consistent with `Progress`. Include a short revision note explaining substantial changes to the plan; avoid duplicating the full decision log.

## Formatting

Use normal Markdown in plan files without an enclosing code fence. Leave one blank line after headings and before lists or code blocks. Use language-tagged fenced blocks for commands, code, and evidence. If presenting the entire plan inside a chat code block, use a longer outer fence to avoid conflicting with inner fences.

Prefer connected prose for explanations. Lists and tables are appropriate for genuinely parallel information. Use checkboxes in `Progress`. Keep the plan concise enough to maintain while retaining the context needed for a reliable handoff.

## ExecPlan skeleton

The following is a template, not an implementation task. Replace placeholders with verified project information and task-specific decisions.

    # <Short, action-oriented title>

    This ExecPlan follows the repository-root PLANS.md and AGENTS.md.
    Progress, Surprises & Discoveries, Decision Log, and Outcomes & Retrospective
    are maintained as work proceeds.

    ## Purpose and scope

    Describe the user-visible outcome, affected workflows, and relevant non-goals.

    ## Progress

    - [ ] Inspect the affected code and confirm the implementation approach.
    - [ ] Implement and validate milestone 1.
    - [ ] Complete acceptance checks and record the handoff.

    Add real timestamps where useful. Split partially completed steps explicitly.

    ## Surprises & Discoveries

    Record observations with concise evidence, or state that none are recorded yet.

    ## Decision Log

    - Decision: <material choice>
      Rationale: <why this approach fits the task>
      Date/Author: <actual date and author>

    ## Outcomes & Retrospective

    Initially pending. At handoff, summarize achieved behavior, actual validation,
    remaining work, and lessons relevant to future changes.

    ## Context and Orientation

    Identify affected files using repository-relative paths. Explain current
    behavior and relevant contracts. Summarize essential context from prior work;
    links are supporting references, not substitutes for that context.

    ## Plan of Work

    ### Milestone 1: <observable result>

    Explain edits, affected files and symbols, resulting behavior, and validation.
    Add further milestones only as needed.

    ## Concrete Steps

    Give commands with their working directories and prerequisites.
    Label expected output separately from observed output.

    ## Validation and Acceptance

    Describe inputs, expected outcomes, relevant tests, and any browser/API checks.
    Record completed checks and clearly identify blocked or outstanding checks.

    ## Idempotence and Recovery

    Explain retry behavior, partial-failure recovery, and any data safeguards.

    ## Artifacts and Notes

    Include concise evidence or relevant artifact paths. Do not include secrets.

    ## Interfaces and Dependencies

    Specify relevant DTOs, interfaces, methods, or Vue components and their contracts.
    Justify new dependencies when needed. For example, a plan affecting
    apps/backend/src/main/java/io/github/artsobol/kurkod/feature/diet/service/DietService.java
    might describe the contract of:

        public interface DietService {
          DietResponse get(Long id);
        }

    The example is illustrative: do not add or replace this interface merely
    because it appears here. Define response fields, authorization, and not-found
    behavior required by the actual task, plus affected frontend consumers.

    ## Revision notes

    Record substantial plan changes and their reasons without repeating the log.
