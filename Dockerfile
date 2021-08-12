FROM adoptopenjdk/openjdk15

ADD SpringBootREST-0.0.1-SNAPSHOT.jar /opt/SpringBootREST-0.0.1-SNAPSHOT.jar

EXPOSE 8999

ENTRYPOINT ["java", "-jar", "/opt/SpringBootREST-0.0.1-SNAPSHOT.jar"]


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
#EXPOSE 9000
#
#CMD ["./mvnw", "spring-boot:run"]