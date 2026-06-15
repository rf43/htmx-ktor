# Deployment

The canonical public demo for this repository is hosted at:

```text
https://htmx-ktor.cursedfunction.io/
```

## Canonical Demo Hosting

The canonical demo is deployed through DigitalOcean App Platform from this repository. Changes that land on `main` trigger the hosting platform's deployment path for that demo.

This repository intentionally keeps deployment automation separate from CI:

- GitHub Actions validates pull requests and pushes to `main`.
- DigitalOcean handles the application build and release after `main` is updated.

That keeps the repository checks focused on test and build health without duplicating the platform deployment pipeline.

## Forks And Reuse

Forks do not need DigitalOcean to run or deploy the project. The application can be run locally with Gradle, packaged as a container with the included `Dockerfile`, or deployed to any platform that supports JVM applications or Docker images.

If a fork uses a different public URL, update the sitemap and robots configuration in `src/main/kotlin/io/ivycreek/plugins/Routing.kt` before deploying.

## Container Build

The included `Dockerfile` installs Node dependencies, rebuilds the Tailwind CSS asset, packages the Ktor application, and runs the server on the port supplied by the platform.

For a local container check:

```bash
docker build -t htmx-ktor .
docker run --rm -p 8080:8080 htmx-ktor
```

## Search Endpoints

The canonical deployed demo exposes:

```text
https://htmx-ktor.cursedfunction.io/sitemap.xml
https://htmx-ktor.cursedfunction.io/robots.txt
```

Both endpoints are implemented in `src/main/kotlin/io/ivycreek/plugins/Routing.kt`.

As of June 15, 2026, the canonical live endpoints returned `200 OK`, Google Search Console showed the `htmx-ktor` sitemap as successful, and the sitemap reported one discovered page.

## Rollback

If a bad release reaches production, use the least invasive rollback available:

- Revert the offending change through a pull request when the source change is wrong.
- Redeploy a previous known-good deployment in DigitalOcean App Platform when the source is sound but the release needs to be backed out quickly.

After rollback, verify:

```bash
curl -I https://htmx-ktor.cursedfunction.io/
curl -I https://htmx-ktor.cursedfunction.io/sitemap.xml
curl -I https://htmx-ktor.cursedfunction.io/robots.txt
```
