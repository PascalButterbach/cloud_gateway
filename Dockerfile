FROM hypriot/rpi-java

ADD cloud-gateway /opt/cloud-gateway

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/cloud-gateway.jar"]
#FROM arm32v7/adoptopenjdk:11-jdk-hotspot
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