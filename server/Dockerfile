#####################################
# BASE IMAGE FOR BUILDING THE PROJECT
#####################################
FROM gradle:7.4.2-jdk17 AS base

WORKDIR /home/gradle/hub

RUN mkdir catalog curatorship discovery hub-app hub-cli persistence techtransfer \
    && chown gradle:gradle . -R

USER gradle

# COPY ALL PROJECTS build.gradle.kts FILES TO THEIR DESTINATIONS
COPY --chown=gradle:gradle  settings.gradle.kts                                                 .
COPY --chown=gradle:gradle  build.gradle.kts                 gradle.properties                  .
COPY --chown=gradle:gradle  catalog/build.gradle.kts         catalog/gradle.properties          ./catalog
COPY --chown=gradle:gradle  curatorship/build.gradle.kts     curatorship/gradle.properties      ./curatorship
COPY --chown=gradle:gradle  discovery/build.gradle.kts       discovery/gradle.properties        ./discovery
COPY --chown=gradle:gradle  hub-app/build.gradle.kts         hub-app/gradle.properties          ./hub-app
COPY --chown=gradle:gradle  hub-cli/build.gradle.kts         hub-cli/gradle.properties          ./hub-cli
COPY --chown=gradle:gradle  persistence/build.gradle.kts     persistence/gradle.properties      ./persistence
COPY --chown=gradle:gradle  techtransfer/build.gradle.kts    techtransfer/gradle.properties     ./techtransfer

# PRE-INSTALL JUST THE DEPENDENCIES -- THIS SHALL SPEEDUP FUTURE BUILDS
RUN gradle clean build

# COPY THE REST OF THE CODE
COPY --chown=gradle:gradle . .





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
FROM eclipse-temurin:17-jdk AS rbase

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
