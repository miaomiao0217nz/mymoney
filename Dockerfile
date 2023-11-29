FROM eclipse-temurin:17.0.9_9-jdk-ubi9-minimal
ARG VER=1.0
WORKDIR /

COPY ./build/libs/app-${VER}.jar app.jar
COPY ./src/main/resources/ /data/
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]