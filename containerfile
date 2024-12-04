FROM maven:3.9.9-eclipse-temurin-21-alpine as build
WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app_group && adduser -S --shell /sbin/nologin -g app_group app_user
USER app_user
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]