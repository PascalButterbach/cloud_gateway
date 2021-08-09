FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} cloud_gateway.jar
ENTRYPOINT ["java","-jar","/cloud_gateway.jar"]