# HTMX-Ktor

A small Kotlin/Ktor sample that renders HTML on the server and uses htmx for component-style page updates without a separate JavaScript application framework.

This repository is best treated as a compact example or starting point, not as a production-ready web application template.

## Features

- Server-rendered HTML with Ktor and `kotlinx.html`
- Showcase content that explains the Ktor route and htmx fragment-swap model
- htmx-powered navigation for dynamic component swaps
- Active navigation state for direct loads, htmx swaps, and browser history
- Direct component URLs that still render the full application shell on refresh
- Interactive calendar rows that load server-rendered event details
- Tailwind utility classes loaded through the Tailwind Play CDN
- Netty-based Ktor server
- Route and htmx contract tests with Ktor `testApplication`
- JaCoCo coverage reporting and verification

## Stack

- Kotlin `2.3.21`
- Ktor `3.5.0`
- Gradle wrapper `9.5.1`
- Logback `1.5.33`
- JaCoCo `0.8.14`
- htmx `2.0.10`
- Tailwind Play CDN

## Prerequisites

- JDK 17 or 21
- No separate Gradle installation is required; use the checked-in Gradle wrapper.

The project was last verified locally with OpenJDK `21.0.10`.

## Getting Started

```bash
git clone https://github.com/rf43/htmx-ktor.git
cd htmx-ktor
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

The test suite verifies the root application shell, static asset routing, htmx navigation attributes, component route registration, and route-specific showcase content.

Build the project:

```bash
./gradlew build
```

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
|-- src/
|   |-- main/
|   |   |-- kotlin/io/ivycreek/
|   |   |   |-- about/
|   |   |   |-- calendar/
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
|   |       `-- static/
|   `-- test/kotlin/io/ivycreek/
|-- build.gradle.kts
|-- gradle.properties
|-- settings.gradle.kts
`-- gradle/wrapper/
```

Each page package generally has two files:

- `Feature.kt` renders the HTML fragment.
- `FeatureRouter.kt` registers the `/components/...` route.

The root page and static resource routing live in `src/main/kotlin/io/ivycreek/plugins/Routing.kt`.
Component routes return fragments for htmx requests and the full application shell for normal browser requests, so pushed URLs remain refreshable and bookmarkable.

## Notes

- The current frontend setup favors demo simplicity over production asset management.
- For production-style usage, replace the Tailwind Play CDN with a real Tailwind build that emits static CSS.
- The project currently loads htmx from a pinned 2.x CDN URL with subresource integrity.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
