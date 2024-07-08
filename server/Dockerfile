FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /build
COPY pom.xml ./
COPY src ./src
RUN mvn install -DskipTests=true

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /build/target/server.jar /app/server.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/app/server.jar","--spring.profiles.active=local"]