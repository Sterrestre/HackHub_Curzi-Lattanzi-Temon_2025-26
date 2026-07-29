# ---- Stage 1: build ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiamo prima solo i file necessari al wrapper e alle dipendenze,
# cosi' Docker puo' riusare la cache se cambia solo il codice sorgente
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN sed -i 's/\r$//' gradlew
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

# Ora copiamo il codice sorgente e buildiamo il jar
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# ---- Stage 2: runtime ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]