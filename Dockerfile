FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/ROOT.jar ROOT.jar
EXPOSE 8443
EXPOSE 443
EXPOSE 8080
EXPOSE 80
ENTRYPOINT ["java", "-jar", "-Dspring.config.location=file:/home/antilamer1709/thing-tracker.properties", "ROOT.jar"]