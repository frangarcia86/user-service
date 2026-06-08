# Stage 1: Build
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY . .
RUN sed -i 's/\r$//' mvnw && chmod +x ./mvnw && ./mvnw package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:25-jdk
WORKDIR /deployments
COPY --from=build /app/target/quarkus-app/lib/ lib/
COPY --from=build /app/target/quarkus-app/*.jar ./
COPY --from=build /app/target/quarkus-app/app/ app/
COPY --from=build /app/target/quarkus-app/quarkus/ quarkus/

EXPOSE 8080
ENTRYPOINT ["java", "-Xmx256m", "-Dquarkus.http.host=0.0.0.0", "-jar", "quarkus-run.jar"]
