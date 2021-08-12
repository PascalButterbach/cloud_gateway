FROM hypriot/rpi-java
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]

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