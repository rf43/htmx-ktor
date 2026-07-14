# HTMX-Ktor

A small Kotlin/Ktor sample that renders HTML on the server and uses htmx for component-style page updates without a separate JavaScript application framework.

[Live demo](https://htmx-ktor.cursedfunction.io)

This repository is best treated as a compact example or starting point, not as a production-ready web application template.

## Features

- Server-rendered HTML with Ktor and `kotlinx.html`
- Showcase content that explains the Ktor route and htmx fragment-swap model
- htmx-powered navigation for dynamic component swaps
- Active navigation state for direct loads, htmx swaps, and browser history
- Direct component URLs that still render the full application shell on refresh
- Server-owned incident search, filtering, sorting, and pagination
- Refreshable incident detail routes with focused htmx swaps
- Tailwind utility classes compiled into a static CSS asset
- Netty-based Ktor server
- Sitemap and robots.txt endpoints for the configured public URL
- Dockerfile-based container packaging for deployment demos
- Route and htmx contract tests with Ktor `testApplication`
- JaCoCo coverage reporting and verification

## Stack

- Kotlin `2.3.21`
- Ktor `3.5.0`
- Gradle wrapper `9.5.1`
- Logback `1.5.33`
- JaCoCo `0.8.14`
- [htmx](https://htmx.org/) `2.0.10`
- Tailwind CSS `4.3.0`
- Tailwind CLI `4.3.0`

## Prerequisites

- JDK 17 or 21
- Node.js and npm for rebuilding Tailwind CSS
- No separate Gradle installation is required; use the checked-in Gradle wrapper.
- Docker is optional and only required for container build checks.

The project was last verified locally with OpenJDK `21.0.10`.

## Getting Started

```bash
git clone https://github.com/rf43/htmx-ktor.git
cd htmx-ktor
npm ci
npm run build:css
./gradlew run
```

Open `http://localhost:8080`.

Development mode is enabled by default through `gradle.properties`.

To run without Ktor development mode:

```bash
./gradlew run -Pdevelopment=false
```

## Development

Run tests:

```bash
./gradlew test
```

The test suite verifies the root application shell, static asset routing, htmx navigation attributes, component route registration, server-owned incident queries, and route-specific showcase content.

Rebuild the compiled Tailwind CSS asset:

```bash
npm run build:css
```

Gradle packages the current `src/main/resources/static/app.css`; it does not regenerate Tailwind CSS. Run `npm run build:css` after changing Tailwind classes or `src/main/resources/styles/tailwind.css`.

Watch Tailwind source inputs during local UI work:

```bash
npm run watch:css
```

Build the project:

```bash
./gradlew build
```

## Continuous Integration

GitHub Actions runs `./gradlew test` followed by `./gradlew build` for pull
requests and pushes to `main`, using Temurin JDK 21. The build includes the
Gradle `check` lifecycle and JaCoCo coverage verification.

Run `./gradlew build` before opening a pull request to reproduce the full
project check locally.

Build the container image:

```bash
docker build -t htmx-ktor .
```

The Docker build regenerates `app.css` before packaging the Ktor distribution.
The Docker build runs Kotlin compilation in-process to avoid short-lived Kotlin daemon files during hosted image builds.

Run the container locally:

```bash
docker run --rm -p 8080:8080 htmx-ktor
```

The server reads `PORT` from the environment and defaults to `8080`, which keeps local runs simple while allowing managed platforms to provide the runtime port.

## Configuration

The application supports these environment variables:

- `PORT` - HTTP port used by the Netty server. Defaults to `8080`.
- `PUBLIC_SITE_URL` - absolute public base URL used by `/sitemap.xml` and `/robots.txt`. Defaults to `https://example.com/`.

Set `PUBLIC_SITE_URL` in the deployment environment so generated search endpoints point at the deployed site. Forks should set this to their own public URL rather than changing source code.

Generate the JaCoCo report:

```bash
./gradlew jacocoTestReport
```

Verify coverage:

```bash
./gradlew jacocoTestCoverageVerification
```

The HTML coverage report is written to `build/reports/jacoco/test/html/index.html`.

## Project Structure

```text
htmx-ktor/
|-- .github/workflows/
|   `-- ci.yml
|-- src/
|   |-- main/
|   |   |-- kotlin/io/ivycreek/
|   |   |   |-- about/
|   |   |   |-- incidents/
|   |   |   |-- contact/
|   |   |   |-- content/
|   |   |   |-- dashboard/
|   |   |   |-- navbar/
|   |   |   |-- plugins/
|   |   |   |-- projects/
|   |   |   |-- team/
|   |   |   `-- Application.kt
|   |   `-- resources/
|   |       |-- logback.xml
|   |       |-- styles/
|   |       `-- static/
|   `-- test/kotlin/io/ivycreek/
|-- build.gradle.kts
|-- Dockerfile
|-- package.json
|-- package-lock.json
|-- .dockerignore
|-- gradle.properties
|-- settings.gradle.kts
`-- gradle/wrapper/
```

Each page package generally has two files:

- `Feature.kt` renders the HTML fragment.
- `FeatureRouter.kt` registers the `/components/...` route.

The root page and static resource routing live in `src/main/kotlin/io/ivycreek/plugins/Routing.kt`.
Component routes return fragments for htmx requests and the full application shell for normal browser requests, so pushed URLs remain refreshable and bookmarkable.
The incident workspace uses ordinary GET query parameters for search, filters, sorting, and paging. htmx submits the same URLs and swaps only the queue or detail region.

## Notes

- Tailwind is built with the CLI from `src/main/resources/styles/tailwind.css` into `src/main/resources/static/app.css`, which Ktor serves at `/app.css`.
- The project currently loads htmx from a pinned 2.x CDN URL with subresource integrity.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
