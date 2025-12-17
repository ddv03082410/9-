# Repository Guidelines

## Project Structure & Module Organization
`springboot/` is a Maven module targeting Java 21; place controllers, services, and DTOs under `src/main/java/com/...`, while MyBatis XML lives in `src/main/resources/mapper` beside `application.yml`. `vue/` hosts the Vite + Vue 3 client with pages in `src/views`, routing in `src/router`, shared helpers in `src/utils`, and environment switches defined through `.env.*`. Static reference files stay inside `files/`, and the canonical schema plus demo data is stored in `xsxk.sql` at the repository root.

## Build, Test, and Development Commands
- `cd springboot && mvn clean package` 每 compile and emit the backend JAR in `target/`.
- `cd springboot && mvn spring-boot:run` 每 boot the API using the datasource values from `application.yml` or exported env vars.
- `cd vue && npm install` 每 install Node (18+) dependencies after cloning or pulling.
- `cd vue && npm run dev` 每 run the HMR dev server, proxying API calls through the URL defined in `.env.development`.
- `cd vue && npm run build` 每 create deployable assets in `dist/` for nginx or the Spring Boot static handler.

## Coding Style & Naming Conventions
Java code uses four-space indentation, `UpperCamelCase` for types, `lowerCamelCase` for members, and REST endpoint paths such as `/api/courses/{courseId}`; service classes end with `Service`, and mapper XML statement IDs must mirror DAO method names. Vue components are single-file components named with kebab-case filenames (`course-table.vue`), `<script setup>` blocks, and scoped SCSS imported from `src/assets`. Keep axios helper modules in `src/utils` and reuse Element Plus components to preserve UI parity.

## Testing Guidelines
Testing dependencies are not yet wired in, so add `spring-boot-starter-test` before introducing backend suites and store them in `springboot/src/test/java` following the `<TypeName>Test` pattern. Target >80% coverage for service logic, and include SQL fixtures or mock data when touching mapper queries. Frontend contributors can introduce Vitest or Cypress specs under `vue/tests`; until CI is added, document manual verification steps (browser, role, data snapshots) inside the PR description.

## Commit & Pull Request Guidelines
With no existing Git history, follow Conventional Commits (`feat: course enrollment cap`) written in the imperative, one logical change per commit, and mention affected layers (API/UI/SQL). Pull requests must summarize user-facing impact, list validation steps, note schema or configuration touches separately, and attach screenshots or recordings for UI updates.

## Security & Configuration Tips
Keep credentials outside the repo: set `SPRING_DATASOURCE_*` as env vars and adjust `.env.development` locally rather than committing secrets. Scrub anything placed under `files/` for PII, avoid committing `target/` or `dist/`, and leverage Hutool validation utilities when handling uploads or exports.
