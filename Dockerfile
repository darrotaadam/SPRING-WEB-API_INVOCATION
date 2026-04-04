FROM maven:3.9.6-amazoncorretto-21 as build
RUN mkdir -p /api
WORKDIR /api
COPY pom.xml /api
COPY src /api/src
RUN mvn -f pom.xml clean package

FROM amazoncorretto:21.0.2-alpine3.19
COPY --from=build /api/target/*.jar app.jar

RUN apk add --no-cache openssl
EXPOSE 8083/tcp
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app.jar"]