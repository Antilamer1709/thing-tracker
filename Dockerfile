FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8443
EXPOSE 443
EXPOSE 8080
EXPOSE 80
VOLUME /tmp
ADD target/ROOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]