#####################################
# BASE IMAGE FOR BUILDING THE PROJECT
#####################################
FROM gradle:8.14.3-jdk21 AS base

WORKDIR /home/gradle/hub

RUN mkdir gradle catalog config curatorship discovery hub-app hub-cli mailer persistence sheets techtransfer \
    && chown gradle:gradle . -R

USER gradle

CMD gradle build --continuous

# COPY ALL PROJECTS build.gradle.kts FILES TO THEIR DESTINATIONS
COPY --chown=gradle:gradle  settings.gradle.kts             ./
COPY --chown=gradle:gradle  build.gradle.kts                ./
COPY --chown=gradle:gradle  gradle/libs.versions.toml       ./gradle
COPY --chown=gradle:gradle  catalog/build.gradle.kts        ./catalog/
COPY --chown=gradle:gradle  config/build.gradle.kts         ./config/
COPY --chown=gradle:gradle  curatorship/build.gradle.kts    ./curatorship/
COPY --chown=gradle:gradle  discovery/build.gradle.kts      ./discovery/
COPY --chown=gradle:gradle  hub-app/build.gradle.kts        ./hub-app/
COPY --chown=gradle:gradle  hub-cli/build.gradle.kts        ./hub-cli/
COPY --chown=gradle:gradle  mailer/build.gradle.kts         ./mailer/
COPY --chown=gradle:gradle  persistence/build.gradle.kts    ./persistence/
COPY --chown=gradle:gradle  sheets/build.gradle.kts         ./sheets/
COPY --chown=gradle:gradle  techtransfer/build.gradle.kts   ./techtransfer/

# PRE-INSTALL JUST THE DEPENDENCIES -- THIS SHALL SPEEDUP FUTURE BUILDS
RUN gradle clean build

# COPY THE REST OF THE CODE
COPY --chown=gradle:gradle . ./





###########################
# BUILDER IMAGE FOR THE CLI
###########################
FROM base AS clibuilder

# BUILD THE FAT JARS
RUN gradle :hub-cli:shadowJar





###########################
# BUILDER IMAGE FOR THE APP
###########################
FROM base AS appbuilder

# BUILD THE FAT JARS
RUN gradle :hub-app:shadowJar





############################
# BASE IMAGE FOR THE RUNNERS
############################
FROM eclipse-temurin:21-jdk AS rbase

WORKDIR /hub





##########################
# RUNNER IMAGE FOR THE APP
##########################
FROM rbase AS app

ENV HUB_HTTP_PORT=8080

EXPOSE ${HUB_HTTP_PORT}

COPY --from=appbuilder /home/gradle/hub/hub-app/build/libs/hub*-all.jar ./hub-app.jar

ENTRYPOINT ["java", "-jar", "/hub/hub-app.jar"]





##########################
# RUNNER IMAGE FOR THE CLI
##########################
FROM rbase AS cli

COPY --from=clibuilder /home/gradle/hub/hub-cli/build/libs/hub*-all.jar ./hub-cli.jar

ENTRYPOINT ["java", "-jar", "/hub/hub-cli.jar"]
