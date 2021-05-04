FROM amazoncorretto:16.0.1-alpine
MAINTAINER Hung Tran <tmhung88@outlook.com>
VOLUME /tmp
ARG JAR_FILE
COPY target/${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=dev"]
