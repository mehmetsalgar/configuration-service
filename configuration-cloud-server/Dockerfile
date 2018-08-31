FROM openjdk:8-jdk-alpine
VOLUME /tmp[:cached]
EXPOSE 8888
COPY target/configuration-cloud-server-1.0-SNAPSHOT.jar configuration-cloud-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/configuration-cloud-server.jar"]