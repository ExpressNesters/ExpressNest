FROM maven:3.6.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY ./src ./src
RUN mvn dependency:go-offline
RUN mvn package

FROM eclipse-temurin:17.0.8.1_1-jdk
WORKDIR /app
COPY --from=build /app/target/feed-service-0.0.1-SNAPSHOT.jar /app/feed-service-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "feed-service-0.0.1-SNAPSHOT.jar"]