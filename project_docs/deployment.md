# Deployment

The canonical public demo for this repository is hosted at:

```text
https://htmx-ktor.cursedfunction.io/
```

## Canonical Demo Hosting

The canonical demo is deployed through DigitalOcean App Platform from this repository. Changes that land on `main` trigger the hosting platform's deployment path for that demo.

The canonical deployment should set:

```text
PUBLIC_SITE_URL=https://htmx-ktor.cursedfunction.io/
```

For GitHub Actions, this value can live in the repository variable named `PUBLIC_SITE_URL`. For DigitalOcean App Platform, set the same name as an application environment variable so the running server uses the canonical public URL.

This repository intentionally keeps deployment automation separate from CI:

- GitHub Actions validates pull requests and pushes to `main`.
- DigitalOcean handles the application build and release after `main` is updated.

That keeps the repository checks focused on test and build health without duplicating the platform deployment pipeline.

## Forks And Reuse

Forks do not need DigitalOcean to run or deploy the project. The application can be run locally with Gradle, packaged as a container with the included `Dockerfile`, or deployed to any platform that supports JVM applications or Docker images.

If a fork uses a different public URL, set `PUBLIC_SITE_URL` in that deployment environment. The source code defaults to `https://example.com/` as a placeholder so forks do not inherit the canonical demo URL.

## Container Build

The included `Dockerfile` installs Node dependencies, rebuilds the Tailwind CSS asset, packages the Ktor application, and runs the server on the port supplied by the platform.

The container image defaults `PUBLIC_SITE_URL` to `https://example.com/`. Override it in the hosting environment for any real deployment.

For a local container check:

```bash
docker build -t htmx-ktor .
docker run --rm -p 8080:8080 htmx-ktor
```

## Search Endpoints

The application exposes:

```text
${PUBLIC_SITE_URL}sitemap.xml
${PUBLIC_SITE_URL}robots.txt
```

Both endpoints are implemented in `src/main/kotlin/io/ivycreek/plugins/Routing.kt` and use the configured `PUBLIC_SITE_URL`.

As of June 15, 2026, the canonical live endpoints returned `200 OK`, Google Search Console showed the `htmx-ktor` sitemap as successful, and the sitemap reported one discovered page.

## Rollback

If a bad release reaches production, use the least invasive rollback available:

- Revert the offending change through a pull request when the source change is wrong.
- Redeploy a previous known-good deployment in DigitalOcean App Platform when the source is sound but the release needs to be backed out quickly.

After rollback, verify:

```bash
curl -I "$PUBLIC_SITE_URL"
curl -I "${PUBLIC_SITE_URL}sitemap.xml"
curl -I "${PUBLIC_SITE_URL}robots.txt"
```
