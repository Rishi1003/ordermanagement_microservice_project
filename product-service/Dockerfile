FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY product-service/pom.xml product-service/
COPY product-service/src product-service/src
RUN mvn -f product-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /app/product-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
