FROM openjdk:latest
VOLUME /home
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
EXPOSE 32561
ENTRYPOINT  ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]