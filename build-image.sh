export APP_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
mvn clean package
mvn spring-boot:build-image
docker tag image-annotation-be:$APP_VERSION paulthestrong/image-annotation-be:$APP_VERSION