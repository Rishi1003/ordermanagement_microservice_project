FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY config-server/pom.xml config-server/
COPY config-server/src config-server/src
RUN mvn -f config-server/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /app/config-server/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
