FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN mvn dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]
#
#ARG JAR_FILE=*.jar
#COPY ${JAR_FILE} cloud_gateway.jar
#ENTRYPOINT ["java","-jar","cloud_gateway.jar"]