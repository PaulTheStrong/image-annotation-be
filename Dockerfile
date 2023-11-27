FROM openjdk:17

ARG APP_VERSION=''
COPY target/image-annotation-be-${APP_VERSION}.jar app/application.jar
WORKDIR app/

CMD ["java", "-jar", "app/application.jar"]