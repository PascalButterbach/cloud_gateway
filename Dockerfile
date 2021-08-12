FROM hypriot/rpi-java
ADD ./target/cloud-gateway.jar cloud-gateway.jar
COPY api_key /tmp/api_key
ENTRYPOINT ["java", "-jar", "cloud-gateway.jar"]

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