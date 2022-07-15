FROM gradle:7.4.2-jdk17 AS builder

WORKDIR /home/gradle/hub

RUN chown gradle:gradle . -R

USER gradle

COPY --chown=gradle:gradle . .

RUN gradle :hub-app:shadowJar :hub-cli:shadowJar





FROM eclipse-temurin:17-jdk AS rbase

WORKDIR /hub





FROM rbase AS app

ENV HUB_HTTP_PORT=8080

EXPOSE ${HUB_HTTP_PORT}

COPY --from=builder /home/gradle/hub/hub-app/build/libs/hub*-all.jar ./hub-app.jar

ENTRYPOINT ["java", "-jar", "/hub/hub-app.jar"]





FROM rbase AS cli

COPY --from=builder /home/gradle/hub/hub-cli/build/libs/hub*-all.jar ./hub-cli.jar

ENTRYPOINT ["java", "-jar", "/hub/hub-cli.jar"]
