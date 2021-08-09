FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=cloud_gateway.jar
RUN cp /app/target/${JAR_FILE} /usr/share/${JAR_FILE}
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "/usr/share/cloud_gateway.jar"]

#
#FROM adoptopenjdk/openjdk11:alpine as compile
#MAINTAINER XXXX <XXXXXXX>
#
## Build the jar using maven
#RUN apk add maven
#WORKDIR /app
#COPY . /app/
#RUN mvn -f pom.xml clean package -DskipTests
#
#FROM adoptopenjdk/openjdk11:alpine-jre
## Copy the packaged jar app file to a smaller JRE base image
#COPY --from=compile "/app/target/cloud_gateway.jar" /usr/share/
#EXPOSE 9000
#ENTRYPOINT ["java", "-jar", "/usr/share/cloud_gateway.jar"]