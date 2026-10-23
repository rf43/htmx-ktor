FROM node:22.23.4-bookworm-slim AS css
WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY src ./src
RUN npm run build:css

FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle ./gradle
COPY src ./src

COPY --from=css /app/src/main/resources/static/app.css ./src/main/resources/static/app.css

RUN chmod +x ./gradlew \
    && ./gradlew installDist \
        -Pdevelopment=false \
        -Pkotlin.compiler.execution.strategy=in-process \
        --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/build/install/htmx-ktor ./

ENV PORT=8080
ENV PUBLIC_SITE_URL=https://example.com/
EXPOSE 8080

USER 10001

CMD ["bin/htmx-ktor"]
