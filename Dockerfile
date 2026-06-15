FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY package.json package-lock.json ./
COPY gradle ./gradle
COPY src ./src

RUN apt-get update \
    && apt-get install -y --no-install-recommends nodejs npm \
    && rm -rf /var/lib/apt/lists/*

RUN npm ci
RUN npm run build:css
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
