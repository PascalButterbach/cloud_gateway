FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} cloud_gateway.jar
ENTRYPOINT ["java","-jar","/app.jar"]