FROM arm32v7/adoptopenjdk:11-jdk-hotspot

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "/app.jar"]

#FROM hypriot/rpi-java
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