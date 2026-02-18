FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY orders-service/pom.xml orders-service/
COPY orders-service/src orders-service/src
RUN mvn -f orders-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /app/orders-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
