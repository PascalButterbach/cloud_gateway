FROM hypriot/rpi-java

ADD build/libs/cloud-gateway.jar /opt/cloud-gateway.jar

EXPOSE 8999

ENTRYPOINT ["java", "-jar", "/opt/cloud-gateway.jar"]

#FROM adoptopenjdk/openjdk11:alpine-slim
#WORKDIR /app
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#
#RUN chmod +x ./mvnw
#RUN ./mvnw dependency:go-offline
#
#COPY src ./src
#
#EXPOSE 8999
#
#CMD ["./mvnw", "spring-boot:run"]