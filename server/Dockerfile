FROM gradle:7-jdk11 AS builder

COPY --chown=gradle:gradle . /home/gradle/source

WORKDIR /home/gradle/source

RUN gradle shadowJar --no-daemon



FROM openjdk:11 AS runner

EXPOSE 8080

RUN mkdir /app

COPY --from=builder /home/gradle/source/build/libs/*.jar /app/hubusp.jar

ENTRYPOINT ["java","-jar","/app/hubusp.jar"]