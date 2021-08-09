FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=cloud_gateway.jar
RUN cp target/${JAR_FILE} /usr/share/${JAR_FILE}
EXPOSE 9000
ENTRYPOINT ["java","-jar","/cloud_gateway.jar"]