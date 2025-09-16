#FROM openjdk:17
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java", \
#            "-XX:-UseContainerSupport", \
#            "-jar", \
#            "-Dspring.profiles.active=stress", \
#            "/app.jar"]

FROM gradle:8.8-jdk17 AS build
WORKDIR /src
COPY . .
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /src/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
