FROM openjdk:11-jdk AS build
WORKDIR /tmp
COPY src /tmp
RUN chmod +x ./gradlew && ./gradlew bootJar

FROM openjdk:11-jdk
WORKDIR /tmp
COPY --from=build /tmp/build/libs/demo-0.0.1-SNAPSHOT.jar /tmp/HeneinBack.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /tmp/HeneinBack.jar"]