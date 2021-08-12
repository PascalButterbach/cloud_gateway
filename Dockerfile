FROM arm32v7/adoptopenjdk:11-jdk-hotspot
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} cloud-gateway.jar
EXPOSE 8999
ENTRYPOINT ["java","-jar","/cloud-gateway.jar"]

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