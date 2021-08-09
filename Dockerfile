FROM openjdk:17-jdk-alpine3.14
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src
EXPOSE 9000
CMD ["./mvnw", "spring-boot:run"]
#
#ARG JAR_FILE=*.jar
#COPY ${JAR_FILE} cloud_gateway.jar
#ENTRYPOINT ["java","-jar","cloud_gateway.jar"]