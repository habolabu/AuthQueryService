FROM gradle:jdk17-alpine
WORKDIR /auth-query-service

COPY src /auth-query-service/src/
COPY build.gradle .
COPY lombok.config .

RUN gradle clean build -x test

ENTRYPOINT ["java", "-jar", "./build/libs/auth-query-service-1.0.0.jar"]