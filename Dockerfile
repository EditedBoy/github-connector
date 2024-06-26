FROM openjdk:17-jdk-slim as builder

WORKDIR /app

COPY build/libs/github-connector-*.jar application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
