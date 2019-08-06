FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/ROOT.jar ROOT.jar
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "ROOT.jar"]