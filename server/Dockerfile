# Stage build
FROM eclipse-temurin:24 AS builder

WORKDIR /opt/app

COPY .mvn/ .mvn

COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY ./src ./src

RUN ./mvnw clean install

FROM eclipse-temurin:24

WORKDIR /opt/app

EXPOSE 8080

COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar

ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]