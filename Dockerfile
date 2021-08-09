FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} cloud_gateway.jar
ENTRYPOINT ["java","-jar","cloud_gateway.jar"]