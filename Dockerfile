# Stage 1: Build
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /deployments
COPY --from=build /app/target/quarkus-app/lib/ lib/
COPY --from=build /app/target/quarkus-app/*.jar ./
COPY --from=build /app/target/quarkus-app/app/ app/
COPY --from=build /app/target/quarkus-app/quarkus/ quarkus/

EXPOSE 8080
ENTRYPOINT ["java", "-Dquarkus.http.host=0.0.0.0", "-jar", "quarkus-run.jar"]
