FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x ./gradlew && ./gradlew installDist -Pdevelopment=false --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/build/install/htmx-ktor ./

ENV PORT=8080
EXPOSE 8080

USER 10001

CMD ["bin/htmx-ktor"]
