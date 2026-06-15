# CI

This repository uses GitHub Actions for pre-merge and post-merge validation.

## Workflow

The CI workflow is defined in `.github/workflows/ci.yml`.

It runs on:

- Pull requests targeting the repository
- Pushes to `main`

The workflow currently runs:

```bash
./gradlew test
./gradlew build
```

`./gradlew build` also runs the Gradle `check` lifecycle, including JaCoCo coverage verification. The separate `test` step keeps test failures visible before the broader build step starts.

## Runtime

CI uses Temurin JDK 21 on Ubuntu. The local README also allows JDK 17 or 21, but JDK 21 is the documented local verification version and keeps CI aligned with the current development environment.

## Notes

- The workflow does not deploy the application. Deployment is handled by the hosting platform after changes reach `main`.
- Gradle packages the checked-in `src/main/resources/static/app.css`; CI does not rebuild Tailwind CSS.
- Run `npm run build:css` locally and commit the resulting CSS asset when Tailwind source or utility classes change.
