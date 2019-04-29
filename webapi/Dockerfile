FROM openjdk:11.0.3-jre-stretch
MAINTAINER Hung Tran <tmhung88@outlook.com>
VOLUME /tmp
ARG JAR_FILE
COPY target/${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=dev"]
