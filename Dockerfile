FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} cloud_gateway.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar","/cloud_gateway.jar"]